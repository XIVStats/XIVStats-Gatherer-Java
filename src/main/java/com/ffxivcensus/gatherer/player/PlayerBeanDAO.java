package com.ffxivcensus.gatherer.player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ffxivcensus.gatherer.config.ApplicationConfig;

/**
 * Data-Access Object encapsulating interactions with the Player table.
 * 
 * @author matthew.hillier
 */
@Repository
public class PlayerBeanDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerBeanDAO.class);
    private ApplicationConfig appConfig;
    private DataSource dataSource;

    public PlayerBeanDAO(@Autowired final ApplicationConfig appConfig, @Autowired final DataSource dataSource) {
        this.appConfig = appConfig;
        this.dataSource = dataSource;
    }

    /**
     * Get an array list containing the added IDs returned by executing a SQL
     * statement.
     * 
     * @return an array list of the IDs successfully added to DB.
     */
    public List<Integer> getAdded(final int startId, final int endId) {
        ResultSet rs;
        List<Integer> addedIDs = new ArrayList<>();
        String sql = "SELECT * FROM tblplayers WHERE `id`>=" + startId + " AND `id`<=" + endId + ";";

        // Convert dataset to array list
        try(Connection conn = openConnection();
            Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                addedIDs.add(rs.getInt(1));
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return addedIDs;
    }

    /**
     * Write a player record to database
     */
    public String saveRecord(final PlayerBean player) {
        String strOut = "";
        // Open connection

        try(Connection conn = openConnection();
            Statement st = conn.createStatement()) {

            // Declare string builders to build up components of statement
            StringBuilder sbFields = new StringBuilder();
            StringBuilder sbValues = new StringBuilder();

            // Set default table name
            String tableName = "tblplayers";

            sbFields.append("INSERT IGNORE INTO ").append(tableName).append(" (");
            sbValues.append(" VALUES (");

            sbFields.append("id, name, realm, race, gender, grand_company,free_company,");
            sbValues.append(player.getId() + ",\"" + player.getPlayerName() + "\",\"" + player.getRealm() + "\",\"" + player.getRace()
                            + "\",'" + player.getGender() + "','" + player.getGrandCompany() + "',\"" + player.getFreeCompany() + "\",");

            sbFields.append("level_gladiator, level_pugilist, level_marauder,level_lancer, level_archer, level_rogue,");
            sbValues.append(player.getLvlGladiator() + "," + player.getLvlPugilist() + "," + player.getLvlMarauder() + ","
                            + player.getLvlLancer() + "," + player.getLvlArcher() + "," + player.getLvlRogue() + ",");

            sbFields.append("level_conjurer, level_thaumaturge, level_arcanist, level_astrologian, level_darkknight,"
                            + " level_machinist,");
            sbValues.append(player.getLvlConjurer() + "," + player.getLvlThaumaturge() + "," + player.getLvlArcanist() + ","
                            + player.getLvlAstrologian() + "," + player.getLvlDarkKnight() + "," + player.getLvlMachinist() + ",");

            sbFields.append("level_scholar, level_redmage, level_samurai,");
            sbValues.append(player.getLvlScholar() + "," + player.getLvlRedMage() + "," + player.getLvlSamurai() + ",");

            sbFields.append("level_carpenter, level_blacksmith, level_armorer, level_goldsmith, level_leatherworker, level_weaver, level_alchemist,");
            sbValues.append(player.getLvlCarpenter() + "," + player.getLvlBlacksmith() + "," + player.getLvlArmorer() + ","
                            + player.getLvlGoldsmith() + "," + player.getLvlLeatherworker() + "," + player.getLvlWeaver() + ","
                            + player.getLvlAlchemist() + ",");

            sbFields.append("level_culinarian, level_miner, level_botanist, level_fisher");
            sbValues.append(player.getLvlCulinarian() + "," + player.getLvlMiner() + "," + player.getLvlBotanist() + ","
                            + player.getLvlFisher());

            if(appConfig.isStoreProgression()) {
                sbFields.append(",");
                sbValues.append(",");

                sbFields.append("p30days, p60days, p90days, p180days, p270days, p360days, p450days, p630days, p960days,");
                sbValues.append(booleanToInt(player.isHas30DaysSub()) + "," + booleanToInt(player.isHas60DaysSub()) + ","
                                + booleanToInt(player.isHas90DaysSub()) + "," + booleanToInt(player.isHas180DaysSub()) + ","
                                + booleanToInt(player.isHas270DaysSub()) + "," + booleanToInt(player.isHas360DaysSub()) + ","
                                + booleanToInt(player.isHas450DaysSub()) + "," + booleanToInt(player.isHas630DaysSub()) + ","
                                + booleanToInt(player.isHas960DaysSub()) + ",");

                sbFields.append("prearr, prehw, presb, arrartbook, hwartbookone, hwartbooktwo, hasencyclopedia, beforemeteor, beforethefall, soundtrack, saweternalbond, "
                                + "sightseeing, arr_25_complete, comm50, moogleplush, topazcarubuncleplush, emeraldcarbuncleplush,");
                sbValues.append(booleanToInt(player.isHasPreOrderArr()) + "," + booleanToInt(player.isHasPreOrderHW()) + ","
                                + booleanToInt(player.isHasPreOrderSB()) + "," + booleanToInt(player.isHasARRArtbook()) + ","
                                + booleanToInt(player.isHasHWArtbookOne()) + "," + booleanToInt(player.isHasHWArtbookTwo()) + ","
                                + booleanToInt(player.isHasEncyclopediaEorzea()) + "," + booleanToInt(player.isHasBeforeMeteor()) + ","
                                + booleanToInt(player.isHasBeforeTheFall()) + "," + booleanToInt(player.isHasSoundtrack()) + ","
                                + booleanToInt(player.isHasAttendedEternalBond()) + "," + booleanToInt(player.isHasCompletedHWSightseeing())
                                + "," + booleanToInt(player.isHasCompleted2pt5()) + "," + booleanToInt(player.isHasFiftyComms()) + ","
                                + booleanToInt(player.isHasMooglePlush()) + "," + booleanToInt(player.isHasTopazCarbunclePlush()) + ","
                                + booleanToInt(player.isHasEmeraldCarbunclePlush()) + ",");

                sbFields.append("hildibrand, ps4collectors, dideternalbond, arrcollector, kobold, sahagin, amaljaa, "
                                + "sylph, moogle, vanuvanu, vath, hw_complete, hw_31_complete, hw_33_complete, sb_complete, legacy_player");
                sbValues.append(booleanToInt(player.isHasCompletedHildibrand()) + "," + booleanToInt(player.isHasPS4Collectors()) + ","
                                + booleanToInt(player.isHasEternalBond()) + "," + booleanToInt(player.isHasARRCollectors()) + ","
                                + booleanToInt(player.isHasKobold()) + "," + booleanToInt(player.isHasSahagin()) + ","
                                + booleanToInt(player.isHasAmaljaa()) + "," + booleanToInt(player.isHasSylph()) + ","
                                + booleanToInt(player.isHasMoogle()) + "," + booleanToInt(player.isHasVanuVanu()) + ","
                                + booleanToInt(player.isHasVath()) + "," + booleanToInt(player.isHasCompletedHW()) + ","
                                + booleanToInt(player.isHasCompleted3pt1()) + "," + booleanToInt(player.isHasCompleted3pt3()) + ","
                                + booleanToInt(player.isHasCompletedSB()) + "," + booleanToInt(player.isLegacyPlayer()));

            }

            if(appConfig.isStoreMinions()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("minions");
                sbValues.append("\"" + StringUtils.join(player.getMinions(), ",") + "\"");
            }
            if(appConfig.isStoreMounts()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("mounts");
                sbValues.append("\"" + StringUtils.join(player.getMounts(), ",") + "\"");
            }

            if(appConfig.isStoreActiveDate()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("date_active");
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

                String sqlDate = sdf.format(player.getDateImgLastModified());
                sbValues.append("\"" + sqlDate + "\"");
            }
            if(appConfig.isStorePlayerActive()) {
                sbFields.append(",");
                sbValues.append(",");
                sbFields.append("is_active");
                sbValues.append(booleanToInt(player.isActive()));
            }

            sbFields.append(")");
            sbValues.append(");");

            String strSQL = sbFields.toString() + sbValues.toString();

            st.executeUpdate(strSQL);
            strOut = "Character " + player.getId() + " written to database successfully.";
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return strOut;
    }

    private int booleanToInt(final boolean value) {
        return BooleanUtils.toInteger(value);
    }

    /**
     * Open a connection to database.
     *
     * @return the opened connection
     * @throws SQLException exception thrown if unable to connect
     */
    protected Connection openConnection() {
        try {
            return dataSource.getConnection();
        } catch(SQLException sqlEx) {
            LOG.error("Connection failed! Please see output console", sqlEx);
            return null;
        }
    }

}
