package com.ffxivcensus.gatherer;

import java.util.ArrayList;
import java.util.Date;

/**
 * Object class to represent a Character/Player. This class specifies the attributes and behaviour of a player object.
 * 
 * @author Matthew Hillier
 * @since v1.0
 * @see PlayerBuilder
 */
public class PlayerBean {
    private int id;
    private String realm;
    private String playerName;
    private String race;
    private String gender;
    private String grandCompany;
    private String freeCompany;
    private int lvlGladiator;
    private int lvlPugilist;
    private int lvlMarauder;
    private int lvlLancer;
    private int lvlArcher;
    private int lvlRogue;
    private int lvlConjurer;
    private int lvlThaumaturge;
    private int lvlArcanist;
    private int lvlDarkKnight;
    private int lvlMachinist;
    private int lvlAstrologian;
    private int lvlScholar;
    private int lvlRedMage;
    private int lvlSamurai;
    private int lvlCarpenter;
    private int lvlBlacksmith;
    private int lvlArmorer;
    private int lvlGoldsmith;
    private int lvlLeatherworker;
    private int lvlWeaver;
    private int lvlAlchemist;
    private int lvlCulinarian;
    private int lvlMiner;
    private int lvlBotanist;
    private int lvlFisher;
    private boolean has30DaysSub;
    private boolean has60DaysSub;
    private boolean has90DaysSub;
    private boolean has180DaysSub;
    private boolean has270DaysSub;
    private boolean has360DaysSub;
    private boolean has450DaysSub;
    private boolean has630DaysSub;
    private boolean has960DaysSub;
    private boolean hasPreOrderArr;
    private boolean hasPreOrderHW;
    private boolean hasPreOrderSB;
    private boolean hasARRArtbook;
    private boolean hasHWArtbookOne;
    private boolean hasHWArtbookTwo;
    private boolean hasEncyclopediaEorzea;
    private boolean hasBeforeMeteor;
    private boolean hasBeforeTheFall;
    private boolean hasSoundtrack;
    private boolean hasAttendedEternalBond;
    private boolean hasCompletedHWSightseeing;
    private boolean hasCompleted2pt5;
    private boolean hasFiftyComms;
    private boolean hasMooglePlush;
    private boolean hasTopazCarbunclePlush;
    private boolean hasEmeraldCarbunclePlush;
    private boolean hasCompletedHildibrand;
    private boolean hasPS4Collectors;
    private boolean hasEternalBond;
    private boolean hasARRCollectors;
    private boolean hasKobold;
    private boolean hasSahagin;
    private boolean hasAmaljaa;
    private boolean hasSylph;
    private boolean hasMoogle;
    private boolean hasVanuVanu;
    private boolean hasVath;
    private boolean hasCompletedHW;
    private boolean hasCompletedSB;
    private boolean hasCompleted3pt1;
    private boolean hasCompleted3pt3;
    private boolean isLegacyPlayer;
    private ArrayList minions;
    private ArrayList mounts;
    private Date dateImgLastModified;
    private boolean isActive;

    public PlayerBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrandCompany() {
        return grandCompany;
    }

    public void setGrandCompany(String grandCompany) {
        this.grandCompany = grandCompany;
    }

    public String getFreeCompany() {
        return freeCompany;
    }

    public void setFreeCompany(String freeCompany) {
        this.freeCompany = freeCompany;
    }

    public int getLvlGladiator() {
        return lvlGladiator;
    }

    public void setLvlGladiator(int lvlGladiator) {
        this.lvlGladiator = lvlGladiator;
    }

    public int getLvlPugilist() {
        return lvlPugilist;
    }

    public void setLvlPugilist(int lvlPugilist) {
        this.lvlPugilist = lvlPugilist;
    }

    public int getLvlMarauder() {
        return lvlMarauder;
    }

    public void setLvlMarauder(int lvlMarauder) {
        this.lvlMarauder = lvlMarauder;
    }

    public int getLvlLancer() {
        return lvlLancer;
    }

    public void setLvlLancer(int lvlLancer) {
        this.lvlLancer = lvlLancer;
    }

    public int getLvlArcher() {
        return lvlArcher;
    }

    public void setLvlArcher(int lvlArcher) {
        this.lvlArcher = lvlArcher;
    }

    public int getLvlRogue() {
        return lvlRogue;
    }

    public void setLvlRogue(int lvlRogue) {
        this.lvlRogue = lvlRogue;
    }

    public int getLvlConjurer() {
        return lvlConjurer;
    }

