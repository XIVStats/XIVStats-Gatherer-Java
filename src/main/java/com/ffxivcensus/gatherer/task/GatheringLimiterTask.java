package com.ffxivcensus.gatherer.task;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.player.CharacterStatus;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;

/**
 * Runnable task managing the shutdown criteria of the gathering process, by managing the {@see ExecutorService}.
 * 
 * @author matthew.hillier
 */
public class GatheringLimiterTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(GatheringLimiterTask.class);

    private ThreadPoolExecutor gathererExecutor;
    private PlayerBeanRepository characterRepository;
    private ApplicationConfig config;

    public GatheringLimiterTask(ApplicationConfig config, final ThreadPoolExecutor gathererExecutor, final PlayerBeanRepository playerBeanRepository) {
        this.config = config;
        this.gathererExecutor = gathererExecutor;
        this.characterRepository = playerBeanRepository;
    }

    @Override
    public void run() {
        LOG.debug("GATHERING CAPPING: Checking whether the gathering should stop...");
        // Check the database for the highest valid character
        PlayerBean highestGathered = characterRepository.findTopByOrderByIdDesc();
        // Check the database for the highest gathered ID so far
        PlayerBean highestValid = characterRepository.findTopByCharacterStatusNotOrderByIdDesc(CharacterStatus.DELETED);
        // if the gap between valid character and gathered ID is greater than the gap limit
        // then
        // - issue gathererService.shutdownNow()
        int maxId = highestGathered == null ? Integer.MAX_VALUE : highestGathered.getId();
        int maxValidId = highestValid == null ? 0 : highestValid.getId();

        if(maxId > config.getAutoStopLowerLimitId() && maxId > maxValidId + config.getAutoStopGap()) {
            LOG.info("GATHERING CAPPING: FINISHING - No valid characters found for at least {} ID's after Character #{}",
                     config.getAutoStopGap(), maxValidId);
            // Issue shutdownNow as this clears the execution queue of any un-started tasks
            gathererExecutor.shutdownNow();
        }
    }

}
