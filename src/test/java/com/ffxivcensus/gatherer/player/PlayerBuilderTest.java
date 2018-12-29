package com.ffxivcensus.gatherer.player;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ffxivcensus.gatherer.lodestone.TestDataLodestonePageLoader;

public class PlayerBuilderTest {

    private PlayerBuilder instance;

    @Before
    public void setUp() {
        instance = new PlayerBuilder();
    }

    public void tearDown() {
        instance = null;
    }

    @Test
    public void testLoadFrom2256025() throws Exception {
        instance.setPageLoader(new TestDataLodestonePageLoader());
        PlayerBean player = instance.getPlayer(2256025);

        // NOTE: All of the following tests assume various pieces of information
        // Testing information that is very unlikely to change
        assertEquals(2256025, player.getId());
        assertEquals("Russell Tyler", player.getPlayerName());
        assertEquals("Omega", player.getRealm());

        // Following can only be assumed to be true based on info at time of test creation
        assertEquals("Hyur", player.getRace());
        assertEquals("male", player.getGender());
        assertEquals("Order of the Twin Adder", player.getGrandCompany());
        // assertEquals("End of Days", playerOne.getFreeCompany());

        // Test classes - levels based on those at time of test creation
        // Tank
        assertTrue(player.getLevelGladiator() == 0);
        assertTrue(player.getLevelMarauder() == 0);
        assertTrue(player.getLevelDarkknight() == 70);

        // Melee DPS
        assertTrue(player.getLevelPugilist() == 70);
        assertTrue(player.getLevelLancer() == 22);
        assertTrue(player.getLevelRogue() == 50);
        assertTrue(player.getLevelSamurai() == 65);

        // Ranged Physical DPS
        assertTrue(player.getLevelArcher() == 70);
        assertTrue(player.getLevelMachinist() == 70);

        // Ranged Magical DPS
        assertTrue(player.getLevelThaumaturge() == 70);
        assertTrue(player.getLevelArcanist() == 70);
        assertTrue(player.getLevelRedmage() == 70);

        // Healer
        assertTrue(player.getLevelConjurer() == 70);
        assertTrue(player.getLevelScholar() == 70);
        assertTrue(player.getLevelAstrologian() == 70);

        // Disciples of the hand
        assertTrue(player.getLevelCarpenter() == 27);
        assertTrue(player.getLevelBlacksmith() == 13);
        assertTrue(player.getLevelArmorer() == 17);
        assertTrue(player.getLevelGoldsmith() == 21);
        assertTrue(player.getLevelLeatherworker() == 0);
        assertTrue(player.getLevelWeaver() == 0);
        assertTrue(player.getLevelAlchemist() == 0);
        assertTrue(player.getLevelCulinarian() == 32);

        // Disciples of the land
        assertTrue(player.getLevelMiner() == 0);
        assertTrue(player.getLevelBotanist() == 46);
        assertTrue(player.getLevelFisher() == 70);

        // Test boolean values
        // Subscription periods
        assertTrue(player.isHas30DaysSub());
        assertTrue(player.isHas60DaysSub());
        assertTrue(player.isHas90DaysSub());
        assertTrue(player.isHas180DaysSub());
        assertTrue(player.isHas270DaysSub());
        assertTrue(player.isHas360DaysSub());
        assertTrue(player.isHas450DaysSub());
        assertTrue(player.isHas630DaysSub());
        assertTrue(player.isHas960DaysSub());

        // Collectibles
        assertTrue(player.isHasPreOrderArr());
        assertTrue(player.isHasPreOrderHW());
        assertTrue(player.isHasPreOrderSB());
        assertTrue(player.isHasPS4Collectors());
        assertTrue(player.isHasARRCollectors());
        // Assuming the below don't change
        assertFalse(player.isHasARRArtbook());
        assertFalse(player.isHasBeforeMeteor());
        assertFalse(player.isHasBeforeTheFall());
        assertFalse(player.isHasSoundtrack());
        assertFalse(player.isHasMooglePlush());

        // Achievements
        assertFalse(player.isHasAttendedEternalBond());
        assertFalse(player.isHasCompletedHWSightseeing());
        assertTrue(player.isHasCompleted2pt5());
        assertTrue(player.isHasFiftyComms());
        assertTrue(player.isHasCompletedHildibrand());
        assertTrue(player.isHasEternalBond());
        assertFalse(player.isHasKobold());
        assertFalse(player.isHasSahagin());
        assertFalse(player.isHasAmaljaa());
        assertFalse(player.isHasSylph());
        assertTrue(player.isHasCompletedHW());
        // Currently no way to definitively tell player has completed the SB Main Scenario
        // Currently assumes minion drop from Kugane, Temple of the Fist or Delta V4
        assertTrue(player.isHasCompletedSB());
        assertTrue(player.isHasCompleted3pt1());
        assertFalse(player.isLegacyPlayer());

        // Test minions string
        // Test for data near start
        assertTrue(player.getMinions().contains("Wayward Hatchling"));
        // Test for data in middle
        assertTrue(player.getMinions().contains("Morbol Seedling"));
        // Test for data from end
        assertTrue(player.getMinions().contains("Wind-up Sun"));

        // Test mounts string
        // Test for data from (near) start
        assertTrue(player.getMounts().contains("Company Chocobo"));
        // Test for data from middle
        assertFalse(player.getMounts().contains("Cavalry Drake"));
        // Test for data from very end
        assertTrue(player.getMounts().contains("Midgardsormr"));
    }

