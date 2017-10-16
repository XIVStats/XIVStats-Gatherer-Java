package com.ffxivcensus.gatherer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.player.PlayerBeanDAO;
import com.ffxivcensus.gatherer.player.PlayerBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * GathererController class of character gathering program. This class makes calls to fetch records from the lodestone, and then
 * subsequently writes them to the database. It also specifies the parameters to run the program with.
 *
 * @author Peter Reid
 * @since v1.0
 * @see PlayerBuilder
 * @see Gatherer
 */
public class GathererController {

	private static final Logger LOG = LoggerFactory.getLogger(GathererController.class);
    private ApplicationConfig appConfig = new ApplicationConfig();
    private HikariDataSource dataSource;
    /**
     * List of playable realms (used when splitting tables).
     */
    private final static String[] realms = {"Adamantoise", "Aegis", "Alexander", "Anima", "Asura", "Atomos", "Bahamut", "Balmung",
                                            "Behemoth", "Belias", "Brynhildr", "Cactuar", "Carbuncle", "Cerberus", "Chocobo", "Coeurl",
                                            "Diabolos", "Durandal", "Excalibur", "Exodus", "Faerie", "Famfrit", "Fenrir", "Garuda",
                                            "Gilgamesh", "Goblin", "Gungnir", "Hades", "Hyperion", "Ifrit", "Ixion", "Jenova", "Kujata",
                                            "Lamia", "Leviathan", "Lich", "Louisoix", "Malboro", "Mandragora", "Masamune", "Mateus",
                                            "Midgardsormr", "Moogle", "Odin", "Omega", "Pandaemonium", "Phoenix", "Ragnarok", "Ramuh",
                                            "Ridill", "Sargatanas", "Shinryu", "Shiva", "Siren", "Tiamat", "Titan", "Tonberry", "Typhon",
                                            "Ultima", "Ultros", "Unicorn", "Valefor", "Yojimbo", "Zalera", "Zeromus", "Zodiark"};

    /**
     * Constructs a new {@link GathererController} and configures with the provided {@link ApplicationConfig}.
     * 
     * @param config Configuration Bean
     */
    public GathererController(final ApplicationConfig config) {
        this.appConfig = config;

    }

    /**
     * Start the gatherer controller instance up.
     *
     * @throws Exception Exception thrown if system is incorrectly configured.
     */
    public void run() throws Exception {
        // Store start time
        long startTime = System.currentTimeMillis();

        // If user attempts to exceed the maximum no. of threads - overwrite their input and set to MAX_THREADS
        if(appConfig.getThreadLimit() > ApplicationConfig.MAX_THREADS) {
            appConfig.setThreadLimit(ApplicationConfig.MAX_THREADS);
        }

        if(!isConfigured()) { // If not configured
            throw new Exception("Program not (correctly) configured");
        } else { // Else configured correctly
            try {
                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setJdbcUrl("jdbc:" + appConfig.getDbUrl() + "/" + appConfig.getDbName());
                hikariConfig.setUsername(appConfig.getDbUser());
                hikariConfig.setPassword(appConfig.getDbPassword());
                hikariConfig.setMaximumPoolSize(appConfig.getThreadLimit());
                hikariConfig.setInitializationFailTimeout(30000);
                if(appConfig.isDbIgnoreSSLWarn()) {
                    hikariConfig.addDataSourceProperty("useSSL", false);
                }

                this.dataSource = new HikariDataSource(hikariConfig);

                PlayerBeanDAO dao = new PlayerBeanDAO(appConfig, dataSource);

                dao.createTable(appConfig.getTableName());

                if(appConfig.getStartId() > appConfig.getEndId()) {
                    LOG.error("Error: The finish id argument needs to be greater than the start id argument");
                } else { // Else pass values into poll method
                    LOG.info("Starting parse of range " + appConfig.getStartId() + " to " + appConfig.getEndId() + " using "
                                       + appConfig.getThreadLimit() + " threads");
                    gatherRange();
                    // Get current time
                    long endTime = System.currentTimeMillis();
                    long seconds = (endTime - startTime) / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    String time = days + " Days, " + hours % 24 + " hrs, " + minutes % 60 + " mins, " + seconds % 60 + " secs";
                    LOG.info("Run completed, " + ((appConfig.getEndId() - appConfig.getStartId()) + 1)
                                       + " character IDs scanned in " + time + " (" + appConfig.getThreadLimit() + " threads)");

                }

            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                if(dataSource != null) {
                    dataSource.close();
                }
            }
        }
    }

