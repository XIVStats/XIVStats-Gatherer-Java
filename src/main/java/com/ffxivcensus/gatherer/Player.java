package com.ffxivcensus.gatherer;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Object class to represent a Character/Player. This class specifies the attributes and behaviour of a player object.
 * It also serves the functionality of fetching player data from the Lodestone.
 *
 * @author Peter Reid
 * @since v1.0
 * @see Gatherer
 */
public class Player {

    /**
     * Number of days inactivity before character is considered inactive
     */
    private final static int ACTIVITY_RANGE_DAYS = 30;

    private static final long ONE_DAY_IN_MILLIS=86400000;

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
    private boolean hasCompleted3pt1;
    private boolean hasCompleted3pt3;
    private boolean isLegacyPlayer;
    private ArrayList minions;
    private ArrayList mounts;
    private Date dateImgLastModified;
    private boolean isActive;

    /**
     * Constructor for player object.
     *
     * @param id id of character to fetch.
     */
    public Player(int id) {
        this.setId(id);
        setRealm(null);
        setPlayerName(null);
        setRace(null);
        setGender(null);
        setGrandCompany(null);
        setFreeCompany(null);
        setLvlGladiator(0);
        setLvlPugilist(0);
        setLvlMarauder(0);
        setLvlLancer(0);
        setLvlArcher(0);
        setLvlRogue(0);
        setLvlConjurer(0);
        setLvlThaumaturge(0);
        setLvlArcanist(0);
        setLvlDarkKnight(0);
        setLvlMachinist(0);
        setLvlAstrologian(0);
        setLvlCarpenter(0);
        setLvlBlacksmith(0);
        setLvlArmorer(0);
        setLvlGoldsmith(0);
        setLvlLeatherworker(0);
        setLvlWeaver(0);
        setLvlAlchemist(0);
        setLvlCulinarian(0);
        setLvlMiner(0);
        setLvlBotanist(0);
        setLvlFisher(0);
        setHas30DaysSub(false);
        setHas60DaysSub(false);
        setHas90DaysSub(false);
        setHas180DaysSub(false);
        setHas270DaysSub(false);
        setHas360DaysSub(false);
        setHas450DaysSub(false);
        setHas630DaysSub(false);
        setHas960DaysSub(false);
        setHasPreOrderArr(false);
        setHasPreOrderHW(false);
        setHasARRArtbook(false);
        setHasHWArtbookOne(false);
        setHasHWArtbookTwo(false);
        setHasEncyclopediaEorzea(false);
        setHasBeforeMeteor(false);
        setHasBeforeTheFall(false);
        setHasSoundtrack(false);
        setHasAttendedEternalBond(false);
        setHasCompletedHWSightseeing(false);
        setHasCompleted2pt5(false);
        setHasFiftyComms(false);
        setHasMooglePlush(false);
        setHasTopazCarbunclePlush(false);
        setHasEmeraldCarbunclePlush(false);
        setHasCompletedHildibrand(false);
        setHasPS4Collectors(false);
        setHasEternalBond(false);
        setHasARRCollectors(false);
        setHasKobold(false);
        setHasSahagin(false);
        setHasAmaljaa(false);
        setHasSylph(false);
        setHasMoogle(false);
        setHasVanuVanu(false);
        setHasVath(false);
        setHasCompletedHW(false);
        setHasCompleted3pt1(false);
        setHasCompleted3pt3(false);
        setDateImgLastModified(new Date());
        setActive(false);
    }

    /**
     * Get the player's ID.
     *
     * @return the ID of the player object
     */
    public int getId() {
        return id;
    }

    /**
     * Set the player's ID.
     *
     * @param id the ID of the player object
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the player's realm.
     *
     * @return the player's realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     * Set the player's realm.
     *
     * @param realm the player's realm
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * Get the player's name.
     *
     * @return the player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Set the player's name.
     *
     * @param playerName the player's name.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Get the player's race.
     *
     * @return the player's race
     */
    public String getRace() {
        return race;
    }

    /**
     * Set the player's race.
     *
     * @param race the player's race.
     */
    public void setRace(String race) {
        this.race = race;
    }

    /**
     * Get the player's gender.
     *
     * @return the player's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the player's gender.
     *
     * @param gender the player's gender.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get the player's grand company.
     *
     * @return the player's grand company.
     */
    public String getGrandCompany() {
        return grandCompany;
    }

    /**
     * Set the player's grand company.
     *
     * @param grandCompany the player's grand company.
     */
    public void setGrandCompany(String grandCompany) {
        this.grandCompany = grandCompany;
    }

    /**
     * Get the player's free company.
     * @return player's free company.
     */
    public String getFreeCompany() {
        return freeCompany;
    }

    /**
     * Set the player's free company.
     * @param freeCompany the player's free company.
     */
    public void setFreeCompany(String freeCompany) {
        this.freeCompany = freeCompany;
    }

    /**
     * Get the player's gladiator level.
     *
     * @return the player's gladiator level.
     */
    public int getLvlGladiator() {
        return lvlGladiator;
    }

    /**
     * Set's the player's gladiator level.
     *
     * @param lvlGladiator the player's gladiator level.
     */
    public void setLvlGladiator(int lvlGladiator) {
        this.lvlGladiator = lvlGladiator;
    }

    /**
     * Get the player's pugilist level.
     *
     * @return the player's pugilist level.
     */
    public int getLvlPugilist() {
        return lvlPugilist;
    }

    /**
     * Set the player's pugilist level.
     *
     * @param lvlPugilist the player's pugilist level.
     */
    public void setLvlPugilist(int lvlPugilist) {
        this.lvlPugilist = lvlPugilist;
    }

    /**
     * Get the player's marauder level.
     *
     * @return the player's marauder level.
     */
    public int getLvlMarauder() {
        return lvlMarauder;
    }

    /**
     * Set the player's marauder level
     *
     * @param lvlMarauder the player's marauder level.
     */
    public void setLvlMarauder(int lvlMarauder) {
        this.lvlMarauder = lvlMarauder;
    }

    /**
     * Get the player's lancer level.
     *
     * @return the player's lancer level.
     */
    public int getLvlLancer() {
        return lvlLancer;
    }

    /**
     * Set the player's lancer level.
     *
     * @param lvlLancer the player's lancer level.
     */
    public void setLvlLancer(int lvlLancer) {
        this.lvlLancer = lvlLancer;
    }

    /**
     * Get the player's archer level.
     *
     * @return the player's archer level.
     */
    public int getLvlArcher() {
        return lvlArcher;
    }

    /**
     * Set the player's archer level.
     *
     * @param lvlArcher the player's archer level.
     */
    public void setLvlArcher(int lvlArcher) {
        this.lvlArcher = lvlArcher;
    }

    /**
     * Get the player's rogue level.
     *
     * @return the player's rogue level.
     */
    public int getLvlRogue() {
        return lvlRogue;
    }

    /**
     * Set the player's rogue level.
     *
     * @param lvlRogue the player's rogue level.
     */
    public void setLvlRogue(int lvlRogue) {
        this.lvlRogue = lvlRogue;
    }

    /**
     * Get the player's conjurer level.
     *
     * @return the player's conjurer level.
     */
    public int getLvlConjurer() {
        return lvlConjurer;
    }

    /**
     * Set the player's conjurer level.
     *
     * @param lvlConjurer the player's conjurer level.
     */
    public void setLvlConjurer(int lvlConjurer) {
        this.lvlConjurer = lvlConjurer;
    }

    /**
     * Get the player's thaumaturge level.
     *
     * @return the player's thaumaturge level.
     */
    public int getLvlThaumaturge() {
        return lvlThaumaturge;
    }

