package com.ffxivcensus.gatherer.player;

public class GearItem {

    /** Gear Item's Lodestone Database ID. */
    private String itemId;
    /** Gear Item's Name (English). */
    private String name;
    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }
    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
