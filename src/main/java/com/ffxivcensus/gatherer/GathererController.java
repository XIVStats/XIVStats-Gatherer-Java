package com.ffxivcensus.gatherer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * GathererController class of character gathering program. This class makes calls to fetch records from the lodestone, and then
 * subsequently writes them to the database. It also specifies the parameters to run the program with.
 *
 * @author Peter Reid
 * @since v1.0
 * @see Player
 * @see Gatherer
 */
public class GathererController {

    //Configuration options
    /**
     * The JDBC URL of the database to modify
     */
    private String dbUrl;
    /**
     * The Username of user of the SQL server user to use.
     */
    private String dbUser;
    /**
     * The password for the user, to use.
     */
    private String dbPassword;
    /**
     * Whether to ignore database SSL verification warnings
     */
    private boolean dbIgnoreSSLWarn;
    /**
     * The character ID to start the gatherer at.
     */
    private int startId = -1;
    /**
     * The character ID to end the gatherer at.
     */
    private int endId = -1;
    /**
     * The next ID to be gathered.
     */
    private int nextID = -1;
    /**
     * User-defined limit for thread count.
     */
    private int threadLimit;

    /**
     * Boolean value indicating whether to store a comma delimited list of minions to database.
     */
    private boolean storeMinions;
    /**
     * Boolean value indicating whether to store a comma delimited list of mounts to database.
     */
    private boolean storeMounts;
    /**
     * Boolean value indicating whether to store bit fields indicating player achievements/progress.
     */
    private boolean storeProgression;
    /**
     * String field to store the table name to write records to (if not splitting tables).
     */
    private String tableName;
    /**
     * Boolean field indicating whether to split character records across multiple tables, with one table for each realm/server.
     */
    private boolean splitTables;
    /**
     * Suffix to be appended to all tables e.g. 15012016
     */
    private String tableSuffix;
    /**
     * Whether to provide console output.
     */
    private boolean quiet;
    /**
     * Whether to output all console output.
     */
    private boolean verbose;
    /**
     * Whether to output failed records
     */
    private boolean printFails;
    /**
     * Whether to store player activity dates
     */
    private boolean storeActiveDate;
    /**
     * Whether to store player activity bit
     */
    private boolean storePlayerActive;

    /**
     * List of playable realms (used when splitting tables).
     */
    private final static String[] realms = {"Adamantoise", "Aegis", "Alexander", "Anima", "Asura", "Atomos", "Bahamut",
            "Balmung", "Behemoth", "Belias", "Brynhildr", "Cactuar", "Carbuncle", "Cerberus", "Chocobo", "Coeurl",
            "Diabolos", "Durandal", "Excalibur", "Exodus", "Faerie", "Famfrit", "Fenrir", "Garuda", "Gilgamesh",
            "Goblin", "Gungnir", "Hades", "Hyperion", "Ifrit", "Ixion", "Jenova", "Kujata", "Lamia", "Leviathan",
            "Lich", "Malboro", "Mandragora", "Masamune", "Mateus", "Midgardsormr", "Moogle", "Odin", "Pandaemonium",
            "Phoenix", "Ragnarok", "Ramuh", "Ridill", "Sargatanas", "Shinryu", "Shiva", "Siren", "Tiamat", "Titan",
            "Tonberry", "Typhon", "Ultima", "Ultros", "Unicorn", "Valefor", "Yojimbo", "Zalera", "Zeromus", "Zodiark"};

    /**
     * Safety limit for thread count - user cannot exceed this limit.
     */
    private final static int MAX_THREADS = 64;

    /**
     * Constructor for GathererController using simply start id and end id. Read other configuration options from config.
     * <p>
     * Other options should be should be established using setters.
     *
     * @param startId the character id to start gatherer run at (inclusive).
     * @param endId   the character id to end the gather run at (inclusive).
     */
    public GathererController(int startId, int endId) {
        this.startId = startId;
        this.endId = endId;
        this.storeMinions = false;
        this.storeMounts = false;
        this.storeProgression = true;
        this.tableName = "tblplayers";
        this.splitTables = false;
        this.tableSuffix = "";
        this.tableName = "tblplayers";
        this.quiet = true;
        this.verbose = false;
        this.printFails = false;

        //Read in config
        try {
            this.readConfig();
        } catch (Exception ex) {
            System.out.println("No config found - please set variables via setters.");
        }
    }

