package com.ffxivcensus.gatherer.player;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for CRUD actions for a given PlayerBean instance.
 * @author matthew.hillier
 *
 */
public interface PlayerBeanRepository extends CrudRepository<PlayerBean, Integer> {

}
