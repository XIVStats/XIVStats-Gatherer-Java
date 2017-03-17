package com.ffxivcensus.gatherer;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * JUnit test class to test the methods of the Player class.
 *
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.PlayerTest
 * @since v1.0
 */
public class PlayerTest {

    /**
     * Perform a test of the getPlayer method, using character #2356533 (Aelia Sokoto, Cerberus) as the test character.
     *
     * @throws Exception exception thrown when reading non-existant character.
     */
    @org.junit.Test
    public void testGetPlayer() throws Exception {
        //Fetch object to test against (Aelia Sokoto, Cerberus)
        Player playerOne = Player.getPlayer(2356533,1);

        //NOTE: All of the following tests assume various pieces of information
        //Testing information that is very unlikely to change
        assertEquals(2356533, playerOne.getId());
        assertEquals("Aelia Sokoto", playerOne.getPlayerName());
        assertEquals("Cerberus", playerOne.getRealm());

        //Following can only be assumed to be true based on info at time of test creation
        assertEquals("Miqo'te", playerOne.getRace());
        assertEquals("female", playerOne.getGender());
        assertEquals("Maelstrom", playerOne.getGrandCompany());
        assertEquals("End of Days", playerOne.getFreeCompany());

        //Test classes - levels based on those at time of test creation
        //Disciples of War
        assertTrue(playerOne.getLvlGladiator() >= 60);
        assertTrue(playerOne.getLvlPugilist() >= 60);
        assertTrue(playerOne.getLvlMarauder() >= 60);
        assertTrue(playerOne.getLvlLancer() >= 60);
        assertTrue(playerOne.getLvlArcher() >= 52);
        assertTrue(playerOne.getLvlRogue() >= 56);

        //Disciples of Magic
        assertTrue(playerOne.getLvlConjurer() >= 60);
        assertTrue(playerOne.getLvlThaumaturge() >= 60);
        assertTrue(playerOne.getLvlArcanist() >= 60);

        //Extra Jobs
        assertTrue(playerOne.getLvlDarkKnight() >= 60);
        assertTrue(playerOne.getLvlMachinist() >= 60);
        assertTrue(playerOne.getLvlAstrologian() >= 60);

        //Disciples of the hand
        assertTrue(playerOne.getLvlCarpenter() >= 53);
        assertTrue(playerOne.getLvlBlacksmith() >= 53);
        assertTrue(playerOne.getLvlArmorer() >= 58);
        assertTrue(playerOne.getLvlGoldsmith() >= 58);
        assertTrue(playerOne.getLvlLeatherworker() >= 53);
        assertTrue(playerOne.getLvlWeaver() >= 56);
        assertTrue(playerOne.getLvlAlchemist() >= 60);
        assertTrue(playerOne.getLvlCulinarian() >= 55);

        //Disciples of the land
        assertTrue(playerOne.getLvlMiner() >= 60);
        assertTrue(playerOne.getLvlBotanist() >= 60);
        assertTrue(playerOne.getLvlFisher() >= 60);

        //Test boolean values
        //Subscription periods
        assertTrue(playerOne.isHas30DaysSub());
        assertEquals(playerOne.getBitHas30DaysSub(), 1);
        assertTrue(playerOne.isHas60DaysSub());
        assertEquals(playerOne.getBitHas60DaysSub(), 1);
        assertTrue(playerOne.isHas90DaysSub());
        assertEquals(playerOne.getBitHas90DaysSub(), 1);
        assertTrue(playerOne.isHas180DaysSub());
        assertEquals(playerOne.getBitHas180DaysSub(), 1);
        assertTrue(playerOne.isHas270DaysSub());
        assertEquals(playerOne.getBitHas270DaysSub(), 1);
        assertTrue(playerOne.isHas360DaysSub());
        assertEquals(playerOne.getBitHas360DaysSub(), 1);
        assertTrue(playerOne.isHas450DaysSub());
        assertEquals(playerOne.getBitHas450DaysSub(), 1);
        assertTrue(playerOne.isHas630DaysSub());
        assertEquals(playerOne.getBitHas630DaysSub(), 1);
        assertTrue(playerOne.isHas630DaysSub());
        assertEquals(playerOne.getBitHas630DaysSub(), 1);

        //Collectibles
        assertTrue(playerOne.isHasPreOrderArr());
        assertEquals(playerOne.getBitHasPreOrderArr(), 1);
        assertTrue(playerOne.isHasPreOrderHW());
        assertEquals(playerOne.getBitHasPreOrderHW(), 1);
        assertTrue(playerOne.isHasPS4Collectors());
        assertEquals(playerOne.getBitHasPS4Collectors(), 1);
        assertTrue(playerOne.isHasARRCollectors());
        assertEquals(playerOne.getBitHasARRCollectors(), 1);
        //Assuming the below don't change
        assertFalse(playerOne.isHasARRArtbook());
        assertEquals(playerOne.getBitHasArrArtbook(), 0);
        assertFalse(playerOne.isHasBeforeMeteor());
        assertEquals(playerOne.getBitHasBeforeMeteor(), 0);
        assertFalse(playerOne.isHasBeforeTheFall());
        assertEquals(playerOne.getBitHasBeforeTheFall(), 0);
        assertFalse(playerOne.isHasSoundtrack());
        assertEquals(playerOne.getBitHasSoundTrack(), 0);
        assertFalse(playerOne.isHasMooglePlush());
        assertEquals(playerOne.getBitHasMooglePlush(), 0);

        //Achievements
        assertTrue(playerOne.isHasAttendedEternalBond());
        assertEquals(playerOne.getBitHasAttendedEternalBond(), 1);
        assertFalse(playerOne.isHasCompletedHWSightseeing());
        assertEquals(playerOne.getBitHasCompletedHWSightseeing(), 0);
        assertTrue(playerOne.isHasCompleted2pt5());
        assertEquals(playerOne.getBitHasCompleted2pt5(), 1);
        assertTrue(playerOne.isHasFiftyComms());
        assertEquals(playerOne.getBitHasFiftyComms(), 1);
        assertTrue(playerOne.isHasCompletedHildibrand());
        assertEquals(playerOne.getBitHasCompletedHildibrand(), 1);
        assertTrue(playerOne.isHasEternalBond());
        assertEquals(playerOne.getBitHasEternalBond(), 1);
        assertTrue(playerOne.isHasKobold());
        assertEquals(playerOne.getBitHasKobold(), 1);
        assertTrue(playerOne.isHasSahagin());
        assertEquals(playerOne.getBitHasSahagin(), 1);
        assertTrue(playerOne.isHasAmaljaa());
        assertEquals(playerOne.getBitHasAmaljaa(), 1);
        assertTrue(playerOne.isHasSylph());
        assertEquals(playerOne.getBitHasSylph(), 1);
        assertTrue(playerOne.isHasCompletedHW());
        assertEquals(playerOne.getBitHasCompletedHW(), 1);
        assertTrue(playerOne.isHasCompleted3pt1());
        assertEquals(playerOne.getBitHasCompleted3pt1(), 1);
        assertFalse(playerOne.getIsLegacyPlayer());
        assertEquals(playerOne.getBitIsLegacyPlayer(), 0);

        //Test minions string
        //Test for data near start
        assertTrue(playerOne.getMinionsString().contains("Wayward Hatchling,Storm Hatchling,Flame Hatchling"));
        //Test for data in middle
        assertTrue(playerOne.getMinionsString().contains("Morbol Seedling"));
        //Test for data from end
        assertTrue(playerOne.getMinionsString().contains("Wind-up Sun"));

        //Test mounts string
        //Test for data from (near) start
        assertTrue(playerOne.getMountsString().contains("Company Chocobo,Draught Chocobo,Ceremony Chocobo,Black Chocobo"));
        //Test for data from middle
        assertTrue(playerOne.getMountsString().contains("Cavalry Drake,Cavalry Elbst"));
        //Test for data from very end
        assertTrue(playerOne.getMountsString().contains("Midgardsormr"));

        //Is active
        assertTrue(playerOne.isActive());
    }

