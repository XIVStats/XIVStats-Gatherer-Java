package com.ffxivcensus.gatherer.player;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for CRUD actions for a given PlayerBean instance.
 *
 * @author matthew.hillier
 */
public interface PlayerBeanRepository extends CrudRepository<PlayerBean, Integer> {

    /**
     * Finds the top-most character by ID.
     *
     * @return Top-most {@link PlayerBean} object by ID.
     */
    PlayerBean findTopByOrderByIdDesc();

    /**
     * Finds the top-most character in the given Statuses
     *
     * @return
     */
    //@Query(value = "SELECT p FROM PlayerBean p WHERE characterStatus != 'DELETED' ORDER BY id DESC")
    PlayerBean findTopByCharacterStatusNotOrderByIdDesc(final CharacterStatus characterStatus);

	Long countByLevelGladiatorBetween(final int lower, final int upper);
	Long countByLevelPugilistBetween(final int lower, final int upper);
	Long countByLevelMarauderBetween(final int lower, final int upper);
	Long countByLevelLancerBetween(final int lower, final int upper);
	Long countByLevelArcherBetween(final int lower, final int upper);
	Long countByLevelRogueBetween(final int lower, final int upper);
	Long countByLevelConjurerBetween(final int lower, final int upper);
	Long countByLevelThaumaturgeBetween(final int lower, final int upper);
	Long countByLevelArcanistBetween(final int lower, final int upper);
	Long countByLevelDarkknightBetween(final int lower, final int upper);
	Long countByLevelMachinistBetween(final int lower, final int upper);
	Long countByLevelAstrologianBetween(final int lower, final int upper);
	Long countByLevelScholarBetween(final int lower, final int upper);
	Long countByLevelRedmageBetween(final int lower, final int upper);
	Long countByLevelSamuraiBetween(final int lower, final int upper);
	Long countByLevelBluemageBetween(final int lower, final int upper);
	Long countByLevelGunbreakerBetween(final int lower, final int upper);
	Long countByLevelDancerBetween(final int lower, final int upper);
	Long countByLevelReaperBetween(final int lower, final int upper);
	Long countByLevelSageBetween(final int lower, final int upper);

	Long countByLevelCarpenterBetween(final int lower, final int upper);
	Long countByLevelBlacksmithBetween(final int lower, final int upper);
	Long countByLevelArmorerBetween(final int lower, final int upper);
	Long countByLevelGoldsmithBetween(final int lower, final int upper);
	Long countByLevelLeatherworkerBetween(final int lower, final int upper);
	Long countByLevelWeaverBetween(final int lower, final int upper);
	Long countByLevelAlchemistBetween(final int lower, final int upper);
	Long countByLevelCulinarianBetween(final int lower, final int upper);

	Long countByLevelMinerBetween(final int lower, final int upper);
	Long countByLevelBotanistBetween(final int lower, final int upper);
	Long countByLevelFisherBetween(final int lower, final int upper);

    /**
     * Method to trim all deleted characters from the top-end of the database.
     * Used to ensure enable re-parsing of new characters where the gatherer overruns at the top-end of the ID numbers.
     */
    @Modifying
    @Transactional
    void deleteByIdGreaterThan(final Integer lastKnownId);
}
