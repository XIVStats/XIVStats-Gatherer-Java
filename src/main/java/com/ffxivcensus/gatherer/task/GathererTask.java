package com.ffxivcensus.gatherer.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ffxivcensus.gatherer.GathererController;
import com.ffxivcensus.gatherer.player.CharacterStatus;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;
import com.ffxivcensus.gatherer.player.PlayerBuilder;

/**
 * Gatherer worker class that implements Runnable class.
 * <p>
 * A worker that executes parses and writes the corresponding player record to the DB.
 *
 * @author Peter Reid
 * @since v1.0
 * @see PlayerBuilder
 * @see GathererController
 * @see java.lang.Runnable
 */
public class GathererTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(GathererTask.class);
    private static final Logger RESULT_LOG = LoggerFactory.getLogger(GathererTask.class.getName() + ".result");

    private int playerId;
    private PlayerBuilder playerBuilder;
    private PlayerBeanRepository playerRepository;

    /**
     * Run the Gatherer.
     */
    @Override
    public void run() {
        try {
            LOG.debug("Starting evaluation of player ID: {}", getPlayerId());

            // Check whether we already know about this character
            PlayerBean player = playerRepository.findOne(getPlayerId());
            if(player == null || !CharacterStatus.DELETED.equals(player.getCharacterStatus())) {
                // Only update characters that have not been deleted
                player = playerBuilder.getPlayer(getPlayerId());
                getPlayerRepository().save(player);
                RESULT_LOG.info("{} - {}", getPlayerId(), player.getCharacterStatus());
            } else {
                RESULT_LOG.info("{} - SKIPPED as they have been previously marked as DELETED", getPlayerId());
            }
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            RESULT_LOG.debug("{} - FAILED", getPlayerId());
        }
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final int playerId) {
        this.playerId = playerId;
    }

    public PlayerBeanRepository getPlayerRepository() {
        return playerRepository;
    }

    @Autowired
    public void setPlayerBuilder(PlayerBuilder playerBuilder) {
        this.playerBuilder = playerBuilder;
    }

    @Autowired
    public void setPlayerRepository(PlayerBeanRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

}