    /**
     * Perform a test of the getPlayer method using character #501646 (Omega Venom, Cerberus) to test data that could
     * not be tested with other tests.
     *
     * @throws Exception exception thrown when reading non-existent character.
     */
    @org.junit.Test
    public void testGetVeteranPlayer() throws Exception {
        Player player = Player.getPlayer(501646,1);

        //Player has 960 days sub, make sure recorded correctly
        assertTrue(player.isHas960DaysSub());
        assertEquals(player.getBitHas960DaysSub(), 1);

        //Player is also a legacy player - so test for that
        assertTrue(player.getIsLegacyPlayer());
        assertEquals(player.getBitIsLegacyPlayer(), 1);
    }


    /**
     * Perform a test of the getPlayer method using character #13002145 (Mi Clay, Yojimbo) to test data
     * that could not be tested with other tests.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @org.junit.Test
    public void testUnplayedPlayer() throws Exception {
        Player player = Player.getPlayer(13002142,1);

        //Test grand company
        assertEquals("none", player.getGrandCompany());
        assertEquals("none", player.getFreeCompany());

        //Test gender
        assertEquals("male", player.getGender());

        //Test that classes are polling correctly, arcanist level will be indicated on page by '-' should be 0 in player object.
        assertEquals(player.getLvlArcanist(), 0);

        //Test fields that are true in other tests
        assertEquals(player.getBitHas30DaysSub(), 0);
        assertEquals(player.getBitHas60DaysSub(), 0);
        assertEquals(player.getBitHas90DaysSub(), 0);
        assertEquals(player.getBitHas180DaysSub(), 0);
        assertEquals(player.getBitHas270DaysSub(), 0);
        assertEquals(player.getBitHas360DaysSub(), 0);
        assertEquals(player.getBitHas450DaysSub(), 0);
        assertEquals(player.getBitHas630DaysSub(), 0);
        assertEquals(player.getBitHas960DaysSub(), 0);
        assertEquals(player.getBitHasPreOrderArr(), 0);
        assertEquals(player.getBitHasPreOrderHW(), 0);
        assertEquals(player.getBitHasAttendedEternalBond(), 0);
        assertEquals(player.getBitHasCompleted2pt5(), 0);
        assertEquals(player.getBitHasFiftyComms(), 0);
        assertEquals(player.getBitHasCompletedHildibrand(), 0);
        assertEquals(player.getBitHasPS4Collectors(), 0);
        assertEquals(player.getBitHasEternalBond(), 0);
        assertEquals(player.getBitHasKobold(), 0);
        assertEquals(player.getBitHasSahagin(), 0);
        assertEquals(player.getBitHasAmaljaa(), 0);
        assertEquals(player.getBitHasSylph(), 0);
        assertEquals(player.getBitHasCompletedHW(), 0);
        assertEquals(player.getBitHasCompleted3pt1(), 0);
        assertEquals(player.getBitHasARRCollectors(), 0);
        //Tricky to test this - testing here that it was at the very least set to some value other than what it is set to a value other than that which it is initialized
        assertTrue(player.getDateImgLastModified() != new Date());
        assertFalse(player.isActive());

        //Test get minions method
        assertTrue(player.getMinions().size() == 0);
        assertTrue(player.getMounts().size() == 0);
    }

    /**
     * Perform a test of the getPlayer method against character #1, which has no Grand Company but does have a free company.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @org.junit.Test
    public void testGetPlayerNoGCHasFC() throws Exception {
        Player player = Player.getPlayer(1,1);

        //Verify that grand company is "None"
        assertEquals("none", player.getGrandCompany());
        assertEquals("NegiNabe", player.getFreeCompany());
    }

    /**
     * Perform a test of the getPlayer method against character #11886920, which has no Free Company but does have a grand company.
     *
     * @throws Exception Exception exception thrown when reading non-existant character.
     */
    @org.junit.Test
    public void testGetPlayerNoFCHasGC() throws Exception {
        Player player = Player.getPlayer(11886920,1);

        //Test that GC is maelstrom
        assertEquals("Maelstrom", player.getGrandCompany());
        assertEquals("none", player.getFreeCompany());
    }


    /**
     * Perform a test of the getPlayer method, using character #71 (Mirai Kuriyama, Aegis) as the test character. Testing
     * for boolean values that have not yet been tested if true condition works.
     *
     * @throws Exception exception thrown when reading non-existant character.
     */
    @org.junit.Test
    public void testGetPlayerWithAllCollectibles() throws Exception {
        Player player = Player.getPlayer(71,1);

        assertEquals(player.getBitHasArrArtbook(), 1);
        assertEquals(player.getBitHasBeforeMeteor(), 1);
        assertEquals(player.getBitHasBeforeTheFall(), 1);
        assertEquals(player.getBitHasSoundTrack(), 1);
        assertEquals(player.getBitHasCompletedHWSightseeing(), 1);
        assertEquals(player.getBitHasMooglePlush(), 1);
    }

    /**
     * Perform a test of the getPlayer method, using character #2356539, which should not exist.
     */
    @org.junit.Test
    public void testGetPlayerInvalid() {
        try {
            //Try to get a character that doesn't exist
            Player player = Player.getPlayer(2356539,1);
            //If no exception thrown fail
            fail("Character should not exist");
        } catch (Exception e) {

        }
    }

}
