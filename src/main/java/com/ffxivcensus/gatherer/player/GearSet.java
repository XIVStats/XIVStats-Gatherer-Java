package com.ffxivcensus.gatherer.player;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A Gear Set assigned to the player at the time of parsing.
 * 
 * @author matthew.hillier
 */
@Entity
@Table(name = "character_gear_sets")
public class GearSet {

    @Id
    private int playerId;
    @OneToOne(mappedBy = "gearSet")
    private PlayerBean player;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "main_hand")
    private GearItem mainHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "head")
    private GearItem head;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "body")
    private GearItem body;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "hands")
    private GearItem hands;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "belt")
    private GearItem belt;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "legs")
    private GearItem legs;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "feet")
    private GearItem feet;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "off_hand")
    private GearItem offHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "ears")
    private GearItem ears;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "neck")
    private GearItem neck;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "wrists")
    private GearItem wrists;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "left_hand")
    private GearItem leftHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "right_hand")
    private GearItem rightHand;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "job_crystal")
    private GearItem jobCrystal;

    /**
     * @return the characterId
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * @param characterId the characterId to set
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * @return the player
     */
    public PlayerBean getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(PlayerBean player) {
        this.player = player;
    }

    /**
     * @return the mainHand
     */
    public GearItem getMainHand() {
        return mainHand;
    }

    /**
     * @param mainHand the mainHand to set
     */
    public void setMainHand(GearItem mainHand) {
        this.mainHand = mainHand;
    }

    /**
     * @return the head
     */
    public GearItem getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(GearItem head) {
        this.head = head;
    }

    /**
     * @return the body
     */
    public GearItem getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(GearItem body) {
        this.body = body;
    }

    /**
     * @return the hands
     */
    public GearItem getHands() {
        return hands;
    }

    /**
     * @param hands the hands to set
     */
    public void setHands(GearItem hands) {
        this.hands = hands;
    }

    /**
     * @return the belt
     */
    public GearItem getBelt() {
        return belt;
    }

    /**
     * @param belt the belt to set
     */
    public void setBelt(GearItem belt) {
        this.belt = belt;
    }

    /**
     * @return the legs
     */
    public GearItem getLegs() {
        return legs;
    }

    /**
     * @param legs the legs to set
     */
    public void setLegs(GearItem legs) {
        this.legs = legs;
    }

    /**
     * @return the feet
     */
    public GearItem getFeet() {
        return feet;
    }

    /**
     * @param feet the feet to set
     */
    public void setFeet(GearItem feet) {
        this.feet = feet;
    }

    /**
     * @return the offHand
     */
    public GearItem getOffHand() {
        return offHand;
    }

    /**
     * @param offHand the offHand to set
     */
    public void setOffHand(GearItem offHand) {
        this.offHand = offHand;
    }

    /**
     * @return the ears
     */
    public GearItem getEars() {
        return ears;
    }

    /**
     * @param ears the ears to set
     */
    public void setEars(GearItem ears) {
        this.ears = ears;
    }

    /**
     * @return the neck
     */
    public GearItem getNeck() {
        return neck;
    }

    /**
     * @param kneck the neck to set
     */
    public void setNeck(GearItem neck) {
        this.neck = neck;
    }

    /**
     * @return the wrists
     */
    public GearItem getWrists() {
        return wrists;
    }

    /**
     * @param wrists the wrists to set
     */
    public void setWrists(GearItem wrists) {
        this.wrists = wrists;
    }

    /**
     * @return the leftHand
     */
    public GearItem getLeftHand() {
        return leftHand;
    }

    /**
     * @param leftHand the leftHand to set
     */
    public void setLeftHand(GearItem leftHand) {
        this.leftHand = leftHand;
    }

    /**
     * @return the rightHand
     */
    public GearItem getRightHand() {
        return rightHand;
    }

    /**
     * @param rightHand the rightHand to set
     */
    public void setRightHand(GearItem rightHand) {
        this.rightHand = rightHand;
    }

    /**
     * @return the jobCrystal
     */
    public GearItem getJobCrystal() {
        return jobCrystal;
    }

    /**
     * @param jobCrystal the jobCrystal to set
     */
    public void setJobCrystal(GearItem jobCrystal) {
        this.jobCrystal = jobCrystal;
    }

}
