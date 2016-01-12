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
 * Main class of character gathering program. This class makes calls to fetch records from the lodestone, and then
 * subsequently writes them to the database. It also specifies the parameters to run the program with.
 *
 * @author Peter Reid
 * @see Player
 */
public class Main {

    //Configuration options
    /**
     * The JDBC URL of the database to modify
     */
    private static String DB_URL;
    /**
     * The Username of user of the MySQL server user to use.
     */
    private static String DB_USER;
    /**
     * The password for the user, to use.
     */
    private static String DB_PASSWORD;

    public static void main(String[] args) {
        //Lowest character ID
        int lowestID;
        int highestID;

        //Store start time
        long startTime = System.currentTimeMillis();

        if (args.length == 0) { //If user provides no command line arguments
            System.out.println("Usage: gatherer <lowest-id> <highest-id>");
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
                    gatherRange(lowestID, highestID);
                }


                //Get current time
                long endTime = System.currentTimeMillis();
                System.out.println("Run completed, " + (highestID - lowestID) + " records written in " + (endTime - startTime) + "ms");

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

        //Read out config
        NodeList nodes = doc.getElementsByTagName("jdbc");
        Element eElement = (Element) nodes.item(0);

        String url = eElement.getElementsByTagName("url").item(0).getTextContent() + eElement.getElementsByTagName("database").item(0).getTextContent();
        DB_URL = url;
        DB_USER = eElement.getElementsByTagName("username").item(0).getTextContent();
        DB_PASSWORD = eElement.getElementsByTagName("password").item(0).getTextContent();
    }

    /**
     * Method to gather data for characters in specified range.
     *
     * @param lowestID  the ID of the lowest character to gather.
     * @param highestID the ID of the highest character to gather.
     */
    public static void gatherRange(int lowestID, int highestID) {

        //Gather each player from min to max
        for (int currentID = lowestID; currentID <= highestID; currentID++) {
            try {
                writeToDB(Player.getPlayer(currentID));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }


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
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
    private static void closeConnection(Connection conn) throws SQLException {
        try {
            conn.close();
        } catch (SQLException sqlEx) {
            System.out.println("Cannot close connection! Has it already been closed");
        }
    }
}
