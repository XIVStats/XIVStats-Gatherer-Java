package com.ffxivcensus.gatherer.task;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ffxivcensus.gatherer.GatheringStatus;

/**
 * Runnable task managing the distribution and setup of {@see Gatherer} tasks into the {@see ExecutorService}.
 * This job will cease when the {@see ExecutorService} stops accepting new jobs, as indicated by the
 * service throwing a new {@see RejectedExecutionException}.
 * 
 * @author matthew.hillier
 */
public class LevemeteTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(LevemeteTask.class);
    private final ThreadPoolExecutor gathererExecutor;
    private final TaskFactory gathererFactory;
    private final GatheringStatus gatheringStatus;

    public LevemeteTask(final ThreadPoolExecutor gathererExecutor,
                        final TaskFactory gathererFactory,
                        final GatheringStatus gatheringStatus) {
        this.gathererExecutor = gathererExecutor;
        this.gathererFactory = gathererFactory;
        this.gatheringStatus = gatheringStatus;
    }

    @Override
    public void run() {
        LOG.debug("Loading new jobs into the gatherer");

        // Check if the executor in shutdown, and if so don't do anything.
        if(gathererExecutor.isTerminated() || gathererExecutor.isTerminating()) {
            LOG.debug("LEVEMETE: SKIP - Gatherer is shutting down");
            return;
        }

        // Check number of jobs present in the executor
        // if < 100
        if(gathererExecutor.getQueue().size() < 100) {
            // then
            // - while executor pool is not at 1000
            while(gathererExecutor.getQueue().size() < 1000 && gatheringStatus.getCurrentId() < gatheringStatus.getFinishId()) {
                // -- check out next ID
                // -- create new gatherer
                GathererTask task = gathererFactory.createGatherer();
                task.setPlayerId(gatheringStatus.getNextId());
                try {
                    LOG.trace("LEVEMETE: Requesting gathering of character #{}", task.getPlayerId());
                    gathererExecutor.execute(task);
                } catch(RejectedExecutionException ree) {
                    // -- catch RejectedExecution error and stop due to executor shutdown
                    LOG.info("LEVEMETE: STOPPING - Gatherer stopped accepting new jobs.");
                }
            }
        }

        // If gathering has reached the finish ID, initiate shutdown of all new tasks
        if(gatheringStatus.getCurrentId() >= gatheringStatus.getFinishId()) {
            LOG.info("LEVEMETE: STOPPING - Initiating graceful shutdown as gatherer has reached user-specified limits.");
            gathererExecutor.shutdown();
        }
    }

}
