package com.ffxivcensus.gatherer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.player.CharacterStatus;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;
import com.ffxivcensus.gatherer.player.PlayerBuilder;
import com.ffxivcensus.gatherer.task.GathererTask;
import com.ffxivcensus.gatherer.task.GatheringLimiterTask;
import com.ffxivcensus.gatherer.task.LevemeteTask;
import com.ffxivcensus.gatherer.task.TaskFactory;

/**
 * GathererController class of character gathering program. This class makes calls to fetch records from the lodestone, and then
 * subsequently writes them to the database. It also specifies the parameters to run the program with.
 *
 * @author Peter Reid
 * @since v1.0
 * @see PlayerBuilder
 * @see GathererTask
 */
@Service
public class GathererController {
    private static final Logger LOG = LoggerFactory.getLogger(GathererController.class);
    private ApplicationConfig appConfig;
    private final TaskFactory taskFactory;
    private final GatheringStatus gatheringStatus;
    private final PlayerBeanRepository playerRepository;

    /**
     * Constructs a new {@link GathererController} and configures with the provided {@link ApplicationConfig}.
     * 
     * @param config Configuration Bean
     */
    public GathererController(@Autowired final ApplicationConfig config, @Autowired final TaskFactory taskFactory,
                              @Autowired final PlayerBeanRepository playerRepository, @Autowired GatheringStatus gatheringStatus) {
        this.appConfig = config;
        this.taskFactory = taskFactory;
        this.gatheringStatus = gatheringStatus;
        this.playerRepository = playerRepository;
    }

    /**
     * Start the gatherer controller instance up.
     * @throws ParseException 
     *
     * @throws Exception Exception thrown if system is incorrectly configured.
     */
    public void run() throws ParseException {
        // Store start time
        long startTime = System.currentTimeMillis();

        // If user attempts to exceed the maximum no. of threads - overwrite their input and set to MAX_THREADS
        if(appConfig.getThreadLimit() > ApplicationConfig.MAX_THREADS) {
            appConfig.setThreadLimit(ApplicationConfig.MAX_THREADS);
        }

        if(!isConfigured()) { // If not configured
            throw new ParseException("Gathering ranges not (correctly) configured");
        } else { // Else configured correctly
            LOG.info("Starting parse of range " + appConfig.getStartId() + " to " + appConfig.getEndId() + " using "
                     + appConfig.getThreadLimit() + " threads");
            gatherCharacters(appConfig.getStartId(), appConfig.getEndId());
            // Get current time
            long endTime = System.currentTimeMillis();
            long seconds = (endTime - startTime) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            PlayerBean highestGathered = playerRepository.findTopByOrderByIdDesc();
            int finalId = appConfig.getEndId() == Integer.MAX_VALUE && highestGathered != null ? highestGathered.getId()
                                                                                               : appConfig.getEndId();
            LOG.info("Run completed, gathered from Character #{} to Character #{} in {} Days, {} Hours, {} Minutes, {} Seconds (using {} threads)",
                     appConfig.getStartId(),
                     finalId,
                     days, hours % 24, minutes % 60, seconds % 60,
                     appConfig.getThreadLimit());
        }
    }

    /**
     * Determine whether the instance is correctly configured.
     *
     * @return string containing warnings/errors in configuration.
     */
    public boolean isConfigured() {
        boolean configured = true;
        if(appConfig.getStartId() < 0) {
            LOG.error("Start ID must be configured to a positive numerical value");
            configured = false;
        }
        if(appConfig.getEndId() < 0) {
            LOG.error("End ID must be configured to a positive numerical value, or left blank.");
            configured = false;
        }
        if(appConfig.getStartId() > appConfig.getEndId()) {
            LOG.error("Error: The finish id argument needs to be greater than the start id argument");
            configured = false;
        }
        return configured;
    }

    /**
     * Method to gather data for characters in specified range.
     */
    private void gatherCharacters(final int startId, final int finishId) {
        // Firstly, clean the top-end of the database
        LOG.debug("Cleaning top-end characters from the database");
        PlayerBean highestValid = playerRepository.findTopByCharacterStatusNotOrderByIdDesc(CharacterStatus.DELETED);
        // Delete everything higher than last known good player
        playerRepository.deleteByIdGreaterThan(highestValid != null ? highestValid.getId() : 0);

        // Setup the gathering parameters
        gatheringStatus.setStartId(startId);
        gatheringStatus.setFinishId(finishId);

        // Now setup the ExecutorServices
        // gatheringExecutor runs only the gathering tasks
        ThreadPoolExecutor gathererExecutor = new ThreadPoolExecutor(appConfig.getThreadLimit(),
                                                                     appConfig.getThreadLimit(),
                                                                     60,
                                                                     TimeUnit.SECONDS,
                                                                     new ArrayBlockingQueue<>(1000));
        // managementExecutor runs all life-cycle management tasks
        ScheduledExecutorService managementExecutor = Executors.newScheduledThreadPool(1);
        // Executes the levemete task once every 10 seconds, starting immediately.
        managementExecutor.scheduleAtFixedRate(new LevemeteTask(gathererExecutor,
                                                                taskFactory,
                                                                gatheringStatus),
                                               0,
                                               5,
                                               TimeUnit.SECONDS);
        // Executes the limiter tasks once every 30 seconds, starting in 30 seconds time.
        managementExecutor.scheduleAtFixedRate(new GatheringLimiterTask(gathererExecutor,
                                                                        playerRepository),
                                               30,
                                               30,
                                               TimeUnit.SECONDS);

        // This is the main idle loop of the application and will continue until the gathering has finished.
        while(!gathererExecutor.isTerminated()) {
            try {
                gathererExecutor.awaitTermination(5, TimeUnit.SECONDS);
            } catch(InterruptedException ie) {
                gathererExecutor.shutdownNow();
                // Remember to re-propogate the interrupt now that we've handled our needs
                Thread.currentThread().interrupt();
            }
        }

        managementExecutor.shutdownNow();
    }
}
