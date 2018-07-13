package com.ffxivcensus.gatherer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsoleTest {
    
    private Console instance;
    
    @Before
    public void setup() {
        instance = new Console();
    }
    
    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testRunWithoutController() {
        instance.run((String[]) null);
    }

}
