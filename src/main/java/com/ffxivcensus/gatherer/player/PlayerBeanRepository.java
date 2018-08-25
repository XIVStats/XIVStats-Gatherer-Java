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

    /**
     * Method to trim all deleted characters from the top-end of the database.
     * Used to ensure enable re-parsing of new characters where the gatherer overruns at the top-end of the ID numbers.
     */
    @Modifying
    @Transactional
    void deleteByIdGreaterThan(final Integer lastKnownId);
}
