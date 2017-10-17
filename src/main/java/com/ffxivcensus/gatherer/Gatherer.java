package com.ffxivcensus.gatherer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ffxivcensus.gatherer.player.CharacterStatus;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanDAO;
import com.ffxivcensus.gatherer.player.PlayerBuilder;
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

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
public class Gatherer implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(Gatherer.class);
	private static final Logger RESULT_LOG = LoggerFactory.getLogger(Gatherer.class.getName() + ".result");

    private final int playerId;
    private final PlayerBeanDAO dao;

    /**
     * Default constructor
     */
    public Gatherer(final GathererController parent, final PlayerBeanDAO playerBeanDAO, final int playerId) {
        this.playerId = playerId;
        this.dao = playerBeanDAO;
    }

    /**
     * Run the Gatherer.
     */
    @Override
    public void run() {
        try {
            LOG.debug("Starting evaluation of player ID: " + playerId);
            // Parse players and write them to DB
            PlayerBean player = PlayerBuilder.getPlayer(playerId, 1);
            // Currently ignore deleted characters (404)
            if(!CharacterStatus.DELETED.equals(player.getCharacterStatus())) {
		        String out = dao.saveRecord(player);
		        LOG.debug(out);
            }
            RESULT_LOG.info(playerId + " - " + player.getCharacterStatus().name());
        } catch(MySQLNonTransientConnectionException failWriteEx) { // If record fails to write due to too many connections
            LOG.trace("Error: Record write failure, reattempting write");
            // Wait a second
            try {
                Thread.currentThread().wait(1);
            } catch(InterruptedException e) {
            }
            // Then attempt to write again
            try {
                String out = dao.saveRecord(PlayerBuilder.getPlayer(playerId, 1));
                LOG.debug(out);
            } catch(Exception e) {
                LOG.error(e.getMessage(), e);
            }
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            RESULT_LOG.debug(playerId + " - FAILED");
        }
    }

}
