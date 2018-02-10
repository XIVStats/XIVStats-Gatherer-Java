package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;
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
 * @see com.ffxivcensus.gatherer.player.PlayerBuilderIT
 * @since v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GathererControllerIT {

    @Autowired
    private ApplicationConfig config;
    private static DataSource dataSource;
    @Autowired
    private GathererController gathererController;
    @Autowired
    private PlayerBeanRepository playerRepository;

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

        gathererController.run();

        // Test for IDs we know exist
        assertNotNull(playerRepository.findOne(11886902));
        assertNotNull(playerRepository.findOne(11886903));
        assertNotNull(playerRepository.findOne(11886990));
        assertNotNull(playerRepository.findOne(11887010));

        // Test that gatherer has not written records that don't exist
        assertNull(playerRepository.findOne(11886909));

        // Test that gatherer has not 'overrun'
        assertNull(playerRepository.findOne(11887011));
        assertNull(playerRepository.findOne(11886901));
    }

    /**
     * Run the program with invalid parameters.
     */
    @Test(expected = Exception.class)
    public void testRunBasicInvalidParams() throws Exception {
        config.setStartId(11887010);

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

        gathererController.run();

        // Test for IDs we know exist
        assertNotNull(playerRepository.findOne(config.getStartId()));
        assertNotNull(playerRepository.findOne(config.getEndId()));
        assertNotNull(playerRepository.findOne(1557362));
        assertNotNull(playerRepository.findOne(1557495));

        // Test that gatherer has not written records that don't exist
        assertNull(playerRepository.findOne(1558259));

        // Test that gatherer has not 'overrun'
        assertNull(playerRepository.findOne(config.getStartId() - 1));
        assertNull(playerRepository.findOne(config.getEndId() + 1));
    }
}