    /**
     * Determine whether the instance is correctly configured.
     *
     * @return string containing warnings/errors in configuration.
     */
    public boolean isConfigured() {
        boolean configured = true;
        if(appConfig.getStartId() < 0) {
            LOG.error("Start ID must be configured to a positive numerical value");
            configured = false;
        }
        if(appConfig.getEndId() < 0) {
            LOG.error("End ID must be configured to a positive numerical value");
            configured = false;
        }
        if(appConfig.getDbUrl() == null || appConfig.getDbUrl().length() <= 5) {
            LOG.error("Database URL has not been configured correctly");
            configured = false;
        }
        if(appConfig.getDbUser() == null || appConfig.getDbUser().length() == 0) {
            LOG.error("Database User has not been configured correctly");
            configured = false;
        }
        if(appConfig.getDbPassword() == null || appConfig.getDbPassword().length() == 0) {
            LOG.error("Database Password has not been configured correctly");
            configured = false;
        }
        if(appConfig.getTableName() == null || appConfig.getTableName().length() == 0) {
            LOG.error("Table name has not been configured correctly");
            configured = false;
        }
        return configured;
    }

    /**
     * Method to gather data for characters in specified range.
     */
    private void gatherRange() {
        // Set next ID
        int nextID = appConfig.getStartId();

        // Setup an executor service that respects the max thread limit
        ExecutorService executor = Executors.newFixedThreadPool(appConfig.getThreadLimit());

        while(nextID <= appConfig.getEndId()) {
            Gatherer worker = new Gatherer(this, new PlayerBeanDAO(appConfig, dataSource), nextID);
            executor.execute(worker);

            nextID++;
        }

        executor.shutdown();
        while(!executor.isTerminated()) {
            // Wait patiently for the executor to finish off everything submitted
        		if(Thread.interrupted()) {
        			// Unless there's an interrupt, in which case shut down gracefully
        			executor.shutdownNow();
        		}
        }
    }

    /**
     * Get the first ID to be processed.
     *
     * @return the first character ID to be processed
     */
    public int getStartId() {
        return appConfig.getStartId();
    }

    /**
     * Get the last ID to be processed.
     *
     * @return the last character ID due to be processed.
     */
    public int getEndId() {
        return appConfig.getEndId();
    }

    /**
     * Get the maximum number of threads allowed for the Gatherer Controller instance.
     *
     * @return the maximum number of threads allowed.
     */
    public int getThreadLimit() {
        return appConfig.getThreadLimit();
    }

    /**
     * Get whether each character's minion set will be written to the database.
     *
     * @return whether each character's minion set will be written to DB.
     */
    public boolean isStoreMinions() {
        return appConfig.isStoreMinions();
    }

    /**
     * Get whether each character's mount set will be written to the database.
     *
     * @return whether each character's mount set will be written to DB.
     */
    public boolean isStoreMounts() {
        return appConfig.isStoreMounts();
    }

    /**
     * Get whether to store boolean values indicating character progression in the database.
     *
     * @return whether to store boolean values indicating character progression in the database.
     */
    public boolean isStoreProgression() {
        return appConfig.isStoreProgression();
    }

    /**
     * Get the name of the table that character records will be written to.
     *
     * @return the name of the table that character records will be written to.
     */
    public String getTableName() {
        return appConfig.getTableName();
    }
}