    public void setLvlConjurer(int lvlConjurer) {
        this.lvlConjurer = lvlConjurer;
    }

    public int getLvlThaumaturge() {
        return lvlThaumaturge;
    }

    public void setLvlThaumaturge(int lvlThaumaturge) {
        this.lvlThaumaturge = lvlThaumaturge;
    }

    public int getLvlArcanist() {
        return lvlArcanist;
    }

    public void setLvlArcanist(int lvlArcanist) {
        this.lvlArcanist = lvlArcanist;
    }

    public int getLvlDarkKnight() {
        return lvlDarkKnight;
    }

    public void setLvlDarkKnight(int lvlDarkKnight) {
        this.lvlDarkKnight = lvlDarkKnight;
    }

    public int getLvlMachinist() {
        return lvlMachinist;
    }

    public void setLvlMachinist(int lvlMachinist) {
        this.lvlMachinist = lvlMachinist;
    }

    public int getLvlAstrologian() {
        return lvlAstrologian;
    }

    public void setLvlAstrologian(int lvlAstrologian) {
        this.lvlAstrologian = lvlAstrologian;
    }

    public int getLvlScholar() {
        return lvlScholar;
    }

    public void setLvlScholar(int lvlScholar) {
        this.lvlScholar = lvlScholar;
    }

    public int getLvlRedMage() {
        return lvlRedMage;
    }

    public void setLvlRedMage(int lvlRedMage) {
        this.lvlRedMage = lvlRedMage;
    }

    public int getLvlSamurai() {
        return lvlSamurai;
    }

    public void setLvlSamurai(int lvlSamurai) {
        this.lvlSamurai = lvlSamurai;
    }

    public int getLvlCarpenter() {
        return lvlCarpenter;
    }

    public void setLvlCarpenter(int lvlCarpenter) {
        this.lvlCarpenter = lvlCarpenter;
    }

    public int getLvlBlacksmith() {
        return lvlBlacksmith;
    }

    public void setLvlBlacksmith(int lvlBlacksmith) {
        this.lvlBlacksmith = lvlBlacksmith;
    }

    public int getLvlArmorer() {
        return lvlArmorer;
    }

    public void setLvlArmorer(int lvlArmorer) {
        this.lvlArmorer = lvlArmorer;
    }

    public int getLvlGoldsmith() {
        return lvlGoldsmith;
    }

    public void setLvlGoldsmith(int lvlGoldsmith) {
        this.lvlGoldsmith = lvlGoldsmith;
    }

    public int getLvlLeatherworker() {
        return lvlLeatherworker;
    }

    public void setLvlLeatherworker(int lvlLeatherworker) {
        this.lvlLeatherworker = lvlLeatherworker;
    }

    public int getLvlWeaver() {
        return lvlWeaver;
    }

    public void setLvlWeaver(int lvlWeaver) {
        this.lvlWeaver = lvlWeaver;
    }

    public int getLvlAlchemist() {
        return lvlAlchemist;
    }

    public void setLvlAlchemist(int lvlAlchemist) {
        this.lvlAlchemist = lvlAlchemist;
    }

    public int getLvlCulinarian() {
        return lvlCulinarian;
    }

    public void setLvlCulinarian(int lvlCulinarian) {
        this.lvlCulinarian = lvlCulinarian;
    }

    public int getLvlMiner() {
        return lvlMiner;
    }

    public void setLvlMiner(int lvlMiner) {
        this.lvlMiner = lvlMiner;
    }

    public int getLvlBotanist() {
        return lvlBotanist;
    }

    public void setLvlBotanist(int lvlBotanist) {
        this.lvlBotanist = lvlBotanist;
    }

    public int getLvlFisher() {
        return lvlFisher;
    }

    public void setLvlFisher(int lvlFisher) {
        this.lvlFisher = lvlFisher;
    }

    public boolean isHas30DaysSub() {
        return has30DaysSub;
    }

    public void setHas30DaysSub(boolean has30DaysSub) {
        this.has30DaysSub = has30DaysSub;
    }

    public boolean isHas60DaysSub() {
        return has60DaysSub;
    }

    public void setHas60DaysSub(boolean has60DaysSub) {
        this.has60DaysSub = has60DaysSub;
    }

    public boolean isHas90DaysSub() {
        return has90DaysSub;
    }

    public void setHas90DaysSub(boolean has90DaysSub) {
        this.has90DaysSub = has90DaysSub;
    }

    public boolean isHas180DaysSub() {
        return has180DaysSub;
    }

