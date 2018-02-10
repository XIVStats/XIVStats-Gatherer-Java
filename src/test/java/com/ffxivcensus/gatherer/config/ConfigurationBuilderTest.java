package com.ffxivcensus.gatherer.config;

import static org.junit.Assert.*;

import org.apache.commons.cli.MissingOptionException;
import org.junit.Test;

import com.ffxivcensus.gatherer.CLIConstants;

public class ConfigurationBuilderTest {

    /**
     * Test configuration from the Command Line
     *
     * @throws Exception
     */
    @Test
    public void testValidCLIConfigPartial() throws Exception {
        String[] args = {"-is", "0",
                         "-f", "100",
                         "-t", "10",
                         "-d", "DatabaseName",
                         "-U", "jdbc:mysql://test-server"};

        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                                                       .getConfiguration();

        // Test that options have set attributes correctly
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
    public void testValidCLIConfigFull() throws Exception {
        String[] args = {"--start=100",
                         "--finish=200",
                         "--threads", "32",
                         "-d", "DatabaseName",
                         "-U", "jdbc:mysql://test-server",
                         "-u", "user",
                         "-p", "password"};

        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                                                       .getConfiguration();

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

        ConfigurationBuilder.createBuilder()
                            .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                            .getConfiguration();

    }

    @Test(expected = MissingOptionException.class)
    public void testHelpFromFail() throws Exception {
        // First test for a user requested help dialog
        String[] args = {"--help"};

        ConfigurationBuilder.createBuilder()
                            .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                            .getConfiguration();
    }

}
