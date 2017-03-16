package com.ffxivcensus.gatherer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * JUnit test class to run tests
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.Console
 * @see com.ffxivcensus.gatherer.GathererController
 * @since v1.0
 */
public class ConsoleTest {
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
     * The database to use
     */
    private static String dbName;
    /**
     * Hostname of database, e.g. mysql://host.example.com:3306
     */
    private static String dbHost;

    /**
     * Before running drop the table.
     */
    @BeforeClass
    public static void setUpBaseClass() {

        try {
            readConfig();
            StringBuilder sbSQL = new StringBuilder();
            //DROP existing test tables
            sbSQL.append("DROP TABLE  tblplayers_test;");
            sbSQL.append(" DROP TABLE tblplayers_test_two;");

            for (String strRealm : GathererController.getRealms()) {
                sbSQL.append(" DROP TABLE " + strRealm + ";");
            }

            java.sql.Connection conn = openConnection();
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sbSQL.toString());
            } catch (SQLException e) {

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

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @org.junit.Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @org.junit.After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }


    /**
     * Make a run of the console application with the following options.
     * <ul>
     * <li>Start ID = 0</li>
     * <li>End ID = 100</li>
     * <li>Thread limit = 10</li>
     * <li>Table Name = tblplayers_test</li>
     * <li>Log mounts</li>
     * <li>Log minions</li>
     * <li>Do not store progress indicators</li>
     * <li>User specified DB name.</li>
     * </ul>
     *
     * @throws Exception
     */
    @Test
    public void testConsole() throws Exception {

        String[] args = {"-is", "0", "-f", "100", "-t", "10", "-T", "tblplayers_test", "-d", dbName,"-U",dbHost, "-bPq"};

        GathererController gc = Console.run(args);
        //Test that options have set attributes correctly
        assertFalse(gc.isStoreProgression()); //b
        assertTrue(gc.isStoreMinions()); //P
        assertTrue(gc.isQuiet()); //q
        assertFalse(gc.isVerbose()); //q
        assertEquals("tblplayers_test", gc.getTableName());
        assertEquals(gc.getStartId(), 0);
        assertEquals(gc.getEndId(), 100);
        assertEquals(gc.getThreadLimit(), 10);

        assertTrue(outContent.toString().contains("Starting parse of range 0 to 100 using 10 threads"));
        assertTrue(outContent.toString().contains("Run completed, 101 character IDs scanned"));
        assertFalse(outContent.toString().contains("does not exist"));
        assertFalse(outContent.toString().contains(" written to database successfully."));
    }

    /**
     * Make a run of the console application with the following options.
     * <ul>
     * <li>Start ID = 100</li>
     * <li>End ID = 200</li>
     * <li>Thread limit = 32</li>
     * <li>Print duds</li>
     * <li>User specified db name</li>
     * <li>User specified db credentials</li>
     * <li>Verbose mode</li>
     * </ul>
     *
     * @throws Exception
     */
    @Test
    public void testConsoleFullOptions() throws Exception {

        String[] args = {"--start=100", "--finish=200", "--threads", "32", "-v", "-d", dbName, "-U", dbHost, "-u", dbUser, "-p", dbPassword};
        GathererController gc = Console.run(args);

        assertEquals(gc.getStartId(), 100);
        assertEquals(gc.getEndId(), 200);
        assertEquals(gc.getThreadLimit(), 32);
        assertTrue(gc.isVerbose());
    }


    @Test
    public void TestConsoleHelpDefault() throws Exception {

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-abimqvxDFPS] -s startid -f";

        //Test for a help dialog displayed upon failure
        String[] args = {""};
        GathererController gc = Console.run(args);
        //Check output
        assertTrue(outContent.toString().contains(strHelp));
    }

    @Test
    public void TestConsoleHelpOnFail() throws Exception {

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-abimqvxDFPS] -s startid -f";
        //Test for a help dialog displayed upon failure
        String[] args = {"-s 0"};
        GathererController gc = Console.run(args);
        //Check output
        assertTrue(outContent.toString().contains(strHelp));

    }

    @Test
    public void TestConsoleHelp() throws Exception {

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-abimqvxDFPS] -s startid -f";

        //First test for a user requested help dialog
        String[] args = {"--help"};
        GathererController gc = Console.run(args);
        //Check output
        assertTrue(outContent.toString().contains(strHelp));
    }

    @Test
    public void testMain() {
        String[] args = {"-s", "1100", "-f", "1400"};
        Console.main(args);
        //Check output
        assertFalse(outContent.toString().contains("does not exist."));
    }

    @Test
    public void testDefault() {
        String[] args = {"-s", "500", "-f", "600"};
        GathererController gc = Console.run(args);
        //Check output
        assertFalse(outContent.toString().contains("does not exist."));
    }

    @Test
    public void testPrintFails() {
        String[] args = {"-s", "700", "-f", "800", "-qF"};
        GathererController gc = Console.run(args);
        //Check output
        assertFalse(outContent.toString().contains("written to database successfully."));
        assertTrue(outContent.toString().contains("does not exist."));
    }

    @Test
    public void testSplitTables() {
        String[] args = {"-s", "900", "-f", "1000", "-x", "_test", "-S"};
        GathererController gc = Console.run(args);
        assertTrue(gc.isSplitTables());
        assertEquals(gc.getTableSuffix(), "_test");
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
        dbHost = elementJDBC.getElementsByTagName("url").item(0).getTextContent();
        dbUser = elementJDBC.getElementsByTagName("username").item(0).getTextContent();
        dbPassword = elementJDBC.getElementsByTagName("password").item(0).getTextContent();
        dbName = elementJDBC.getElementsByTagName("database").item(0).getTextContent();
    }

    /**
     * Execute a SQL query and return the results.
     *
     * @param conn   the SQL connection to use.
     * @param strSQL the SQL statement to execute.
     * @return the result set of added rows.
     */
    public static ResultSet executeStatement(Connection conn, String strSQL) {
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(strSQL);

            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get an array list containing the added IDs returned by executing a SQL statement.
     *
     * @param conn   the SQL connection to use.
     * @param strSQL the SQL statement to execute
     * @return an array list of the IDs successfully added to DB.
     */
    public static ArrayList getAdded(Connection conn, String strSQL) {
        ResultSet rs;
        ArrayList addedIDs = new ArrayList();
        rs = executeStatement(conn, strSQL);
        //Convert dataset to array list
        try {
            while (rs.next()) {
                addedIDs.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addedIDs;
    }
}