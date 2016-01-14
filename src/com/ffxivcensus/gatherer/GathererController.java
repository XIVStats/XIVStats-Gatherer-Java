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
 * @see Player
 * @see Gatherer
 */
public class GathererController {

    //Configuration options
    /**
     * The JDBC URL of the database to modify
     */
    private static String dbUrl;
    /**
     * The Username of user of the SQL server user to use.
     */
    private static String dbUser;
    /**
     * The password for the user, to use.
     */
    private static String dbPassword;

    private static int startId;
    private static int endId;
    private static int nextID;
    /**
     * User-defined limit for thread count.
     */
    private static int threadLimit;
    /**
     * Safety limit for thread count - user cannot exceed this limit.
     */
    private final static int MAX_THREADS = 64;

    public static void main(String[] args) {
        //Lowest character ID
        int lowestID;
        int highestID;

        //Store start time
        long startTime = System.currentTimeMillis();

        if (args.length == 0) { //If user provides no command line arguments
            System.out.println("Usage: XIVStats.jar <lowest-id> <highest-id>");
            System.exit(1);
        } else { //Else valid set of args
            try { //Try to convert params to ints

                //Determine lowest id param
                lowestID = Integer.parseInt(args[0]);
                //Determine highest id param
                highestID = Integer.parseInt(args[1]);

                //Read in config
                try {
                    readConfig();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

                //Create DB table if it doesn't exist
                //Open connection
                Connection conn = openConnection();
                try {
                    Statement st = conn.createStatement();
                    String strSQL = "CREATE TABLE IF NOT EXISTS tblPlayers (id INTEGER PRIMARY KEY,name TEXT,realm TEXT,race TEXT,gender TEXT,grand_company TEXT"
                            + ",level_gladiator INTEGER,level_pugilist INTEGER,level_marauder INTEGER,level_lancer INTEGER,level_archer INTEGER"
                            + ",level_rogue INTEGER,level_conjurer INTEGER,level_thaumaturge INTEGER,level_arcanist INTEGER,level_darkknight INTEGER, level_machinist INTEGER"
                            + ",level_astrologian INTEGER,level_carpenter INTEGER,level_blacksmith INTEGER,level_armorer INTEGER,level_goldsmith INTEGER"
                            + ",level_leatherworker INTEGER,level_weaver INTEGER,level_alchemist INTEGER,level_culinarian INTEGER,level_miner INTEGER"
                            + ",level_botanist INTEGER,level_fisher INTEGER,p30days BIT, p60days BIT, p90days BIT, p180days BIT, p270days BIT"
                            + ",p360days BIT,p450days BIT,p630days BIT,prearr BIT,prehw BIT, artbook BIT, beforemeteor BIT, beforethefall BIT"
                            + ",soundtrack BIT,saweternalbond BIT,sightseeing BIT,arr_25_complete BIT,comm50 BIT,moogleplush BIT"
                            + ",hildibrand BIT, ps4collectors BIT, dideternalbond BIT, arrcollector BIT, kobold BIT, sahagin BIT, amaljaa BIT, sylph BIT, hwcomplete BIT, hw_31_complete BIT);";

                    st.executeUpdate(strSQL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                closeConnection(conn);


                if (highestID < lowestID) {
                    System.out.println("Error: The second argument needs to be greater than the first argument");
                } else { //Else pass values into poll method
                    startId = lowestID;
                    endId = highestID;
                    System.out.println("Starting parse of range " + startId + " to " + endId + " using " + threadLimit + " threads");
                    gatherRange();
                }

                //Get current time
                long endTime = System.currentTimeMillis();
                long seconds = (endTime - startTime) / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                String time = days + " Days, " + hours % 24 + " hrs, " + minutes % 60 + " mins, " + seconds % 60 + " secs";
                System.out.print("\nRun completed, " + (highestID - lowestID) + " records written in " + time + " (" + threadLimit + " threads)");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }

    /**
     * Read configuration from config.xml
     *
     * @throws ParserConfigurationException Indicates a serious configuration error.
     * @throws IOException                  Indicates an error reading the file specified.
     * @throws SAXException                 Indicates an error parsing XML.
     */
    public static void readConfig() throws ParserConfigurationException, IOException, SAXException {
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

        String url = "jdbc:" + elementJDBC.getElementsByTagName("url").item(0).getTextContent() + elementJDBC.getElementsByTagName("database").item(0).getTextContent();
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
    public static void gatherRange() {

        //Set next ID
        nextID = startId;

        //Start up up new threads up to limit
        //Create array to store thread references into
        Thread[] threads = new Thread[threadLimit];
        for (int index = 0; index < threadLimit; index++) {
            threads[index] = new Thread(new Gatherer());
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
     * Write a player record to the database.
     * @param player the player to write to the database.
     */
    public static void writeToDB(Player player) {
        //Open connection
        Connection conn = openConnection();
        try {
            //TODO Add hw_complete and hw_31_complete to table fields
            Statement st = conn.createStatement();
            String strSQL = "INSERT IGNORE INTO tblPlayers (id, name, realm, race, gender, grand_company, level_gladiator, level_pugilist, level_marauder,"
                    + "level_lancer, level_archer, level_rogue, level_conjurer, level_thaumaturge, level_arcanist, level_astrologian, level_darkknight, level_machinist, level_carpenter,"
                    + "level_blacksmith, level_armorer, level_goldsmith, level_leatherworker, level_weaver, level_alchemist,"
                    + "level_culinarian, level_miner, level_botanist, level_fisher, p30days, p60days, p90days, p180days, p270days, p360days, p450days, p630days,"
                    + "prearr, prehw, artbook, beforemeteor, beforethefall, soundtrack, saweternalbond, sightseeing, arr_25_complete, comm50, moogleplush,"
                    + "hildibrand, ps4collectors, dideternalbond, arrcollector, kobold, sahagin, amaljaa, sylph, hwcomplete, hw_31_complete) "
                    + "VALUES(" + player.getId() + ",\"" + player.getPlayerName() + "\",\"" + player.getRealm() + "\",\"" + player.getRace() + "\",'" + player.getGender() + "','" + player.getGrandCompany() + "'," + player.getLvlGladiator() + "," + player.getLvlPugilist() + "," + player.getLvlMarauder() + ","
                    + player.getLvlLancer() + "," + player.getLvlArcher() + "," + player.getLvlRogue() + "," + player.getLvlConjurer() + "," + player.getLvlThaumaturge() + "," + player.getLvlArcanist() + "," + player.getLvlAstrologian() + "," + player.getLvlDarkKnight() + "," + player.getLvlMachinist() + "," + player.getLvlCarpenter() + ","
                    + player.getLvlBlacksmith() + "," + player.getLvlArmorer() + "," + player.getLvlGoldsmith() + "," + player.getLvlLeatherworker() + "," + player.getLvlWeaver() + "," + player.getLvlAlchemist() + ","
                    + player.getLvlCulinarian() + "," + player.getLvlMiner() + "," + player.getLvlBotanist() + "," + player.getLvlFisher() + "," + player.getBitHas30DaysSub() + "," + player.getBitHas60DaysSub() + "," + player.getBitHas90DaysSub() + "," + player.getBitHas180DaysSub() + "," + player.getBitHas270DaysSub() + "," + player.getBitHas360DaysSub() + "," + player.getBitHas450DaysSub() + "," + player.getBitHas630DaysSub() + ","
                    + player.getBitHasPreOrderArr() + "," + player.getBitHasPreOrderHW() + "," + player.getBitHasArtBook() + "," + player.getBitHasBeforeMeteor() + "," + player.getBitHasBeforeTheFall() + "," + player.getBitHasSoundTrack() + "," + player.getBitHasAttendedEternalBond() + "," + player.getBitHasCompletedHWSightseeing() + "," + player.getBitHasCompleted2pt5() + "," + player.getBitHasFiftyComms() + "," + player.getBitHasMooglePlush() + ","
                    + player.getBitHasCompletedHildibrand() + "," + player.getBitHasPS4Collectors() + "," + player.getBitHasEternalBond() + "," + player.getBitHasARRCollectors() + "," + player.getBitHasKobold() + "," + player.getBitHasSahagin() + "," + player.getBitHasAmaljaa() + "," + player.getBitHasSylph() + "," + player.getBitHasCompletedHW() + "," + player.getBitHasCompleted3pt1()
                    + ");";

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
    private static Connection openConnection() {
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
    private static void closeConnection(Connection conn) {
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
    public static int getNextID() {
        if (nextID < endId) {
            int next = nextID;
            //Increment id
            nextID++;
            return nextID;
        } else {
            return -1;
        }
    }

    /**
     * Get the first ID to be processed.
     *
     * @return the first character ID to be processed
     */
    public static int getStartId() {
        return startId;
    }

    /**
     * Get the last ID to be processed.
     *
     * @return the last character ID due to be processed.
     */
    public static int getEndId() {
        return endId;
    }

}
