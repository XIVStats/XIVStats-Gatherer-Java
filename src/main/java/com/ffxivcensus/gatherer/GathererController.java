package com.ffxivcensus.gatherer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;

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

    private ApplicationConfig appConfig = new ApplicationConfig();
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

        if(isConfigured().length() > 0) { // If not configured
            throw new Exception("Program not (correctly) configured");
        } else { // Else configured correctly
            try {

                if(appConfig.isSplitTables()) { // If specified to split tables
                    for(String realm : realms) { // Create a table for each realm
                        createTable("tbl" + realm.toLowerCase() + appConfig.getTableSuffix());
                    }
                } else { // Else just create a single table
                    createTable(appConfig.getTableName());
                }

                if(appConfig.getStartId() > appConfig.getEndId()) {
                    System.out.println("Error: The finish id argument needs to be greater than the start id argument");
                } else { // Else pass values into poll method
                    System.out.println("Starting parse of range " + appConfig.getStartId() + " to " + appConfig.getEndId() + " using "
                                       + appConfig.getThreadLimit() + " threads");
                    gatherRange();
                    // Get current time
                    long endTime = System.currentTimeMillis();
                    long seconds = (endTime - startTime) / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    String time = days + " Days, " + hours % 24 + " hrs, " + minutes % 60 + " mins, " + seconds % 60 + " secs";
                    System.out.println("Run completed, " + ((appConfig.getEndId() - appConfig.getStartId()) + 1)
                                       + " character IDs scanned in " + time + " (" + appConfig.getThreadLimit() + " threads)");

                }

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Determine whether the instance is correctly configured.
     *
     * @return string containing warnings/errors in configuration.
     */
    public String isConfigured() {
        boolean configured = true;
        StringBuilder sbOut = new StringBuilder();
        if(appConfig.getStartId() < 0) {
            sbOut.append("Start ID must be configured to a positive numerical value\n");
            configured = false;
        }
        if(appConfig.getEndId() < 0) {
            sbOut.append("End ID must be configured to a positive numerical value\n");
            configured = false;
        }
        if(appConfig.getDbUrl() == null || appConfig.getDbUrl().length() <= 5) {
            sbOut.append("Database URL has not been configured correctly\n");
            configured = false;
        }
        if(appConfig.getDbUser() == null || appConfig.getDbUser().length() == 0) {
            sbOut.append("Database User has not been configured correctly\n");
            configured = false;
        }
        if(appConfig.getDbPassword() == null || appConfig.getDbPassword().length() == 0) {
            sbOut.append("Database Password has not been configured correctly\n");
            configured = false;
        }
        if(appConfig.getTableName() == null || appConfig.getTableName().length() == 0) {
            sbOut.append("Table name has not been configured correctly\n");
            configured = false;
        }
        return sbOut.toString();
    }

    /**
     * Create the table to write character records to.
     *
     * @param tableName the name of the table to setup.
     */
    private void createTable(final String tableName) {
        // Create DB table if it doesn't exist
        // Open connection
        Connection conn = openConnection();
        try {
            Statement st = conn.createStatement();
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("CREATE TABLE IF NOT EXISTS ");
            sbSQL.append(tableName);
            sbSQL.append(" (id INTEGER PRIMARY KEY,name TEXT,realm TEXT,race TEXT,gender TEXT,grand_company TEXT,free_company TEXT,");
            sbSQL.append("level_gladiator INTEGER,level_pugilist INTEGER,level_marauder INTEGER,level_lancer INTEGER,level_archer INTEGER,");
            sbSQL.append("level_rogue INTEGER,level_conjurer INTEGER,level_thaumaturge INTEGER,level_arcanist INTEGER,level_darkknight INTEGER, level_machinist INTEGER,");
            sbSQL.append("level_astrologian INTEGER, level_scholar INTEGER, level_redmage INTEGER, level_samurai INTEGER, level_carpenter INTEGER, level_blacksmith INTEGER,");
            sbSQL.append("level_armorer INTEGER,level_goldsmith INTEGER,level_leatherworker INTEGER,level_weaver INTEGER,level_alchemist INTEGER,level_culinarian INTEGER,");
            sbSQL.append("level_miner INTEGER,level_botanist INTEGER,level_fisher INTEGER");
            if(appConfig.isStoreProgression()) {
                sbSQL.append(",");
                sbSQL.append("p30days BIT, p60days BIT, p90days BIT, p180days BIT, p270days BIT,p360days BIT,p450days BIT,p630days BIT,p960days BIT,");
                sbSQL.append("prearr BIT, prehw BIT, presb BIT, arrartbook BIT, hwartbookone BIT, hwartbooktwo BIT, hasencyclopedia BIT, ");
                sbSQL.append("beforemeteor BIT, beforethefall BIT, ps4collectors BIT, ");
                sbSQL.append("soundtrack BIT,saweternalbond BIT,sightseeing BIT,comm50 BIT,moogleplush BIT,");
                sbSQL.append("topazcarubuncleplush BIT,emeraldcarbuncleplush BIT,");
                sbSQL.append("hildibrand BIT, dideternalbond BIT, arrcollector BIT,");
                sbSQL.append("kobold BIT, sahagin BIT, amaljaa BIT, sylph BIT,  moogle BIT, vanuvanu BIT, vath BIT,");
                sbSQL.append("arr_25_complete BIT,hw_complete BIT, hw_31_complete BIT, hw_33_complete BIT, legacy_player BIT");
            }
            if(appConfig.isStoreMounts()) {
                sbSQL.append(",mounts TEXT");
            }
            if(appConfig.isStoreMinions()) {
                sbSQL.append(",minions TEXT");
            }
            if(appConfig.isStoreActiveDate()) {
                sbSQL.append(",");
                sbSQL.append("date_active DATE");
            }
            if(appConfig.isStorePlayerActive()) {
                sbSQL.append(",");
                sbSQL.append("is_active BIT");
            }
            sbSQL.append(");");

            st.executeUpdate(sbSQL.toString());
        } catch(SQLException e) {
            e.printStackTrace();
        }
        closeConnection(conn);
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
            Gatherer worker = new Gatherer(this, nextID);
            executor.execute(worker);

            nextID++;
        }

        executor.shutdown();
        while(!executor.isTerminated()) {
            // Wait patiently for the executor to finish off everything submitted
        }
    }

    /**
     * Write a player record to database
     */
    protected String writeToDB(final PlayerBean player) {
        String strOut = "";
        // Open connection
        Connection conn = openConnection();
        try {
            Statement st = conn.createStatement();

            // Declare string builders to build up components of statement
            StringBuilder sbFields = new StringBuilder();
            StringBuilder sbValues = new StringBuilder();

            // Set default table name
            String tableName;
            // Determine table to write to
            if(appConfig.isSplitTables()) {
                tableName = "tbl" + player.getRealm().toLowerCase() + appConfig.getTableSuffix();
            } else {
                tableName = this.appConfig.getTableName() + appConfig.getTableSuffix();
            }

            sbFields.append("INSERT IGNORE INTO ").append(tableName).append(" (");
            sbValues.append(" VALUES (");

            sbFields.append("id, name, realm, race, gender, grand_company,free_company,");
            sbValues.append(player.getId() + ",\"" + player.getPlayerName() + "\",\"" + player.getRealm() + "\",\"" + player.getRace()
                            + "\",'" + player.getGender() + "','" + player.getGrandCompany() + "',\"" + player.getFreeCompany() + "\",");

            sbFields.append("level_gladiator, level_pugilist, level_marauder,level_lancer, level_archer, level_rogue,");
            sbValues.append(player.getLvlGladiator() + "," + player.getLvlPugilist() + "," + player.getLvlMarauder() + ","
                            + player.getLvlLancer() + "," + player.getLvlArcher() + "," + player.getLvlRogue() + ",");

            sbFields.append("level_conjurer, level_thaumaturge, level_arcanist, level_astrologian, level_darkknight,"
                            + " level_machinist,");
            sbValues.append(player.getLvlConjurer() + "," + player.getLvlThaumaturge() + "," + player.getLvlArcanist() + ","
                            + player.getLvlAstrologian() + "," + player.getLvlDarkKnight() + "," + player.getLvlMachinist() + ",");

            sbFields.append("level_scholar, level_redmage, level_samurai,");
            sbValues.append(player.getLvlScholar() + "," + player.getLvlRedMage() + "," + player.getLvlSamurai() + ",");

            sbFields.append("level_carpenter, level_blacksmith, level_armorer, level_goldsmith, level_leatherworker, level_weaver, level_alchemist,");
            sbValues.append(player.getLvlCarpenter() + "," + player.getLvlBlacksmith() + "," + player.getLvlArmorer() + ","
                            + player.getLvlGoldsmith() + "," + player.getLvlLeatherworker() + "," + player.getLvlWeaver() + ","
                            + player.getLvlAlchemist() + ",");

            sbFields.append("level_culinarian, level_miner, level_botanist, level_fisher");
            sbValues.append(player.getLvlCulinarian() + "," + player.getLvlMiner() + "," + player.getLvlBotanist() + ","
                            + player.getLvlFisher());

            if(appConfig.isStoreProgression()) {
                sbFields.append(",");
                sbValues.append(",");

                sbFields.append("p30days, p60days, p90days, p180days, p270days, p360days, p450days, p630days, p960days,");
                sbValues.append(booleanToInt(player.isHas30DaysSub()) + "," + booleanToInt(player.isHas60DaysSub()) + ","
                                + booleanToInt(player.isHas90DaysSub()) + "," + booleanToInt(player.isHas180DaysSub()) + ","
                                + booleanToInt(player.isHas270DaysSub()) + "," + booleanToInt(player.isHas360DaysSub()) + ","
                                + booleanToInt(player.isHas450DaysSub()) + "," + booleanToInt(player.isHas630DaysSub()) + ","
                                + booleanToInt(player.isHas960DaysSub()) + ",");

                sbFields.append("prearr, prehw, presb, arrartbook, hwartbookone, hwartbooktwo, hasencyclopedia, beforemeteor, beforethefall, soundtrack, saweternalbond, "
                                + "sightseeing, arr_25_complete, comm50, moogleplush, topazcarubuncleplush, emeraldcarbuncleplush,");
                sbValues.append(booleanToInt(player.isHasPreOrderArr()) + "," + booleanToInt(player.isHasPreOrderHW()) + ","
                                + booleanToInt(player.isHasPreOrderSB()) + "," + booleanToInt(player.isHasARRArtbook()) + ","
                                + booleanToInt(player.isHasHWArtbookOne()) + "," + booleanToInt(player.isHasHWArtbookTwo()) + ","
                                + booleanToInt(player.isHasEncyclopediaEorzea()) + "," + booleanToInt(player.isHasBeforeMeteor()) + ","
                                + booleanToInt(player.isHasBeforeTheFall()) + "," + booleanToInt(player.isHasSoundtrack()) + ","
                                + booleanToInt(player.isHasAttendedEternalBond()) + "," + booleanToInt(player.isHasCompletedHWSightseeing())
                                + "," + booleanToInt(player.isHasCompleted2pt5()) + "," + booleanToInt(player.isHasFiftyComms()) + ","
                                + booleanToInt(player.isHasMooglePlush()) + "," + booleanToInt(player.isHasTopazCarbunclePlush()) + ","
                                + booleanToInt(player.isHasEmeraldCarbunclePlush()) + ",");

                sbFields.append("hildibrand, ps4collectors, dideternalbond, arrcollector, kobold, sahagin, amaljaa, "
                                + "sylph, moogle, vanuvanu, vath, hw_complete, hw_31_complete, hw_33_complete, legacy_player");
                sbValues.append(booleanToInt(player.isHasCompletedHildibrand()) + "," + booleanToInt(player.isHasPS4Collectors()) + ","
                                + booleanToInt(player.isHasEternalBond()) + "," + booleanToInt(player.isHasARRCollectors()) + ","
                                + booleanToInt(player.isHasKobold()) + "," + booleanToInt(player.isHasSahagin()) + ","
                                + booleanToInt(player.isHasAmaljaa()) + "," + booleanToInt(player.isHasSylph()) + ","
                                + booleanToInt(player.isHasMoogle()) + "," + booleanToInt(player.isHasVanuVanu()) + ","
                                + booleanToInt(player.isHasVath()) + "," + booleanToInt(player.isHasCompletedHW()) + ","
                                + booleanToInt(player.isHasCompleted3pt1()) + "," + booleanToInt(player.isHasCompleted3pt3()) + ","
                                + booleanToInt(player.isHasCompletedSB()) + "," + booleanToInt(player.isLegacyPlayer()));

            }

            if(appConfig.isStoreMinions()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("minions");
                sbValues.append("\"" + StringUtils.join(player.getMinions(), ",") + "\"");
            }
            if(appConfig.isStoreMounts()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("mounts");
                sbValues.append("\"" + StringUtils.join(player.getMounts(), ",") + "\"");
            }

            if(appConfig.isStoreActiveDate()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("date_active");
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

                String sqlDate = sdf.format(player.getDateImgLastModified());
                sbValues.append("\"" + sqlDate + "\"");
            }
            if(appConfig.isStorePlayerActive()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("is_active");
                sbValues.append(booleanToInt(player.isActive()));
            }

            sbFields.append(")");
            sbValues.append(");");

            String strSQL = sbFields.toString() + sbValues.toString();

            st.executeUpdate(strSQL);
            if(!appConfig.isQuiet() || isVerbose()) {
                strOut = "Character " + player.getId() + " written to database successfully.";
            }
            closeConnection(conn);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return strOut;
    }

    private int booleanToInt(final boolean value) {
        return BooleanUtils.toInteger(value);
    }

    /**
     * Open a connection to database.
     *
     * @return the opened connection
     * @throws SQLException exception thrown if unable to connect
     */
    protected Connection openConnection() {
        try {
            String connString = "jdbc:" + appConfig.getDbUrl() + "/" + appConfig.getDbName();
            if(appConfig.isDbIgnoreSSLWarn()) {
                connString += "?useSSL=false";
            }
            Connection connection = DriverManager.getConnection(connString, appConfig.getDbUser(), appConfig.getDbPassword());
            return connection;
        } catch(SQLException sqlEx) {
            System.out.println("Connection failed! Please see output console");
            sqlEx.printStackTrace();
            return null;
        }
    }

    /**
     * Close the specified connection.
     *
     * @param conn the connection to throw
     * @throws SQLException exception thrown if unable to close connection.
     */
    protected void closeConnection(final Connection conn) {
        try {
            conn.close();
        } catch(SQLException sqlEx) {
            System.out.println("Cannot close connection! Has it already been closed");
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
     * Set the maximum number of threads allowed for the gatherer controller instance
     *
     * @param threadLimit the maximum number of threads allowed.
     */
    public void setThreadLimit(final int threadLimit) {
        this.appConfig.setThreadLimit(threadLimit);
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
     * Set whether to store each character's minion set to the database.
     *
     * @param storeMinions whether to store each character's minion set to the database.
     */
    public void setStoreMinions(final boolean storeMinions) {
        this.appConfig.setStoreMinions(storeMinions);
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
     * Set whether each character's mount set will be written to the database.
     *
     * @param storeMounts whether each character's mount set will be written to DB.
     */
    public void setStoreMounts(final boolean storeMounts) {
        this.appConfig.setStoreMounts(storeMounts);
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
     * Set whether to store boolean values indicating character progression in the database.
     *
     * @param storeProgression whether to store boolean values indicating character progression in the database.
     */
    public void setStoreProgression(final boolean storeProgression) {
        this.appConfig.setStoreProgression(storeProgression);
    }

    /**
     * Get the name of the table that character records will be written to.
     *
     * @return the name of the table that character records will be written to.
     */
    public String getTableName() {
        return appConfig.getTableName();
    }

    /**
     * Set the name of the table that character records will be written to.
     *
     * @param tableName the name of the table that character records will be written to.
     */
    public void setTableName(final String tableName) {
        this.appConfig.setTableName(tableName);
    }

    /**
     * Whether the single player table is being split up into one table for each realm.
     *
     * @return whether the single player table is being split up into one table for each realm.
     */
    public boolean isSplitTables() {
        return appConfig.isSplitTables();
    }

    /**
     * Set whether the single player table should be split up into one table for each realm.
     *
     * @param splitTables whether the single player table is being split up into one table for each realm.
     */
    public void setSplitTables(final boolean splitTables) {
        this.appConfig.setSplitTables(splitTables);
    }

    /**
     * Get the suffix to append to all tables.
     *
     * @return the suffix to append to all tables.
     */
    public String getTableSuffix() {
        return appConfig.getTableSuffix();
    }

    /**
     * Set the suffix to append to all tables.
     *
     * @param tableSuffix the suffix to append to all tables.
     */
    public void setTableSuffix(final String tableSuffix) {
        this.appConfig.setTableSuffix(tableSuffix);
    }

    /**
     * Set whether to run the program in quiet mode (no console output).
     *
     * @return whether to run the program in quiet mode
     */
    public boolean isQuiet() {
        return appConfig.isQuiet();
    }

    /**
     * Set whether to run the program in quiet mode (print each successfully written record).
     *
     * @param quiet whether to run the program in quiet mode.
     */
    public void setQuiet(final boolean quiet) {
        this.appConfig.setQuiet(quiet);
    }

    /**
     * Set whether to print an output when records don't exist.
     *
     * @return whether to print an output when records don't exist.
     */
    public boolean isVerbose() {
        return appConfig.isVerbose();
    }

    /**
     * Set whether to print an output when records don't exist.
     *
     * @param verbose whether to print an output when records don't exist.
     */
    public void setVerbose(final boolean verbose) {
        this.appConfig.setVerbose(verbose);
    }

    /**
     * Get list of realms to create tables for
     *
     * @return array of realm names
     */
    public static String[] getRealms() {
        return realms;
    }

    /**
     * Set the SQL server database url.
     *
     * @param dbUrl the SQL server database url.
     */
    public void setDbUrl(final String dbUrl) {
        this.appConfig.setDbUrl(dbUrl);
    }

    /**
     * Set the SQL server database user.
     *
     * @param dbUser the SQL server database user.
     */
    public void setDbUser(final String dbUser) {
        this.appConfig.setDbUser(dbUser);
    }

    /**
     * Set the SQL Server user password.
     *
     * @param dbPassword the SQL Server user password.
     */
    public void setDbPassword(final String dbPassword) {
        this.appConfig.setDbPassword(dbPassword);
    }

    /**
     * Get whether to print record write fails
     *
     * @return whether to print record write fails
     */
    public boolean isPrintFails() {
        return appConfig.isPrintFails();
    }

    /**
     * Set whether to print record write fails
     *
     * @param printFails whether to print record write fails
     */
    public void setPrintFails(final boolean printFails) {
        this.appConfig.setPrintFails(printFails);
    }

    /**
     * Set whether to store the last active date of a character
     *
     * @param storeActiveDate whether to store the last active date of a character
     */
    public void setStoreActiveDate(final boolean storeActiveDate) {
        this.appConfig.setStoreActiveDate(storeActiveDate);
    }

    /**
     * Set whether to store a boolean value indicating player activity
     *
     * @param storePlayerActive whether to store a boolean value indicating player activity
     */
    public void setStorePlayerActive(final boolean storePlayerActive) {
        this.appConfig.setStorePlayerActive(storePlayerActive);
    }

    /**
     * Set whether to ignore database ssl verification warnings
     *
     * @param dbIgnoreSSLWarn whether to ignore database ssl verification warnings
     */
    public void setDbIgnoreSSLWarn(final boolean dbIgnoreSSLWarn) {
        this.appConfig.setDbIgnoreSSLWarn(dbIgnoreSSLWarn);
    }
}