    /**
     * Constructor for GathererController taking additional parameters to set configuration of gatherer controller instance.
     *
     * @param startId the character id to start gatherer run at (inclusive).
     * @param endId the character if to end the gatherer run at (inclusive).
     * @param quiet whether to provide console output indicating progress in adding characters.
     * @param verbose whether to provide console output indicating IDs where characters were not found.
     * @param storeMinions whether to store a player's minions to a comma delimited list in DB.
     * @param storeMounts whether to store a player's mounts to a comma delimited list in DB.
     * @param storeProgression whether to store boolean values indicating character achivements/progress in DB.
     * @param dbUrl the URL of the database server to connect to (e.g. mysql://localhost:3307).
     * @param dbName the name of the database to use to store character records.
     * @param dbUser the username of the SQL server user to connect to the database with.
     * @param dbPassword the password of the SQL server user to connect to the database with.
     * @param threads the number of threads to run the program with.
     * @param tableName the name of the SQL table to write character records to.
     */
    public GathererController(int startId, int endId, boolean quiet, boolean verbose, boolean storeMinions, boolean storeMounts, boolean storeProgression,
                              String dbUrl, String dbName, String dbUser, String dbPassword, int threads, String tableName) {
        this.startId = startId;
        this.endId = endId;
        this.storeMinions = storeMinions;
        this.storeMounts = storeMounts;
        this.storeProgression = storeProgression;
        this.quiet = quiet;
        this.verbose = verbose;
        this.printFails = false;

        //Read in config
        try {
            this.readConfig();
        } catch (Exception ex) {
        }

        //Parameters specified should outweigh config
        this.dbUrl = "jdbc:" + dbUrl + "/" + dbName;
        if (this.dbIgnoreSSLWarn) {
            this.dbUrl += "?useSSL=false";
        }
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.threadLimit = threads;
        this.tableName = tableName;
        this.tableSuffix = "";
    }

    /**
     * Constructor for GathererController taking additional parameters to set configuration of gatherer controller instance.
     *
     * To be used when you want to split the single table into a table for each realm.
     *
     * @param startId the character id to start gatherer run at (inclusive).
     * @param endId the character if to end the gatherer run at (inclusive).
     * @param quiet whether to provide console output indicating progress in adding characters.
     * @param verbose whether to provide console output indicating IDs where characters were not found.
     * @param storeMinions whether to store a player's minions to a comma delimited list in DB.
     * @param storeMounts whether to store a player's mounts to a comma delimited list in DB.
     * @param storeProgression whether to store boolean values indicating character achivements/progress in DB.
     * @param dbUrl the URL of the database server to connect to (e.g. mysql://localhost:3307).
     * @param dbName the name of the database to use to store character records.
     * @param dbUser the username of the SQL server user to connect to the database with.
     * @param dbPassword the password of the SQL server user to connect to the database with.
     * @param threads the number of threads to run the program with.
     * @param tableSuffix the suffix to apply to all tables created, e.g. 23012016
     * @param splitTables whether to split up the single player table into one for each realm/server.
     */
    public GathererController(int startId, int endId, boolean quiet, boolean verbose, boolean storeMinions, boolean storeMounts, boolean storeProgression,
                              String dbUrl, String dbName, String dbUser, String dbPassword, int threads,
                              String tableSuffix, boolean splitTables) {
        this.startId = startId;
        this.endId = endId;
        this.storeMinions = storeMinions;
        this.storeMounts = storeMounts;
        this.storeProgression = storeProgression;
        this.quiet = quiet;
        this.verbose = verbose;
        this.printFails = false;

        //Read in config
        try {
            this.readConfig();
        } catch (Exception ex) {
        }

        //Parameters specified should outweigh config
        this.dbUrl = "jdbc:" + dbUrl + "/" + dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.threadLimit = threads;
        this.tableName = "tblplayers";
        this.tableSuffix = tableSuffix;
        this.splitTables = splitTables;
        this.storeActiveDate = true;
        this.storePlayerActive = true;
    }

