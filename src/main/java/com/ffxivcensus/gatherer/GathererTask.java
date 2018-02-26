package com.ffxivcensus.gatherer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    private int iteration;
    private int playerId;
    private PlayerBeanRepository playerRepository;

    /**
     * Run the Gatherer.
     */
    @Override
    public void run() {
        try {
            LOG.debug("Starting evaluation of player ID: " + getPlayerId());

            // Check whether we already know about this character
            PlayerBean player = playerRepository.findOne(getPlayerId());
            if(player == null || !CharacterStatus.DELETED.equals(player.getCharacterStatus())) {
                // Only update characters that have not been deleted
                player = getPlayerRepository().save(PlayerBuilder.getPlayer(getPlayerId(), 1));
                RESULT_LOG.info(getPlayerId() + " - " + player.getCharacterStatus().name());
            } else {
                RESULT_LOG.info(getPlayerId() + " - SKIPPED as they have been previously marked as DELETED");
            }
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            RESULT_LOG.debug(getPlayerId() + " - FAILED");
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
    public void setPlayerRepository(PlayerBeanRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(final int iteration) {
        this.iteration = iteration;
    }

}
