package com.ffxivcensus.gatherer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Main class of character gathering program.
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

        if (args.length == 0) { //If user provides no command line arguments
            System.out.println("Usage: gatherer <lowest-id> <highest-id>");
            System.exit(1);
        } else { //Else valid set of args
            try { //Try to convert params to ints

                //Determine lowest id param
                lowestID = Integer.parseInt(args[0]);
                //Determine highest id param
                highestID = Integer.parseInt(args[1]);

                if (highestID < lowestID) {
                    System.out.println("Error: The second argument needs to be greater than the first argument");
                } else { //Else pass values into poll method
                    gatherRange(lowestID, highestID);
                }

                //Read in config
                try {
                    readConfig();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

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
        File fXmlFile = new File("/config.xml");
        //Initialize parsers
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        //Parse the file
        Document doc = dBuilder.parse(fXmlFile);
        //Normalize the parsed value
        doc.getDocumentElement().normalize();

        //Read out config
        Node dbNode = doc.getElementsByTagName("jdbc").item(0);
        Element eElement = (Element) dbNode;
        String url = eElement.getAttribute("url") + eElement.getAttribute("database");
        DB_URL = url;
        DB_USER = eElement.getAttribute("username");
        DB_PASSWORD = eElement.getAttribute("password");
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
                writeToDB(getPlayer(currentID));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    /**
     * Fetch a character's information from the lodestone/
     *
     * @param playerID the ID of the character to fetch
     * @return the character matching the ID provided
     */
    public static Player getPlayer(int playerID) {
        return null;
    }


    public static void writeToDB(Player player) {
    }

    /**
     * Open a connection to database.
     *
     * @return the opened connection
     * @throws SQLException exception thrown if unable to connect
     */
    private static Connection openConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return connection;
        } catch (SQLException sqlEx) {
            System.out.println("Connection failed! Please see output console");
            sqlEx.printStackTrace();
            throw new SQLException(sqlEx);
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