    public void setHas180DaysSub(boolean has180DaysSub) {
        this.has180DaysSub = has180DaysSub;
    }

    public boolean isHas270DaysSub() {
        return has270DaysSub;
    }

    public void setHas270DaysSub(boolean has270DaysSub) {
        this.has270DaysSub = has270DaysSub;
    }

    public boolean isHas360DaysSub() {
        return has360DaysSub;
    }

    public void setHas360DaysSub(boolean has360DaysSub) {
        this.has360DaysSub = has360DaysSub;
    }

    public boolean isHas450DaysSub() {
        return has450DaysSub;
    }

    public void setHas450DaysSub(boolean has450DaysSub) {
        this.has450DaysSub = has450DaysSub;
    }

    public boolean isHas630DaysSub() {
        return has630DaysSub;
    }

    public void setHas630DaysSub(boolean has630DaysSub) {
        this.has630DaysSub = has630DaysSub;
    }

    public boolean isHas960DaysSub() {
        return has960DaysSub;
    }

    public void setHas960DaysSub(boolean has960DaysSub) {
        this.has960DaysSub = has960DaysSub;
    }

    public boolean isHasPreOrderArr() {
        return hasPreOrderArr;
    }

    public void setHasPreOrderArr(boolean hasPreOrderArr) {
        this.hasPreOrderArr = hasPreOrderArr;
    }

    public boolean isHasPreOrderHW() {
        return hasPreOrderHW;
    }

    public void setHasPreOrderHW(boolean hasPreOrderHW) {
        this.hasPreOrderHW = hasPreOrderHW;
    }

    public boolean isHasPreOrderSB() {
        return hasPreOrderSB;
    }

    public void setHasPreOrderSB(boolean hasPreOrderSB) {
        this.hasPreOrderSB = hasPreOrderSB;
    }

    public boolean isHasARRArtbook() {
        return hasARRArtbook;
    }

    public void setHasARRArtbook(boolean hasARRArtbook) {
        this.hasARRArtbook = hasARRArtbook;
    }

    public boolean isHasHWArtbookOne() {
        return hasHWArtbookOne;
    }

    public void setHasHWArtbookOne(boolean hasHWArtbookOne) {
        this.hasHWArtbookOne = hasHWArtbookOne;
    }

    public boolean isHasHWArtbookTwo() {
        return hasHWArtbookTwo;
    }

    public void setHasHWArtbookTwo(boolean hasHWArtbookTwo) {
        this.hasHWArtbookTwo = hasHWArtbookTwo;
    }

    public boolean isHasEncyclopediaEorzea() {
        return hasEncyclopediaEorzea;
    }

    public void setHasEncyclopediaEorzea(boolean hasEncyclopediaEorzea) {
        this.hasEncyclopediaEorzea = hasEncyclopediaEorzea;
    }

    public boolean isHasBeforeMeteor() {
        return hasBeforeMeteor;
    }

    public void setHasBeforeMeteor(boolean hasBeforeMeteor) {
        this.hasBeforeMeteor = hasBeforeMeteor;
    }

    public boolean isHasBeforeTheFall() {
        return hasBeforeTheFall;
    }

    public void setHasBeforeTheFall(boolean hasBeforeTheFall) {
        this.hasBeforeTheFall = hasBeforeTheFall;
    }

    public boolean isHasSoundtrack() {
        return hasSoundtrack;
    }

    public void setHasSoundtrack(boolean hasSoundtrack) {
        this.hasSoundtrack = hasSoundtrack;
    }

    public boolean isHasAttendedEternalBond() {
        return hasAttendedEternalBond;
    }

    public void setHasAttendedEternalBond(boolean hasAttendedEternalBond) {
        this.hasAttendedEternalBond = hasAttendedEternalBond;
    }

    public boolean isHasCompletedHWSightseeing() {
        return hasCompletedHWSightseeing;
    }

    public void setHasCompletedHWSightseeing(boolean hasCompletedHWSightseeing) {
        this.hasCompletedHWSightseeing = hasCompletedHWSightseeing;
    }

    public boolean isHasCompleted2pt5() {
        return hasCompleted2pt5;
    }

    public void setHasCompleted2pt5(boolean hasCompleted2pt5) {
        this.hasCompleted2pt5 = hasCompleted2pt5;
    }

    public boolean isHasFiftyComms() {
        return hasFiftyComms;
    }

    public void setHasFiftyComms(boolean hasFiftyComms) {
        this.hasFiftyComms = hasFiftyComms;
    }

    public boolean isHasMooglePlush() {
        return hasMooglePlush;
    }

