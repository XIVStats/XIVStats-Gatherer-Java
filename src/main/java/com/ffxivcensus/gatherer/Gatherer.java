package com.ffxivcensus.gatherer;

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
            System.out.println("Starting evaluation of player ID: " + playerId);
            // Parse players and write them to DB
            String out = dao.saveRecord(PlayerBuilder.getPlayer(playerId, 1));
            System.out.println(out);
        } catch(MySQLNonTransientConnectionException failWriteEx) { // If record fails to write due to too many connections
            System.out.println("Error: Record write failure, reattempting write");
            // Wait a second
            try {
                Thread.currentThread().wait(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            // Then attempt to write again
            try {
                String out = dao.saveRecord(PlayerBuilder.getPlayer(playerId, 1));
                System.out.println(out);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
