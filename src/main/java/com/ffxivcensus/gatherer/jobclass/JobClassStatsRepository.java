package com.ffxivcensus.gatherer.jobclass;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for CRUD actions for a given JobClassStats instance.
 *
 * @author matthew.hillier
 */
public interface JobClassStatsRepository extends CrudRepository<JobClassStatsBean, JobClassStatsPrimaryKey> {


}
