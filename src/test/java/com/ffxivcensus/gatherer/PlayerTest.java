package com.ffxivcensus.gatherer;

import com.ffxivcensus.gatherer.Player;
import static org.junit.Assert.*;

/**
 * JUnit test class to test the methods of the Player class.
 *
 * @author Peter Reid
 * @see Player
 * @since 1.0
 */
public class PlayerTest {

    /**
     * Perform a test of the getPlayer method, using character #2356533 (Aelia Sokoto, Cerberus) as the test character.
     * @throws Exception excception thrown when reading non-existant character.
     */
    @org.junit.Test
    public void testGetPlayer() throws Exception {
        //Fetch object to test against (Aelia Sokoto, Cerberus)
        Player playerOne = Player.getPlayer(2356533);

        //NOTE: All of the following tests assume various pieces of information
        //Testing information that is very unlikely to change
        assertEquals(playerOne.getId(),2356533);
        assertEquals(playerOne.getPlayerName(),"Aelia Sokoto");
        assertEquals(playerOne.getRealm(), "Cerberus");

        //Following can only be assumed to be true based on info at time of test creation
        assertEquals(playerOne.getRace(),"Miqo'te");
        assertEquals(playerOne.getGender(),"female");
        assertEquals(playerOne.getGrandCompany(),"Maelstrom");


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
        assertTrue(playerOne.getLvlAstrologian() >= 60);

        //Extra Jobs
        assertTrue(playerOne.getLvlDarkKnight() >= 60);
        assertTrue(playerOne.getLvlMachinist() >= 60);
        assertTrue(playerOne.getLvlAstrologian() >= 60);

        //Disciples of the hand
        assertTrue(playerOne.getLvlCarpenter() >= 53);
        assertTrue(playerOne.getLvlBlacksmith() >= 53);
        assertTrue(playerOne.getLvlArmorer() >=  58);
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
        assertTrue(playerOne.isHas60DaysSub());
        assertTrue(playerOne.isHas90DaysSub());
        assertTrue(playerOne.isHas180DaysSub());
        assertTrue(playerOne.isHas270DaysSub());
        assertTrue(playerOne.isHas360DaysSub());
        assertTrue(playerOne.isHas450DaysSub());
        assertTrue(playerOne.isHas630DaysSub());
        assertTrue(playerOne.isHas630DaysSub());

        //Collectibles
        assertTrue(playerOne.isHasPreOrderArr());
        assertTrue(playerOne.isHasPreOrderHW());
        assertTrue(playerOne.isHasPS4Collectors());
        assertTrue(playerOne.isHasARRCollectors());
        //Assuming the below don't change
        assertFalse(playerOne.isHasArtbook());
        assertFalse(playerOne.isHasBeforeMeteor());
        assertFalse(playerOne.isHasBeforeTheFall());
        assertFalse(playerOne.isHasSoundtrack());
        assertFalse(playerOne.isHasMooglePlush());

        //Achievements
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
        assertFalse(playerOne.getIsLegacyPlayer());
    }

    /**
     * Perform a test of the getPlayer method, using character #2356539, which should not exist.
     */
    @org.junit.Test
    public void testGetPlayerInvalid() {
        try{
            //Try to get a character that doesn't exist
            Player player = Player.getPlayer(2356539);
            //If no exception thrown fail
            fail("Character should not exist");
        } catch (Exception e){

        }
    }

}