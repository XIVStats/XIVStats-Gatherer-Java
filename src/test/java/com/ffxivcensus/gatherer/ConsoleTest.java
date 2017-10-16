package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        String[] args = {"-is", "0",
                         "-f", "100",
                         "-t", "10",
                         "-d", config.getDbName(),
                         "-U", config.getDbUrl(),
                         "-bP"};

        GathererController gc = Console.run(args);
        // Test that options have set attributes correctly
        assertFalse(gc.isStoreProgression()); // b
        assertTrue(gc.isStoreMinions()); // P
        assertEquals(gc.getStartId(), 0);
        assertEquals(gc.getEndId(), 100);
        assertEquals(gc.getThreadLimit(), 10);
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
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        String[] args = {"--start=100",
                         "--finish=200",
                         "--threads", "32",
                         "-d", config.getDbName(),
                         "-U", config.getDbUrl(),
                         "-u", config.getDbUser(),
                         "-p", config.getDbPassword()};
        GathererController gc = Console.run(args);

        assertEquals(gc.getStartId(), 100);
        assertEquals(gc.getEndId(), 200);
        assertEquals(gc.getThreadLimit(), 32);
    }

    @Test
    public void TestConsoleHelpDefault() throws Exception {
        // Test for a help dialog displayed upon failure
        String[] args = {""};
        GathererController gc = Console.run(args);
        // Check the Gatherer hasn't been initialized.
        Assert.assertNull(gc);
    }

    @Test
    public void TestConsoleHelpOnFail() throws Exception {
        // Test for a help dialog displayed upon failure
        String[] args = {"-s 0"};
        GathererController gc = Console.run(args);
        // Check the Gatherer hasn't been initialized.
        Assert.assertNull(gc);

    }

    @Test
    public void TestConsoleHelp() throws Exception {
        // First test for a user requested help dialog
        String[] args = {"--help"};
        GathererController gc = Console.run(args);
        // Check the Gatherer hasn't been initialized.
        Assert.assertNull(gc);
    }
}