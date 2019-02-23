package com.ffxivcensus.gatherer.config;

import static org.junit.Assert.*;

import java.io.File;

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
    public void testNoXML() throws Exception {

        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadXMLConfiguration(new File("noFileHere.xml"))
                                                       .getConfiguration();

        // Test that options are set to the defaults
        assertEquals(-1, config.getStartId());
        assertEquals(Integer.MAX_VALUE, config.getEndId());
        assertEquals(ApplicationConfig.MAX_THREADS, config.getThreadLimit());
        assertEquals(ApplicationConfig.DEFAULT_DATABASE_HOST, config.getDbUrl());
        assertEquals(ApplicationConfig.DEFAULT_AUTOSTOP_GAP, config.getAutoStopGap());
    }

    /**
     * Test configuration from the Command Line
     *
     * @throws Exception
     */
    @Test
    public void testXmlBasic() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadXMLConfiguration(new File(this.getClass().getResource("/config/test_config.xml").toURI()))
                                                       .getConfiguration();

        // Test that options are set to the defaults
        assertEquals(-1, config.getStartId());
        assertEquals(Integer.MAX_VALUE, config.getEndId());
        assertEquals("mysql://testbox:3306", config.getDbUrl());
        assertEquals(32, config.getThreadLimit());
        assertEquals(10000000, config.getAutoStopLowerLimitId());
    }

    /**
     * Test configuration from the Command Line
     *
     * @throws Exception
     */
    @Test
    public void testValidCLIConfigNull() throws Exception {
        String[] args = null;

        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                                                       .getConfiguration();

        // Test that options have set attributes correctly
        assertEquals(-1, config.getStartId());
        assertEquals(Integer.MAX_VALUE, config.getEndId());
    }

    /**
     * Test configuration from the Command Line
     *
     * @throws Exception
     */
    @Test
    public void testValidCLIConfigMinimal() throws Exception {
        String[] args = {"-s", "0"};

        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                                                       .getConfiguration();

        // Test that options have set attributes correctly
        assertEquals(0, config.getStartId());
        assertEquals(Integer.MAX_VALUE, config.getEndId());
    }

    /**
     * Test configuration from the Command Line
     *
     * @throws Exception
     */
    @Test
    public void testValidCLIConfigPartial() throws Exception {
        String[] args = {"-is", "0",
                         "-f", "100",
                         "-g", "56",
                         "-t", "10",
                         "-d", "DatabaseName",
                         "-U", "jdbc:mysql://test-server"};

        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                                                       .getConfiguration();

        // Test that options have set attributes correctly
        assertEquals(0, config.getStartId());
        assertEquals(100, config.getEndId());
        assertEquals(10, config.getThreadLimit());
        assertEquals(56, config.getAutoStopGap());
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
                         "-p", "password",
                         "-a", "56000",
                         "-g", "150"};

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
        assertEquals(56000, config.getAutoStopLowerLimitId());
        assertEquals(150, config.getAutoStopGap());
    }

    @Test(expected = MissingOptionException.class)
    public void testFailOnMissingMandatoryOption() throws Exception {
        // Test for a help dialog displayed upon failure
        String[] args = {""};

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
