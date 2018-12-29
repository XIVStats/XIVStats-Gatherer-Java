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

        assertEquals("Russell Tyler", player.getPlayerName());
    }

    @Test
    public void testLoadFrom22763008() throws Exception {
        instance.setPageLoader(new TestDataLodestonePageLoader());
        PlayerBean player = instance.getPlayer(22763008);

        assertEquals("R'ythri Tia", player.getPlayerName());
    }

}
