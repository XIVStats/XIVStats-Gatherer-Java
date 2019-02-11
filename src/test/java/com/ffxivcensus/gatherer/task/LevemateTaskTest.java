package com.ffxivcensus.gatherer.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.ffxivcensus.gatherer.GatheringStatus;

public class LevemateTaskTest {

    @Mock
    private ThreadPoolExecutor mockExecutor;
    @Mock
    private BlockingQueue<Runnable> mockQueue;
    @Mock
    private TaskFactory mockFactry;
    @Mock
    private GathererTask mockTask;
    private GatheringStatus status;
    private LevemeteTask instance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        status = new GatheringStatus();
        instance = new LevemeteTask(mockExecutor, mockFactry, status);
    }

    @After
    public void tearDown() {
        instance = null;
        status = null;
    }

    @Test
    public void testPoolTerminated() {
        when(mockExecutor.isTerminated()).thenReturn(true);

        instance.run();

        verify(mockExecutor, never()).getQueue();
    }

    @Test
    public void testPoolTerminating() {
        when(mockExecutor.isTerminating()).thenReturn(true);

        instance.run();

        verify(mockExecutor, never()).getQueue();
    }

    @Test
    public void testPoolHealthy() {
        when(mockExecutor.isTerminated()).thenReturn(false);
        when(mockExecutor.getQueue()).thenReturn(mockQueue);
        when(mockQueue.size()).thenReturn(101);

        instance.run();

        verify(mockExecutor).getQueue();
    }

    @Test
    public void testPoolRefill() {
        when(mockExecutor.isTerminated()).thenReturn(false);
        when(mockExecutor.getQueue()).thenReturn(mockQueue);
        when(mockQueue.size()).thenReturn(0, 0, 1000);
        when(mockFactry.createGatherer()).thenReturn(mockTask);

        instance.run();

        verify(mockExecutor).execute(Mockito.any());
        assertEquals(1, status.getCurrentId());
    }

    @Test
    public void testPoolRefillReachLimit() {
        when(mockExecutor.isTerminated()).thenReturn(false);
        when(mockExecutor.getQueue()).thenReturn(mockQueue);
        when(mockQueue.size()).thenReturn(0, 0, 1000);
        when(mockFactry.createGatherer()).thenReturn(mockTask);
        status.setFinishId(0);

        instance.run();

        verify(mockExecutor, never()).execute(Mockito.any());
        assertEquals(0, status.getCurrentId());
    }

    @Test
    public void testPoolRefillTerminatedDuringRefill() {
        when(mockExecutor.isTerminated()).thenReturn(false);
        when(mockExecutor.getQueue()).thenReturn(mockQueue);
        when(mockQueue.size()).thenReturn(0, 0, 1, 2, 3, 1000);
        doThrow(RejectedExecutionException.class).when(mockExecutor).execute(Mockito.any());
        when(mockFactry.createGatherer()).thenReturn(mockTask);

        instance.run();

        verify(mockExecutor).execute(Mockito.any());
        assertEquals(1, status.getCurrentId());
    }

}
