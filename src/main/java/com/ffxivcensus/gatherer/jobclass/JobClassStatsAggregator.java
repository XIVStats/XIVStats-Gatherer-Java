package com.ffxivcensus.gatherer.jobclass;

import com.ffxivcensus.gatherer.player.PlayerBeanRepository;

import java.util.Map;

public class JobClassStatsAggregator {

	private PlayerBeanRepository playerBeanRepository;
	private JobClassStatsRepository jobClassStatsRepository;

	public JobClassStatsAggregator(final PlayerBeanRepository playerBeanRepository, final JobClassStatsRepository jobClassStatsRepository) {
		this.playerBeanRepository = playerBeanRepository;
		this.jobClassStatsRepository = jobClassStatsRepository;
	}

	public long countByLevelBetween(final JobClass jobClass, final int lower, final int upper) {
		long count = -1;
		switch (jobClass) {
			case GLD:
				count = playerBeanRepository.countByLevelGladiatorBetween(lower, upper);
				break;
			case MRD:
				count = playerBeanRepository.countByLevelMarauderBetween(lower, upper);
				break;
			case DRK:
				count = playerBeanRepository.countByLevelDarkknightBetween(lower, upper);
				break;
			case GNB:
				count = playerBeanRepository.countByLevelGunbreakerBetween(lower, upper);
				break;
			case CNJ:
				count = playerBeanRepository.countByLevelConjurerBetween(lower,upper);
				break;
			case AST:
				count = playerBeanRepository.countByLevelAstrologianBetween(lower, upper);
				break;
			case SGE:
				count = playerBeanRepository.countByLevelSageBetween(lower, upper);
				break;
			case PGL:
				count = playerBeanRepository.countByLevelPugilistBetween(lower, upper);
				break;
			case LNC:
				count = playerBeanRepository.countByLevelLancerBetween(lower, upper);
				break;
			case ROG:
				count = playerBeanRepository.countByLevelRogueBetween(lower, upper);
				break;
			case SAM:
				count = playerBeanRepository.countByLevelSamuraiBetween(lower, upper);
				break;
			case RPR:
				count = playerBeanRepository.countByLevelReaperBetween(lower,upper);
				break;
			case ARC:
				count = playerBeanRepository.countByLevelArcherBetween(lower, upper);
				break;
			case MCH:
				count = playerBeanRepository.countByLevelMachinistBetween(lower, upper);
				break;
			case DNC:
				count = playerBeanRepository.countByLevelDancerBetween(lower, upper);
				break;
			case THM:
				count = playerBeanRepository.countByLevelThaumaturgeBetween(lower, upper);
				break;
			case ACN:
				count = playerBeanRepository.countByLevelArcanistBetween(lower, upper);
				break;
			case RDM:
				count = playerBeanRepository.countByLevelRedmageBetween(lower, upper);
				break;
			case BLU:
				count = playerBeanRepository.countByLevelBluemageBetween(lower, upper);
				break;
			case CRP:
				count = playerBeanRepository.countByLevelCarpenterBetween(lower, upper);
				break;
			case BSM:
				count = playerBeanRepository.countByLevelBlacksmithBetween(lower, upper);
				break;
			case ARM:
				count = playerBeanRepository.countByLevelArmorerBetween(lower, upper);
				break;
			case GSM:
				count = playerBeanRepository.countByLevelGoldsmithBetween(lower, upper);
				break;
			case LTW:
				count = playerBeanRepository.countByLevelLeatherworkerBetween(lower, upper);
				break;
			case WVR:
				count = playerBeanRepository.countByLevelWeaverBetween(lower, upper);
				break;
			case ALC:
				count = playerBeanRepository.countByLevelAlchemistBetween(lower, upper);
				break;
			case CUL:
				count = playerBeanRepository.countByLevelCulinarianBetween(lower, upper);
				break;
			case MIN:
				count = playerBeanRepository.countByLevelMinerBetween(lower, upper);
				break;
			case BTN:
				count = playerBeanRepository.countByLevelBotanistBetween(lower, upper);
				break;
			case FSH:
				count = playerBeanRepository.countByLevelFisherBetween(lower, upper);
				break;
		}
		return count;
	}

	public long countByLevelBetweenExcludeStartingLevel(final JobClass jobClass, final int lower, final int upper, final int startingLevel) {
		int lowerBound = lower;
		if (startingLevel == lower) {
			lowerBound += 1;
		}
		return countByLevelBetween(jobClass, lowerBound, upper);
	}

	public void aggregate(final String dataLabel) {
		for (Map.Entry<JobClass, Integer> entry : JobClassStartingLevelMap.MAP.entrySet()) {
			JobClassStatsBean jobClassStatsBean = new JobClassStatsBean(entry.getKey().toString(), dataLabel);
			jobClassStatsBean.setStartingLevel(entry.getValue().longValue());
			jobClassStatsBean.setCountStartingLvl(countByLevelBetween(entry.getKey(),entry.getValue(),entry.getValue()));
			jobClassStatsBean.setLvl1To29(countByLevelBetweenExcludeStartingLevel(entry.getKey(), 1, 29, entry.getValue()));
			jobClassStatsBean.setLvl30To49(countByLevelBetweenExcludeStartingLevel(entry.getKey(), 30, 49, entry.getValue()));
			jobClassStatsBean.setLvl50To59(countByLevelBetweenExcludeStartingLevel(entry.getKey(), 50, 59, entry.getValue()));
			jobClassStatsBean.setLvl60To69(countByLevelBetweenExcludeStartingLevel(entry.getKey(), 60, 69, entry.getValue()));
			jobClassStatsBean.setLvl70To79(countByLevelBetweenExcludeStartingLevel(entry.getKey(), 70, 79, entry.getValue()));
			jobClassStatsBean.setLvl80To89(countByLevelBetweenExcludeStartingLevel(entry.getKey(), 80, 89, entry.getValue()));
			jobClassStatsBean.setLvl90To99(countByLevelBetweenExcludeStartingLevel(entry.getKey(), 90, 99, entry.getValue()));
			this.jobClassStatsRepository.save(jobClassStatsBean);
		}
	}
}
