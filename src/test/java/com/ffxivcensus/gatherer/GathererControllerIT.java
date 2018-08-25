package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.player.CharacterStatus;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;

/**
 * Test the core functionality of the program, including CLI parameters.
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.player.PlayerBuilder
 * @see com.ffxivcensus.gatherer.task.GathererTask
 * @see com.ffxivcensus.gatherer.GathererController
 * @see com.ffxivcensus.gatherer.player.PlayerBuilderIT
 * @since v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GathererControllerIT {

    @Autowired
    private ApplicationConfig config;
    @Autowired
    private GathererController gathererController;
    @Autowired
    private PlayerBeanRepository playerRepository;

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

        // Test that gatherer has correctly identified the deleted character
        assertEquals(CharacterStatus.DELETED, playerRepository.findOne(11886909).getCharacterStatus());

        // Test that gatherer has not 'overrun'
        assertNull(playerRepository.findOne(11887011));
        assertNull(playerRepository.findOne(11886901));
    }

    /**
     * Run the program with invalid parameters.
     */
    @Test(expected = Exception.class)
    public void testRunBasicInvalidParams() throws Exception {
        config.setStartId(-1);
        config.setEndId(-100);

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
        config.setThreadLimit(100);

        gathererController.run();

        // Test for IDs we know exist
        assertNotNull(playerRepository.findOne(config.getStartId()));
        assertNotNull(playerRepository.findOne(config.getEndId()));
        assertNotNull(playerRepository.findOne(1557362));
        assertNotNull(playerRepository.findOne(1557495));

        // Test that gatherer has correctly identified the deleted character
        assertEquals(CharacterStatus.DELETED, playerRepository.findOne(1558259).getCharacterStatus());

        // Test that gatherer has not 'overrun'
        assertNull(playerRepository.findOne(config.getStartId() - 1));
        assertNull(playerRepository.findOne(config.getEndId() + 1));
    }

    @Test
    public void testRunBeyondValidCharacters() throws Exception {
        config.setStartId(50000000);
        config.setEndId(60000000);
        config.setThreadLimit(32);

        gathererController.run();

        // Test that character 50,000,000 was gathered, but didn't exist
        PlayerBean expectedDeleted = playerRepository.findOne(50000000);
        assertEquals(CharacterStatus.DELETED, expectedDeleted.getCharacterStatus());

        // Test that character 50,001,000 wasn't gathered (e.g. the gathering stopped)
        assertNull(playerRepository.findOne(50001000));
    }

    /**
     * Ignore this test for the moment. As JUnit doesn't guarantee execution order, this one can cause the previous check to fail as it's
     * already deleted the data needed by the previous test to validate it's result.
     * 
     * @throws Exception
     */
    /*@Ignore
    @Test
    public void testRunInterrupted() throws Exception {
        config.setStartId(90000000);
        config.setEndId(100000000);
        config.setThreadLimit(32);

        // Set the interrupt flag on this thread to ensure the close-down signal is already in place before gathering begins
        Thread.currentThread().interrupt();
        gathererController.run();

        // Test that character 90,000,000 hasn't been gathered
        PlayerBean expectedDeleted = playerRepository.findOne(90000000);
        assertNull(expectedDeleted);
    }*/
}
