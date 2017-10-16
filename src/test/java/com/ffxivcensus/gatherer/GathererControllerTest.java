package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;
import com.ffxivcensus.gatherer.player.PlayerBeanDAO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Test the core functionality of the program, including CLI parameters.
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.player.PlayerBuilder
 * @see com.ffxivcensus.gatherer.Gatherer
 * @see com.ffxivcensus.gatherer.GathererController
 * @see com.ffxivcensus.gatherer.player.PlayerBuilderTest
 * @since v1.0
 */
public class GathererControllerTest {

    /**
     * Test gathering run of range from 11886902 to 11887010
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @Test
    public void testRunBasic() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        config.setStartId(11886902);
        config.setEndId(11887010);
        config.setThreadLimit(40);

        GathererController gathererController = new GathererController(config);
        try {
            gathererController.run();
        } catch(Exception e) {
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:" + config.getDbUrl() + "/" + config.getDbName());
        hikariConfig.setUsername(config.getDbUser());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setMaximumPoolSize(config.getThreadLimit());
        if(config.isDbIgnoreSSLWarn()) {
            hikariConfig.addDataSourceProperty("useSSL", false);
        }

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        PlayerBeanDAO dao = new PlayerBeanDAO(config, dataSource);

        List<Integer> addedIDs = dao.getAdded(config.getTableName(), config.getStartId(), config.getEndId());

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

        dataSource.close();
    }

    /**
     * Run the program with invalid parameters.
     */
    @Test
    public void testRunBasicInvalidParams() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        config.setStartId(11887010);
        config.setEndId(11886902);

        // Store start time
        long startTime = System.currentTimeMillis();
        GathererController gathererController = new GathererController(config);
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
    public void testRunAdvancedOptions() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        config.setStartId(1557260);
        config.setEndId(1558260);
        config.setStoreMinions(true);
        config.setStoreMounts(true);
        config.setStoreProgression(true);

        GathererController gathererController = new GathererController(config);
        try {
            gathererController.run();
        } catch(Exception e) {
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:" + config.getDbUrl() + "/" + config.getDbName());
        hikariConfig.setUsername(config.getDbUser());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setMaximumPoolSize(config.getThreadLimit());
        if(config.isDbIgnoreSSLWarn()) {
            hikariConfig.addDataSourceProperty("useSSL", false);
        }

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        PlayerBeanDAO dao = new PlayerBeanDAO(config, dataSource);

        // Test that records were successfully written to db
        List<Integer> addedIDs = dao.getAdded(config.getTableName(), config.getStartId(), config.getEndId());

        // Test for IDs we know exist
        assertTrue(addedIDs.contains(config.getStartId()));
        assertTrue(addedIDs.contains(config.getEndId()));
        assertTrue(addedIDs.contains(1557362));
        assertTrue(addedIDs.contains(1557495));

        // Test that gatherer has not written records that don't exist
        assertFalse(addedIDs.contains(1558259));

        // Test that gatherer has not 'overrun'
        assertFalse(addedIDs.contains(config.getStartId() - 1));
        assertFalse(addedIDs.contains(config.getEndId() + 1));

        dataSource.close();
    }

    /**
     * Invoke a test run using options that will cause the program not to run.
     */
    @Test
    public void testRunMisconfigured() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();
        // Set invalid options
        config.setDbUser("");
        config.setDbPassword("");
        config.setDbUrl("mysq");

        GathererController gathererController = new GathererController(config);

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

    }

    @Test
    public void testRunMisconfiguredTwo() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();
        config.setStartId(0);
        config.setEndId(100);
        // Set invalid options
        config.setDbUser(null);
        config.setDbPassword(null);
        config.setDbUrl(null);

        GathererController gathererController = new GathererController(config);

        String strOut = gathererController.isConfigured();
        assertTrue(strOut.contains("Database URL has not been configured correctly"));
        assertTrue(strOut.contains("Database User has not been configured correctly"));
        assertTrue(strOut.contains("Database Password has not been configured correctly"));

    }
}
