package com.ffxivcensus.gatherer.jobclass;

import java.util.HashMap;
import java.util.Map;

public class JobClassStartingLevelMap {

	public static final Map<JobClass, Integer> MAP = new HashMap<JobClass, Integer>() {
		{
			put(JobClass.GLD, 1);
			put(JobClass.MRD, 1);
			put(JobClass.DRK, 30);
			put(JobClass.GNB, 60);

			put(JobClass.CNJ, 1);
			put(JobClass.AST, 30);
			put(JobClass.SGE, 70);

			put(JobClass.PGL, 1);
			put(JobClass.LNC, 1);
			put(JobClass.ROG, 1);
			put(JobClass.SAM, 50);
			put(JobClass.RPR, 70);
			put(JobClass.ARC, 1);
			put(JobClass.MCH, 30);
			put(JobClass.DNC, 60);
			put(JobClass.THM, 1);
			put(JobClass.ACN, 1);
			put(JobClass.RDM, 50);
			put(JobClass.BLU, 1);

			put(JobClass.CRP, 1);
			put(JobClass.BSM, 1);
			put(JobClass.ARM, 1);
			put(JobClass.GSM, 1);
			put(JobClass.LTW, 1);
			put(JobClass.WVR, 1);
			put(JobClass.ALC, 1);
			put(JobClass.CUL, 1);

			put(JobClass.MIN, 1);
			put(JobClass.BTN, 1);
			put(JobClass.FSH, 1);

		}
	};
}