    /**
     * Start the gatherer controller instance up.
     * @throws Exception Exception thrown if system is incorrectly configured.
     */
    public void run() throws Exception {
        //Store start time
        long startTime = System.currentTimeMillis();

        //If user attempts to exceed the maximum no. of threads - overwrite their input and set to MAX_THREADS
        if (this.threadLimit > MAX_THREADS){
            this.threadLimit = MAX_THREADS;
        }

        if (isConfigured().length() > 0) { //If not configured
            throw new Exception("Program not (correctly) configured");
        } else { //Else configured correctly
            try {

                if(splitTables) { //If specified to split tables
                    for (String realm : realms) { //Create a table for each realm
                        this.createTable("tbl"+realm.toLowerCase()+tableSuffix);
                    }
                } else{ //Else just create a single table
                    this.createTable(tableName);
                }

                if (startId > endId) {
                    System.out.println("Error: The finish id argument needs to be greater than the start id argument");
                } else { //Else pass values into poll method
                    System.out.println("Starting parse of range " + startId + " to " + endId + " using " + threadLimit + " threads");
                    gatherRange();
                    //Get current time
                    long endTime = System.currentTimeMillis();
                    long seconds = (endTime - startTime) / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    String time = days + " Days, " + hours % 24 + " hrs, " + minutes % 60 + " mins, " + seconds % 60 + " secs";
                    System.out.println("Run completed, " + ((endId - startId) + 1) + " character IDs scanned in " + time + " (" + threadLimit + " threads)");

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Determine whether the instance is correctly configured.
     * @return string containing warnings/errors in configuration.
     */
    public String isConfigured() {
        boolean configured = true;
        StringBuilder sbOut = new StringBuilder();
        if ( this.startId < 0) {
            sbOut.append("Start ID must be configured to a positive numerical value\n");
            configured = false;
        }
        if ( this.endId < 0) {
            sbOut.append("End ID must be configured to a positive numerical value\n");
            configured = false;
        }
        if (this.dbUrl == null || dbUrl.length() <= 5) {
            sbOut.append("Database URL has not been configured correctly\n");
            configured = false;
        }
        if (this.dbUser == null || dbUser.length() == 0) {
            sbOut.append("Database User has not been configured correctly\n");
            configured = false;
        }
        if (this.dbPassword == null || dbPassword.length() == 0) {
            sbOut.append("Database Password has not been configured correctly\n");
        }
        if (this.tableName == null || this.tableName.length() == 0){
            sbOut.append("Table name has not been configured correctly\n");
        }
        return sbOut.toString();
    }

    /**
     * Create the table to write character records to.
     * @param tableName the name of the table to setup.
     */
    private void createTable(String tableName) {
        //Create DB table if it doesn't exist
        //Open connection
        Connection conn = openConnection();
        try {
            Statement st = conn.createStatement();
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("CREATE TABLE IF NOT EXISTS ");
            sbSQL.append(tableName);
            sbSQL.append(" (id INTEGER PRIMARY KEY,name TEXT,realm TEXT,race TEXT,gender TEXT,grand_company TEXT,free_company TEXT,");
            sbSQL.append("level_gladiator INTEGER,level_pugilist INTEGER,level_marauder INTEGER,level_lancer INTEGER,level_archer INTEGER,");
            sbSQL.append("level_rogue INTEGER,level_conjurer INTEGER,level_thaumaturge INTEGER,level_arcanist INTEGER,level_darkknight INTEGER, level_machinist INTEGER,");
            sbSQL.append("level_astrologian INTEGER,level_carpenter INTEGER,level_blacksmith INTEGER,level_armorer INTEGER,level_goldsmith INTEGER,");
            sbSQL.append("level_leatherworker INTEGER,level_weaver INTEGER,level_alchemist INTEGER,level_culinarian INTEGER,level_miner INTEGER,");
            sbSQL.append("level_botanist INTEGER,level_fisher INTEGER");
            if (this.storeProgression) {
                sbSQL.append(",");
                sbSQL.append("p30days BIT, p60days BIT, p90days BIT, p180days BIT, p270days BIT,p360days BIT,p450days BIT,p630days BIT,p960days BIT,");
                sbSQL.append("prearr BIT,prehw BIT, arrartbook BIT, hwartbookone BIT, hwartbooktwo BIT, hasencyclopedia BIT, ");
                sbSQL.append("beforemeteor BIT, beforethefall BIT, ps4collectors BIT, ");
                sbSQL.append("soundtrack BIT,saweternalbond BIT,sightseeing BIT,comm50 BIT,moogleplush BIT,");
                sbSQL.append("topazcarubuncleplush BIT,emeraldcarbuncleplush BIT,");
                sbSQL.append("hildibrand BIT, dideternalbond BIT, arrcollector BIT,");
                sbSQL.append("kobold BIT, sahagin BIT, amaljaa BIT, sylph BIT,  moogle BIT, vanuvanu BIT, vath BIT,");
                sbSQL.append("arr_25_complete BIT,hw_complete BIT, hw_31_complete BIT, hw_33_complete BIT, legacy_player BIT");
            }
            if (this.storeMounts) {
                sbSQL.append(",mounts TEXT");
            }
            if (this.storeMinions) {
                sbSQL.append(",minions TEXT");
            }
            if(this.storeActiveDate) {
                sbSQL.append(",");
                sbSQL.append("date_active DATE");
            }
            if(this.storePlayerActive) {
                sbSQL.append(",");
                sbSQL.append("is_active BIT");
            }
            sbSQL.append(");");

            st.executeUpdate(sbSQL.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(conn);
    }

    /**
     * Read configuration from config.xml
     *
     * @throws ParserConfigurationException Indicates a serious configuration error.
     * @throws IOException                  Indicates an error reading the file specified.
     * @throws SAXException                 Indicates an error parsing XML.
     */
    private void readConfig() throws ParserConfigurationException, IOException, SAXException {
        //Set config file location
        File xmlFile = new File("config.xml");
        //Initialize parsers
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        //Parse the file
        Document doc = dBuilder.parse(xmlFile);

        //Read out db config
        NodeList nodesJDBC = doc.getElementsByTagName("jdbc");
        Element elementJDBC = (Element) nodesJDBC.item(0);

        String url = "jdbc:" + elementJDBC.getElementsByTagName("url").item(0).getTextContent() + "/"
                    + elementJDBC.getElementsByTagName("database").item(0).getTextContent();
        dbUrl = url;
        dbUser = elementJDBC.getElementsByTagName("username").item(0).getTextContent();
        dbPassword = elementJDBC.getElementsByTagName("password").item(0).getTextContent();

        //Read out execution config
        NodeList nodesExecConf = doc.getElementsByTagName("execution");
        Element elementExecConf = (Element) nodesExecConf.item(0);
        threadLimit  = Integer.parseInt(elementExecConf.getElementsByTagName("threads").item(0).getTextContent());
    }

    /**
     * Method to gather data for characters in specified range.
     */
    private void gatherRange() {

        //Set next ID
        nextID = startId;

        //Start up up new threads up to limit
        //Create array to store thread references into
        Thread[] threads = new Thread[threadLimit];
        for (int index = 0; index < threadLimit; index++) {
            threads[index] = new Thread(new Gatherer(this));
        }

        //Start up threads
        for (Thread thread : threads) {
            thread.start();
        }

        //Spin down threads
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Write a player record to database
     */
    protected String writeToDB(Player player) {
        String strOut = "";
        //Open connection
        Connection conn = openConnection();
        try {
            Statement st = conn.createStatement();

            //Declare string builders to build up components of statement
            StringBuilder sbFields = new StringBuilder();
            StringBuilder sbValues = new StringBuilder();

            //Set default table name
            String tableName;
            //Determine table to write to
            if (splitTables){
                tableName = "tbl" + player.getRealm().toLowerCase() +  tableSuffix;
            } else {
                tableName = this.tableName + tableSuffix;
            }

            sbFields.append("INSERT IGNORE INTO ").append(tableName).append(" (");
            sbValues.append(" VALUES (");

            sbFields.append("id, name, realm, race, gender, grand_company,free_company,");
            sbValues.append(player.getId() + ",\"" + player.getPlayerName() + "\",\"" + player.getRealm() + "\",\""
                            + player.getRace() + "\",'" + player.getGender() + "','" + player.getGrandCompany() + "',\""
                            + player.getFreeCompany() + "\",");

            sbFields.append("level_gladiator, level_pugilist, level_marauder,level_lancer, level_archer, level_rogue,");
            sbValues.append(player.getLvlGladiator() + "," + player.getLvlPugilist() + "," + player.getLvlMarauder()
                            + "," + player.getLvlLancer() + "," + player.getLvlArcher() + "," + player.getLvlRogue() + ",");

            sbFields.append("level_conjurer, level_thaumaturge, level_arcanist, level_astrologian, level_darkknight," +
                            " level_machinist, level_carpenter,");
            sbValues.append(player.getLvlConjurer() + "," + player.getLvlThaumaturge() + "," + player.getLvlArcanist()
                            + "," + player.getLvlAstrologian() + "," + player.getLvlDarkKnight() + "," + player.getLvlMachinist()
                            + "," + player.getLvlCarpenter() + ",");

            sbFields.append("level_blacksmith, level_armorer, level_goldsmith, level_leatherworker, level_weaver, level_alchemist,");
            sbValues.append(player.getLvlBlacksmith() + "," + player.getLvlArmorer() + "," + player.getLvlGoldsmith()
                            + "," + player.getLvlLeatherworker() + "," + player.getLvlWeaver() + "," + player.getLvlAlchemist()
                            + ",");

            sbFields.append("level_culinarian, level_miner, level_botanist, level_fisher");
            sbValues.append(player.getLvlCulinarian() + "," + player.getLvlMiner() + "," + player.getLvlBotanist() + "," + player.getLvlFisher());

            if(this.storeProgression){
                sbFields.append(",");
                sbValues.append(",");

                sbFields.append("p30days, p60days, p90days, p180days, p270days, p360days, p450days, p630days, p960days,");
                sbValues.append(player.getBitHas30DaysSub() + "," + player.getBitHas60DaysSub() + ","
                        + player.getBitHas90DaysSub() + "," + player.getBitHas180DaysSub() + ","
                        + player.getBitHas270DaysSub() + "," + player.getBitHas360DaysSub() + ","
                        + player.getBitHas450DaysSub() + "," + player.getBitHas630DaysSub() + ","
                        + player.getBitHas960DaysSub() + ",");

                sbFields.append("prearr, prehw, arrartbook, hwartbookone, hwartbooktwo, hasencyclopedia, beforemeteor, beforethefall, soundtrack, saweternalbond, "
                                + "sightseeing, arr_25_complete, comm50, moogleplush, topazcarubuncleplush, emeraldcarbuncleplush,");
                sbValues.append(player.getBitHasPreOrderArr() + "," + player.getBitHasPreOrderHW() + ","
                                + player.getBitHasArrArtbook() + "," + player.getBitHasHWArtbookOne() + ","
                                + player.getBitHasHWArtbookTwo() + "," + player.getBitHasEncyclopediaEorzea() + ","
                                + player.getBitHasBeforeMeteor() + ","
                                + player.getBitHasBeforeTheFall() + "," + player.getBitHasSoundTrack() + ","
                                + player.getBitHasAttendedEternalBond() + "," + player.getBitHasCompletedHWSightseeing()
                                + "," + player.getBitHasCompleted2pt5() + "," + player.getBitHasFiftyComms() + ","
                                + player.getBitHasMooglePlush() + "," + player.getBitHasTopazCarbunclePlush() + ","
                                + player.getBitHasEmeraldCarbunclePlush() + ",");

                sbFields.append("hildibrand, ps4collectors, dideternalbond, arrcollector, kobold, sahagin, amaljaa, "
                                + "sylph, moogle, vanuvanu, vath, hw_complete, hw_31_complete, hw_33_complete, legacy_player");
                sbValues.append(player.getBitHasCompletedHildibrand() + "," + player.getBitHasPS4Collectors() + ","
                        + player.getBitHasEternalBond() + "," + player.getBitHasARRCollectors() + ","
                        + player.getBitHasKobold() + "," + player.getBitHasSahagin() + "," + player.getBitHasAmaljaa() + ","
                        + player.getBitHasSylph() + "," + player.getBitHasMoogle() + "," + player.getBitHasVanuVanu() + ","
                        + player.getBitHasVath() + "," + player.getBitHasCompletedHW() + ","
                        + player.getBitHasCompleted3pt1() + "," + player.getBitHasCompleted3pt3() + ","
                        + player.getBitIsLegacyPlayer());

            }

            if(this.storeMinions){
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("minions");
                sbValues.append("\"" + player.getMinionsString() + "\"");
            }
            if(this.storeMounts){
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("mounts");
                sbValues.append("\"" + player.getMountsString() + "\"");
            }


            if(this.storeActiveDate) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("date_active");
                java.util.Date dt = new java.util.Date();

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

                String sqlDate = sdf.format(player.getDateImgLastModified());
                sbValues.append("\"" + sqlDate + "\"");
            }
            if(this.storePlayerActive) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("is_active");
                sbValues.append(player.getBitIsActive());
            }

            sbFields.append(")");
            sbValues.append(");");

            String strSQL = sbFields.toString() + sbValues.toString();
            st.executeUpdate(strSQL);
            if(!this.quiet || this.isVerbose()) {
                strOut = "Character " + player.getId() + " written to database successfully.";
            }
            closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strOut;
    }

    /**
     * Open a connection to database.
     *
     * @return the opened connection
     * @throws SQLException exception thrown if unable to connect
     */
    protected Connection openConnection() {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            return connection;
        } catch (SQLException sqlEx) {
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
    protected void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException sqlEx) {
            System.out.println("Cannot close connection! Has it already been closed");
        }
    }

    /**
     * Get the next character ID to be parsed.
     *
     * @return the next character ID to be parsed.
     */
    public int getNextID() {
        if (nextID <= endId) {
            int next = nextID;
            //Increment id
            nextID++;
            return next;
        } else {
            return -1;
        }
    }

    /**
     * Get the first ID to be processed.
     *
     * @return the first character ID to be processed
     */
    public int getStartId() {
        return startId;
    }

    /**
     * Get the last ID to be processed.
     *
     * @return the last character ID due to be processed.
     */
    public int getEndId() {
        return endId;
    }

    /**
     * Get the maximum number of threads allowed for the Gatherer Controller instance.
     * @return the maximum number of threads allowed.
     */
    public int getThreadLimit() {
        return threadLimit;
    }

    /**
     * Set the maximum number of threads allowed for the gatherer controller instance
     * @param threadLimit the maximum number of threads allowed.
     */
    public void setThreadLimit(int threadLimit) {
        this.threadLimit = threadLimit;
    }

    /**
     * Get whether each character's minion set will be written to the database.
     * @return whether each character's minion set will be written to DB.
     */
    public boolean isStoreMinions() {
        return storeMinions;
    }

    /**
     * Set whether to store each character's minion set to the database.
     * @param storeMinions whether to store each character's minion set to the database.
     */
    public void setStoreMinions(boolean storeMinions) {
        this.storeMinions = storeMinions;
    }

    /**
     * Get whether each character's mount set will be written to the database.
     * @return whether each character's mount set will be written to DB.
     */
    public boolean isStoreMounts() {
        return storeMounts;
    }

    /**
     * Set whether each character's mount set will be written to the database.
     * @param storeMounts whether each character's mount set will be written to DB.
     */
    public void setStoreMounts(boolean storeMounts) {
        this.storeMounts = storeMounts;
    }

    /**
     * Get whether to store boolean values indicating character progression in the database.
     * @return whether to store boolean values indicating character progression in the database.
     */
    public boolean isStoreProgression() {
        return storeProgression;
    }

    /**
     * Set whether to store boolean values indicating character progression in the database.
     * @param storeProgression whether to store boolean values indicating character progression in the database.
     */
    public void setStoreProgression(boolean storeProgression) {
        this.storeProgression = storeProgression;
    }

    /**
     * Get the name of the table that character records will be written to.
     * @return the name of the table that character records will be written to.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Set the name of the table that character records will be written to.
     * @param tableName the name of the table that character records will be written to.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Whether the single player table is being split up into one table for each realm.
     * @return whether the single player table is being split up into one table for each realm.
     */
    public boolean isSplitTables() {
        return splitTables;
    }

    /**
     * Set whether the single player table should be split up into one table for each realm.
     * @param splitTables whether the single player table is being split up into one table for each realm.
     */
    public void setSplitTables(boolean splitTables) {
        this.splitTables = splitTables;
    }

    /**
     * Get the suffix to append to all tables.
     * @return the suffix to append to all tables.
     */
    public String getTableSuffix() {
        return tableSuffix;
    }

    /**
     * Set the suffix to append to all tables.
     * @param tableSuffix the suffix to append to all tables.
     */
    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }

    /**
     * Set whether to run the program in quiet mode (no console output).
     * @return whether to run the program in quiet mode
     */
    public boolean isQuiet() {
        return quiet;
    }

    /**
     * Set whether to run the program in quiet mode (print each successfully written record).
     * @param quiet whether to run the program in quiet mode.
     */
    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    /**
     * Set whether to print an output when records don't exist.
     * @return whether to print an output when records don't exist.
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Set whether to print an output when records don't exist.
     * @param verbose whether to print an output when records don't exist.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    /**
     * Get list of realms to create tables for
     * @return array of realm names
     */
    public static String[] getRealms() {
        return realms;
    }

    /**
     * Set the SQL server database url.
     * @param dbUrl the SQL server database url.
     */
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    /**
     * Set the SQL server database user.
     * @param dbUser the SQL server database user.
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * Set the SQL Server user password.
     * @param dbPassword the SQL Server user password.
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    /**
     * Get the 'safety limit' of threads that is set in the program.
     * @return the thread 'safety limit'
     */
    public static int getMaxThreads() {
        return MAX_THREADS;
    }

    /**
     * Get whether to print record write fails
     * @return whether to print record write fails
     */
    public boolean isPrintFails() {
        return printFails;
    }

    /**
     * Set whether to print record write fails
     * @param printFails whether to print record write fails
     */
    public void setPrintFails(boolean printFails) {
        this.printFails = printFails;
    }

    /**
     * Set whether to store the last active date of a character
     * @param storeActiveDate whether to store the last active date of a character
     */
    public void setStoreActiveDate(boolean storeActiveDate) {
        this.storeActiveDate = storeActiveDate;
    }

    /**
     * Set whether to store a boolean value indicating player activity
     * @param storePlayerActive whether to store a boolean value indicating player activity
     */
    public void setStorePlayerActive(boolean storePlayerActive) {
        this.storePlayerActive = storePlayerActive;
    }

    /**
     * Set whether to ignore database ssl verification warnings
     * @param dbIgnoreSSLWarn whether to ignore database ssl verification warnings
     */
    public void setDbIgnoreSSLWarn(boolean dbIgnoreSSLWarn) {
        this.dbIgnoreSSLWarn = dbIgnoreSSLWarn;
    }
}
