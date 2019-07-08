package com.ffxivcensus.gatherer.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ffxivcensus.gatherer.edb.EorzeaDatabaseCache;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBuilder;

/**
 * JUnit test class to test the methods of the Player class.
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.player.PlayerBuilderIT
 * @since v1.0
 */
public class PlayerBuilderIT {

    private PlayerBuilder instance;
    // Slightly hacky, but mimicking the Spring singleton instance in order to save on performance
    private static EorzeaDatabaseCache edbCache;
    @Mock
    private GearItemRepository gearItemRepository;

    @BeforeClass
    public static void beforeClass() {
        edbCache = new EorzeaDatabaseCache();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new PlayerBuilder();
        instance.setEorzeaDatabaseCache(edbCache);
        instance.setGearItemRepository(gearItemRepository);
    }

    @After
    public void tearDown() {
        instance = null;
    }

    @AfterClass
    public static void afterClass() {
        edbCache = null;
    }

    /**
     * Perform a test of the getPlayer method, using character #2356533 (Aelia Sokoto, Cerberus) as the test character.
     *
     * @throws Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testGetPlayer() throws Exception {
        // Fetch object to test against (Aelia Sokoto, Cerberus)
        PlayerBean playerOne = instance.getPlayer(2356533, null);

        // NOTE: All of the following tests assume various pieces of information
        // Testing information that is very unlikely to change
        assertEquals(2356533, playerOne.getId());
        assertEquals("Nazreen Eby", playerOne.getPlayerName());
        assertEquals("Cerberus", playerOne.getRealm());

        // Following can only be assumed to be true based on info at time of test creation
        assertEquals("Au Ra", playerOne.getRace());
        assertEquals("female", playerOne.getGender());
        assertEquals("Maelstrom", playerOne.getGrandCompany());
        // assertEquals("End of Days", playerOne.getFreeCompany());

        // Test classes - levels based on those at time of test creation
        // Tank
        assertTrue(playerOne.getLevelGladiator() >= 60);
        assertTrue(playerOne.getLevelMarauder() >= 60);
        assertTrue(playerOne.getLevelDarkknight() >= 60);
        assertTrue(playerOne.getLevelGunbreaker() >= 0);

        // Melee DPS
        assertTrue(playerOne.getLevelPugilist() >= 60);
        assertTrue(playerOne.getLevelLancer() >= 60);
        assertTrue(playerOne.getLevelRogue() >= 56);
        assertTrue(playerOne.getLevelSamurai() >= 70);

        // Ranged Physical DPS
        assertTrue(playerOne.getLevelArcher() >= 52);
        assertTrue(playerOne.getLevelMachinist() >= 60);
        assertTrue(playerOne.getLevelDancer() >= 0);

        // Ranged Magical DPS
        assertTrue(playerOne.getLevelThaumaturge() >= 60);
        assertTrue(playerOne.getLevelArcanist() >= 60);
        assertTrue(playerOne.getLevelRedmage() >= 70);
        assertTrue(playerOne.getLevelBluemage() <= 60);

        // Healer
        assertTrue(playerOne.getLevelConjurer() >= 60);
        assertTrue(playerOne.getLevelScholar() >= 67);
        assertTrue(playerOne.getLevelAstrologian() >= 60);

        // Disciples of the hand
        assertTrue(playerOne.getLevelCarpenter() >= 53);
        assertTrue(playerOne.getLevelBlacksmith() >= 53);
        assertTrue(playerOne.getLevelArmorer() >= 58);
        assertTrue(playerOne.getLevelGoldsmith() >= 58);
        assertTrue(playerOne.getLevelLeatherworker() >= 53);
        assertTrue(playerOne.getLevelWeaver() >= 56);
        assertTrue(playerOne.getLevelAlchemist() >= 60);
        assertTrue(playerOne.getLevelCulinarian() >= 55);

        // Disciples of the land
        assertTrue(playerOne.getLevelMiner() >= 60);
        assertTrue(playerOne.getLevelBotanist() >= 60);
        assertTrue(playerOne.getLevelFisher() >= 60);

        // The Forbidden Land, Eureka
        assertTrue(playerOne.getLevelEureka() >= 48);

        // Test boolean values
        // Subscription periods
        assertTrue(playerOne.isHas30DaysSub());
        assertTrue(playerOne.isHas60DaysSub());
        assertTrue(playerOne.isHas90DaysSub());
        assertTrue(playerOne.isHas180DaysSub());
        assertTrue(playerOne.isHas270DaysSub());
        assertTrue(playerOne.isHas360DaysSub());
        assertTrue(playerOne.isHas450DaysSub());
        assertTrue(playerOne.isHas630DaysSub());
        assertTrue(playerOne.isHas630DaysSub());

        // Collectibles
        assertTrue(playerOne.isHasPreOrderArr());
        assertTrue(playerOne.isHasPreOrderHW());
        assertTrue(playerOne.isHasPreOrderSB());
        assertTrue(playerOne.isHasPS4Collectors());
        assertTrue(playerOne.isHasARRCollectors());
        // Assuming the below don't change
        assertFalse(playerOne.isHasARRArtbook());
        assertFalse(playerOne.isHasBeforeMeteor());
        assertFalse(playerOne.isHasBeforeTheFall());
        assertFalse(playerOne.isHasSoundtrack());
        assertFalse(playerOne.isHasMooglePlush());

        // Achievements
        assertTrue(playerOne.isHasAttendedEternalBond());
        assertFalse(playerOne.isHasCompletedHWSightseeing());
        assertTrue(playerOne.isHasCompleted2pt5());
        assertTrue(playerOne.isHasFiftyComms());
        assertTrue(playerOne.isHasCompletedHildibrand());
        assertTrue(playerOne.isHasEternalBond());
        assertTrue(playerOne.isHasKobold());
        assertTrue(playerOne.isHasSahagin());
        assertTrue(playerOne.isHasAmaljaa());
        assertTrue(playerOne.isHasSylph());
        assertTrue(playerOne.isHasCompletedHW());
        // Currently no way to definitively tell player has completed the SB Main Scenario
        // Currently assumes minion drop from Kugane, Temple of the Fist or Delta V4
        assertTrue(playerOne.isHasCompletedSB());
        assertTrue(playerOne.isHasCompleted3pt1());
        assertFalse(playerOne.isLegacyPlayer());

        // Test minions string
        // Test for data near start
        assertTrue(playerOne.getMinions().contains("Wayward Hatchling"));
        // Test for data in middle
        assertTrue(playerOne.getMinions().contains("Morbol Seedling"));
        // Test for data from end
        assertTrue(playerOne.getMinions().contains("Wind-up Sun"));

        // Test mounts string
        // Test for data from (near) start
        assertTrue(playerOne.getMounts().contains("Company Chocobo"));
        // Test for data from middle
        assertTrue(playerOne.getMounts().contains("Cavalry Drake"));
        // Test for data from very end
        assertTrue(playerOne.getMounts().contains("Midgardsormr"));

        // Is active
        // assertTrue(playerOne.isActive());
    }

    /**
     * Perform a test of the getPlayer method using character #501646 (Omega Venom, Cerberus) to test data that could
     * not be tested with other tests.
     *
     * @throws Exception exception thrown when reading non-existent character.
     */
    @Test
    public void testGetVeteranPlayer() throws Exception {
        PlayerBean player = instance.getPlayer(501646, null);

        // Player has 960 days sub, make sure recorded correctly
        assertTrue(player.isHas960DaysSub());

        // Player is also a legacy player - so test for that
        assertTrue(player.isLegacyPlayer());
    }

