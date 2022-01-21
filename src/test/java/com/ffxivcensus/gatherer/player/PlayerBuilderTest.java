package com.ffxivcensus.gatherer.player;

import com.ffxivcensus.gatherer.edb.EorzeaDatabaseCache;
import com.ffxivcensus.gatherer.lodestone.TestDataLodestonePageLoader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerBuilderTest {

    private PlayerBuilder instance;
    private static EorzeaDatabaseCache EDB_CACHE = new EorzeaDatabaseCache();

    @Before
    public void setUp() {
        instance = new PlayerBuilder();
        // TODO: Figure out a mock of this
        instance.setEorzeaDatabaseCache(EDB_CACHE);
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
        assertEquals(80, player.getLevelGladiator());
        assertEquals(32, player.getLevelMarauder());
        assertEquals(80, player.getLevelDarkknight());
        assertEquals(82, player.getLevelGunbreaker());

        // Melee DPS
        assertEquals(80, player.getLevelPugilist());
        assertEquals(80, player.getLevelLancer());
        assertEquals(80, player.getLevelRogue());
        assertEquals(80, player.getLevelSamurai());
        assertEquals(71, player.getLevelReaper());

        // Ranged Physical DPS
        assertEquals(80, player.getLevelArcher());
        assertEquals(80, player.getLevelMachinist());
        assertEquals(80, player.getLevelDancer());

        // Ranged Magical DPS
        assertEquals(80, player.getLevelThaumaturge());
        assertEquals(86, player.getLevelArcanist());
        assertEquals(80, player.getLevelRedmage());
        assertEquals(70, player.getLevelBluemage());

        // Healer
        assertEquals(80, player.getLevelConjurer());
        assertEquals(86, player.getLevelScholar());
        assertEquals(80, player.getLevelAstrologian());
        assertEquals(74, player.getLevelSage());

        // Disciples of the hand
        assertEquals(80, player.getLevelCarpenter());
        assertEquals(34, player.getLevelBlacksmith());
        assertEquals(25, player.getLevelArmorer());
        assertEquals(26, player.getLevelGoldsmith());
        assertEquals(0, player.getLevelLeatherworker());
        assertEquals(0, player.getLevelWeaver());
        assertEquals(80, player.getLevelAlchemist());
        assertEquals(80, player.getLevelCulinarian());

        // Disciples of the land
        assertEquals(25, player.getLevelMiner());
        assertEquals(80, player.getLevelBotanist());
        assertEquals(87, player.getLevelFisher());

        // Bozjan Southern Front
        assertEquals(25, player.getLevelBozja());

        // The Forbidden Land, Eureka
        assertEquals(60, player.getLevelEureka());

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
        assertTrue(player.isHasSylph());
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
	public void testLoadFrom33000061() throws Exception {
		instance.setPageLoader(new TestDataLodestonePageLoader());
		PlayerBean player = instance.getPlayer(33000061);

		assertEquals(33000061, player.getId());
		assertTrue(player.getMinions().size() == 0);
		assertTrue(player.getMounts().size() > 0);

	}

	@Test
	public void testLoadFrom33000046() throws Exception {
		instance.setPageLoader(new TestDataLodestonePageLoader());
		PlayerBean player = instance.getPlayer(33000046);

		assertEquals(33000046, player.getId());
		assertTrue(player.getMinions().size() > 0);
		assertTrue(player.getMounts().size() == 0);

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
        assertEquals("Maelstrom", player.getGrandCompany());
        // assertEquals("End of Days", playerOne.getFreeCompany());

        // Test classes - levels based on those at time of test creation
        // Tank
        assertEquals(0, player.getLevelGladiator());
        assertEquals(52, player.getLevelMarauder());
        assertEquals(0, player.getLevelDarkknight());
        assertEquals(0, player.getLevelGunbreaker());

        // Melee DPS
        assertEquals(0, player.getLevelPugilist());
        assertEquals(0, player.getLevelLancer());
        assertEquals(0, player.getLevelRogue());
        assertEquals(0, player.getLevelSamurai());

        // Ranged Physical DPS
        assertEquals(0, player.getLevelArcher());
        assertEquals(0, player.getLevelMachinist());
        assertEquals(0, player.getLevelDancer());

        // Ranged Magical DPS
        assertEquals(0, player.getLevelThaumaturge());
        assertEquals(34, player.getLevelArcanist());
        assertEquals(0, player.getLevelRedmage());
        assertEquals(22, player.getLevelBluemage());

        // Healer
        assertEquals(0, player.getLevelConjurer());
        assertEquals(34, player.getLevelScholar());
        assertEquals(0, player.getLevelAstrologian());

        // Disciples of the hand
        assertEquals(0, player.getLevelCarpenter());
        assertEquals(0, player.getLevelBlacksmith());
        assertEquals(0, player.getLevelArmorer());
        assertEquals(0, player.getLevelGoldsmith());
        assertEquals(0, player.getLevelLeatherworker());
        assertEquals(0, player.getLevelWeaver());
        assertEquals(0, player.getLevelAlchemist());
        assertEquals(0, player.getLevelCulinarian());

        // Disciples of the land
        assertEquals(0, player.getLevelMiner());
        assertEquals(0, player.getLevelBotanist());
        assertEquals(0, player.getLevelFisher());

        // The Forbidden Land, Eureka
        assertEquals(0, player.getLevelEureka());

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
        assertTrue(player.getMounts().contains("Company Chocobo"));
        // Test for data from middle
        assertFalse(player.getMounts().contains("Cavalry Drake"));
        // Test for data from very end
        assertFalse(player.getMounts().contains("Midgardsormr"));
    }

}
