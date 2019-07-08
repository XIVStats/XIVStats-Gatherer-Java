package com.ffxivcensus.gatherer.player;

import javax.persistence.Column;
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
    /** Gear Item's iLevel. */
    @Column(name = "ilevel")
    private int iLevel;
    /** Gear Item's Name (English). */
    private String name;
    /** Gear Item's Category (English). */
    private String category;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getiLevel() {
        return iLevel;
    }

    public void setiLevel(int iLevel) {
        this.iLevel = iLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
