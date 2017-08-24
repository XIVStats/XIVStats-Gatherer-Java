package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.BeforeClass;
import org.junit.Test;
import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;
import com.ffxivcensus.gatherer.player.PlayerBeanDAO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
     * Before running drop the table.
     */
    @BeforeClass
    public static void setUpBaseClass() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:" + config.getDbUrl() + "/" + config.getDbName());
        hikariConfig.setUsername(config.getDbUser());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setMaximumPoolSize(config.getThreadLimit());
        hikariConfig.addDataSourceProperty("useSSL", !config.isDbIgnoreSSLWarn());

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        PlayerBeanDAO dao = new PlayerBeanDAO(config, dataSource);

        dao.dropTable("tblplayers_test");
        dao.dropTable("tblplayers_test_two");

        for(String strRealm : GathererController.getRealms()) {
            dao.dropTable("DROP TABLE tbl" + strRealm.toLowerCase() + "_test;");
        }

        dataSource.close();
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
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        String[] args = {"-is", "0",
                         "-f", "100",
                         "-t", "10",
                         "-T", "tblplayers_test",
                         "-d", config.getDbName(),
                         "-U", config.getDbUrl(),
                         "-bPq"};

        GathererController gc = Console.run(args);
        // Test that options have set attributes correctly
        assertFalse(gc.isStoreProgression()); // b
        assertTrue(gc.isStoreMinions()); // P
        assertTrue(gc.isQuiet()); // q
        assertFalse(gc.isVerbose()); // q
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
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        String[] args = {"--start=100",
                         "--finish=200",
                         "--threads", "32",
                         "-v",
                         "-d", config.getDbName(),
                         "-U", config.getDbUrl(),
                         "-u", config.getDbUser(),
                         "-p", config.getDbPassword()};
        GathererController gc = Console.run(args);

        assertEquals(gc.getStartId(), 100);
        assertEquals(gc.getEndId(), 200);
        assertEquals(gc.getThreadLimit(), 32);
        assertTrue(gc.isVerbose());
    }

    @Test
    public void TestConsoleHelpDefault() throws Exception {

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-abimqvxDFPS] -s startid -f";

        // Test for a help dialog displayed upon failure
        String[] args = {""};
        Console.run(args);
        // Check output
        assertTrue(outContent.toString().contains(strHelp));
    }

    @Test
    public void TestConsoleHelpOnFail() throws Exception {

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-abimqvxDFPS] -s startid -f";
        // Test for a help dialog displayed upon failure
        String[] args = {"-s 0"};
        Console.run(args);
        // Check output
        assertTrue(outContent.toString().contains(strHelp));

    }

    @Test
    public void TestConsoleHelp() throws Exception {

        String strHelp = "usage: java -jar XIVStats-Gatherer-Java.jar [-abimqvxDFPS] -s startid -f";

        // First test for a user requested help dialog
        String[] args = {"--help"};
        Console.run(args);
        // Check output
        assertTrue(outContent.toString().contains(strHelp));
    }

    @Test
    public void testMain() {
        String[] args = {"-s", "1100",
                         "-f", "1400"};
        Console.main(args);
        // Check output
        assertFalse(outContent.toString().contains("does not exist."));
    }

    @Test
    public void testDefault() {
        String[] args = {"-s", "500",
                         "-f", "600"};
        Console.run(args);
        // Check output
        assertFalse(outContent.toString().contains("does not exist."));
    }

    @Test
    public void testPrintFails() {
        String[] args = {"-s", "700",
                         "-f", "800",
                         "-qF"};
        Console.run(args);
        // Check output
        assertFalse(outContent.toString().contains("written to database successfully."));
        assertTrue(outContent.toString().contains("does not exist."));
    }

    @Test
    public void testSplitTables() {
        String[] args = {"-s", "900",
                         "-f", "1000",
                         "-x", "_test",
                         "-S"};
        GathererController gc = Console.run(args);
        assertTrue(gc.isSplitTables());
        assertEquals(gc.getTableSuffix(), "_test");
    }
}