    /**
     * Set the player's thaumaturge level.
     *
     * @param lvlThaumaturge the player's thaumaturge level.
     */
    public void setLvlThaumaturge(int lvlThaumaturge) {
        this.lvlThaumaturge = lvlThaumaturge;
    }

    /**
     * Set the player's arcanist level.
     *
     * @return the player's arcanist level.
     */
    public int getLvlArcanist() {
        return lvlArcanist;
    }

    /**
     * Set the player's arcanist level.
     *
     * @param lvlArcanist the player's arcanist level
     */
    public void setLvlArcanist(int lvlArcanist) {
        this.lvlArcanist = lvlArcanist;
    }

    /**
     * Get the players dark knight level.
     *
     * @return the player's dark knight level.
     */
    public int getLvlDarkKnight() {
        return lvlDarkKnight;
    }

    /**
     * Set the player's dark knight level.
     *
     * @param lvlDarkKnight the player's dark knight level.
     */
    public void setLvlDarkKnight(int lvlDarkKnight) {
        this.lvlDarkKnight = lvlDarkKnight;
    }

    /**
     * Get the player's machinist level.
     *
     * @return the player's machinist level.
     */
    public int getLvlMachinist() {
        return lvlMachinist;
    }

    /**
     * Set the player's machinist level.
     *
     * @param lvlMachinist the player's machinist level.
     */
    public void setLvlMachinist(int lvlMachinist) {
        this.lvlMachinist = lvlMachinist;
    }

    /**
     * Get the player's astrologian level.
     *
     * @return the player's astrologian level.
     */
    public int getLvlAstrologian() {
        return lvlAstrologian;
    }

    /**
     * Set the player's astrologian level.
     *
     * @param lvlAstrologian the player's astrologian level.
     */
    public void setLvlAstrologian(int lvlAstrologian) {
        this.lvlAstrologian = lvlAstrologian;
    }

    /**
     * Get the player's carpenter level.
     *
     * @return the player's carpenter level.
     */
    public int getLvlCarpenter() {
        return lvlCarpenter;
    }

    /**
     * Set the player's carpenter's level.
     *
     * @param lvlCarpenter the player's carpenter level.
     */
    public void setLvlCarpenter(int lvlCarpenter) {
        this.lvlCarpenter = lvlCarpenter;
    }

    /**
     * Get the player's blacksmith level.
     *
     * @return the player's blacksmith level.
     */
    public int getLvlBlacksmith() {
        return lvlBlacksmith;
    }

    /**
     * Set the player's blacksmith level.
     *
     * @param lvlBlacksmith the blacksmith level.
     */
    public void setLvlBlacksmith(int lvlBlacksmith) {
        this.lvlBlacksmith = lvlBlacksmith;
    }

    /**
     * Get the player's armorer level.
     *
     * @return the player's armorer level.
     */
    public int getLvlArmorer() {
        return lvlArmorer;
    }

    /**
     * Set the player's armorer level.
     *
     * @param lvlArmorer the player's armorer level.
     */
    public void setLvlArmorer(int lvlArmorer) {
        this.lvlArmorer = lvlArmorer;
    }

    /**
     * Get the player's goldsmith level.
     *
     * @return the player's goldsmith level.
     */
    public int getLvlGoldsmith() {
        return lvlGoldsmith;
    }

    /**
     * Set the player's goldsmith level.
     *
     * @param lvlGoldsmith the player's goldsmith level.
     */
    public void setLvlGoldsmith(int lvlGoldsmith) {
        this.lvlGoldsmith = lvlGoldsmith;
    }

    /**
     * Get the player's leatherworker level.
     *
     * @return the player's leatherworker level.
     */
    public int getLvlLeatherworker() {
        return lvlLeatherworker;
    }

    /**
     * Set the player's leatherworker level.
     *
     * @param lvlLeatherworker the player's leatherworker level.
     */
    public void setLvlLeatherworker(int lvlLeatherworker) {
        this.lvlLeatherworker = lvlLeatherworker;
    }

    /**
     * Get the player's weaver level.
     *
     * @return the player's weaver level.
     */
    public int getLvlWeaver() {
        return lvlWeaver;
    }

    /**
     * Set the player's weaver level.
     *
     * @param lvlWeaver the player's waever level.
     */
    public void setLvlWeaver(int lvlWeaver) {
        this.lvlWeaver = lvlWeaver;
    }

    /**
     * Get the player's alchemist level.
     *
     * @return the player's alchemist level
     */
    public int getLvlAlchemist() {
        return lvlAlchemist;
    }

    /**
     * Set the player's alchemist level.
     *
     * @param lvlAlchemist the player's alchemist level.
     */
    public void setLvlAlchemist(int lvlAlchemist) {
        this.lvlAlchemist = lvlAlchemist;
    }

    /**
     * Get the player's culinarian level.
     *
     * @return the player's culinarian level.
     */
    public int getLvlCulinarian() {
        return lvlCulinarian;
    }

    /**
     * Set the player's culinarian level.
     *
     * @param lvlCulinarian the player's culinarian level.
     */
    public void setLvlCulinarian(int lvlCulinarian) {
        this.lvlCulinarian = lvlCulinarian;
    }

    /**
     * Get the player's miner level.
     *
     * @return the player's miner level.
     */
    public int getLvlMiner() {
        return lvlMiner;
    }

    /**
     * Set the player's miner level.
     *
     * @param lvlMiner the player's miner level.
     */
    public void setLvlMiner(int lvlMiner) {
        this.lvlMiner = lvlMiner;
    }

    /**
     * Get the player's botanist level.
     *
     * @return the player's botanist level.
     */
    public int getLvlBotanist() {
        return lvlBotanist;
    }

    /**
     * Set the player's botanist level.
     *
     * @param lvlBotanist the player's botanist level.
     */
    public void setLvlBotanist(int lvlBotanist) {
        this.lvlBotanist = lvlBotanist;
    }

    /**
     * Get the player's fisher level.
     *
     * @return the player's fisher level.
     */
    public int getLvlFisher() {
        return lvlFisher;
    }

    /**
     * Set the player's fisher level.
     *
     * @param lvlFisher the player's fisher level.
     */
    public void setLvlFisher(int lvlFisher) {
        this.lvlFisher = lvlFisher;
    }

    /**
     * Get whether the player has had 30 days of subscription time.
     *
     * @return whether the player has had 30 days of subscription time.
     */
    public boolean isHas30DaysSub() {
        return has30DaysSub;
    }

