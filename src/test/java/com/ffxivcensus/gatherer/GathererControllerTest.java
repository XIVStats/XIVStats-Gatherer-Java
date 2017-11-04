package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;
import com.ffxivcensus.gatherer.player.PlayerBeanDAO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

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
@RunWith(MockitoJUnitRunner.class)
public class GathererControllerTest {

    private ApplicationConfig config;
    private static DataSource dataSource;
    @Mock
    private GathererFactory mockFactory;

    @BeforeClass
    public static void setUpClass() throws ParserConfigurationException, IOException, SAXException, SQLException, LiquibaseException {
        ApplicationConfig config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:" + config.getDbUrl() + "/" + config.getDbName());
        hikariConfig.setUsername(config.getDbUser());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setMaximumPoolSize(config.getThreadLimit());
        if(config.isDbIgnoreSSLWarn()) {
            hikariConfig.addDataSourceProperty("useSSL", false);
        }

        dataSource = new HikariDataSource(hikariConfig);

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));

        Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);

        liquibase.update(new Contexts(), new LabelExpression());
    }

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
        config = ConfigurationBuilder.createBuilder().loadXMLConfiguration().getConfiguration();
        when(mockFactory.createGatherer()).thenAnswer(new Answer<Gatherer>() {

            @Override
            public Gatherer answer(InvocationOnMock invocation) throws Throwable {
                return new Gatherer();
            }

        });
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        dataSource.unwrap(HikariDataSource.class).close();
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
        config.setStartId(11886902);
        config.setEndId(11887010);
        config.setThreadLimit(40);

        GathererController gathererController = new GathererController(config, mockFactory, dataSource);

        gathererController.run();

        PlayerBeanDAO dao = new PlayerBeanDAO(config, dataSource);

        List<Integer> addedIDs = dao.getAdded(config.getStartId(), config.getEndId());

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
    @Test(expected = Exception.class)
    public void testRunBasicInvalidParams() throws Exception {
        config.setStartId(11887010);

        GathererController gathererController = new GathererController(config, mockFactory, dataSource);

        gathererController.run();
    }

    /**
     * Perform a test run of GathererController with values passed in via
     * constructor.
     */
    @Test
    public void testRunAdvancedOptions() throws Exception {
        config.setStartId(1557260);
        config.setEndId(1558260);
        config.setStoreMinions(true);
        config.setStoreMounts(true);
        config.setStoreProgression(true);

        GathererController gathererController = new GathererController(config, mockFactory, dataSource);

        gathererController.run();

        PlayerBeanDAO dao = new PlayerBeanDAO(config, dataSource);

        // Test that records were successfully written to db
        List<Integer> addedIDs = dao.getAdded(config.getStartId(), config.getEndId());

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
}
