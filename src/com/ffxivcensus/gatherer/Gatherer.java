package com.ffxivcensus.gatherer;

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
            } catch (Exception e) {
                e.getMessage();
            }
            //Get the nextID
            nextID = GathererController.getNextID();
        }
    }

}


