package com.ffxivcensus.gatherer;

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

    private GathererController parent;

    private int playerId;

    /**
     * Default constructor
     */
    public Gatherer(GathererController parent, int playerId) {
        this.parent = parent;
        this.playerId = playerId;
    }

    /**
     * Run the Gatherer.
     */
    public void run() {
        try {
            if(parent.isVerbose()) {
                System.out.println("Starting evaluation of player ID: " + playerId);
            }
            // Parse players and write them to DB
            String out = parent.writeToDB(PlayerBuilder.getPlayer(playerId, 1));
            if(!parent.isQuiet()) { // If not running in quiet mode
                System.out.println(out);
            }
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
                String out = parent.writeToDB(PlayerBuilder.getPlayer(playerId, 1));
                if(!parent.isQuiet()) {
                    System.out.println(out);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            if(parent.isVerbose() || parent.isPrintFails()) {
                System.out.println(e.getMessage());
            }
        }
    }

}
