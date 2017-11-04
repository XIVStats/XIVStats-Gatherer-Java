package com.ffxivcensus.gatherer.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffxivcensus.gatherer.config.ApplicationConfig;

/**
 * Data-Access Object encapsulating interactions with the Player table.
 * 
 * @author matthew.hillier
 */
@Repository
public class PlayerBeanDAO {
    private ApplicationConfig appConfig;
    private JdbcTemplate jdbcTemplate;

    public PlayerBeanDAO(@Autowired final ApplicationConfig appConfig, @Autowired final JdbcTemplate jdbcTemplate) {
        this.appConfig = appConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Get an array list containing the added IDs returned by executing a SQL
     * statement.
     * 
     * @return an array list of the IDs successfully added to DB.
     */
    public List<Integer> getAdded(final int startId, final int endId) {
        List<Integer> addedIDs = new ArrayList<>();
        String sql = "SELECT * FROM tblplayers WHERE `id`>= ? AND `id`<= ?;";

        jdbcTemplate.query(sql, new Object[] {startId, endId}, (rs, rowNum) -> addedIDs.add(rs.getInt("id")));

        return addedIDs;
    }

    /**
     * Write a player record to database
     */
    public String saveRecord(final PlayerBean player) {
        String strOut = "";

        // Declare string builders to build up components of statement
        StringBuilder sbFields = new StringBuilder();

        List<Object> values = new ArrayList<>();

        sbFields.append("INSERT IGNORE INTO tblplayers (");

        sbFields.append("id, name, realm, race, gender, grand_company,free_company,");
        values.add(player.getId());
        values.add(player.getPlayerName());
        values.add(player.getRealm());
        values.add(player.getRace());
        values.add(player.getGender());
        values.add(player.getGrandCompany());
        values.add(player.getFreeCompany());

        sbFields.append("level_gladiator, level_pugilist, level_marauder,level_lancer, level_archer, level_rogue,");
        values.add(player.getLvlGladiator());
        values.add(player.getLvlPugilist());
        values.add(player.getLvlMarauder());
        values.add(player.getLvlLancer());
        values.add(player.getLvlArcher());
        values.add(player.getLvlRogue());

        sbFields.append("level_conjurer, level_thaumaturge, level_arcanist, level_astrologian, level_darkknight,"
                        + " level_machinist,");
        values.add(player.getLvlConjurer());
        values.add(player.getLvlThaumaturge());
        values.add(player.getLvlArcanist());
        values.add(player.getLvlAstrologian());
        values.add(player.getLvlDarkKnight());
        values.add(player.getLvlMachinist());

        sbFields.append("level_scholar, level_redmage, level_samurai,");
        values.add(player.getLvlScholar());
        values.add(player.getLvlRedMage());
        values.add(player.getLvlSamurai());

        sbFields.append("level_carpenter, level_blacksmith, level_armorer, level_goldsmith, level_leatherworker, level_weaver, level_alchemist,");
        values.add(player.getLvlCarpenter());
        values.add(player.getLvlBlacksmith());
        values.add(player.getLvlArmorer());
        values.add(player.getLvlGoldsmith());
        values.add(player.getLvlLeatherworker());
        values.add(player.getLvlWeaver());
        values.add(player.getLvlAlchemist());

        sbFields.append("level_culinarian, level_miner, level_botanist, level_fisher");
        values.add(player.getLvlCulinarian());
        values.add(player.getLvlMiner());
        values.add(player.getLvlBotanist());
        values.add(player.getLvlFisher());

        if(appConfig.isStoreProgression()) {
            sbFields.append(", p30days, p60days, p90days, p180days, p270days, p360days, p450days, p630days, p960days,");
            values.add(booleanToInt(player.isHas30DaysSub()));
            values.add(booleanToInt(player.isHas60DaysSub()));
            values.add(booleanToInt(player.isHas90DaysSub()));
            values.add(booleanToInt(player.isHas180DaysSub()));
            values.add(booleanToInt(player.isHas270DaysSub()));
            values.add(booleanToInt(player.isHas360DaysSub()));
            values.add(booleanToInt(player.isHas450DaysSub()));
            values.add(booleanToInt(player.isHas630DaysSub()));
            values.add(booleanToInt(player.isHas960DaysSub()));

            sbFields.append("prearr, prehw, presb, arrartbook, hwartbookone, hwartbooktwo, hasencyclopedia, beforemeteor, beforethefall, soundtrack, saweternalbond, "
                            + "sightseeing, arr_25_complete, comm50, moogleplush, topazcarubuncleplush, emeraldcarbuncleplush,");
            values.add(booleanToInt(player.isHasPreOrderArr()));
            values.add(booleanToInt(player.isHasPreOrderHW()));
            values.add(booleanToInt(player.isHasPreOrderSB()));
            values.add(booleanToInt(player.isHasARRArtbook()));
            values.add(booleanToInt(player.isHasHWArtbookOne()));
            values.add(booleanToInt(player.isHasHWArtbookTwo()));
            values.add(booleanToInt(player.isHasEncyclopediaEorzea()));
            values.add(booleanToInt(player.isHasBeforeMeteor()));
            values.add(booleanToInt(player.isHasBeforeTheFall()));
            values.add(booleanToInt(player.isHasSoundtrack()));
            values.add(booleanToInt(player.isHasAttendedEternalBond()));
            values.add(booleanToInt(player.isHasCompletedHWSightseeing()));
            values.add(booleanToInt(player.isHasCompleted2pt5()));
            values.add(booleanToInt(player.isHasFiftyComms()));
            values.add(booleanToInt(player.isHasMooglePlush()));
            values.add(booleanToInt(player.isHasTopazCarbunclePlush()));
            values.add(booleanToInt(player.isHasEmeraldCarbunclePlush()));

            sbFields.append("hildibrand, ps4collectors, dideternalbond, arrcollector, kobold, sahagin, amaljaa, "
                            + "sylph, moogle, vanuvanu, vath, hw_complete, hw_31_complete, hw_33_complete, sb_complete, legacy_player");
            values.add(booleanToInt(player.isHasCompletedHildibrand()));
            values.add(booleanToInt(player.isHasPS4Collectors()));
            values.add(booleanToInt(player.isHasEternalBond()));
            values.add(booleanToInt(player.isHasARRCollectors()));
            values.add(booleanToInt(player.isHasKobold()));
            values.add(booleanToInt(player.isHasSahagin()));
            values.add(booleanToInt(player.isHasAmaljaa()));
            values.add(booleanToInt(player.isHasSylph()));
            values.add(booleanToInt(player.isHasMoogle()));
            values.add(booleanToInt(player.isHasVanuVanu()));
            values.add(booleanToInt(player.isHasVath()));
            values.add(booleanToInt(player.isHasCompletedHW()));
            values.add(booleanToInt(player.isHasCompleted3pt1()));
            values.add(booleanToInt(player.isHasCompleted3pt3()));
            values.add(booleanToInt(player.isHasCompletedSB()));
            values.add(booleanToInt(player.isLegacyPlayer()));

        }

        if(appConfig.isStoreMinions()) {
            sbFields.append(", minions");
            values.add(StringUtils.join(player.getMinions(), ","));
        }
        if(appConfig.isStoreMounts()) {
            sbFields.append(", mounts");
            values.add(StringUtils.join(player.getMounts(), ","));
        }

        if(appConfig.isStoreActiveDate()) {
            sbFields.append(", date_active");
            values.add(player.getDateImgLastModified());
        }
        if(appConfig.isStorePlayerActive()) {
            sbFields.append(", is_active");
            values.add(booleanToInt(player.isActive()));
        }

        sbFields.append(")");

        String[] params = new String[values.size()];
        Arrays.fill(params, "?");

        String strSQL = sbFields.toString() + " VALUES(" + String.join(",", params) + ");";

        jdbcTemplate.update(strSQL, values.toArray());

        strOut = "Character " + player.getId() + " written to database successfully.";
        return strOut;
    }

    private int booleanToInt(final boolean value) {
        return BooleanUtils.toInteger(value);
    }

}
