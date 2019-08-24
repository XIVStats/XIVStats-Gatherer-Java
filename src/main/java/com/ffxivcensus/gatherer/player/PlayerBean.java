package com.ffxivcensus.gatherer.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ffxivcensus.gatherer.util.StringListConverter;

/**
 * Object class to represent a Character/Player. This class specifies the attributes and behaviour of a player object.
 *
 * @author Matthew Hillier
 * @since v1.0
 * @see PlayerBuilder
 */
@Entity
@Table(name = "tblplayers")
public class PlayerBean {
    private static final String NOT_AVAILABLE = "N/A";
    @Id
    private int id;
    private String realm = NOT_AVAILABLE;
    private String name = NOT_AVAILABLE;
    private String race = NOT_AVAILABLE;
    private String gender = NOT_AVAILABLE;
    private String grandCompany = NOT_AVAILABLE;
    private String freeCompany = NOT_AVAILABLE;
    private int levelGladiator;
    private int levelPugilist;
    private int levelMarauder;
    private int levelLancer;
    private int levelArcher;
    private int levelRogue;
    private int levelConjurer;
    private int levelThaumaturge;
    private int levelArcanist;
    private int levelDarkknight;
    private int levelMachinist;
    private int levelAstrologian;
    private int levelScholar;
    private int levelRedmage;
    private int levelSamurai;
    private int levelBluemage;
    private int levelGunbreaker;
    private int levelDancer;
    private int levelCarpenter;
    private int levelBlacksmith;
    private int levelArmorer;
    private int levelGoldsmith;
    private int levelLeatherworker;
    private int levelWeaver;
    private int levelAlchemist;
    private int levelCulinarian;
    private int levelMiner;
    private int levelBotanist;
    private int levelFisher;
    private int levelEureka;
    @Column(name = "p30days")
    private boolean has30DaysSub;
    @Column(name = "p60days")
    private boolean has60DaysSub;
    @Column(name = "p90days")
    private boolean has90DaysSub;
    @Column(name = "p180days")
    private boolean has180DaysSub;
    @Column(name = "p270days")
    private boolean has270DaysSub;
    @Column(name = "p360days")
    private boolean has360DaysSub;
    @Column(name = "p450days")
    private boolean has450DaysSub;
    @Column(name = "p630days")
    private boolean has630DaysSub;
    @Column(name = "p960days")
    private boolean has960DaysSub;
    @Column(name = "prearr")
    private boolean hasPreOrderArr;
    @Column(name = "prehw")
    private boolean hasPreOrderHW;
    @Column(name = "presb")
    private boolean hasPreOrderSB;
    @Column(name = "preshb")
    private boolean hasPreOrderShB;
    @Column(name = "arrartbook")
    private boolean hasARRArtbook;
    @Column(name = "hwartbookone")
    private boolean hasHWArtbookOne;
    @Column(name = "hwartbooktwo")
    private boolean hasHWArtbookTwo;
    @Column(name = "sbartbook")
    private boolean hasSBArtbook;
    @Column(name = "sbartbooktwo")
    private boolean hasSBArtbookTwo;
    @Column(name = "hasencyclopedia")
    private boolean hasEncyclopediaEorzea;
    @Column(name = "beforemeteor")
    private boolean hasBeforeMeteor;
    @Column(name = "beforethefall")
    private boolean hasBeforeTheFall;
    @Column(name = "soundtrack")
    private boolean hasSoundtrack;
    @Column(name = "saweternalbond")
    private boolean hasAttendedEternalBond;
    @Column(name = "sightseeing")
    private boolean hasCompletedHWSightseeing;
    @Column(name = "arr_25_complete")
    private boolean hasCompleted2pt5;
    @Column(name = "comm50")
    private boolean hasFiftyComms;
    @Column(name = "moogleplush")
    private boolean hasMooglePlush;
    @Column(name = "topazcarubuncleplush")
    private boolean hasTopazCarbunclePlush;
    @Column(name = "emeraldcarbuncleplush")
    private boolean hasEmeraldCarbunclePlush;
    @Column(name = "hildibrand")
    private boolean hasCompletedHildibrand;
    @Column(name = "ps4collectors")
    private boolean hasPS4Collectors;
    @Column(name = "dideternalbond")
    private boolean hasEternalBond;
    @Column(name = "arrcollector")
    private boolean hasARRCollectors;
    @Column(name = "kobold")
    private boolean hasKobold;
    @Column(name = "sahagin")
    private boolean hasSahagin;
    @Column(name = "amaljaa")
    private boolean hasAmaljaa;
    @Column(name = "sylph")
    private boolean hasSylph;
    @Column(name = "moogle")
    private boolean hasMoogle;
    @Column(name = "vanuvanu")
    private boolean hasVanuVanu;
    @Column(name = "vath")
    private boolean hasVath;
    @Column(name = "hw_complete")
    private boolean hasCompletedHW;
    @Column(name = "sb_complete")
    private boolean hasCompletedSB;
    @Column(name = "hw_31_complete")
    private boolean hasCompleted3pt1;
    @Column(name = "hw_33_complete")
    private boolean hasCompleted3pt3;
    @Column(name = "legacy_player")
    private boolean isLegacyPlayer;
    @Column(name = "minions")
    @Convert(converter = StringListConverter.class)
    private List<String> minions = new ArrayList<>();
    @Column(name = "mounts")
    @Convert(converter = StringListConverter.class)
    private List<String> mounts = new ArrayList<>();
    @Column(name = "date_active")
    private Date dateImgLastModified;
    @Column(name = "is_active")
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private CharacterStatus characterStatus = CharacterStatus.ACTIVE;

