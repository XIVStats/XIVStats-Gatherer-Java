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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public PlayerBean getPlayer() {
        return player;
    }

    public void setPlayer(PlayerBean player) {
        this.player = player;
    }

    public GearItem getMainHand() {
        return mainHand;
    }

    public void setMainHand(GearItem mainHand) {
        this.mainHand = mainHand;
    }

    public GearItem getHead() {
        return head;
    }

    public void setHead(GearItem head) {
        this.head = head;
    }

    public GearItem getBody() {
        return body;
    }

    public void setBody(GearItem body) {
        this.body = body;
    }

    public GearItem getHands() {
        return hands;
    }

    public void setHands(GearItem hands) {
        this.hands = hands;
    }

    public GearItem getBelt() {
        return belt;
    }

    public void setBelt(GearItem belt) {
        this.belt = belt;
    }

    public GearItem getLegs() {
        return legs;
    }

    public void setLegs(GearItem legs) {
        this.legs = legs;
    }

    public GearItem getFeet() {
        return feet;
    }

    public void setFeet(GearItem feet) {
        this.feet = feet;
    }

    public GearItem getOffHand() {
        return offHand;
    }

    public void setOffHand(GearItem offHand) {
        this.offHand = offHand;
    }

    public GearItem getEars() {
        return ears;
    }

    public void setEars(GearItem ears) {
        this.ears = ears;
    }

    public GearItem getNeck() {
        return neck;
    }

    public void setNeck(GearItem neck) {
        this.neck = neck;
    }

    public GearItem getWrists() {
        return wrists;
    }

    public void setWrists(GearItem wrists) {
        this.wrists = wrists;
    }

    public GearItem getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(GearItem leftHand) {
        this.leftHand = leftHand;
    }

    public GearItem getRightHand() {
        return rightHand;
    }

    public void setRightHand(GearItem rightHand) {
        this.rightHand = rightHand;
    }

    public GearItem getJobCrystal() {
        return jobCrystal;
    }

    public void setJobCrystal(GearItem jobCrystal) {
        this.jobCrystal = jobCrystal;
    }

}
