package com.ffxivcensus.gatherer;

import static org.junit.Assert.fail;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ConsoleTest {

    private Console instance;
    @Mock
    private GathererController controllerMock;

    @Before
    public void setup() {
        instance = new Console();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testRunWithoutController() {
        instance.run((String[]) null);
    }

    @Test
    public void testRunWithParseException() throws Exception {
        Mockito.doThrow(ParseException.class).when(controllerMock).run();
        instance.setGathererController(controllerMock);
        instance.run((String[]) null);
    }

}