    /**
     * Perform a test of the getPlayer method using character #13002145 (Momo Haru, Fenrir) to test data
     * that could not be tested with other tests.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testUnplayedPlayer() throws Exception {
        PlayerBean player = instance.getPlayer(13002142, null);

        // Test grand company
        assertEquals("none", player.getGrandCompany());
        assertEquals("none", player.getFreeCompany());

        // Test gender
        assertEquals("male", player.getGender());

        // Test that classes are polling correctly, pugilist level will be indicated on page by '-' should be 0 in player object.
        assertEquals(0, player.getLevelPugilist());

        // Test fields that are true in other tests
        assertFalse(player.isHas30DaysSub());
        assertFalse(player.isHas60DaysSub());
        assertFalse(player.isHas90DaysSub());
        assertFalse(player.isHas180DaysSub());
        assertFalse(player.isHas270DaysSub());
        assertFalse(player.isHas360DaysSub());
        assertFalse(player.isHas450DaysSub());
        assertFalse(player.isHas630DaysSub());
        assertFalse(player.isHas960DaysSub());
        assertFalse(player.isHasPreOrderArr());
        assertFalse(player.isHasPreOrderHW());
        assertFalse(player.isHasAttendedEternalBond());
        assertFalse(player.isHasCompleted2pt5());
        assertFalse(player.isHasFiftyComms());
        assertFalse(player.isHasCompletedHildibrand());
        assertFalse(player.isHasPS4Collectors());
        assertFalse(player.isHasEternalBond());
        assertFalse(player.isHasKobold());
        assertFalse(player.isHasSahagin());
        assertFalse(player.isHasAmaljaa());
        assertFalse(player.isHasSylph());
        assertFalse(player.isHasCompletedHW());
        assertFalse(player.isHasCompleted3pt1());
        assertFalse(player.isHasARRCollectors());
        // Tricky to test this - testing here that it was at the very least set to some value other than what it is set to a value other
        // than that which it is initialized
        assertTrue(player.getDateImgLastModified() != new Date());

        // Test get minions method
        assertTrue(player.getMinions().size() == 0);
        assertTrue(player.getMounts().size() <= 1);
    }

    /**
     * Perform a test of the getPlayer method against character #1, which has no Grand Company but does have a free company.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testGetPlayerNoGCHasFC() throws Exception {
        PlayerBean player = instance.getPlayer(1, null);

        // Verify that grand company is "None"
        assertEquals("none", player.getGrandCompany());
        assertEquals("NegiNabe", player.getFreeCompany());
    }

    /**
     * Perform a test of the getPlayer method against character #11886920, which has no Free Company but does have a grand company.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testGetPlayerNoFCHasGC() throws Exception {
        PlayerBean player = instance.getPlayer(11886920, null);

        // Test that GC is maelstrom
        assertEquals("Maelstrom", player.getGrandCompany());
        assertEquals("none", player.getFreeCompany());
    }

    /**
     * Perform a test of the getPlayer method, using character #71 (Mirai Kuriyama, Aegis) as the test character. Testing
     * for boolean values that have not yet been tested if true condition works.
     *
     * @throws Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testGetPlayerWithAllCollectibles() throws Exception {
        PlayerBean player = instance.getPlayer(71, null);

        assertTrue(player.isHasARRArtbook());
        assertTrue(player.isHasBeforeMeteor());
        assertTrue(player.isHasBeforeTheFall());
        assertTrue(player.isHasSoundtrack());
        assertTrue(player.isHasCompletedHWSightseeing());
        assertTrue(player.isHasMooglePlush());
    }

    @Test
    public void testGetPlayerWithEureka() throws Exception {
        PlayerBean player = instance.getPlayer(2256025, null);

        assertTrue(player.getLevelEureka() > 50);
    }

    /**
     * Perform a test of the getPlayer method, using character #2356539, which should not exist.
     */
    @Test
    public void testGetPlayerInvalid() {
        try {
            // Try to get a character that doesn't exist
            PlayerBean player = instance.getPlayer(2356539, null);
            assertEquals("Character should be marked as DELETED", CharacterStatus.DELETED, player.getCharacterStatus());
        } catch(Exception e) {

        }
    }

}
