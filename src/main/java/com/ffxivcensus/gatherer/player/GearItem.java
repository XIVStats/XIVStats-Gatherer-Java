package com.ffxivcensus.gatherer.player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Gear Item from the Lodestone / Eorzea Database.
 * 
 * @author matthew.hillier
 */
@Entity
@Table(name = "gear_items")
public class GearItem {

    /** Gear Item's Lodestone Database ID. */
    @Id
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
