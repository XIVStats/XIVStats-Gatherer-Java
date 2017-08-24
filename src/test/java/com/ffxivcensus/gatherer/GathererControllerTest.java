package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Test the core functionality of the program, including CLI parameters.
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.PlayerBuilder
 * @see com.ffxivcensus.gatherer.Gatherer
 * @see com.ffxivcensus.gatherer.GathererController
 * @see com.ffxivcensus.gatherer.PlayerBuilderTest
 * @since v1.0
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
     * The database to use
     */
    private static String dbName;
    /**
     * Hostname of database, e.g. mysql://host.example.com:3306
     */
    private static String dbHost;

    @BeforeClass
    public static void setUpBaseClass() {
        try {
            readConfig();

        } catch(ParserConfigurationException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Before running each test, drop tables and pipe output into buffer
     */
    @Before
    public void setUpDB() {
        StringBuilder sbSQL = new StringBuilder();
        // DROP existing test tables
        sbSQL.append("DROP TABLE  tblplayers_test;");
        sbSQL.append("DROP TABLE tblplayers_test_two;");

        for(String strRealm : GathererController.getRealms()) {
            sbSQL.append("DROP TABLE " + strRealm + ";");
        }

        java.sql.Connection conn = openConnection();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sbSQL.toString());
        } catch(SQLException e) {

        }
        closeConnection(conn);
    }

    /**
     * Test gathering run of range from 11886902 to 11887010
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @Test
    public void testRunBasic() throws IOException, SAXException, ParserConfigurationException {
        int startId = 11886902;
        int endId = 11887010;
        GathererController gathererController = new GathererController(startId, endId);
        gathererController.setTableName("tblplayers_test_3");
        gathererController.setVerbose(true);
        gathererController.setThreadLimit(40);
        try {
            gathererController.run();
        } catch(Exception e) {
        }
        // Test that records were successfully written to db
        java.sql.Connection conn = openConnection();
        String strSQL = "SELECT * FROM " + gathererController.getTableName() + " WHERE `id`>=" + startId + " AND `id`<=" + endId + ";";
        List<Integer> addedIDs = getAdded(conn, strSQL);

        closeConnection(conn);

        // Test for IDs we know exist
        assertTrue(addedIDs.contains(11886902));
        assertTrue(addedIDs.contains(11886903));
        assertTrue(addedIDs.contains(11886990));
        assertTrue(addedIDs.contains(11887010));

        // Test that gatherer has not written records that don't exist
        assertFalse(addedIDs.contains(11886909));

        // Test that gatherer has not 'overrun'
        assertFalse(addedIDs.contains(11887011));
        assertFalse(addedIDs.contains(11886901));
    }

    /**
     * Run the program with invalid parameters.
     */
    @Test
    public void testRunBasicInvalidParams() {
        int startId = 11887010;
        int endId = 11886902;

        // Store start time
        long startTime = System.currentTimeMillis();
        GathererController gathererController = new GathererController(startId, endId);
        try {
            gathererController.run();
        } catch(Exception e) {
        }
        long endTime = System.currentTimeMillis();
        // Program will close in less than 3 seconds if invalid params supplied
        assertTrue((endTime - startTime) <= 3000);

    }

    /**
     * Perform a test run of GathererController with values passed in via
     * constructor.
     */
    @Test
    public void testRunAdvancedOptions() {
        int startId = 1557260;
        int endId = 1558260;

        GathererController gathererController = new GathererController(startId, endId, true, false, true, true, true, dbHost, dbName,
                                                                       dbUser, dbPassword, 32, "tblplayers_test_two");
        try {
            gathererController.run();
        } catch(Exception e) {
        }

        // Test that records were successfully written to db
        java.sql.Connection conn = openConnection();
        String strSQL = "SELECT * FROM tblplayers_test_two WHERE `id`>=" + startId + " AND `id`<=" + endId + ";";
        List<Integer> addedIDs = getAdded(conn, strSQL);
        closeConnection(conn);

        // Test for IDs we know exist
        assertTrue(addedIDs.contains(startId));
        assertTrue(addedIDs.contains(endId));
        assertTrue(addedIDs.contains(1557362));
        assertTrue(addedIDs.contains(1557495));

        // Test that gatherer has not written records that don't exist
        assertFalse(addedIDs.contains(1558259));

        // Test that gatherer has not 'overrun'
        assertFalse(addedIDs.contains(startId - 1));
        assertFalse(addedIDs.contains(endId + 1));

    }

    /**
     * Invoke a test run in which the single characters table is being split
     * across several tables, one for each realm.
     * Also testing non-verbose mode, debug output (print non-existant records).
     */
    @Test
    public void testRunSplitTables() {
        int startId = 1557260;
        int endId = 1558260;

        GathererController gathererController = new GathererController(startId, endId, false, true, false, false, false, dbHost, dbName,
                                                                       dbUser, dbPassword, 50, "_test", true);
        try {
            gathererController.run();
        } catch(Exception e) {
        }
        assertEquals(gathererController.getThreadLimit(), gathererController.getThreadLimit());

        // Test that records were successfully written to db
        java.sql.Connection conn = openConnection();
        String strSQLCerberus = "SELECT * FROM tblcerberus_test WHERE `id`>=" + startId + " AND `id`<=" + endId + ";";
        List<Integer> addedIDsCerberus = getAdded(conn, strSQLCerberus);

        String strSQLShiva = "SELECT * FROM tblshiva_test WHERE `id`>=" + startId + " AND `id`<=" + endId + ";";
        List<Integer> addedIDsShiva = getAdded(conn, strSQLShiva);

        String strSQLMoogle = "SELECT * FROM tblmoogle_test WHERE `id`>=" + startId + " AND `id`<=" + endId + ";";
        List<Integer> addedIDsMoogle = getAdded(conn, strSQLMoogle);
        closeConnection(conn);

        // Test for IDs we know exist in cerberus (realm of startID char)
        assertTrue(addedIDsCerberus.contains(startId));
        assertTrue(addedIDsCerberus.contains(1557648));
        assertTrue(addedIDsCerberus.contains(1558244));

        assertTrue(addedIDsShiva.contains(1557297));

        // Test for ids that will exist on Moogle
        assertTrue(addedIDsMoogle.contains(1557265));
        assertTrue(addedIDsMoogle.contains(endId));
        assertTrue(addedIDsMoogle.contains(1557301));

        // Test that gatherer has not written records that don't exist on cerberus
        assertFalse(addedIDsCerberus.contains(endId));

    }

    /**
     * Invoke a test run using options that will cause the program not to run.
     */
    @Test
    public void testRunMisconfigured() {
        int startId = -1;
        int endId = -1;

        GathererController gathererController = new GathererController(startId, endId);
        // Set invalid options
        gathererController.setDbUser("");
        gathererController.setDbPassword("");
        gathererController.setDbUrl("mysq");
        gathererController.setTableName("");

        try {
            gathererController.run();
        } catch(Exception e) {
            assertEquals("Program not (correctly) configured", e.getMessage());
        }
        String strOut = gathererController.isConfigured();
        assertTrue(strOut.contains("Start ID must be configured to a positive numerical value"));
        assertTrue(strOut.contains("End ID must be configured to a positive numerical value"));
        assertTrue(strOut.contains("Database URL has not been configured correctly"));
        assertTrue(strOut.contains("Database User has not been configured correctly"));
        assertTrue(strOut.contains("Database Password has not been configured correctly"));
        assertTrue(strOut.contains("Table name has not been configured correctly"));

    }

    @Test
    public void testRunMisconfiguredTwo() {
        int startId = 0;
        int endId = 100;

        GathererController gathererController = new GathererController(startId, endId);
        // Set invalid options
        gathererController.setDbUser(null);
        gathererController.setDbPassword(null);
        gathererController.setDbUrl(null);
        gathererController.setTableName(null);

        String strOut = gathererController.isConfigured();
        assertTrue(strOut.contains("Database URL has not been configured correctly"));
        assertTrue(strOut.contains("Database User has not been configured correctly"));
        assertTrue(strOut.contains("Database Password has not been configured correctly"));
        assertTrue(strOut.contains("Table name has not been configured correctly"));

    }

    // Utility methods
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
    private static void closeConnection(final java.sql.Connection conn) {
        try {
            conn.close();
        } catch(SQLException sqlEx) {
            System.out.println("Cannot close connection! Has it already been closed");
        }
    }

    /**
     * Read configuration from config.xml
     *
     * @throws ParserConfigurationException Indicates a serious configuration
     *             error.
     * @throws IOException Indicates an error reading the file specified.
     * @throws SAXException Indicates an error parsing XML.
     */
    public static void readConfig() throws ParserConfigurationException, IOException, SAXException {
        // Set config file location
        File xmlFile = new File("config.xml");
        // Initialize parsers
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        // Parse the file
        Document doc = dBuilder.parse(xmlFile);

        // Read out db config
        NodeList nodesJDBC = doc.getElementsByTagName("jdbc");
        Element elementJDBC = (Element) nodesJDBC.item(0);

        String url = "jdbc:" + elementJDBC.getElementsByTagName("url").item(0).getTextContent() + "/"
                     + elementJDBC.getElementsByTagName("database").item(0).getTextContent();
        dbUrl = url;
        dbUser = elementJDBC.getElementsByTagName("username").item(0).getTextContent();
        dbPassword = elementJDBC.getElementsByTagName("password").item(0).getTextContent();
        dbName = elementJDBC.getElementsByTagName("database").item(0).getTextContent();
        dbHost = elementJDBC.getElementsByTagName("url").item(0).getTextContent();
    }

    /**
     * Execute a SQL query and return the results.
     *
     * @param conn the SQL connection to use.
     * @param strSQL the SQL statement to execute.
     * @return the result set of added rows.
     */
    public static ResultSet executeStatement(final Connection conn, final String strSQL) {
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(strSQL);

            return rs;

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get an array list containing the added IDs returned by executing a SQL
     * statement.
     *
     * @param conn the SQL connection to use.
     * @param strSQL the SQL statement to execute
     * @return an array list of the IDs successfully added to DB.
     */
    public static List<Integer> getAdded(final Connection conn, final String strSQL) {
        ResultSet rs;
        List<Integer> addedIDs = new ArrayList<>();
        rs = executeStatement(conn, strSQL);
        // Convert dataset to array list
        try {
            while(rs.next()) {
                addedIDs.add(rs.getInt(1));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return addedIDs;
    }
}