    /**
     * Get bit value of whether player has had 30 days of subscription.
     *
     * @return whether the player has had 30 days of subscription time as a bit value.
     */
    public int getBitHas30DaysSub() {
        if (isHas30DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 30 days of subscription time.
     *
     * @param has30DaysSub whether the player has had 30 days of subscription time.
     */
    public void setHas30DaysSub(boolean has30DaysSub) {
        this.has30DaysSub = has30DaysSub;
    }

    /**
     * Get whether the player has had 60 days of subscription time.
     *
     * @return whether the player has had 60 days of subscription time.
     */
    public boolean isHas60DaysSub() {
        return has60DaysSub;
    }

    /**
     * Get bit value of whether the player has had 60 days of subscription time.
     *
     * @return whether the player has had 60 days of subscription time as a bit value.
     */
    public int getBitHas60DaysSub() {
        if (isHas60DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 60 days of subscription time.
     *
     * @param has60DaysSub whether the player has had 60 days of subscription time.
     */
    public void setHas60DaysSub(boolean has60DaysSub) {
        this.has60DaysSub = has60DaysSub;
    }

    /**
     * Get whether the player has had 90 days of subscription time.
     *
     * @return whether the player has had 90 days of subscription time.
     */
    public boolean isHas90DaysSub() {
        return has90DaysSub;
    }

    /**
     * Get bit value of whether the player has had 90 days of subscription time.
     *
     * @return whether the player has had 90 days of subscription time as a bit value.
     */
    public int getBitHas90DaysSub() {
        if (isHas90DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 90 days of subscription time.
     *
     * @param has90DaysSub whether the player has had 90 days of subscription time.
     */
    public void setHas90DaysSub(boolean has90DaysSub) {
        this.has90DaysSub = has90DaysSub;
    }

    /**
     * Get whether the player has had 180 days of subscription time.
     *
     * @return whether the player has had 180 days of subscription time.
     */
    public boolean isHas180DaysSub() {
        return has180DaysSub;
    }

    /**
     * Get bit value of whether the player has had 180 days of subscription time.
     *
     * @return whether the player has had 180 days of subscription time as a bit value.
     */
    public int getBitHas180DaysSub() {
        if (isHas180DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 180 days of subscription time.
     *
     * @param has180DaysSub whether the player has had 180 days of subscription time.
     */
    public void setHas180DaysSub(boolean has180DaysSub) {
        this.has180DaysSub = has180DaysSub;
    }

    /**
     * Get whether the player has had 270 days of subscription time.
     *
     * @return whether the player has had 270 days of subscription time.
     */
    public boolean isHas270DaysSub() {
        return has270DaysSub;
    }

    /**
     * Get bit value of whether the player has had 270 days of subscription time.
     *
     * @return whether the player has had 270 days of subscription time as a bit value.
     */
    public int getBitHas270DaysSub() {
        if (isHas270DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 270 days of subscription time.
     *
     * @param has270DaysSub whether the player has had 270 days of subscription time.
     */
    public void setHas270DaysSub(boolean has270DaysSub) {
        this.has270DaysSub = has270DaysSub;
    }

    /**
     * Get whether the player has had 360 days of subscription time.
     *
     * @return whether the player has had 360 days of subscription time.
     */
    public boolean isHas360DaysSub() {
        return has360DaysSub;
    }

    /**
     * Get bit value of whether the player has had 360 days of subscription time.
     *
     * @return whether the player has had 360 days of subscription time as a bit value.
     */
    public int getBitHas360DaysSub() {
        if (isHas360DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 360 days of subscription time.
     *
     * @param has360DaysSub whether the player has had 360 days of subscription time.
     */
    public void setHas360DaysSub(boolean has360DaysSub) {
        this.has360DaysSub = has360DaysSub;
    }

    /**
     * Get whether the player has had 450 days of subscription time.
     *
     * @return whether the player has had 450 days of subscription time.
     */
    public boolean isHas450DaysSub() {
        return has450DaysSub;
    }

    /**
     * Get bit value of whether the player has had 450 days of subscription time.
     *
     * @return whether the player has had 450 days of subscription time as a bit value.
     */
    public int getBitHas450DaysSub() {
        if (isHas450DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 450 days of subscription time.
     *
     * @param has450DaysSub whether the player has had 450 days of subscription time.
     */
    public void setHas450DaysSub(boolean has450DaysSub) {
        this.has450DaysSub = has450DaysSub;
    }

    /**
     * Set whether the player has had 630 days of subscription time.
     *
     * @param has630DaysSub whether the player has had 630 days of subscription time.
     */
    public void setHas630DaysSub(boolean has630DaysSub) {
        this.has630DaysSub = has630DaysSub;
    }

    /**
     * Get whether the player has had 630 days of subscription time.
     *
     * @return whether the player has had 630 days of subscription time.
     */
    public boolean isHas630DaysSub() {
        return has630DaysSub;
    }

    /**
     * Get bit value of whether the player has had 630 days of subscription time.
     *
     * @return whether the player has had 630 days of subscription time as a bit value.
     */
    public int getBitHas630DaysSub() {
        if (isHas630DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has had 960 days of subscription time.
     *
     * @param has960DaysSub whether the player has had 630 days of subscription time.
     */
    public void setHas960DaysSub(boolean has960DaysSub) {
        this.has960DaysSub = has960DaysSub;
    }

    /**
     * Get whether the player has had 960 days of subscription time.
     *
     * @return whether the player has had 960 days of subscription time.
     */
    public boolean isHas960DaysSub() {
        return has960DaysSub;
    }

    /**
     * Get bit value of whether the player has had 960 days of subscription time.
     *
     * @return whether the player has had 960 days of subscription time as a bit value.
     */
    public int getBitHas960DaysSub() {
        if (isHas960DaysSub()) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * Get whether the player had preordered "A Realm Reborn".
     *
     * @return whether the player had preordered "A Realm Reborn".
     */
    public boolean isHasPreOrderArr() {
        return hasPreOrderArr;
    }

    /**
     * Get the bit value representing whether player had preordered "A Realm Reborn".
     *
     * @return bit value representing whether player had preordered "A Realm Reborn".
     */
    public int getBitHasPreOrderArr() {
        if (isHasPreOrderArr()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player had preordered "A Realm Reborn"
     *
     * @param hasPreOrderArr whether the player had preordered "A Realm Reborn"
     */
    public void setHasPreOrderArr(boolean hasPreOrderArr) {
        this.hasPreOrderArr = hasPreOrderArr;
    }

    /**
     * Get whether the user had preordered "Heavensward".
     *
     * @return whether the user had preordered "Heavensward".
     */
    public boolean isHasPreOrderHW() {
        return hasPreOrderHW;
    }

    /**
     * Get bit value stating whether the user had preordered "Heavensward".
     *
     * @return bit value representing whether the user had preordered "Heavensward".
     */
    public int getBitHasPreOrderHW() {
        if (isHasPreOrderHW()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the user had preordered "Heavensward".
     *
     * @param hasPreOrderHW whether the user had preordered "Heavensward".
     */
    public void setHasPreOrderHW(boolean hasPreOrderHW) {
        this.hasPreOrderHW = hasPreOrderHW;
    }

    /**
     * Gets whether the user has purchased the artbook.
     *
     * @return whether the user has purchased the artbook.
     */
    public boolean isHasARRArtbook() {
        return hasARRArtbook;
    }

    /**
     * Get bit value as to whether the player has purchased the artbook.
     *
     * @return bit value as to whether the player has purchased the artbook.
     */
    public int getBitHasArrArtbook() {
        if (isHasARRArtbook()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has purchased the artbook.
     *
     * @param hasARRArtbook whether the player has purchased the artbook.
     */
    public void setHasARRArtbook(boolean hasARRArtbook) {
        this.hasARRArtbook = hasARRArtbook;
    }

    /**
     * Get whether the player has purchased the before the meteor sound track.
     *
     * @return whether the player has purchased the before the meteor sound track.
     */
    public boolean isHasBeforeMeteor() {
        return hasBeforeMeteor;
    }

    /**
     * Get bit value for whether the player has purchased the before the meteor sound track.
     *
     * @return bit value whether the player has purchased the before the meteor sound track.
     */
    public int getBitHasBeforeMeteor() {
        if (isHasBeforeMeteor()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has purchased the before the meteor sound track.
     *
     * @param hasBeforeMeteor whether the player has purchased the before the meteor sound track.
     */
    public void setHasBeforeMeteor(boolean hasBeforeMeteor) {
        this.hasBeforeMeteor = hasBeforeMeteor;
    }

    /**
     * Get whether the player has purchased the before the fall sound track.
     *
     * @return whether the player has purchased the before the fall sound track.
     */
    public boolean isHasBeforeTheFall() {
        return hasBeforeTheFall;
    }

    /**
     * Get bit value for whether the player has purchased the before the fall sound track.
     *
     * @return bit value whether the player has purchased the before the fall sound track.
     */
    public int getBitHasBeforeTheFall() {
        if (isHasBeforeTheFall()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has purchased the before the fall sound track.
     *
     * @param hasBeforeTheFall whether the player has purchased the before the fall sound track.
     */
    public void setHasBeforeTheFall(boolean hasBeforeTheFall) {
        this.hasBeforeTheFall = hasBeforeTheFall;
    }

    /**
     * Get whether the player has purchased the sound track.
     *
     * @return whether the player has purchased the sound track.
     */
    public boolean isHasSoundtrack() {
        return hasSoundtrack;
    }

    /**
     * Get bit value for whether the player has purchased the sound track.
     *
     * @return bit value whether the player has purchased the sound track.
     */
    public int getBitHasSoundTrack() {
        if (isHasSoundtrack()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has purchased the sound track.
     *
     * @param hasSoundtrack whether the player has purchased the sound track.
     */
    public void setHasSoundtrack(boolean hasSoundtrack) {
        this.hasSoundtrack = hasSoundtrack;
    }

    /**
     * Gets whether the player has attended an "Eternal Bond" ceremony.
     *
     * @return whether the player has attended an "Eternal Bond" ceremony.
     */
    public boolean isHasAttendedEternalBond() {
        return hasAttendedEternalBond;
    }

    /**
     * Get bit value for whether the player has attended an "Eternal Bond" ceremony.
     *
     * @return bit value whether the player has attended an "Eternal Bond" ceremony.
     */
    public int getBitHasAttendedEternalBond() {
        if (isHasAttendedEternalBond()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has attended an "Eternal Bond" ceremony.
     *
     * @param hasAttendedEternalBond whether the player has attended an "Eternal Bond" ceremony.
     */
    public void setHasAttendedEternalBond(boolean hasAttendedEternalBond) {
        this.hasAttendedEternalBond = hasAttendedEternalBond;
    }

    /**
     * Get whether the player has completed the Heavensward sightseeing log.
     *
     * @return whether the player has completed the Heavensward sightseeing log.
     */
    public boolean isHasCompletedHWSightseeing() {
        return hasCompletedHWSightseeing;
    }

    /**
     * Get bit value indicating whether player has completed Heavensward sightseeing log.
     *
     * @return bit value indicating whether player has completed Heavensward sightseeing log.
     */
    public int getBitHasCompletedHWSightseeing() {
        if (isHasCompletedHWSightseeing()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the Heavensward sightseeing log.
     *
     * @param hasCompletedHWSightseeing whether the player has completed the Heavensward sightseeing log.
     */
    public void setHasCompletedHWSightseeing(boolean hasCompletedHWSightseeing) {
        this.hasCompletedHWSightseeing = hasCompletedHWSightseeing;
    }

    /**
     * Get whether the player has completed the 2.5 story.
     *
     * @return whether the player has completed the 2.5 story.
     */
    public boolean isHasCompleted2pt5() {
        return hasCompleted2pt5;
    }

    /**
     * Get bit value as to whether the player has completed the 2.5 story.
     *
     * @return bit value as to whether the player has completed the 2.5 story.
     */
    public int getBitHasCompleted2pt5() {
        if (isHasCompleted2pt5()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the 2.5 story.
     *
     * @param hasCompleted2pt5 whether the player has completed the 2.5 story.
     */
    public void setHasCompleted2pt5(boolean hasCompleted2pt5) {
        this.hasCompleted2pt5 = hasCompleted2pt5;
    }

    /**
     * Set whether the player has received 50 player commendations.
     *
     * @return whether the player has received 50 player commendations.
     */
    public boolean isHasFiftyComms() {
        return hasFiftyComms;
    }

    /**
     * Get bit value as to whether the player has received 50 player commendations.
     *
     * @return bit value for whether the player has received 50 player commendations.
     */
    public int getBitHasFiftyComms() {
        if (isHasFiftyComms()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has received 50 player commendations.
     *
     * @param hasFiftyComms whether the player has received 50 player commendations.
     */
    public void setHasFiftyComms(boolean hasFiftyComms) {
        this.hasFiftyComms = hasFiftyComms;
    }

    /**
     * Get whether the player has purchased the "Moogle Plush".
     *
     * @return whether the player has purchased the "Moogle Plush".
     */
    public boolean isHasMooglePlush() {
        return hasMooglePlush;
    }

    /**
     * Get bit value whether the player has received 50 player commendations.
     *
     * @return whether the player has received 50 player commendations.
     */
    public int getBitHasMooglePlush() {
        if (isHasMooglePlush()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has purchased the "Moogle Plush".
     *
     * @param hasMooglePlush whether the player has purchased the "Moogle Plush".
     */
    public void setHasMooglePlush(boolean hasMooglePlush) {
        this.hasMooglePlush = hasMooglePlush;
    }

    /**
     * Get whether the player has completed the Hildibrand quest series.
     *
     * @return whether the player has completed the Hildibrand quest series.
     */
    public boolean isHasCompletedHildibrand() {
        return hasCompletedHildibrand;
    }

    /**
     * Get bit value whether the player has completed the Hildibrand quest series.
     *
     * @return bit value whether the player has completed the Hildibrand quest series.
     */
    public int getBitHasCompletedHildibrand() {
        if (isHasCompletedHildibrand()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the Hildibrand quest series.
     *
     * @param hasCompletedHildibrand whether the player has completed the Hildibrand quest series.
     */
    public void setHasCompletedHildibrand(boolean hasCompletedHildibrand) {
        this.hasCompletedHildibrand = hasCompletedHildibrand;
    }

    /**
     * Get whether the player has purchased the PS4 collectors edition.
     *
     * @return whether the player has purchased the PS4 collectors edition.
     */
    public boolean isHasPS4Collectors() {
        return hasPS4Collectors;
    }

    /**
     * Get bit value whether the player has purchased the PS4 collectors edition.
     *
     * @return bit value whether the player has purchased the PS4 collectors edition.
     */
    public int getBitHasPS4Collectors() {
        if (isHasPS4Collectors()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has purchased the PS4 collectors edition.
     *
     * @param hasPS4Collectors whether the player has purchased the PS4 collectors edition.
     */
    public void setHasPS4Collectors(boolean hasPS4Collectors) {
        this.hasPS4Collectors = hasPS4Collectors;
    }

    /**
     * Get whether player has an "Eternal Bond" with another player.
     *
     * @return whether player has an "Eternal Bond" with another player.
     */
    public boolean isHasEternalBond() {
        return hasEternalBond;
    }

    /**
     * Get bit value as to whether player has an "Eternal Bond" with another player.
     *
     * @return whether player has an "Eternal Bond" with another player.
     */
    public int getBitHasEternalBond() {
        if (isHasEternalBond()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether player has an "Eternal Bond" with another player.
     *
     * @param hasEternalBond whether player has an "Eternal Bond" with another player.
     */
    public void setHasEternalBond(boolean hasEternalBond) {
        this.hasEternalBond = hasEternalBond;
    }

    /**
     * Get whether the player has purchased the "A Realm Reborn" collectors edition.
     *
     * @return whether the player has purchased the "A Realm Reborn" collectors edition.
     */
    public boolean isHasARRCollectors() {
        return hasARRCollectors;
    }

    /**
     * Get bit value whether the player has purchased the "A Realm Reborn" collectors edition.
     *
     * @return bit value whether the player has purchased the "A Realm Reborn" collectors edition.
     */
    public int getBitHasARRCollectors() {
        if (isHasARRCollectors()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has purchased the "A Realm Reborn" collectors edition.
     *
     * @param hasARRCollectors whether the player has purchased the "A Realm Reborn" collectors edition.
     */
    public void setHasARRCollectors(boolean hasARRCollectors) {
        this.hasARRCollectors = hasARRCollectors;
    }

    /**
     * Get whether the player has completed the "Kobold" dailies reputation.
     *
     * @return whether the player has completed the "Kobold" dailies reputation.
     */
    public boolean isHasKobold() {
        return hasKobold;
    }

    /**
     * Get bit value whether the player has completed the "Kobold" dailies reputation.
     *
     * @return bit value whether the player has completed the "Kobold" dailies reputation.
     */
    public int getBitHasKobold() {
        if (isHasKobold()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the "Kobold" dailies reputation.
     *
     * @param hasKobold whether the player has completed the "Kobold" dailies reputation.
     */
    public void setHasKobold(boolean hasKobold) {
        this.hasKobold = hasKobold;
    }

    /**
     * Get whether the player has completed the "Sahagin" dailies reputation.
     *
     * @return whether the player has completed the "Sahagin" dailies reputation.
     */
    public boolean isHasSahagin() {
        return hasSahagin;
    }

    /**
     * Get bit value for whether the player has completed the "Sahagin" dailies reputation.
     *
     * @return bit value for whether the player has completed the "Sahagin" dailies reputation.
     */
    public int getBitHasSahagin() {
        if (isHasSahagin()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the "Sahagin" dailies reputation.
     *
     * @param hasSahagin whether the player has completed the "Sahagin" dailies reputation.
     */
    public void setHasSahagin(boolean hasSahagin) {
        this.hasSahagin = hasSahagin;
    }

    /**
     * Get whether the player has completed the "Amaljaa" dailies reputation.
     *
     * @return whether the player has completed the "Amaljaa" dailies reputation.
     */
    public boolean isHasAmaljaa() {
        return hasAmaljaa;
    }

    /**
     * Get bit value whether the player has completed the "Amaljaa" dailies reputation.
     *
     * @return bit value whether the player has completed the "Amaljaa" dailies reputation.
     */
    public int getBitHasAmaljaa() {
        if (isHasAmaljaa()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the "Amaljaa" dailies reputation.
     *
     * @param hasAmaljaa whether the player has completed the "Amaljaa" dailies reputation.
     */
    public void setHasAmaljaa(boolean hasAmaljaa) {
        this.hasAmaljaa = hasAmaljaa;
    }

    /**
     * Get whether the player has completed the "Sylph" dailies reputation.
     *
     * @return whether the player has completed the "Sylph" dailies reputation.
     */
    public boolean isHasSylph() {
        return hasSylph;
    }

    /**
     * Get bit value whether the player has completed the "Sylph" dailies reputation.
     *
     * @return bit value whether the player has completed the "Sylph" dailies reputation.
     */
    public int getBitHasSylph() {
        if (isHasSylph()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the "Sylph" dailies reputation.
     *
     * @param hasSylph whether the player has completed the "Sylph" dailies reputation.
     */
    public void setHasSylph(boolean hasSylph) {
        this.hasSylph = hasSylph;
    }

    /**
     * Get whether the player has completed the Heavensward 3.0 story.
     *
     * @return whether the player has completed the Heavensward 3.0 story.
     */
    public boolean isHasCompletedHW() {
        return hasCompletedHW;
    }

    /**
     * Get bit value whether the player has completed the Heavensward 3.0 story.
     *
     * @return bit value whether the player has completed the Heavensward 3.0 story.
     */
    public int getBitHasCompletedHW() {
        if (isHasCompletedHW()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the player has completed the Heavensward 3.0 story.
     *
     * @param hasCompletedHW whether the player has completed the Heavensward 3.0 story.
     */
    public void setHasCompletedHW(boolean hasCompletedHW) {
        this.hasCompletedHW = hasCompletedHW;
    }

    /**
     * Get whether the user has completed the 3.1 story.
     *
     * @return whether the user has completed the 3.1 story.
     */
    public boolean isHasCompleted3pt1() {
        return hasCompleted3pt1;
    }

    /**
     * Get bit value whether the user has completed the 3.1 story.
     *
     * @return bit value
     */
    public int getBitHasCompleted3pt1() {
        if (hasCompleted3pt1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the user has completed the 3.1 story.
     *
     * @param hasCompleted3pt1 whether the user has completed the 3.1 story.
     */
    public void setHasCompleted3pt1(boolean hasCompleted3pt1) {
        this.hasCompleted3pt1 = hasCompleted3pt1;
    }

    /**
     * Get whether the user has completed the 3.3 story.
     *
     * @return whether the user has completed the 3.3 story.
     */
    public boolean isHasCompleted3pt3() {
        return hasCompleted3pt3;
    }

    /**
     * Get bit value whether the user has completed the 3.3 story.
     *
     * @return bit value
     */
    public int getBitHasCompleted3pt3() {
        if (hasCompleted3pt3) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the user has completed the 3.3 story.
     *
     * @param hasCompleted3pt3 whether the user has completed the 3.3 story.
     */
    public void setHasCompleted3pt3(boolean hasCompleted3pt3) {
        this.hasCompleted3pt3 = hasCompleted3pt3;
    }

    /**
     * Set the date on which the player's avatar was last modified
     * @param dateImgLastModified the date on which the player's avatar was last modified
     */
    public void setDateImgLastModified(Date dateImgLastModified) {
        this.dateImgLastModified = dateImgLastModified;
    }

    /**
     * Get whether the user played 1.0.
     *
     * @return whether the user has played 1.0
     */
    public boolean getIsLegacyPlayer() {
        return isLegacyPlayer;
    }

    /**
     * Get bit value whether the user has played 1.0
     *
     * @return bit value indicating whether player has played 1.0.
     */
    public int getBitIsLegacyPlayer() {
        if (isLegacyPlayer) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Set whether the user has played 1.0.
     *
     * @param isLegacyPlayer whether the player has played 1.0.
     */
    public void setIsLegacyPlayer(boolean isLegacyPlayer) {
        this.isLegacyPlayer = isLegacyPlayer;
    }

    /**
     * Get the character's minion set as a comma delimited string.
     * @return comma delimited string of player's minions.
     */
    public String getMinionsString(){
        StringBuilder sbMinions = new StringBuilder();
        for(int index =0; index < this.minions.size(); index++){//For each minion string
            if(index != 0){
                sbMinions.append(",");
            }
            sbMinions.append(this.minions.get(index).toString());
        }
        return sbMinions.toString();
    }

    /**
     * Get the character's mount set as a comma delimited string.
     * @return comma delimited string of player's mounts.
     */
    public String getMountsString(){
        StringBuilder sbMounts = new StringBuilder();
        for(int index =0; index < this.mounts.size(); index++){//For each mount string
            if(index != 0){
                sbMounts.append(",");
            }
            sbMounts.append(this.mounts.get(index).toString());
        }
        return sbMounts.toString();
    }


    /**
     * Set player class levels.
     *
     * @param arrLevels integer array of classes in order displayed on lodestone.
     */
    public void setLevels(int[] arrLevels) {
        this.setLvlGladiator(arrLevels[0]);
        this.setLvlPugilist(arrLevels[1]);
        this.setLvlMarauder(arrLevels[2]);
        this.setLvlLancer(arrLevels[3]);
        this.setLvlArcher(arrLevels[4]);
        this.setLvlRogue(arrLevels[5]);
        this.setLvlConjurer(arrLevels[6]);
        this.setLvlThaumaturge(arrLevels[7]);
        this.setLvlArcanist(arrLevels[8]);
        this.setLvlDarkKnight(arrLevels[9]);
        this.setLvlMachinist(arrLevels[10]);
        this.setLvlAstrologian(arrLevels[11]);
        this.setLvlCarpenter(arrLevels[12]);
        this.setLvlBlacksmith(arrLevels[13]);
        this.setLvlArmorer(arrLevels[14]);
        this.setLvlGoldsmith(arrLevels[15]);
        this.setLvlLeatherworker(arrLevels[16]);
        this.setLvlWeaver(arrLevels[17]);
        this.setLvlAlchemist(arrLevels[18]);
        this.setLvlCulinarian(arrLevels[19]);
        this.setLvlMiner(arrLevels[20]);
        this.setLvlBotanist(arrLevels[21]);
        this.setLvlFisher(arrLevels[22]);
    }

    /**
     * Get player's mount set.
     *
     * @return the player's mounts as an arraylist.
     */
    public ArrayList getMounts() {
        return mounts;
    }

    /**
     * Set player's mount set.
     *
     * @param mounts the player's mounts as an arraylist.
     */
    public void setMounts(ArrayList mounts) {
        this.mounts = mounts;
    }

    /**
     * Get player's minion set.
     *
     * @return the player's minions as an arraylist.
     */
    public ArrayList getMinions() {
        return minions;
    }

    /**
     * Set player's minion set.
     *
     * @param minions the player's minions as an arraylist.
     */
    public void setMinions(ArrayList minions) {
        this.minions = minions;
    }

    /**
     * Determine if a player has a specified mount
     *
     * @param mountName the name of the mount to check for.
     * @return whether the player has the specified mount.
     */
    public boolean doesPlayerHaveMount(String mountName) {
        return this.mounts.contains(mountName);
    }

    /**
     * Determine if a player has a specified minion.
     *
     * @param minionName the name of the minion to check for
     * @return whether the player has the specified minion.
     */
    public boolean doesPlayerHaveMinion(String minionName) {
        return this.minions.contains(minionName);
    }

    /**
     * Fetch a player from the lodestone specified by ID.
     *
     * @param playerID the ID of the player to fetch
     * @return the player object matching the specified ID.
     * @throws Exception exception thrown if more class levels returned than anticipated.
     */
    public static Player getPlayer(int playerID) throws Exception {
        //Initialize player object to return
        Player player = new Player(playerID);
        //Declare HTML document
        Document doc;

        //URL to connect to
        String url = "http://eu.finalfantasyxiv.com/lodestone/character/" + playerID + "/";

        try {
            //Fetch the specified URL
            doc = Jsoup.connect(url).get();
            player.setPlayerName(getNameFromPage(doc));
            player.setRealm(getRealmFromPage(doc));
            player.setRace(getRaceFromPage(doc));
            player.setGender(getGenderFromPage(doc));
            player.setGrandCompany(getGrandCompanyFromPage(doc));
            player.setFreeCompany(getFreeCompanyFromPage(doc));
            player.setDateImgLastModified(getDateLastUpdatedFromPage(doc,playerID));
            player.setLevels(getLevelsFromPage(doc));
            player.setMounts(getMountsFromPage(doc));
            player.setMinions(getMinionsFromPage(doc));
            player.setHas30DaysSub(player.doesPlayerHaveMinion("Wind-up Cursor"));
            player.setHas60DaysSub(player.doesPlayerHaveMinion("Black Chocobo Chick"));
            player.setHas90DaysSub(player.doesPlayerHaveMinion("Beady Eye"));
            player.setHas180DaysSub(player.doesPlayerHaveMinion("Minion Of Light"));
            player.setHas270DaysSub(player.doesPlayerHaveMinion("Wind-up Leader"));
            player.setHas360DaysSub(player.doesPlayerHaveMinion("Wind-up Odin"));
            player.setHas450DaysSub(player.doesPlayerHaveMinion("Wind-up Goblin"));
            player.setHas630DaysSub(player.doesPlayerHaveMinion("Wind-up Nanamo"));
            player.setHas960DaysSub(player.doesPlayerHaveMinion("Wind-up Firion"));
            player.setHasPreOrderArr(player.doesPlayerHaveMinion("Cait Sith Doll"));
            player.setHasPreOrderHW(player.doesPlayerHaveMinion("Chocobo Chick Courier"));
            player.setHasARRArtbook(player.doesPlayerHaveMinion("Model Enterprise"));
            player.setHasHWArtbookOne(player.doesPlayerHaveMinion("Wind-Up Relm"));
            player.setHasHWArtbookTwo(player.doesPlayerHaveMinion("Wind-Up Hraesvelgr"));
            player.setHasEncyclopediaEorzea(player.doesPlayerHaveMinion("Namingway"));
            player.setHasBeforeMeteor(player.doesPlayerHaveMinion("Wind-up Dalamud"));
            player.setHasBeforeTheFall(player.doesPlayerHaveMinion("Set Of Primogs"));
            player.setHasSoundtrack(player.doesPlayerHaveMinion("Wind-up Bahamut"));
            player.setHasAttendedEternalBond(player.doesPlayerHaveMinion("Demon Box"));
            player.setHasCompletedHWSightseeing(player.doesPlayerHaveMinion("Fledgling Apkallu"));
            player.setHasCompleted2pt5(player.doesPlayerHaveMinion("Midgardsormr"));
            player.setHasFiftyComms(player.doesPlayerHaveMinion("Princely Hatchling"));
            player.setHasMooglePlush(player.doesPlayerHaveMinion("Wind-up Delivery Moogle"));
            player.setHasTopazCarbunclePlush(player.doesPlayerHaveMinion("Heliodor Carbuncle"));
            player.setHasEmeraldCarbunclePlush(player.doesPlayerHaveMinion("Peridot Carbuncle"));
            player.setHasCompletedHildibrand(player.doesPlayerHaveMinion("Wind-up Gentleman"));
            player.setHasPS4Collectors(player.doesPlayerHaveMinion("Wind-up Moogle"));
            player.setHasCompleted3pt1(player.doesPlayerHaveMinion("Wind-up Haurchefant"));
            player.setHasCompleted3pt3(player.doesPlayerHaveMinion("Wind-up Aymeric"));
            player.setHasEternalBond(player.doesPlayerHaveMount("Ceremony Chocobo"));
            player.setHasARRCollectors(player.doesPlayerHaveMount("Coeurl"));
            player.setHasKobold(player.doesPlayerHaveMount("Bomb Palanquin"));
            player.setHasSahagin(player.doesPlayerHaveMount("Cavalry Elbst"));
            player.setHasAmaljaa(player.doesPlayerHaveMount("Cavalry Drake"));
            player.setHasSylph(player.doesPlayerHaveMount("Laurel Goobbue"));
            player.setHasMoogle(player.doesPlayerHaveMount("Cloud Mallow"));
            player.setHasVanuVanu(player.doesPlayerHaveMount("Sanuwa"));
            player.setHasVath(player.doesPlayerHaveMount("Kongamato"));
            player.setHasCompletedHW(player.doesPlayerHaveMount("Midgardsormr"));
            player.setIsLegacyPlayer(player.doesPlayerHaveMount("Legacy Chocobo"));
            player.setActive(player.isPlayerActiveInDateRange());
        } catch (IOException ioEx) {
            throw new Exception("Character " + playerID + " does not exist.: " + ioEx.getMessage());
        }
        return player;
    }

    /**
     * Determine whether a player is active based upon the last modified date of their full body image
     * @return whether player has been active inside the activity window
     */
    private boolean isPlayerActiveInDateRange() {

        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();

        Date nowMinusIncludeRange = new Date(t - (ACTIVITY_RANGE_DAYS * ONE_DAY_IN_MILLIS));
        return this.dateImgLastModified.after(nowMinusIncludeRange); //If the date occurs between the include range and now, then return true. Else false
    }

    /**
     * Given a lodestone profile page, return the name of the character.
     *
     * @param doc the lodestone profile page.
     * @return the name of the character.
     */
    private static String getNameFromPage(Document doc) {
        String[] parts = doc.title().split(Pattern.quote("|"));
        String name = parts[0].trim();
        return name;
    }

    /**
     * Given a lodestone profile page, return the realm of the character.
     *
     * @param doc the lodestone profile page.
     * @return the realm of the character.
     */
    private static String getRealmFromPage(Document doc) {
        //Get elements in the player name area
        Elements elements = doc.getElementsByClass("player_name_txt");
        String realmName = elements.get(0).getElementsByTag("span").text().replace("(", "").replace(")", "");
        //Return the realm name (contained in span)
        return realmName;
    }

    /**
     * Given a lodestone profile page, return the race of the character.
     *
     * @param doc the lodestone profile page.
     * @return the race of the character.
     */
    private static String getRaceFromPage(Document doc) {
        return doc.getElementsByClass("chara_profile_title").get(0).text().split(Pattern.quote("/"))[0].trim();
    }

    /**
     * Given a lodestone profile page, return the gender of the character.
     *
     * @param doc the lodestone profile page.
     * @return the gender of the character.
     */
    private static String getGenderFromPage(Document doc) {
        String[] parts = doc.getElementsByClass("chara_profile_title").get(0).text().split(Pattern.quote("/"));
        String gender = parts[2].trim();
        if (gender.equals("♂")) {
            return "male";
        } else if (gender.equals("♀")) {
            return "female";
        } else {
            return null;
        }
    }

    /**
     * Given a lodestone profile page, return the grand company of the character.
     *
     * @param doc the lodestone profile page.
     * @return the grand company of the character.
     */
    private static String getGrandCompanyFromPage(Document doc) {
        String gc = null;
        String fc = null;
        //Get all elements with class chara_profile_box_info
        Elements elements = doc.getElementsByClass("txt_name");

        //Checks to see if optional FC has been added
        if (elements.size() == 5) {
            fc = elements.get(4).getElementsByTag("a").text();
        }

        if (elements.size() == 5) { //If GC and FC present
            gc = elements.get(3).text().split(Pattern.quote("/"))[0];
        } else if (elements.size() == 4) { //If only GC present
            gc = elements.get(3).text().split(Pattern.quote("/"))[0];
            if (!gc.equals("Immortal Flames") && !gc.equals("Order of the Twin Adder") && !gc.equals("Maelstrom")) { //If not a valid GC
                gc = "none";
            }
        } else if (elements.size() == 3) {
            gc = "none";
        }
        return gc;
    }

    /**
     * Given a lodestone profile page, return the free company of the character.
     *
     * @param doc the lodestone profile page.
     * @return the free company of the character.
     */
    private static String getFreeCompanyFromPage(Document doc) {
        String gc = null;
        String fc = null;
        //Get all elements with class chara_profile_box_info
        Elements elements = doc.getElementsByClass("txt_name");

        //Checks to see if optional FC has been added
        if (elements.size() == 5) {
            fc = elements.get(4).getElementsByTag("a").text();
        } else if (elements.size() == 4) { //If only 4 elements present
            //Assume that gc is what is in slot 3
            gc = elements.get(3).text().split(Pattern.quote("/"))[0];
            if (!gc.equals("Immortal Flames") && !gc.equals("Order of the Twin Adder") && !gc.equals("Maelstrom")) { //If not a valid GC
                gc = "none";
                fc = elements.get(3).text().split(Pattern.quote("/"))[0];
            } else {
                fc= "none";
            }
        } else if (elements.size() == 3) {
            fc = "none";
        }
        return fc;
    }

    /**
     * Given a lodestone profile page, return the levelset of the character.
     *
     * @param doc the lodestone profile page
     * @return the set of levels of the player in the order displayed on the lodestone.
     * @throws Exception Exception thrown if more classes found than anticipated.
     */
    private static int[] getLevelsFromPage(Document doc) throws Exception {
        //Initialize array list in which to store levels (in order displayed on lodestone)
        ArrayList levels = new ArrayList();
        //Get the html of the table for levels
        Elements table = doc.getElementsByClass("class_list").select("tr");
        //I

        for (Element e : table) { //For each row of table
            //Select the first level
            String lvlOne = e.select("td:eq(1)").text();
            //Initialize var to store second
            String lvlTwo = null;

            if (e.select("td").size() > 3) {
                //Second level
                lvlTwo = e.select("td:eq(4)").text();
            }

            if (lvlOne.equals("-")) {//If a dash
                levels.add(0);
            } else {
                levels.add(Integer.parseInt(lvlOne));
            }

            if (lvlTwo.equals("-")) {
                levels.add(0);
            } else if (!lvlTwo.equals("")) {
                levels.add(Integer.parseInt(lvlTwo));
            }
        }

        //Initialize int array
        int[] arrLevels = new int[levels.size()];
        //Convert array list to array of ints
        for (int index = 0; index < levels.size(); index++) {
            arrLevels[index] = Integer.parseInt(levels.get(index).toString());
        }

        //Check if levels array is larger than this system is programmed for
        if (arrLevels.length > 23) {
            throw new Exception("Error: More class levels found than anticipated (23). The class definitions need to be updated.");
        }

        return arrLevels;
    }

    /**
     * Get the set of minions from a page.
     *
     * @param doc the lodestone profile page to parse.
     * @return the set of strings representing the player's minions.
     */
    private static ArrayList getMinionsFromPage(Document doc) {
        //Get minion box element
        Element minionBox = doc.getElementsByClass("minion_box").get(1);
        //Get minions
        Elements minionSet = minionBox.getElementsByTag("a");
        //Initialize array in which to store minions
        ArrayList minions = new ArrayList();
        for (int index = 0; index < minionSet.size(); index++) { //For each minion link store into array
            minions.add(minionSet.get(index).attr("title"));
        }
        return minions;
    }


    /**
     * Get the set of mounts from a page.
     *
     * @param doc the lodestone profile page to parse.
     * @return the set of strings representing the player's mounts.
     */
    private static ArrayList getMountsFromPage(Document doc) {
        //Get minion box element
        Element minionBox = doc.getElementsByClass("minion_box").get(0);
        //Get mounts
        Elements mountSet = minionBox.getElementsByTag("a");
        //Initialize array in which to store minions
        ArrayList mounts = new ArrayList();
        for (int index = 0; index < mountSet.size(); index++) { //For each mount link store into array
            mounts.add(mountSet.get(index).attr("title"));
        }
        return mounts;
    }

    /**
     * Gets the last-modified date of the Character full body image.
     * @param doc the lodestone profile page to parse
     * @return the date on which the full body image was last modified.
     */
    private static Date getDateLastUpdatedFromPage(Document doc, int id) throws Exception {
        Date dateLastModified = new Date();
        //Get character image URL.
        String imgUrl = doc.getElementsByClass("bg_chara_264").get(0).getElementsByTag("img").get(0).attr("src");
        String strLastModifiedDate = "";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.head(imgUrl).asJson();

            strLastModifiedDate = jsonResponse.getHeaders().get("Last-Modified").toString();
        }
        catch (Exception e) {
            System.out.println("Setting last-active date to ARR launch date due to an an error loading character " + id + "'s profile image: " + e.getMessage());
            strLastModifiedDate = "[Sat, 24 Aug 2013 00:00:01 GMT]";
        }

        strLastModifiedDate = strLastModifiedDate.replace("[", "");
        strLastModifiedDate = strLastModifiedDate.replace("]", "");
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

        try {
            dateLastModified = dateFormat.parse(strLastModifiedDate);
        } catch (ParseException e) {
            throw new Exception("Could not correctly parse date 'Last-Modified' header from full body image for character id" + id);
        }
        return dateLastModified;
    }

    /**
     * Get the date on which the Character's full body image was last modified
     * @return the date on which the Character's full body image was last modified
     */
    public Date getDateImgLastModified() {
        return dateImgLastModified;
    }

    /**
     * Get whether a Player is active
     * @return whether Player is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Get whether a Player is active
     * @return whether Player is active
     */
    public int getBitIsActive() {
        if(this.isActive) return 1;
        return 0;
    }

    /**
     * Set whether Player is active
     * @param active whether player is considered active
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Get whether the Player has the Artbook 'Stone and Steel'
     * @return whether the Player has the Artbook 'Stone and Steel'
     */
    public boolean isHasHWArtbookOne() {
        return hasHWArtbookOne;
    }

    /**
     * Get whether the Player has the Artbook 'Stone and Steel'
     * @return whether the Player has the Artbook 'Stone and Steel'
     */
    public int getBitHasHWArtbookOne() {
        if(this.hasHWArtbookOne) return 1;
        return 0;
    }

    /**
     * Set whether the Player has the Artbook 'Stone and Steel'
     * @param hasHWArtbookOne whether the Player has the Artbook 'Stone and Steel'
     */
    public void setHasHWArtbookOne(boolean hasHWArtbookOne) {
        this.hasHWArtbookOne = hasHWArtbookOne;
    }

    /**
     * Get whether Player has the Artbook 'Scars of War'
     * @return whether Player has the Artbook 'Scars of War'
     */
    public boolean isHasHWArtbookTwo() {
        return hasHWArtbookTwo;
    }

    /**
     * Get whether Player has the Artbook 'Scars of War'
     * @return whether Player has the Artbook 'Scars of War'
     */
    public int getBitHasHWArtbookTwo() {
        if(this.hasHWArtbookTwo) return 1;
        return 0;
    }

    /**
     * Set whether Player has the Artbook 'Scars of War'
     * @param hasHWArtbookTwo whether Player has the Artbook 'Scars of War'
     */
    public void setHasHWArtbookTwo(boolean hasHWArtbookTwo) {
        this.hasHWArtbookTwo = hasHWArtbookTwo;
    }

    /**
     * Get whether the Player has the FFXIV Lorebook - Encyclopedia Eorzea
     * @return whether the Player has the FFXIV Lorebook - Encyclopedia Eorzea
     */
    public boolean isHasEncyclopediaEorzea() {
        return hasEncyclopediaEorzea;
    }

    /**
     * Get whether the Player has the FFXIV Lorebook - Encyclopedia Eorzea
     * @return whether the Player has the FFXIV Lorebook - Encyclopedia Eorzea
     */
    public int getBitHasEncyclopediaEorzea() {
        if(this.hasEncyclopediaEorzea) return 1;
        return 0;
    }

    /**
     * Set whether the Player has the FFXIV Lorebook - Encyclopedia Eorzea
     * @param hasEncyclopediaEorzea whether the Player has the FFXIV Lorebook - Encyclopedia Eorzea
     */
    public void setHasEncyclopediaEorzea(boolean hasEncyclopediaEorzea) {
        this.hasEncyclopediaEorzea = hasEncyclopediaEorzea;
    }

    /**
     * Get whether the player has the topaz carbuncle plush
     * @return whether the player has the topaz carbuncle plush
     */
    public boolean isHasTopazCarbunclePlush() {
        return hasTopazCarbunclePlush;
    }

    /**
     * Get whether the player has the topaz carbuncle plush
     * @return whether the player has the topaz carbuncle plush
     */
    public int getBitHasTopazCarbunclePlush() {
        if(this.hasTopazCarbunclePlush) return 1;
        return 0;
    }

    /**
     * Set whether the player has the topaz carbuncle plush
     * @param hasTopazCarbunclePlush whether the player has the topaz carbuncle plush
     */
    public void setHasTopazCarbunclePlush(boolean hasTopazCarbunclePlush) {
        this.hasTopazCarbunclePlush = hasTopazCarbunclePlush;
    }

    /**
     * Get whether the player has the emerald carbuncle plush
     * @return whether the player has the emerald carbuncle plush
     */
    public boolean isHasEmeraldCarbunclePlush() {
        return hasEmeraldCarbunclePlush;
    }

    /**
     * Get whether the player has the emerald carbuncle plush
     * @return whether the player has the emerald carbuncle plush
     */
    public int getBitHasEmeraldCarbunclePlush() {
        if(this.hasEmeraldCarbunclePlush) return 1;
        return 0;
    }

    /**
     * Set whether the player has the emerald carbuncle plush
     * @param hasEmeraldCarbunclePlush whether the player has the emerald carbuncle plush
     */
    public void setHasEmeraldCarbunclePlush(boolean hasEmeraldCarbunclePlush) {
        this.hasEmeraldCarbunclePlush = hasEmeraldCarbunclePlush;
    }

    /**
     * Get whether player has reached Rank 7 with Moogle Beast Tribe
     * @return whether player has reached Rank 7 with Moogle Beast Tribe
     */
    public boolean isHasMoogle() {
        return hasMoogle;
    }

    /**
     * Get whether player has reached Rank 7 with Moogle Beast Tribe
     * @return whether player has reached Rank 7 with Moogle Beast Tribe
     */
    public int getBitHasMoogle() {
        if(this.hasMoogle) return 1;
        return 0;
    }


    /**
     * Set whether player has reached Rank 7 with Moogle Beast Tribe
     * @param hasMoogle whether player has reached Rank 7 with Moogle Beast Tribe
     */
    public void setHasMoogle(boolean hasMoogle) {
        this.hasMoogle = hasMoogle;
    }

    /**
     * Get whether player has reached Rank 7 with Vanu Vanu Beast Tribe
     * @return whether player has reached Rank 7 with Vanu Vanu Beast Tribe
     */
    public boolean isHasVanuVanu() {
        return hasVanuVanu;
    }

    /**
     * Get whether player has reached Rank 7 with Vanu Vanu Beast Tribe
     * @return whether player has reached Rank 7 with Vanu Vanu Beast Tribe
     */
    public int getBitHasVanuVanu() {
        if(this.hasVanuVanu) return 1;
        return 0;
    }


    /**
     * Set whether player has reached Rank 7 with Vanu Vanu Beast Tribe
     * @param hasVanuVanu whether player has reached Rank 7 with Vanu Vanu Beast Tribe
     */
    public void setHasVanuVanu(boolean hasVanuVanu) {
        this.hasVanuVanu = hasVanuVanu;
    }

    /**
     * Get whether player has reached Rank 7 with Vath Beast Tribe
     * @return whether player has reached Rank 7 with Vath Tribe
     */
    public boolean isHasVath() {
        return hasVath;
    }

    /**
     * Get whether player has reached Rank 7 with Vath Beast Tribe
     * @return whether player has reached Rank 7 with Vath Beast Tribe
     */
    public int getBitHasVath() {
        if(this.hasVath) return 1;
        return 0;
    }

    /**
     * Set whether player has reached Rank 7 with Vath Beast Tribe
     * @param hasVath whether player has reached Rank 7 with Vath Beast Tribe
     */
    public void setHasVath(boolean hasVath) {
        this.hasVath = hasVath;
    }
}
