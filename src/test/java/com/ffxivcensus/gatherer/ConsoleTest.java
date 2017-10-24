package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.MissingOptionException;
import org.junit.Assert;
import org.junit.Test;
import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;

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
        String[] args = {"-is", "0",
                         "-f", "100",
                         "-t", "10",
                         "-d", "DatabaseName",
                         "-U", "jdbc:mysql://test-server",
                         "-bP"};

        Console c = new Console();
        ApplicationConfig config = c.buildConfig(Console.setupOptions(), args);

        // Test that options have set attributes correctly
        assertFalse(config.isStoreProgression()); // b
        assertTrue(config.isStoreMinions()); // P
        assertEquals(config.getStartId(), 0);
        assertEquals(config.getEndId(), 100);
        assertEquals(config.getThreadLimit(), 10);
    }

    /**
     * Make a run of the console application with the following options.
     * <ul>
     * <li>Start ID = 100</li>
     * <li>End ID = 200</li>
     * <li>Thread limit = 32</li>
     * <li>User specified db name</li>
     * <li>User specified db credentials</li>
     * </ul>
     *
     * @throws Exception
     */
    @Test
    public void testConsoleFullOptions() throws Exception {
        String[] args = {"--start=100",
                         "--finish=200",
                         "--threads", "32",
                         "-d", "DatabaseName",
                         "-U", "jdbc:mysql://test-server",
                         "-u", "user",
                         "-p", "password"};

        Console c = new Console();
        ApplicationConfig config = c.buildConfig(Console.setupOptions(), args);

        assertEquals(100, config.getStartId());
        assertEquals(200, config.getEndId());
        assertEquals(32, config.getThreadLimit());
        assertEquals("DatabaseName", config.getDbName());
        assertEquals("jdbc:mysql://test-server", config.getDbUrl());
        assertEquals("user", config.getDbUser());
        assertEquals("password", config.getDbPassword());
    }

    @Test(expected = MissingOptionException.class)
    public void testFailOnMissingMandatoryOption() throws Exception {
        // Test for a help dialog displayed upon failure
        String[] args = {"-s 0"};
        Console c = new Console();
        GathererController gc = c.prepareGatherer(c.buildConfig(Console.setupOptions(), args), args);
        // Check the Gatherer hasn't been initialized.
        Assert.assertNull(gc);

    }

    @Test(expected = MissingOptionException.class)
    public void testHelpFromFail() throws Exception {
        // First test for a user requested help dialog
        String[] args = {"--help"};
        Console c = new Console();
        GathererController gc = c.prepareGatherer(c.buildConfig(Console.setupOptions(), args), args);
        // Check the Gatherer hasn't been initialized.
        Assert.assertNull(gc);
    }

    @Test
    public void testHelpFromParam() throws Exception {
        // First test for a user requested help dialog
        String[] args = {"-s=0",
                         "-f=100",
                         "--help"};
        Console c = new Console();
        GathererController gc = c.prepareGatherer(c.buildConfig(Console.setupOptions(), args), args);
        // Check the Gatherer hasn't been initialized.
        Assert.assertNull(gc);
    }
}