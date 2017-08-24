package com.ffxivcensus.gatherer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

/**
 * JUnit test class to test the methods of the Player class.
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.PlayerBuilderTest
 * @since v1.0
 */
public class PlayerBuilderTest {

    /**
     * Perform a test of the getPlayer method, using character #2356533 (Aelia Sokoto, Cerberus) as the test character.
     *
     * @throws Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testGetPlayer() throws Exception {
        // Fetch object to test against (Aelia Sokoto, Cerberus)
        PlayerBean playerOne = PlayerBuilder.getPlayer(2356533, 1);

        // NOTE: All of the following tests assume various pieces of information
        // Testing information that is very unlikely to change
        assertEquals(2356533, playerOne.getId());
        assertEquals("Aelia Sokoto", playerOne.getPlayerName());
        assertEquals("Cerberus", playerOne.getRealm());

        // Following can only be assumed to be true based on info at time of test creation
        assertEquals("Miqo'te", playerOne.getRace());
        assertEquals("female", playerOne.getGender());
        assertEquals("Maelstrom", playerOne.getGrandCompany());
        assertEquals("End of Days", playerOne.getFreeCompany());

        // Test classes - levels based on those at time of test creation
        // Tank
        assertTrue(playerOne.getLvlGladiator() >= 60);
        assertTrue(playerOne.getLvlMarauder() >= 60);
        assertTrue(playerOne.getLvlDarkKnight() >= 60);

        // Melee DPS
        assertTrue(playerOne.getLvlPugilist() >= 60);
        assertTrue(playerOne.getLvlLancer() >= 60);
        assertTrue(playerOne.getLvlRogue() >= 56);
        assertTrue(playerOne.getLvlSamurai() >= 70);

        // Ranged Physical DPS
        assertTrue(playerOne.getLvlArcher() >= 52);
        assertTrue(playerOne.getLvlMachinist() >= 60);

        // Ranged Magical DPS
        assertTrue(playerOne.getLvlThaumaturge() >= 60);
        assertTrue(playerOne.getLvlArcanist() >= 60);
        assertTrue(playerOne.getLvlRedMage() >= 70);

        // Healer
        assertTrue(playerOne.getLvlConjurer() >= 60);
        assertTrue(playerOne.getLvlScholar() >= 67);
        assertTrue(playerOne.getLvlAstrologian() >= 60);

        // Disciples of the hand
        assertTrue(playerOne.getLvlCarpenter() >= 53);
        assertTrue(playerOne.getLvlBlacksmith() >= 53);
        assertTrue(playerOne.getLvlArmorer() >= 58);
        assertTrue(playerOne.getLvlGoldsmith() >= 58);
        assertTrue(playerOne.getLvlLeatherworker() >= 53);
        assertTrue(playerOne.getLvlWeaver() >= 56);
        assertTrue(playerOne.getLvlAlchemist() >= 60);
        assertTrue(playerOne.getLvlCulinarian() >= 55);

        // Disciples of the land
        assertTrue(playerOne.getLvlMiner() >= 60);
        assertTrue(playerOne.getLvlBotanist() >= 60);
        assertTrue(playerOne.getLvlFisher() >= 60);

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
        assertTrue(playerOne.isActive());
    }

    /**
     * Perform a test of the getPlayer method using character #501646 (Omega Venom, Cerberus) to test data that could
     * not be tested with other tests.
     *
     * @throws Exception exception thrown when reading non-existent character.
     */
    @Test
    public void testGetVeteranPlayer() throws Exception {
        PlayerBean player = PlayerBuilder.getPlayer(501646, 1);

        // Player has 960 days sub, make sure recorded correctly
        assertTrue(player.isHas960DaysSub());

        // Player is also a legacy player - so test for that
        assertTrue(player.isLegacyPlayer());
    }

    /**
     * Perform a test of the getPlayer method using character #13002145 (Mi Clay, Yojimbo) to test data
     * that could not be tested with other tests.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testUnplayedPlayer() throws Exception {
        PlayerBean player = PlayerBuilder.getPlayer(13002142, 1);

        // Test grand company
        assertEquals("none", player.getGrandCompany());
        assertEquals("none", player.getFreeCompany());

        // Test gender
        assertEquals("male", player.getGender());

        // Test that classes are polling correctly, arcanist level will be indicated on page by '-' should be 0 in player object.
        assertEquals(player.getLvlArcanist(), 0);

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
        assertTrue(player.getMounts().size() == 0);
    }

    /**
     * Perform a test of the getPlayer method against character #1, which has no Grand Company but does have a free company.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @Test
    public void testGetPlayerNoGCHasFC() throws Exception {
        PlayerBean player = PlayerBuilder.getPlayer(1, 1);

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
        PlayerBean player = PlayerBuilder.getPlayer(11886920, 1);

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
        PlayerBean player = PlayerBuilder.getPlayer(71, 1);

        assertTrue(player.isHasARRArtbook());
        assertTrue(player.isHasBeforeMeteor());
        assertTrue(player.isHasBeforeTheFall());
        assertTrue(player.isHasSoundtrack());
        assertTrue(player.isHasCompletedHWSightseeing());
        assertTrue(player.isHasMooglePlush());
    }

    /**
     * Perform a test of the getPlayer method, using character #2356539, which should not exist.
     */
    @Test
    public void testGetPlayerInvalid() {
        try {
            // Try to get a character that doesn't exist
            PlayerBuilder.getPlayer(2356539, 1);
            // If no exception thrown fail
            fail("Character should not exist");
        } catch(Exception e) {

        }
    }

}
