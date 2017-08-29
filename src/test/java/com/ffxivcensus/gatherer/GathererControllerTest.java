package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;
import com.ffxivcensus.gatherer.player.PlayerBeanDAO;

/**
 * Test the core functionality of the program, including CLI parameters.
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.player.PlayerBuilder
 * @see com.ffxivcensus.gatherer.Gatherer
 * @see com.ffxivcensus.gatherer.GathererController
 * @see com.ffxivcensus.gatherer.PlayerBuilderTest
 * @since v1.0
 */
public class GathererControllerTest {

    /**
     * Before running each test, drop tables and pipe output into buffer
     */
    @Before
    public void setUpDB() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        PlayerBeanDAO dao = new PlayerBeanDAO(config);

        dao.dropTable("tblplayers_test");
        dao.dropTable("tblplayers_test_two");

        for(String strRealm : GathererController.getRealms()) {
            dao.dropTable("DROP TABLE tbl" + strRealm + ";");
        }
    }

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
        config.setTableName("tblplayers_test_3");
        config.setVerbose(true);
        config.setThreadLimit(40);

        GathererController gathererController = new GathererController(config);
        try {
            gathererController.run();
        } catch(Exception e) {
        }
        PlayerBeanDAO dao = new PlayerBeanDAO(config);

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
        config.setQuiet(true);
        config.setVerbose(false);
        config.setStoreMinions(true);
        config.setStoreMounts(true);
        config.setStoreProgression(true);
        config.setTableName("tblplayers_test_two");

        GathererController gathererController = new GathererController(config);
        try {
            gathererController.run();
        } catch(Exception e) {
        }

        PlayerBeanDAO dao = new PlayerBeanDAO(config);

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

    }

    /**
     * Invoke a test run in which the single characters table is being split
     * across several tables, one for each realm.
     * Also testing non-verbose mode, debug output (print non-existant records).
     */
    @Test
    public void testRunSplitTables() throws Exception {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        config.setStartId(1557260);
        config.setEndId(1558260);
        config.setQuiet(false);
        config.setVerbose(true);
        config.setStoreMinions(false);
        config.setStoreMounts(false);
        config.setStoreProgression(false);
        config.setThreadLimit(50);
        config.setTableSuffix("_test");
        config.setSplitTables(true);

        GathererController gathererController = new GathererController(config);
        try {
            gathererController.run();
        } catch(Exception e) {
        }
        assertEquals(gathererController.getThreadLimit(), gathererController.getThreadLimit());

        // Test that records were successfully written to db
        PlayerBeanDAO dao = new PlayerBeanDAO(config);

        List<Integer> addedIDsCerberus = dao.getAdded("tblcerberus_test", config.getStartId(), config.getEndId());
        List<Integer> addedIDsShiva = dao.getAdded("tblshiva_test", config.getStartId(), config.getEndId());
        List<Integer> addedIDsMoogle = dao.getAdded("tblmoogle_test", config.getStartId(), config.getEndId());

        // Test for IDs we know exist in cerberus (realm of startID char)
        assertTrue(addedIDsCerberus.contains(config.getStartId()));
        assertTrue(addedIDsCerberus.contains(1557648));
        assertTrue(addedIDsCerberus.contains(1558244));

        assertTrue(addedIDsShiva.contains(1557297));

        // Test for ids that will exist on Moogle
        assertTrue(addedIDsMoogle.contains(1557265));
        assertTrue(addedIDsMoogle.contains(config.getEndId()));
        assertTrue(addedIDsMoogle.contains(1557301));

        // Test that gatherer has not written records that don't exist on cerberus
        assertFalse(addedIDsCerberus.contains(config.getEndId()));

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
        config.setTableName("");

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
        assertTrue(strOut.contains("Table name has not been configured correctly"));

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
        config.setTableName(null);

        GathererController gathererController = new GathererController(config);

        String strOut = gathererController.isConfigured();
        assertTrue(strOut.contains("Database URL has not been configured correctly"));
        assertTrue(strOut.contains("Database User has not been configured correctly"));
        assertTrue(strOut.contains("Database Password has not been configured correctly"));
        assertTrue(strOut.contains("Table name has not been configured correctly"));

    }
}
