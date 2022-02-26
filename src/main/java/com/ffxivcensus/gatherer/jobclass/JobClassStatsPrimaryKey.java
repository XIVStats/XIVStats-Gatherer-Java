package com.ffxivcensus.gatherer.jobclass;

import java.io.Serializable;

public class JobClassStatsPrimaryKey implements Serializable {

	private String classAbbreviation;
	private String runId;

	public JobClassStatsPrimaryKey(String classAbbreviation, String runId) {
		this.classAbbreviation = classAbbreviation;
		this.runId = runId;
	}

	public JobClassStatsPrimaryKey(){}
}
