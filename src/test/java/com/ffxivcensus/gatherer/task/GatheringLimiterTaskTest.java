package com.ffxivcensus.gatherer.task;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ThreadPoolExecutor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.ffxivcensus.gatherer.player.CharacterStatus;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;

public class GatheringLimiterTaskTest {

    @Mock
    private ThreadPoolExecutor mockExecutor;
    @Mock
    private PlayerBeanRepository mockRepository;
    private GatheringLimiterTask instance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new GatheringLimiterTask(mockExecutor, mockRepository);
    }
    
    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testNothingGathered() {
        when(mockRepository.findTopByOrderByIdDesc()).thenReturn(null);
        when(mockRepository.findTopByCharacterStatusNotOrderByIdDesc(Mockito.any(CharacterStatus.class))).thenReturn(null);
        
        instance.run();
        
        verify(mockExecutor).shutdownNow();
    }
    
    @Test
    public void testContinueCondition() {
        PlayerBean topId = new PlayerBean();
        topId.setId(150);
        PlayerBean topValid = new PlayerBean();
        topValid.setId(100);
        
        when(mockRepository.findTopByOrderByIdDesc()).thenReturn(topId);
        when(mockRepository.findTopByCharacterStatusNotOrderByIdDesc(Mockito.any(CharacterStatus.class))).thenReturn(topValid);
        
        instance.run();
        
        verify(mockExecutor, never()).shutdownNow();
    }
    
    @Test
    public void testContinueAtMarginCondition() {
        PlayerBean topId = new PlayerBean();
        topId.setId(5100);
        PlayerBean topValid = new PlayerBean();
        topValid.setId(100);
        
        when(mockRepository.findTopByOrderByIdDesc()).thenReturn(topId);
        when(mockRepository.findTopByCharacterStatusNotOrderByIdDesc(Mockito.any(CharacterStatus.class))).thenReturn(topValid);
        
        instance.run();
        
        verify(mockExecutor, never()).shutdownNow();
    }
    
    @Test
    public void testStopCondition() {
        PlayerBean topId = new PlayerBean();
        topId.setId(5101);
        PlayerBean topValid = new PlayerBean();
        topValid.setId(100);
        
        when(mockRepository.findTopByOrderByIdDesc()).thenReturn(topId);
        when(mockRepository.findTopByCharacterStatusNotOrderByIdDesc(Mockito.any(CharacterStatus.class))).thenReturn(topValid);
        
        instance.run();
        
        verify(mockExecutor).shutdownNow();
    }

}