    public void setHasMooglePlush(boolean hasMooglePlush) {
        this.hasMooglePlush = hasMooglePlush;
    }

    public boolean isHasTopazCarbunclePlush() {
        return hasTopazCarbunclePlush;
    }

    public void setHasTopazCarbunclePlush(boolean hasTopazCarbunclePlush) {
        this.hasTopazCarbunclePlush = hasTopazCarbunclePlush;
    }

    public boolean isHasEmeraldCarbunclePlush() {
        return hasEmeraldCarbunclePlush;
    }

    public void setHasEmeraldCarbunclePlush(boolean hasEmeraldCarbunclePlush) {
        this.hasEmeraldCarbunclePlush = hasEmeraldCarbunclePlush;
    }

    public boolean isHasCompletedHildibrand() {
        return hasCompletedHildibrand;
    }

    public void setHasCompletedHildibrand(boolean hasCompletedHildibrand) {
        this.hasCompletedHildibrand = hasCompletedHildibrand;
    }

    public boolean isHasPS4Collectors() {
        return hasPS4Collectors;
    }

    public void setHasPS4Collectors(boolean hasPS4Collectors) {
        this.hasPS4Collectors = hasPS4Collectors;
    }

    public boolean isHasEternalBond() {
        return hasEternalBond;
    }

    public void setHasEternalBond(boolean hasEternalBond) {
        this.hasEternalBond = hasEternalBond;
    }

    public boolean isHasARRCollectors() {
        return hasARRCollectors;
    }

    public void setHasARRCollectors(boolean hasARRCollectors) {
        this.hasARRCollectors = hasARRCollectors;
    }

    public boolean isHasKobold() {
        return hasKobold;
    }

    public void setHasKobold(boolean hasKobold) {
        this.hasKobold = hasKobold;
    }

    public boolean isHasSahagin() {
        return hasSahagin;
    }

    public void setHasSahagin(boolean hasSahagin) {
        this.hasSahagin = hasSahagin;
    }

    public boolean isHasAmaljaa() {
        return hasAmaljaa;
    }

    public void setHasAmaljaa(boolean hasAmaljaa) {
        this.hasAmaljaa = hasAmaljaa;
    }

    public boolean isHasSylph() {
        return hasSylph;
    }

    public void setHasSylph(boolean hasSylph) {
        this.hasSylph = hasSylph;
    }

    public boolean isHasMoogle() {
        return hasMoogle;
    }

    public void setHasMoogle(boolean hasMoogle) {
        this.hasMoogle = hasMoogle;
    }

    public boolean isHasVanuVanu() {
        return hasVanuVanu;
    }

    public void setHasVanuVanu(boolean hasVanuVanu) {
        this.hasVanuVanu = hasVanuVanu;
    }

    public boolean isHasVath() {
        return hasVath;
    }

    public void setHasVath(boolean hasVath) {
        this.hasVath = hasVath;
    }

    public boolean isHasCompletedHW() {
        return hasCompletedHW;
    }

    public void setHasCompletedHW(boolean hasCompletedHW) {
        this.hasCompletedHW = hasCompletedHW;
    }

    public boolean isHasCompletedSB() {
        return hasCompletedSB;
    }

    public void setHasCompletedSB(boolean hasCompletedSB) {
        this.hasCompletedSB = hasCompletedSB;
    }

    public boolean isHasCompleted3pt1() {
        return hasCompleted3pt1;
    }

    public void setHasCompleted3pt1(boolean hasCompleted3pt1) {
        this.hasCompleted3pt1 = hasCompleted3pt1;
    }

    public boolean isHasCompleted3pt3() {
        return hasCompleted3pt3;
    }

    public void setHasCompleted3pt3(boolean hasCompleted3pt3) {
        this.hasCompleted3pt3 = hasCompleted3pt3;
    }

    public boolean isLegacyPlayer() {
        return isLegacyPlayer;
    }

    public void setLegacyPlayer(boolean isLegacyPlayer) {
        this.isLegacyPlayer = isLegacyPlayer;
    }

    public ArrayList getMinions() {
        return minions;
    }

    public void setMinions(ArrayList minions) {
        this.minions = minions;
    }

    public ArrayList getMounts() {
        return mounts;
    }

    public void setMounts(ArrayList mounts) {
        this.mounts = mounts;
    }

    public Date getDateImgLastModified() {
        return dateImgLastModified;
    }

    public void setDateImgLastModified(Date dateImgLastModified) {
        this.dateImgLastModified = dateImgLastModified;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}