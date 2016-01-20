package com.ffxivcensus.gatherer;

import com.sun.rowset.internal.Row;
import org.jsoup.Connection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test the core functionality of the program, including CLI parameters.
 *
 * @author Peter Reid
 * @since 1.0pre
 * @see com.ffxivcensus.gatherer.Player
 * @see com.ffxivcensus.gatherer.Gatherer
 * @see com.ffxivcensus.gatherer.GathererController
 * @see com.ffxivcensus.gatherer.PlayerTest
 */
public class GathererControllerTest {
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

    /**
     * Before running drop the table.
     */
    @BeforeClass
    public static void setUpBaseClass(){
        try {
            readConfig();
            String strSQL = "DROP TABLE  tblplayers;";
            java.sql.Connection conn = openConnection();
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(strSQL);
            } catch (SQLException e) {
                System.out.println("Error executing SQL statement to DROP TABLE");
            }
            closeConnection(conn);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test gathering run of range from 11886902 to 11887010
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @org.junit.Test
    public void testRunMain() throws IOException, SAXException, ParserConfigurationException {
        ResultSet rs;
        String[] arrParams = {"11886902","11887010"};
        GathererController.main(arrParams);
        //Array list of results
        ArrayList addedIDs = new ArrayList();

        //Test that records were successfully written to db
        java.sql.Connection conn = openConnection();
        String strSQL = "SELECT * FROM tblPlayers WHERE `id`>=" + arrParams[0] + " AND `id`<=" + arrParams[1] +";";
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(strSQL);

            //Convert dataset to array list
            while(rs.next()){
                addedIDs.add(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(conn);

        //Test for IDs we know exist
        assertTrue(addedIDs.contains(11886902));
        assertTrue(addedIDs.contains(11886903));
        assertTrue(addedIDs.contains(11886990));
        assertTrue(addedIDs.contains(11887010));

        //Test that gatherer has not written records that don't exist
        assertFalse(addedIDs.contains(11886909));

        //Test that gatherer has not 'overrun'
        assertFalse(addedIDs.contains(11887011));
        assertFalse(addedIDs.contains(11886901));
    }

    /**
     * Run the program with invalid parameters.
     */
    @org.junit.Test
    public void testRunMainInvalidParams(){
        String[] arrParams = {"11887010","11886902"};

        //Store start time
        long startTime = System.currentTimeMillis();
        GathererController.main(arrParams);
        long endTime = System.currentTimeMillis();
        //Program will close in less than 3 seconds if invalid params supplied
        assertTrue((endTime - startTime) <= 3000);

    }

    /**
     * Run the program with no params
     */
    @org.junit.Test
    public void testRunNoParams(){

        //Store start time
        long startTime = System.currentTimeMillis();
        GathererController.main(new String[0]);
        long endTime = System.currentTimeMillis();
        //Program will close in less than 3 seconds if invalid params supplied
        assertTrue((endTime - startTime) <= 3000);
    }

    //Utility methods
    /**
     * Open a connection to database.
     *
     * @return the opened connection
     * @throws SQLException exception thrown if unable to connect
     */
    private static java.sql.Connection openConnection() {
        try {
            java.sql.Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
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
    private static void closeConnection(java.sql.Connection conn) {
        try {
            conn.close();
        } catch (SQLException sqlEx) {
            System.out.println("Cannot close connection! Has it already been closed");
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

        String url = "jdbc:" + elementJDBC.getElementsByTagName("url").item(0).getTextContent() + "/" + elementJDBC.getElementsByTagName("database").item(0).getTextContent();
        dbUrl = url;
        dbUser = elementJDBC.getElementsByTagName("username").item(0).getTextContent();
        dbPassword = elementJDBC.getElementsByTagName("password").item(0).getTextContent();
    }

}