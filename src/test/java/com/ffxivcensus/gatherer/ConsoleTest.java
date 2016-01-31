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
     *     <li>Start ID = 0</li>
     *     <li>End ID = 100</li>
     *     <li>Thread limit = 10</li>
     *     <li>Table Name = tblplayers_test</li>
     *     <li>Log mounts</li>
     *     <li>Log minions</li>
     *     <li>Do not store progress indicators</li>
     *     <li>User specified DB name.</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testConsole() throws Exception {

        String[] args = {"-s", "0", "-f", "100", "-t", "10","-T","tblplayers_test","-d",dbName,"-bPq"};

        GathererController gc = Console.run(args);
        //Test that options have set attributes correctly
        assertFalse(gc.isStoreProgression()); //b
        assertTrue(gc.isStoreMinions()); //P
        assertFalse(gc.isVerbose()); //q
        assertFalse(gc.isPrintDuds()); //q
        assertEquals("tblplayers_test",gc.getTableName());
        assertEquals(gc.getStartId(),0);
        assertEquals(gc.getEndId(),100);
        assertEquals(gc.getThreadLimit(),10);
    }

    /**
     * Make a run of the console application with the following options.
     * <ul>
     *     <li>Start ID = 100</li>
     *     <li>End ID = 200</li>
     *     <li>Thread limit = 32</li>
     *     <li>Print duds</li>
     *     <li>User specified db name</li>
     *     <li>User specified db credentials</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testConsoleFullOptions() throws Exception {

        String[] args = {"--start=100", "--finish=200", "--threads", "32","-D","-d",dbName,"-U","mysql://localhost:3306","-u",dbUser,"-p",dbPassword};
        GathererController gc = Console.run(args);

        assertEquals(gc.getStartId(),100);
        assertEquals(gc.getEndId(),200);
        assertEquals(gc.getThreadLimit(),32);
        assertTrue(gc.isPrintDuds());
    }


    @Test
    public void TestConsoleHelpDefault() throws Exception{

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-bmqDPS] -s startid -f\n" +
                "            finishid [-d database-name] [-u database-user] [-p\n" +
                "            database-user-password] [-U database-url] [-T table] [-t\n" +
                "            threads]\n" +
                " -b,--do-not-store-progress               do not store boolean data\n" +
                "                                          indicating player progress\n" +
                " -d,--database <database-name>            database name\n" +
                " -D,--debug                               run program in debug mode - full\n" +
                "                                          console output\n" +
                " -f,--finish <end-id>                     the character id to conclude\n" +
                "                                          gather run at (inclusive)\n" +
                " -h,--help                                display this help message\n" +
                " -m,--store-mounts                        store mount data set for each\n" +
                "                                          player into the database\n" +
                " -P,--store-minions                       store minion data set for each\n" +
                "                                          player into the database\n" +
                " -p,--password <database-user-password>   database user password\n" +
                " -q,--quiet                               run program in quiet mode - no\n" +
                "                                          console output\n" +
                " -s,--start <start-id>                    the character id to start\n" +
                "                                          gatherer run from (inclusive)\n" +
                " -S,--split-table <table-suffix>          split tblplayers into multiple\n" +
                "                                          tables\n" +
                " -t,--threads <no-threads>                number of gatherer threads to\n" +
                "                                          run\n" +
                " -T,--table <table>                       the table to write records to\n" +
                " -u,--user <database-user>                database user\n" +
                " -U,--url <database-server-url>           the database url of the database\n" +
                "                                          server to connect to e.g.\n" +
                "                                          mysql://localhost:3306\n";

        //Test for a help dialog displayed upon failure
        String[] args = {""};
        GathererController gc = Console.run(args);
        //Check output
        assertEquals(strHelp.replaceAll("\n", " "),outContent.toString().replaceAll("\r\n", " "));

    }

    @Test
    public void TestConsoleHelpOnFail() throws Exception{

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-bmqDPS] -s startid -f\n" +
                "            finishid [-d database-name] [-u database-user] [-p\n" +
                "            database-user-password] [-U database-url] [-T table] [-t\n" +
                "            threads]\n" +
                " -b,--do-not-store-progress               do not store boolean data\n" +
                "                                          indicating player progress\n" +
                " -d,--database <database-name>            database name\n" +
                " -D,--debug                               run program in debug mode - full\n" +
                "                                          console output\n" +
                " -f,--finish <end-id>                     the character id to conclude\n" +
                "                                          gather run at (inclusive)\n" +
                " -h,--help                                display this help message\n" +
                " -m,--store-mounts                        store mount data set for each\n" +
                "                                          player into the database\n" +
                " -P,--store-minions                       store minion data set for each\n" +
                "                                          player into the database\n" +
                " -p,--password <database-user-password>   database user password\n" +
                " -q,--quiet                               run program in quiet mode - no\n" +
                "                                          console output\n" +
                " -s,--start <start-id>                    the character id to start\n" +
                "                                          gatherer run from (inclusive)\n" +
                " -S,--split-table <table-suffix>          split tblplayers into multiple\n" +
                "                                          tables\n" +
                " -t,--threads <no-threads>                number of gatherer threads to\n" +
                "                                          run\n" +
                " -T,--table <table>                       the table to write records to\n" +
                " -u,--user <database-user>                database user\n" +
                " -U,--url <database-server-url>           the database url of the database\n" +
                "                                          server to connect to e.g.\n" +
                "                                          mysql://localhost:3306\n";

        //Test for a help dialog displayed upon failure
        String[] args = {"-s 0"};
        GathererController gc = Console.run(args);
        //Check output
        assertEquals(strHelp.replaceAll("\n", " "),outContent.toString().replaceAll("\r\n", " "));

    }

    @Test
    public void TestConsoleHelp() throws Exception{

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-bmqDPS] -s startid -f\n" +
                "            finishid [-d database-name] [-u database-user] [-p\n" +
                "            database-user-password] [-U database-url] [-T table] [-t\n" +
                "            threads]\n" +
                " -b,--do-not-store-progress               do not store boolean data\n" +
                "                                          indicating player progress\n" +
                " -d,--database <database-name>            database name\n" +
                " -D,--debug                               run program in debug mode - full\n" +
                "                                          console output\n" +
                " -f,--finish <end-id>                     the character id to conclude\n" +
                "                                          gather run at (inclusive)\n" +
                " -h,--help                                display this help message\n" +
                " -m,--store-mounts                        store mount data set for each\n" +
                "                                          player into the database\n" +
                " -P,--store-minions                       store minion data set for each\n" +
                "                                          player into the database\n" +
                " -p,--password <database-user-password>   database user password\n" +
                " -q,--quiet                               run program in quiet mode - no\n" +
                "                                          console output\n" +
                " -s,--start <start-id>                    the character id to start\n" +
                "                                          gatherer run from (inclusive)\n" +
                " -S,--split-table <table-suffix>          split tblplayers into multiple\n" +
                "                                          tables\n" +
                " -t,--threads <no-threads>                number of gatherer threads to\n" +
                "                                          run\n" +
                " -T,--table <table>                       the table to write records to\n" +
                " -u,--user <database-user>                database user\n" +
                " -U,--url <database-server-url>           the database url of the database\n" +
                "                                          server to connect to e.g.\n" +
                "                                          mysql://localhost:3306\n";

        //First test for a user requested help dialog
        String[] args = {"--help"};
        GathererController gc = Console.run(args);
        //Check output
        assertEquals(strHelp.replaceAll("\n", " "),outContent.toString().replaceAll("\r\n", " "));

    }

    @Test
    public void testMain(){
        String[] args = {"-s 200", "-f=300"};
        Console.main(args);
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