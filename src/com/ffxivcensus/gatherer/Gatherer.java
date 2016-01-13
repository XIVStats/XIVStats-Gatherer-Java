package com.ffxivcensus.gatherer;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

/**
 * Gatherer worker class that implements Runnable class.
 * <p>
 * A worker that executes parses and writes the corresponding player record to the DB.
 *
 * @author Peter Reid
 * @see Player
 * @see GathererController
 */
public class Gatherer implements Runnable {

    /**
     * Default constructor
     */
    public Gatherer() {
    }

    /**
     * Run the Gatherer.
     */
    public void run() {
        int nextID = GathererController.getNextID();
        while (nextID != -1) { //While we still have IDs to parse
                try {
                    //Parse players and write them to DB
                    GathererController.writeToDB(Player.getPlayer(nextID));
                } catch (MySQLNonTransientConnectionException failWriteEx) { //If record fails to write due to too many connections
                    System.out.println("Error: Record write failure, reattempting write");
                    //Wait a second
                    try {
                        Thread.currentThread().wait(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                }
            //Get the nextID
            nextID = GathererController.getNextID();
        }
    }

}


