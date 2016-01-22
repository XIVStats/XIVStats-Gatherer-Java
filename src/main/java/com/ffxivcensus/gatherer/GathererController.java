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
 * @since 1.0pre
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
     * Setup  a gatherer using simply start id and end id. Read other configuration options from config.
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

        //Read in config
        try {
            this.readConfig();
        } catch (Exception ex) {
            System.out.println("No config found - please set variables via setters.");
        }
    }


    public GathererController(int startId, int endId, boolean storeMinions, boolean storeMounts, boolean storeProgression,
                              String dbUrl, String dbName, String dbUser, String dbPassword, int threads, String tableName) {
        this.startId = startId;
        this.endId = endId;
        this.storeMinions = storeMinions;
        this.storeMounts = storeMounts;
        this.storeProgression = storeProgression;

        //Read in config
        try {
            this.readConfig();
        } catch (Exception ex) {
        }

        this.dbUrl = "jdbc:" + dbUrl + "/" + dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.threadLimit = threads;
        this.tableName = tableName;
        this.tableSuffix = "";
    }

    public GathererController(int startId, int endId, boolean storeMinions, boolean storeMounts, boolean storeProgression,
                              String dbUrl, String dbName, String dbUser, String dbPassword, int threads,
                              String tableSuffix, boolean splitTables) {
        this.startId = startId;
        this.endId = endId;
        this.storeMinions = storeMinions;
        this.storeMounts = storeMounts;
        this.storeProgression = storeProgression;

        //Read in config
        try {
            this.readConfig();
        } catch (Exception ex) {
        }

        this.dbUrl = "jdbc:" + dbUrl + "/" + dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.threadLimit = threads;
        this.tableName = "tblplayers";
        this.tableSuffix = tableSuffix;
    }

    public void run() {
        //Store start time
        long startTime = System.currentTimeMillis();

        if (!isConfigured()) { //If not configured
            System.out.println("System configuration error");
        } else { //Else configured correctly
            try {

                if(splitTables) { //If specified to split tables
                    for (String realm : realms) { //Create a table for each realm
                        this.createTable("tbl"+realm+tableSuffix);
                    }
                } else{ //Else just create a single table
                    this.createTable(tableName);
                }

                if (startId > endId) {
                    System.out.println("Error: The second argument needs to be greater than the first argument");
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

    private boolean isConfigured() {
        boolean configured = true;
        if (this.startId == 0) {
            System.out.println("Start ID must be configured to a positive numerical value");
            configured = false;
        }
        if (this.endId == 0) {
            System.out.println("End ID must be configured to a positive numerical value");
            configured = false;
        }
        if (this.dbUrl == null) {
            System.out.println("Database URL has not been configured");
            configured = false;
        }
        if (this.dbUser == null) {
            System.out.println("Database User has not been configred");
            configured = false;
        }
        if (this.dbPassword == null) {
            System.out.println("Database Password has not been configured");
        }
        return configured;
    }

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
                sbSQL.append("prearr BIT,prehw BIT, artbook BIT, beforemeteor BIT, beforethefall BIT, ps4collectors BIT, ");
                sbSQL.append("soundtrack BIT,saweternalbond BIT,sightseeing BIT,comm50 BIT,moogleplush BIT,");
                sbSQL.append("hildibrand BIT, dideternalbond BIT, arrcollector BIT,");
                sbSQL.append("kobold BIT, sahagin BIT, amaljaa BIT, sylph BIT,");
                sbSQL.append("arr_25_complete BIT,hwcomplete BIT, hw_31_complete BIT, legacy_player BIT");
            }
            if (this.storeMounts) {
                sbSQL.append(",mounts TEXT");
            }
            if (this.storeMinions) {
                sbSQL.append(",minions TEXT");
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
    public void readConfig() throws ParserConfigurationException, IOException, SAXException {
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
        int userThreadLimit = Integer.parseInt(elementExecConf.getElementsByTagName("threads").item(0).getTextContent());
        if (userThreadLimit <= MAX_THREADS) {
            threadLimit = userThreadLimit;
        } else {
            threadLimit = 4;
        }
    }

    /**
     * Method to gather data for characters in specified range.
     */
    public void gatherRange() {

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
    public void writeToDB(Player player) {
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
                tableName = "tbl" + player.getRealm() +  tableSuffix;
            } else {
                tableName = this.tableName + tableSuffix;
            }

            sbFields.append("INSERT IGNORE INTO ").append(tableName).append(" (");
            sbValues.append(" VALUES (");

            sbFields.append("id, name, realm, race, gender, grand_company,free_company,");
            sbValues.append(player.getId() + ",\"" + player.getPlayerName() + "\",\"" + player.getRealm() + "\",\""
                            + player.getRace() + "\",'" + player.getGender() + "','" + player.getGrandCompany() + "','"
                            + player.getFreeCompany() + "',");

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

                sbFields.append("prearr, prehw, artbook, beforemeteor, beforethefall, soundtrack, saweternalbond, "
                                + "sightseeing, arr_25_complete, comm50, moogleplush,");
                sbValues.append(player.getBitHasPreOrderArr() + "," + player.getBitHasPreOrderHW() + ","
                                + player.getBitHasArtBook() + "," + player.getBitHasBeforeMeteor() + ","
                                + player.getBitHasBeforeTheFall() + "," + player.getBitHasSoundTrack() + ","
                                + player.getBitHasAttendedEternalBond() + "," + player.getBitHasCompletedHWSightseeing()
                                + "," + player.getBitHasCompleted2pt5() + "," + player.getBitHasFiftyComms() + ","
                                + player.getBitHasMooglePlush() + ",");

                sbFields.append("hildibrand, ps4collectors, dideternalbond, arrcollector, kobold, sahagin, amaljaa, "
                                + "sylph, hwcomplete, hw_31_complete, legacy_player");
                sbValues.append(player.getBitHasCompletedHildibrand() + "," + player.getBitHasPS4Collectors() + ","
                        + player.getBitHasEternalBond() + "," + player.getBitHasARRCollectors() + ","
                        + player.getBitHasKobold() + "," + player.getBitHasSahagin() + "," + player.getBitHasAmaljaa()
                        + "," + player.getBitHasSylph() + "," + player.getBitHasCompletedHW() + ","
                        + player.getBitHasCompleted3pt1() + "," + player.getBitIsLegacyPlayer());


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

                sbFields.append(")");
                sbValues.append(");");
            }

            String strSQL = sbFields.toString() + sbValues.toString();
            st.executeUpdate(strSQL);
            System.out.println("Character " + player.getId() + " written to database successfully.");
            closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

}
