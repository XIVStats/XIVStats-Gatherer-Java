package com.ffxivcensus.gatherer.player;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ffxivcensus.gatherer.task.GathererTask;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * Builder class for creating PlayerBean objects from the Lodestone.
 *
 * @author Peter Reid
 * @author Matthew Hillier
 * @since v1.0
 * @see GathererTask
 */
public class PlayerBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerBuilder.class);
    /**
     * Number of days inactivity before character is considered inactive
     */
    private final static int ACTIVITY_RANGE_DAYS = 30;

    private static final long ONE_DAY_IN_MILLIS = 86400000;

    /**
     * Set player class levels.
     * As of 4.0, this is now parsed in the order:
     * - Gladiator
     * - Marauder
     * - Dark Knight
     * - Monk
     * - Dragoon
     * - Ninja
     * - Samurai
     * - White Mage
     * - Scholar
     * - Astrologian
     * - Bard
     * - Machinist
     * - Black Mage
     * - Summoner
     * - Red Mage
     *
     * @param arrLevels integer array of classes in order displayed on lodestone.
     */
    public static void setLevels(final PlayerBean player, final int[] arrLevels) {
        player.setLevelGladiator(arrLevels[0]);
        player.setLevelMarauder(arrLevels[1]);
        player.setLevelDarkknight(arrLevels[2]);
        player.setLevelPugilist(arrLevels[3]);
        player.setLevelLancer(arrLevels[4]);
        player.setLevelRogue(arrLevels[5]);
        player.setLevelSamurai(arrLevels[6]);
        player.setLevelConjurer(arrLevels[7]);
        player.setLevelScholar(arrLevels[8]);
        player.setLevelAstrologian(arrLevels[9]);
        player.setLevelArcher(arrLevels[10]);
        player.setLevelMachinist(arrLevels[11]);
        player.setLevelThaumaturge(arrLevels[12]);
        player.setLevelArcanist(arrLevels[13]);
        player.setLevelRedmage(arrLevels[14]);
        player.setLevelCarpenter(arrLevels[15]);
        player.setLevelBlacksmith(arrLevels[16]);
        player.setLevelArmorer(arrLevels[17]);
        player.setLevelGoldsmith(arrLevels[18]);
        player.setLevelLeatherworker(arrLevels[19]);
        player.setLevelWeaver(arrLevels[20]);
        player.setLevelAlchemist(arrLevels[21]);
        player.setLevelCulinarian(arrLevels[22]);
        player.setLevelMiner(arrLevels[23]);
        player.setLevelBotanist(arrLevels[24]);
        player.setLevelFisher(arrLevels[25]);
    }

    /**
     * Determine if a player has a specified mount
     *
     * @param mountName the name of the mount to check for.
     * @return whether the player has the specified mount.
     */
    public static boolean doesPlayerHaveMount(final PlayerBean player, final String mountName) {
        return player.getMounts().contains(mountName);
    }

    /**
     * Determine if a player has a specified minion.
     *
     * @param minionName the name of the minion to check for
     * @return whether the player has the specified minion.
     */
    public static boolean doesPlayerHaveMinion(final PlayerBean player, final String minionName) {
        return player.getMinions().contains(minionName);
    }

    /**
     * Fetch a player from the lodestone specified by ID.
     *
     * @param playerID the ID of the player to fetch
     * @param attempt the number of times this character has been attempted.
     * @return the player object matching the specified ID.
     * @throws Exception exception thrown if more class levels returned than anticipated.
     */
    public static PlayerBean getPlayer(final int playerID, int attempt) throws Exception {
        // Initialize player object to return
        PlayerBean player = new PlayerBean();
        player.setId(playerID);
        // Declare HTML document
        Document doc;

        // URL to connect to
        String url = "http://eu.finalfantasyxiv.com/lodestone/character/" + playerID + "/";

        try {
            // Fetch the specified URL
            doc = Jsoup.connect(url).timeout(5000).get();
            player.setPlayerName(getNameFromPage(doc));
            player.setRealm(getRealmFromPage(doc));
            player.setRace(getRaceFromPage(doc));
            player.setGender(getGenderFromPage(doc));
            player.setGrandCompany(getGrandCompanyFromPage(doc));
            player.setFreeCompany(getFreeCompanyFromPage(doc));
            player.setDateImgLastModified(getDateLastUpdatedFromPage(doc, playerID));
            setLevels(player, getLevelsFromPage(doc));
            player.setMounts(getMountsFromPage(doc));
            player.setMinions(getMinionsFromPage(doc));
            player.setHas30DaysSub(doesPlayerHaveMinion(player, "Wind-up Cursor"));
            player.setHas60DaysSub(doesPlayerHaveMinion(player, "Black Chocobo Chick"));
            player.setHas90DaysSub(doesPlayerHaveMinion(player, "Beady Eye"));
            player.setHas180DaysSub(doesPlayerHaveMinion(player, "Minion Of Light"));
            player.setHas270DaysSub(doesPlayerHaveMinion(player, "Wind-up Leader"));
            player.setHas360DaysSub(doesPlayerHaveMinion(player, "Wind-up Odin"));
            player.setHas450DaysSub(doesPlayerHaveMinion(player, "Wind-up Goblin"));
            player.setHas630DaysSub(doesPlayerHaveMinion(player, "Wind-up Nanamo"));
            player.setHas960DaysSub(doesPlayerHaveMinion(player, "Wind-up Firion"));
            player.setHasPreOrderArr(doesPlayerHaveMinion(player, "Cait Sith Doll"));
            player.setHasPreOrderHW(doesPlayerHaveMinion(player, "Chocobo Chick Courier"));
            player.setHasPreOrderSB(doesPlayerHaveMinion(player, "Wind-up Red Mage"));
            player.setHasARRArtbook(doesPlayerHaveMinion(player, "Model Enterprise"));
            player.setHasHWArtbookOne(doesPlayerHaveMinion(player, "Wind-Up Relm"));
            player.setHasHWArtbookTwo(doesPlayerHaveMinion(player, "Wind-Up Hraesvelgr"));
            player.setHasEncyclopediaEorzea(doesPlayerHaveMinion(player, "Namingway"));
            player.setHasBeforeMeteor(doesPlayerHaveMinion(player, "Wind-up Dalamud"));
            player.setHasBeforeTheFall(doesPlayerHaveMinion(player, "Set Of Primogs"));
            player.setHasSoundtrack(doesPlayerHaveMinion(player, "Wind-up Bahamut"));
            player.setHasAttendedEternalBond(doesPlayerHaveMinion(player, "Demon Box"));
            player.setHasCompletedHWSightseeing(doesPlayerHaveMinion(player, "Fledgling Apkallu"));
            player.setHasCompleted2pt5(doesPlayerHaveMinion(player, "Midgardsormr"));
            player.setHasFiftyComms(doesPlayerHaveMinion(player, "Princely Hatchling"));
            player.setHasMooglePlush(doesPlayerHaveMinion(player, "Wind-up Delivery Moogle"));
            player.setHasTopazCarbunclePlush(doesPlayerHaveMinion(player, "Heliodor Carbuncle"));
            player.setHasEmeraldCarbunclePlush(doesPlayerHaveMinion(player, "Peridot Carbuncle"));
            player.setHasCompletedHildibrand(doesPlayerHaveMinion(player, "Wind-up Gentleman"));
            player.setHasPS4Collectors(doesPlayerHaveMinion(player, "Wind-up Moogle"));
            player.setHasCompleted3pt1(doesPlayerHaveMinion(player, "Wind-up Haurchefant"));
            player.setHasCompleted3pt3(doesPlayerHaveMinion(player, "Wind-up Aymeric"));
            player.setHasEternalBond(doesPlayerHaveMount(player, "Ceremony Chocobo"));
            player.setHasARRCollectors(doesPlayerHaveMount(player, "Coeurl"));
            player.setHasKobold(doesPlayerHaveMount(player, "Bomb Palanquin"));
            player.setHasSahagin(doesPlayerHaveMount(player, "Cavalry Elbst"));
            player.setHasAmaljaa(doesPlayerHaveMount(player, "Cavalry Drake"));
            player.setHasSylph(doesPlayerHaveMount(player, "Laurel Goobbue"));
            player.setHasMoogle(doesPlayerHaveMount(player, "Cloud Mallow"));
            player.setHasVanuVanu(doesPlayerHaveMount(player, "Sanuwa"));
            player.setHasVath(doesPlayerHaveMount(player, "Kongamato"));
            player.setHasCompletedHW(doesPlayerHaveMount(player, "Midgardsormr"));
            // Main Scenario quest doesn't drop a minion, so instead assume players will at least play one of the Level 70 dungeons and
            // eventually get the minion
            player.setHasCompletedSB(doesPlayerHaveMinion(player, "Ivon Coeurlfist Doll") || doesPlayerHaveMinion(player, "Dress-up Yugiri")
                                     || doesPlayerHaveMinion(player, "Wind-up Exdeath"));
            player.setLegacyPlayer(doesPlayerHaveMount(player, "Legacy Chocobo"));
            player.setActive(isPlayerActiveInDateRange(player));
            player.setCharacterStatus(player.isActive() ? CharacterStatus.ACTIVE : CharacterStatus.INACTIVE);
        } catch(HttpStatusException httpe) {
            switch (httpe.getStatusCode()) {
                case 429:
                    // Generate random number 1->20*attempt no and sleep for it
                    Random rand = new Random();
                    int max = attempt * 20;
                    int min = (attempt - 1) + 1;
                    int randomNum = rand.nextInt(max - min + 1) + min;
                    LOG.trace("Experiencing rate limiting (HTTP 429) while fetching id " + playerID + " (attempt " + attempt
                              + "), waiting " + randomNum + "ms then retrying...");
                    TimeUnit.MILLISECONDS.sleep(randomNum);
                    player = PlayerBuilder.getPlayer(playerID, ++attempt);
                    break;
                case HttpStatus.SC_NOT_FOUND:
                    LOG.info("Character " + playerID + " does not exist. (404)");
                    player.setCharacterStatus(CharacterStatus.DELETED);
                    break;
                default:
                    throw new Exception("Unexpected HTTP Status Code: " + httpe.getStatusCode(), httpe);
            }
        }
        return player;
    }

    /**
     * Determine whether a player is active based upon the last modified date of their full body image
     *
     * @return whether player has been active inside the activity window
     */
    private static boolean isPlayerActiveInDateRange(final PlayerBean player) {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();

        Date nowMinusIncludeRange = new Date(t - (ACTIVITY_RANGE_DAYS * ONE_DAY_IN_MILLIS));
        return player.getDateImgLastModified().after(nowMinusIncludeRange); // If the date occurs between the include range and now, then
                                                                            // return true. Else false
    }

    /**
     * Given a lodestone profile page, return the name of the character.
     *
     * @param doc the lodestone profile page.
     * @return the name of the character.
     */
    private static String getNameFromPage(final Document doc) {
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
    private static String getRealmFromPage(final Document doc) {
        // Get elements in the player name area
        String realmName = doc.getElementsByClass("frame__chara__world").get(0).text().replace("(", "").replace(")", "");
        // Return the realm name (contained in span)
        return realmName;
    }

    /**
     * Given a lodestone profile page, return the race of the character.
     *
     * @param doc the lodestone profile page.
     * @return the race of the character.
     */
    private static String getRaceFromPage(final Document doc) {
        return doc.getElementsByClass("character-block__name").get(0).textNodes().get(0).text().trim();
    }

    /**
     * Given a lodestone profile page, return the gender of the character.
     *
     * @param doc the lodestone profile page.
     * @return the gender of the character.
     */
    private static String getGenderFromPage(final Document doc) {
        String[] parts = doc.getElementsByClass("character-block__name").get(0).text().split(Pattern.quote("/"));
        String gender = parts[1].trim();
        if(gender.equals("♂")) {
            return "male";
        } else if(gender.equals("♀")) {
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
    private static String getGrandCompanyFromPage(final Document doc) {
        String gc = null;
        // Get all elements with class chara_profile_box_info
        Elements elements = doc.getElementsByClass("character-block__box");
        if(elements.size() == 5) {
            gc = elements.get(3).getElementsByClass("character-block__name").get(0).text().split("/")[0].trim();
        } else if(elements.size() == 4) {
            if(elements.get(3).getElementsByClass("character__freecompany__name").size() > 0) { // If box is fc
                gc = "none";
            } else {
                gc = elements.get(3).getElementsByClass("character-block__name").get(0).text().split("/")[0].trim();
            }
        } else {
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
    private static String getFreeCompanyFromPage(final Document doc) {
        String fc = null;
        // Get all elements with class chara_profile_box_info
        Elements elements = doc.getElementsByClass("character-block__box");

        // Checks to see if optional FC has been added
        if(elements.size() == 5) {
            fc = elements.get(4).getElementsByClass("character__freecompany__name").get(0).getElementsByTag("a").text();
        } else if(elements.size() == 4) { // If only 4 elements present

            if(elements.get(3).getElementsByClass("character__freecompany__name").size() > 0) { // If box is fc
                fc = elements.get(3).getElementsByClass("character__freecompany__name").get(0).getElementsByTag("a").text();
            } else { // Else must not be gc
                fc = "none";
            }
        } else {
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
    private static int[] getLevelsFromPage(final Document doc) throws Exception {
        // Initialize array list in which to store levels (in order displayed on lodestone)
        List<Integer> levels = new ArrayList<>();
        Elements discipleBoxes = doc.getElementsByClass("character__job");

        for(int i = 0; i < discipleBoxes.size(); i++) {
            Elements levelBoxes = discipleBoxes.get(i).getElementsByClass("character__job__level");
            for(Element levelBox : levelBoxes) {
                String strLvl = levelBox.text();
                if(strLvl.equals("-")) {
                    levels.add(0);
                } else {
                    levels.add(Integer.parseInt(strLvl));
                }
            }
        }

        // Initialize int array
        int[] arrLevels = new int[levels.size()];
        // Convert array list to array of ints
        for(int index = 0; index < levels.size(); index++) {
            arrLevels[index] = Integer.parseInt(levels.get(index).toString());
        }

        // Check if levels array is larger than this system is programmed for
        // As of 4.0, this is now 26 - SCH and SMN are 2 jobs, + SAM & RDM
        if(arrLevels.length > 26) {
            throw new Exception("Error: More class levels found (" + arrLevels.length
                                + ") than anticipated (26). The class definitions need to be updated.");
        }

        return arrLevels;
    }

    /**
     * Get the set of minions from a page.
     *
     * @param doc the lodestone profile page to parse.
     * @return the set of strings representing the player's minions.
     */
    private static List<String> getMinionsFromPage(final Document doc) {

        // Initialize array in which to store minions
        List<String> minions = new ArrayList<>();
        // Get minion box element
        Elements minionBoxes = doc.getElementsByClass("character__minion");
        if(minionBoxes.size() > 0) {
            // Get minions
            Elements minionSet = minionBoxes.get(0).getElementsByTag("li");
            for(int index = 0; index < minionSet.size(); index++) { // For each minion link store into array
                minions.add(minionSet.get(index).getElementsByTag("div").attr("data-tooltip"));
            }
        }
        return minions;
    }

    /**
     * Get the set of mounts from a page.
     *
     * @param doc the lodestone profile page to parse.
     * @return the set of strings representing the player's mounts.
     */
    private static List<String> getMountsFromPage(final Document doc) {

        // Initialize array in which to store minions
        List<String> mounts = new ArrayList<>();

        // Get minion box element
        Elements minionBoxes = doc.getElementsByClass("character__mounts");
        // Get mounts
        if(minionBoxes.size() > 0) {
            Elements mountSet = minionBoxes.get(0).getElementsByTag("li");
            for(int index = 0; index < mountSet.size(); index++) { // For each mount link store into array
                mounts.add(mountSet.get(index).getElementsByTag("div").attr("data-tooltip"));
            }
        }
        return mounts;
    }

    /**
     * Gets the last-modified date of the Character full body image.
     *
     * @param doc the lodestone profile page to parse
     * @return the date on which the full body image was last modified.
     */
    private static Date getDateLastUpdatedFromPage(final Document doc, final int id) throws Exception {
        Date dateLastModified = new Date();
        // Get character image URL.
        String imgUrl = doc.getElementsByClass("character__detail__image").get(0).getElementsByTag("a").get(0).getElementsByTag("img")
                           .get(0).attr("src");
        String strLastModifiedDate = "";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.head(imgUrl).asJson();

            strLastModifiedDate = jsonResponse.getHeaders().get("Last-Modified").toString();
        } catch(Exception e) {
            LOG.warn("Setting last-active date to ARR launch date due to an an error loading character " + id
                     + "'s profile image: " + e.getMessage());
            strLastModifiedDate = "[Sat, 24 Aug 2013 00:00:01 GMT]";
        }

        strLastModifiedDate = strLastModifiedDate.replace("[", "");
        strLastModifiedDate = strLastModifiedDate.replace("]", "");
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

        try {
            dateLastModified = dateFormat.parse(strLastModifiedDate);
        } catch(ParseException e) {
            throw new Exception("Could not correctly parse date 'Last-Modified' header from full body image for character id" + id);
        }
        return dateLastModified;
    }
}
