package com.ffxivcensus.gatherer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.player.PlayerBeanDAO;
import com.ffxivcensus.gatherer.player.PlayerBuilder;

/**
 * GathererController class of character gathering program. This class makes calls to fetch records from the lodestone, and then
 * subsequently writes them to the database. It also specifies the parameters to run the program with.
 *
 * @author Peter Reid
 * @since v1.0
 * @see PlayerBuilder
 * @see Gatherer
 */
@Service
public class GathererController {

    private static final Logger LOG = LoggerFactory.getLogger(GathererController.class);
    private final ApplicationConfig appConfig;
    private final GathererFactory gathererFactory;
    private DataSource dataSource;
    /**
     * List of playable realms (used when splitting tables).
     */
    public final static String[] REALMS = {"Adamantoise", "Aegis", "Alexander", "Anima", "Asura", "Atomos", "Bahamut", "Balmung",
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
    public GathererController(@Autowired final ApplicationConfig config, @Autowired final GathererFactory gathererFactory,
                              @Autowired DataSource dataSource) {
        this.appConfig = config;
        this.gathererFactory = gathererFactory;
        this.dataSource = dataSource;
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
                throw new Exception(ex);
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
            Gatherer worker = gathererFactory.createGatherer();
            worker.setPlayerBeanDAO(new PlayerBeanDAO(appConfig, dataSource));
            worker.setPlayerId(nextID);
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
}
