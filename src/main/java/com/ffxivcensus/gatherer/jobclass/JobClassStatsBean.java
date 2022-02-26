package com.ffxivcensus.gatherer.jobclass;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Object class to represent a Job class. T
 *
 * @author Peter Reid
 * @since v1.0
 */
@Entity
@Table(name = "tblclasses")
@IdClass(JobClassStatsPrimaryKey.class)
public class JobClassStatsBean implements Serializable {
	@Id
	private String classAbbreviation;
	@Id
	private  String runId;
	private Long startingLevel;
	private Long countStartingLvl;
	@Column(name = "lvl_1_to_29")
	private Long lvl1To29;
	@Column(name = "lvl_30_to_49")
	private Long lvl30To49;
	@Column(name = "lvl_50_to_59")
	private Long lvl50To59;
	@Column(name = "lvl_60_to_69")
	private Long lvl60To69;
	@Column(name = "lvl_70_to_79")
	private Long lvl70To79;
	@Column(name = "lvl_80_to_89")
	private Long lvl80To89;
	@Column(name = "lvl_90_to_99")
	private Long lvl90To99;

	public JobClassStatsBean(String classAbbreviation, String runId) {
		this.classAbbreviation = classAbbreviation;
		this.runId = runId;
	}

	public JobClassStatsBean(){
	}

	public Long getStartingLevel() {
		return startingLevel;
	}

	public void setStartingLevel(Long startingLevel) {
		this.startingLevel = startingLevel;
	}

	public Long getCountStartingLvl() {
		return countStartingLvl;
	}

	public void setCountStartingLvl(Long countStartingLvl) {
		this.countStartingLvl = countStartingLvl;
	}

	public Long getLvl1To29() {
		return lvl1To29;
	}

	public void setLvl1To29(Long lvl1To29) {
		this.lvl1To29 = lvl1To29;
	}

	public Long getLvl30To49() {
		return lvl30To49;
	}

	public void setLvl30To49(Long lvl30To49) {
		this.lvl30To49 = lvl30To49;
	}

	public Long getLvl50To59() {
		return lvl50To59;
	}

	public void setLvl50To59(Long lvl50To59) {
		this.lvl50To59 = lvl50To59;
	}

	public Long getLvl60To69() {
		return lvl60To69;
	}

	public void setLvl60To69(Long lvl60To69) {
		this.lvl60To69 = lvl60To69;
	}

	public Long getLvl70To79() {
		return lvl70To79;
	}

	public void setLvl70To79(Long lvl70To79) {
		this.lvl70To79 = lvl70To79;
	}

	public Long getLvl80To89() {
		return lvl80To89;
	}

	public void setLvl80To89(Long lvl80To89) {
		this.lvl80To89 = lvl80To89;
	}

	public Long getLvl90To99() {
		return lvl90To99;
	}

	public void setLvl90To99(Long lvl90To99) {
		this.lvl90To99 = lvl90To99;
	}
}
