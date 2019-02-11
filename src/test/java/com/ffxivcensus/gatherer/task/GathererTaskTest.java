package com.ffxivcensus.gatherer.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ffxivcensus.gatherer.player.CharacterStatus;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;

public class GathererTaskTest {
    
    @Mock
    private PlayerBeanRepository mockRepo;
    private GathererTask instance;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new GathererTask();
        instance.setPlayerRepository(mockRepo);
    }
    
    @After
    public void tearDown() {
        instance = null;
    }
    
    @Test
    public void runWithSuccess() {
        when(mockRepo.findOne(Mockito.anyInt())).thenReturn(null);
        
        ArgumentCaptor<PlayerBean> argument = ArgumentCaptor.forClass(PlayerBean.class);
        
        instance.setPlayerId(2256025);
        instance.run();
        
        verify(mockRepo).save(argument.capture());
        assertEquals(2256025, argument.getValue().getId());
        assertEquals(CharacterStatus.ACTIVE, argument.getValue().getCharacterStatus());
    }

    @Test
    public void runWithError() {
        doThrow(RuntimeException.class).when(mockRepo).findOne(Mockito.anyInt());
        instance.run();
    }

}