    public PlayerBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(final String realm) {
        this.realm = realm;
    }

    public String getPlayerName() {
        return name;
    }

    public void setPlayerName(final String playerName) {
        this.name = playerName;
    }

    public String getRace() {
        return race;
    }

    public void setRace(final String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getGrandCompany() {
        return grandCompany;
    }

    public void setGrandCompany(final String grandCompany) {
        this.grandCompany = grandCompany;
    }

    public String getFreeCompany() {
        return freeCompany;
    }

    public void setFreeCompany(final String freeCompany) {
        this.freeCompany = freeCompany;
    }

    public int getLevelGladiator() {
        return levelGladiator;
    }

    public void setLevelGladiator(final int lvlGladiator) {
        this.levelGladiator = lvlGladiator;
    }

    public int getLevelPugilist() {
        return levelPugilist;
    }

    public void setLevelPugilist(final int lvlPugilist) {
        this.levelPugilist = lvlPugilist;
    }

    public int getLevelMarauder() {
        return levelMarauder;
    }

    public void setLevelMarauder(final int lvlMarauder) {
        this.levelMarauder = lvlMarauder;
    }

    public int getLevelLancer() {
        return levelLancer;
    }

    public void setLevelLancer(final int lvlLancer) {
        this.levelLancer = lvlLancer;
    }

    public int getLevelArcher() {
        return levelArcher;
    }

    public void setLevelArcher(final int lvlArcher) {
        this.levelArcher = lvlArcher;
    }

    public int getLevelRogue() {
        return levelRogue;
    }

    public void setLevelRogue(final int lvlRogue) {
        this.levelRogue = lvlRogue;
    }

    public int getLevelConjurer() {
        return levelConjurer;
    }

    public void setLevelConjurer(final int lvlConjurer) {
        this.levelConjurer = lvlConjurer;
    }

    public int getLevelThaumaturge() {
        return levelThaumaturge;
    }

    public void setLevelThaumaturge(final int lvlThaumaturge) {
        this.levelThaumaturge = lvlThaumaturge;
    }

    public int getLevelArcanist() {
        return levelArcanist;
    }

    public void setLevelArcanist(final int lvlArcanist) {
        this.levelArcanist = lvlArcanist;
    }

    public int getLevelDarkknight() {
        return levelDarkknight;
    }

    public void setLevelDarkknight(final int lvlDarkknight) {
        this.levelDarkknight = lvlDarkknight;
    }

    public int getLevelMachinist() {
        return levelMachinist;
    }

    public void setLevelMachinist(final int lvlMachinist) {
        this.levelMachinist = lvlMachinist;
    }

    public int getLevelAstrologian() {
        return levelAstrologian;
    }

    public void setLevelAstrologian(final int lvlAstrologian) {
        this.levelAstrologian = lvlAstrologian;
    }

    public int getLevelScholar() {
        return levelScholar;
    }

    public void setLevelScholar(final int lvlScholar) {
        this.levelScholar = lvlScholar;
    }

    public int getLevelRedmage() {
        return levelRedmage;
    }

    public void setLevelRedmage(final int lvlRedmage) {
        this.levelRedmage = lvlRedmage;
    }

    public int getLevelSamurai() {
        return levelSamurai;
    }

    public void setLevelSamurai(final int lvlSamurai) {
        this.levelSamurai = lvlSamurai;
    }

    public int getLevelBluemage() {
        return levelBluemage;
    }

    public void setLevelBluemage(int levelBluemage) {
        this.levelBluemage = levelBluemage;
    }

    public int getLevelGunbreaker() {
        return levelGunbreaker;
    }

    public void setLevelGunbreaker(int levelGunbreaker) {
        this.levelGunbreaker = levelGunbreaker;
    }

    public int getLevelDancer() {
        return levelDancer;
    }

    public void setLevelDancer(int levelDancer) {
        this.levelDancer = levelDancer;
    }

    public int getLevelCarpenter() {
        return levelCarpenter;
    }

    public void setLevelCarpenter(final int lvlCarpenter) {
        this.levelCarpenter = lvlCarpenter;
    }

    public int getLevelBlacksmith() {
        return levelBlacksmith;
    }

    public void setLevelBlacksmith(final int lvlBlacksmith) {
        this.levelBlacksmith = lvlBlacksmith;
    }

    public int getLevelArmorer() {
        return levelArmorer;
    }

    public void setLevelArmorer(final int lvlArmorer) {
        this.levelArmorer = lvlArmorer;
    }

    public int getLevelGoldsmith() {
        return levelGoldsmith;
    }

    public void setLevelGoldsmith(final int lvlGoldsmith) {
        this.levelGoldsmith = lvlGoldsmith;
    }

    public int getLevelLeatherworker() {
        return levelLeatherworker;
    }

    public void setLevelLeatherworker(final int lvlLeatherworker) {
        this.levelLeatherworker = lvlLeatherworker;
    }

    public int getLevelWeaver() {
        return levelWeaver;
    }

    public void setLevelWeaver(final int lvlWeaver) {
        this.levelWeaver = lvlWeaver;
    }

    public int getLevelAlchemist() {
        return levelAlchemist;
    }

    public void setLevelAlchemist(final int lvlAlchemist) {
        this.levelAlchemist = lvlAlchemist;
    }

    public int getLevelCulinarian() {
        return levelCulinarian;
    }

    public void setLevelCulinarian(final int lvlCulinarian) {
        this.levelCulinarian = lvlCulinarian;
    }

    public int getLevelMiner() {
        return levelMiner;
    }

    public void setLevelMiner(final int lvlMiner) {
        this.levelMiner = lvlMiner;
    }

    public int getLevelBotanist() {
        return levelBotanist;
    }

    public void setLevelBotanist(final int lvlBotanist) {
        this.levelBotanist = lvlBotanist;
    }

    public int getLevelFisher() {
        return levelFisher;
    }

    public void setLevelFisher(final int lvlFisher) {
        this.levelFisher = lvlFisher;
    }

    public int getLevelEureka() {
        return levelEureka;
    }

    public void setLevelEureka(int levelEureka) {
        this.levelEureka = levelEureka;
    }

    public boolean isHas30DaysSub() {
        return has30DaysSub;
    }

    public void setHas30DaysSub(final boolean has30DaysSub) {
        this.has30DaysSub = has30DaysSub;
    }

    public boolean isHas60DaysSub() {
        return has60DaysSub;
    }

    public void setHas60DaysSub(final boolean has60DaysSub) {
        this.has60DaysSub = has60DaysSub;
    }

    public boolean isHas90DaysSub() {
        return has90DaysSub;
    }

    public void setHas90DaysSub(final boolean has90DaysSub) {
        this.has90DaysSub = has90DaysSub;
    }

    public boolean isHas180DaysSub() {
        return has180DaysSub;
    }

    public void setHas180DaysSub(final boolean has180DaysSub) {
        this.has180DaysSub = has180DaysSub;
    }

    public boolean isHas270DaysSub() {
        return has270DaysSub;
    }

    public void setHas270DaysSub(final boolean has270DaysSub) {
        this.has270DaysSub = has270DaysSub;
    }

    public boolean isHas360DaysSub() {
        return has360DaysSub;
    }

    public void setHas360DaysSub(final boolean has360DaysSub) {
        this.has360DaysSub = has360DaysSub;
    }

    public boolean isHas450DaysSub() {
        return has450DaysSub;
    }

    public void setHas450DaysSub(final boolean has450DaysSub) {
        this.has450DaysSub = has450DaysSub;
    }

    public boolean isHas630DaysSub() {
        return has630DaysSub;
    }

    public void setHas630DaysSub(final boolean has630DaysSub) {
        this.has630DaysSub = has630DaysSub;
    }

    public boolean isHas960DaysSub() {
        return has960DaysSub;
    }

    public void setHas960DaysSub(final boolean has960DaysSub) {
        this.has960DaysSub = has960DaysSub;
    }

    public boolean isHasPreOrderArr() {
        return hasPreOrderArr;
    }

    public void setHasPreOrderArr(final boolean hasPreOrderArr) {
        this.hasPreOrderArr = hasPreOrderArr;
    }

    public boolean isHasPreOrderHW() {
        return hasPreOrderHW;
    }

    public void setHasPreOrderHW(final boolean hasPreOrderHW) {
        this.hasPreOrderHW = hasPreOrderHW;
    }

    public boolean isHasPreOrderSB() {
        return hasPreOrderSB;
    }

    public void setHasPreOrderSB(final boolean hasPreOrderSB) {
        this.hasPreOrderSB = hasPreOrderSB;
    }

    public boolean isHasPreOrderShB() {
        return hasPreOrderShB;
    }

    public void setHasPreOrderShB(final boolean hasPreOrderShB) {
        this.hasPreOrderShB = hasPreOrderShB;
    }

    public boolean isHasARRArtbook() {
        return hasARRArtbook;
    }

    public void setHasARRArtbook(final boolean hasARRArtbook) {
        this.hasARRArtbook = hasARRArtbook;
    }

    public boolean isHasHWArtbookOne() {
        return hasHWArtbookOne;
    }

    public void setHasHWArtbookOne(final boolean hasHWArtbookOne) {
        this.hasHWArtbookOne = hasHWArtbookOne;
    }

    public boolean isHasHWArtbookTwo() {
        return hasHWArtbookTwo;
    }

    public void setHasHWArtbookTwo(final boolean hasHWArtbookTwo) {
        this.hasHWArtbookTwo = hasHWArtbookTwo;
    }

    public boolean isHasSBArtbook() {
        return hasSBArtbook;
    }

    public void setHasSBArtbook(final boolean hasSBArtbook) {
        this.hasSBArtbook = hasSBArtbook;
    }

    public boolean isHasSBArtbookTwo() {
        return hasSBArtbookTwo;
    }

    public void setHasSBArtbookTwo(boolean hasSBArtBookTwo) {
        this.hasSBArtbookTwo = hasSBArtBookTwo;
    }

    public boolean isHasEncyclopediaEorzea() {
        return hasEncyclopediaEorzea;
    }

    public void setHasEncyclopediaEorzea(final boolean hasEncyclopediaEorzea) {
        this.hasEncyclopediaEorzea = hasEncyclopediaEorzea;
    }

    public boolean isHasBeforeMeteor() {
        return hasBeforeMeteor;
    }

    public void setHasBeforeMeteor(final boolean hasBeforeMeteor) {
        this.hasBeforeMeteor = hasBeforeMeteor;
    }

    public boolean isHasBeforeTheFall() {
        return hasBeforeTheFall;
    }

    public void setHasBeforeTheFall(final boolean hasBeforeTheFall) {
        this.hasBeforeTheFall = hasBeforeTheFall;
    }

    public boolean isHasSoundtrack() {
        return hasSoundtrack;
    }

    public void setHasSoundtrack(final boolean hasSoundtrack) {
        this.hasSoundtrack = hasSoundtrack;
    }

    public boolean isHasAttendedEternalBond() {
        return hasAttendedEternalBond;
    }

    public void setHasAttendedEternalBond(final boolean hasAttendedEternalBond) {
        this.hasAttendedEternalBond = hasAttendedEternalBond;
    }

    public boolean isHasCompletedHWSightseeing() {
        return hasCompletedHWSightseeing;
    }

    public void setHasCompletedHWSightseeing(final boolean hasCompletedHWSightseeing) {
        this.hasCompletedHWSightseeing = hasCompletedHWSightseeing;
    }

    public boolean isHasCompleted2pt5() {
        return hasCompleted2pt5;
    }

    public void setHasCompleted2pt5(final boolean hasCompleted2pt5) {
        this.hasCompleted2pt5 = hasCompleted2pt5;
    }

    public boolean isHasFiftyComms() {
        return hasFiftyComms;
    }

    public void setHasFiftyComms(final boolean hasFiftyComms) {
        this.hasFiftyComms = hasFiftyComms;
    }

    public boolean isHasMooglePlush() {
        return hasMooglePlush;
    }

    public void setHasMooglePlush(final boolean hasMooglePlush) {
        this.hasMooglePlush = hasMooglePlush;
    }

    public boolean isHasTopazCarbunclePlush() {
        return hasTopazCarbunclePlush;
    }

    public void setHasTopazCarbunclePlush(final boolean hasTopazCarbunclePlush) {
        this.hasTopazCarbunclePlush = hasTopazCarbunclePlush;
    }

    public boolean isHasEmeraldCarbunclePlush() {
        return hasEmeraldCarbunclePlush;
    }

    public void setHasEmeraldCarbunclePlush(final boolean hasEmeraldCarbunclePlush) {
        this.hasEmeraldCarbunclePlush = hasEmeraldCarbunclePlush;
    }

    public boolean isHasCompletedHildibrand() {
        return hasCompletedHildibrand;
    }

    public void setHasCompletedHildibrand(final boolean hasCompletedHildibrand) {
        this.hasCompletedHildibrand = hasCompletedHildibrand;
    }

    public boolean isHasPS4Collectors() {
        return hasPS4Collectors;
    }

    public void setHasPS4Collectors(final boolean hasPS4Collectors) {
        this.hasPS4Collectors = hasPS4Collectors;
    }

    public boolean isHasEternalBond() {
        return hasEternalBond;
    }

    public void setHasEternalBond(final boolean hasEternalBond) {
        this.hasEternalBond = hasEternalBond;
    }

    public boolean isHasARRCollectors() {
        return hasARRCollectors;
    }

    public void setHasARRCollectors(final boolean hasARRCollectors) {
        this.hasARRCollectors = hasARRCollectors;
    }

    public boolean isHasKobold() {
        return hasKobold;
    }

    public void setHasKobold(final boolean hasKobold) {
        this.hasKobold = hasKobold;
    }

    public boolean isHasSahagin() {
        return hasSahagin;
    }

    public void setHasSahagin(final boolean hasSahagin) {
        this.hasSahagin = hasSahagin;
    }

    public boolean isHasAmaljaa() {
        return hasAmaljaa;
    }

    public void setHasAmaljaa(final boolean hasAmaljaa) {
        this.hasAmaljaa = hasAmaljaa;
    }

    public boolean isHasSylph() {
        return hasSylph;
    }

    public void setHasSylph(final boolean hasSylph) {
        this.hasSylph = hasSylph;
    }

    public boolean isHasMoogle() {
        return hasMoogle;
    }

    public void setHasMoogle(final boolean hasMoogle) {
        this.hasMoogle = hasMoogle;
    }

    public boolean isHasVanuVanu() {
        return hasVanuVanu;
    }

    public void setHasVanuVanu(final boolean hasVanuVanu) {
        this.hasVanuVanu = hasVanuVanu;
    }

    public boolean isHasVath() {
        return hasVath;
    }

    public void setHasVath(final boolean hasVath) {
        this.hasVath = hasVath;
    }

    public boolean isHasCompletedHW() {
        return hasCompletedHW;
    }

    public void setHasCompletedHW(final boolean hasCompletedHW) {
        this.hasCompletedHW = hasCompletedHW;
    }

    public boolean isHasCompletedSB() {
        return hasCompletedSB;
    }

    public void setHasCompletedSB(final boolean hasCompletedSB) {
        this.hasCompletedSB = hasCompletedSB;
    }

    public boolean isHasCompleted3pt1() {
        return hasCompleted3pt1;
    }

    public void setHasCompleted3pt1(final boolean hasCompleted3pt1) {
        this.hasCompleted3pt1 = hasCompleted3pt1;
    }

    public boolean isHasCompleted3pt3() {
        return hasCompleted3pt3;
    }

    public void setHasCompleted3pt3(final boolean hasCompleted3pt3) {
        this.hasCompleted3pt3 = hasCompleted3pt3;
    }

    public boolean isLegacyPlayer() {
        return isLegacyPlayer;
    }

    public void setLegacyPlayer(final boolean isLegacyPlayer) {
        this.isLegacyPlayer = isLegacyPlayer;
    }

    public List<String> getMinions() {
        return minions;
    }

    public void setMinions(final List<String> minions) {
        this.minions = minions;
    }

    public List<String> getMounts() {
        return mounts;
    }

    public void setMounts(final List<String> mounts) {
        this.mounts = mounts;
    }

    public Date getDateImgLastModified() {
        return dateImgLastModified;
    }

    public void setDateImgLastModified(final Date dateImgLastModified) {
        this.dateImgLastModified = dateImgLastModified;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public CharacterStatus getCharacterStatus() {
        return characterStatus;
    }

    public void setCharacterStatus(final CharacterStatus characterStatus) {
        this.characterStatus = characterStatus;
    }
}