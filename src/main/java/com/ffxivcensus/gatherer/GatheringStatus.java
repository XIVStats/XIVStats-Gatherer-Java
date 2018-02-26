package com.ffxivcensus.gatherer;

public class GatheringStatus {

    /** Initial starting ID number. */
    private int startId;
    /** Finishing ending ID number. */
    private int finishId = Integer.MAX_VALUE;
    /** Current ID number. */
    private int currentId;

    /**
     * Fetches the Starting ID number.
     * 
     * @return the startId
     */
    public int getStartId() {
        return startId;
    }

    /**
     * Sets the Starting and Current ID numbers.
     * Current ID always get set to <code>startId - 1</code> to ensure that the first ID number issued is the first one requested.
     * 
     * @param startId the startId to set
     */
    public void setStartId(final int startId) {
        this.startId = startId;
        this.currentId = startId - 1;
    }

    /**
     * Fetches the Finishing ID number.
     * 
     * @return the finishId
     */
    public int getFinishId() {
        return finishId;
    }

    /**
     * Sets the Finishing ID number.
     * 
     * @param finishId the finishId to set
     */
    public void setFinishId(final int finishId) {
        this.finishId = finishId;
    }

    /**
     * Fetches the Current/Most Recent ID Number.
     * 
     * @return the currentId
     */
    public int getCurrentId() {
        return currentId;
    }

    /**
     * Issues the next available ID number and increments the current ID number to match.
     * This method is synchronous to mitigate issues where multiple threads may try to hit this at the same time and, therefore, end up with
     * the same ID number issued twice.
     * 
     * @return Next available ID number.
     */
    public synchronized int getNextId() {
        int nextId = currentId + 1;
        currentId = nextId;
        return nextId;
    }

}
