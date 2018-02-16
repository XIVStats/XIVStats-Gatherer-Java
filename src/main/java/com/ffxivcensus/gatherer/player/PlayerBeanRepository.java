package com.ffxivcensus.gatherer.player;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
     * @return Top-most {@link PlayerBean} object by ID.
     */
    PlayerBean findTopByOrderByIdDesc();

    /**
     * Located the last non-deleted Character ID.
     * 
     * @return
     */
    @Query(value = "SELECT MAX(id) FROM PlayerBean p WHERE characterStatus != 'DELETED'")
    Integer findTopByIdByCharacterStatusNotDeleted();

    /**
     * Method to trim all deleted characters from the top-end of the database.
     * Used to ensure enable re-parsing of new characters where the gatherer overruns at the top-end of the ID numbers.
     */
    @Modifying
    @Transactional
    void deleteByIdGreaterThan(final Integer lastKnownId);
}