    @Test
    public void testLoadFrom22763008() throws Exception {
        instance.setPageLoader(new TestDataLodestonePageLoader());
        PlayerBean player = instance.getPlayer(22763008);


        // NOTE: All of the following tests assume various pieces of information
        // Testing information that is very unlikely to change
        assertEquals(22763008, player.getId());
        assertEquals("R'ythri Tia", player.getPlayerName());
        assertEquals("Tonberry", player.getRealm());

        // Following can only be assumed to be true based on info at time of test creation
        assertEquals("Miqo'te", player.getRace());
        assertEquals("male", player.getGender());
        assertEquals("none", player.getGrandCompany());
        // assertEquals("End of Days", playerOne.getFreeCompany());

        // Test classes - levels based on those at time of test creation
        // Tank
        assertTrue(player.getLevelGladiator() == 0);
        assertTrue(player.getLevelMarauder() == 8);
        assertTrue(player.getLevelDarkknight() == 0);

        // Melee DPS
        assertTrue(player.getLevelPugilist() == 0);
        assertTrue(player.getLevelLancer() == 0);
        assertTrue(player.getLevelRogue() == 0);
        assertTrue(player.getLevelSamurai() == 0);

        // Ranged Physical DPS
        assertTrue(player.getLevelArcher() == 0);
        assertTrue(player.getLevelMachinist() == 0);

        // Ranged Magical DPS
        assertTrue(player.getLevelThaumaturge() == 0);
        assertTrue(player.getLevelArcanist() == 0);
        assertTrue(player.getLevelRedmage() == 0);

        // Healer
        assertTrue(player.getLevelConjurer() == 0);
        assertTrue(player.getLevelScholar() == 0);
        assertTrue(player.getLevelAstrologian() == 0);

        // Disciples of the hand
        assertTrue(player.getLevelCarpenter() == 0);
        assertTrue(player.getLevelBlacksmith() == 0);
        assertTrue(player.getLevelArmorer() == 0);
        assertTrue(player.getLevelGoldsmith() == 0);
        assertTrue(player.getLevelLeatherworker() == 0);
        assertTrue(player.getLevelWeaver() == 0);
        assertTrue(player.getLevelAlchemist() == 0);
        assertTrue(player.getLevelCulinarian() == 0);

        // Disciples of the land
        assertTrue(player.getLevelMiner() == 0);
        assertTrue(player.getLevelBotanist() == 0);
        assertTrue(player.getLevelFisher() == 0);

        // Test boolean values
        // Subscription periods
        assertFalse(player.isHas30DaysSub());
        assertFalse(player.isHas60DaysSub());
        assertFalse(player.isHas90DaysSub());
        assertFalse(player.isHas180DaysSub());
        assertFalse(player.isHas270DaysSub());
        assertFalse(player.isHas360DaysSub());
        assertFalse(player.isHas450DaysSub());
        assertTrue(player.isHas630DaysSub());
        assertTrue(player.isHas960DaysSub());

        // Collectibles
        assertFalse(player.isHasPreOrderArr());
        assertTrue(player.isHasPreOrderHW());
        assertTrue(player.isHasPreOrderSB());
        assertTrue(player.isHasPS4Collectors());
        assertFalse(player.isHasARRCollectors());
        // Assuming the below don't change
        assertFalse(player.isHasARRArtbook());
        assertFalse(player.isHasBeforeMeteor());
        assertFalse(player.isHasBeforeTheFall());
        assertFalse(player.isHasSoundtrack());
        assertFalse(player.isHasMooglePlush());

        // Achievements
        assertFalse(player.isHasAttendedEternalBond());
        assertFalse(player.isHasCompletedHWSightseeing());
        assertFalse(player.isHasCompleted2pt5());
        assertFalse(player.isHasFiftyComms());
        assertFalse(player.isHasCompletedHildibrand());
        assertFalse(player.isHasEternalBond());
        assertFalse(player.isHasKobold());
        assertFalse(player.isHasSahagin());
        assertFalse(player.isHasAmaljaa());
        assertFalse(player.isHasSylph());
        assertFalse(player.isHasCompletedHW());
        // Currently no way to definitively tell player has completed the SB Main Scenario
        // Currently assumes minion drop from Kugane, Temple of the Fist or Delta V4
        assertFalse(player.isHasCompletedSB());
        assertFalse(player.isHasCompleted3pt1());
        assertFalse(player.isLegacyPlayer());

        // Test minions string
        // Test for data near start
        assertFalse(player.getMinions().contains("Wayward Hatchling"));
        // Test for data in middle
        assertFalse(player.getMinions().contains("Morbol Seedling"));
        // Test for data from end
        assertFalse(player.getMinions().contains("Wind-up Sun"));

        // Test mounts string
        // Test for data from (near) start
        assertFalse(player.getMounts().contains("Company Chocobo"));
        // Test for data from middle
        assertFalse(player.getMounts().contains("Cavalry Drake"));
        // Test for data from very end
        assertFalse(player.getMounts().contains("Midgardsormr"));
    }

}
