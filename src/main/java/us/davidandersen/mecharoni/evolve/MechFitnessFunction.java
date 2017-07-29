package us.davidandersen.mecharoni.evolve;

import us.davidandersen.mecharoni.entity.Mech;
import us.davidandersen.mecharoni.evolve.EvolveMech.FitnessCheckerConfig;

public class MechFitnessFunction
{
	private final FitnessCheckerConfig config;

	public MechFitnessFunction(final FitnessCheckerConfig config)
	{
		this.config = config;
	}

	public double eval(final Mech a)
	{
		double score = 0;
		if (a.getTons() > config.tons)
		{
			score -= a.getTons();
		}
		if (a.getSlots() > config.slots)
		{
			score -= a.getSlots();
		}
		if (a.getEnergySlots() > config.energySlots)
		{
			score -= a.getEnergySlots();
		}
		if (a.getBallisticSlots() > config.ballisticSlots)
		{
			score -= a.getBallisticSlots();
		}
		if (a.getMissileSlots() > config.missileSlots)
		{
			score -= a.getMissileSlots();
		}
		if (a.getEcmSlots() > config.ecmSlots)
		{
			score -= a.getEcmSlots();
		}
		if (a.getAmsSlots() > config.amsSlots)
		{
			score -= a.getAmsSlots();
		}
		if (a.heatExpended(30) > a.heatRegained(30) * 2.5)
		{
			score -= a.heatExpended(30);
		}
		score *= 10000;
		// score += a.totalDps();
		score += a.damageOverTime(30);
		score += a.getExternalHeatSinks();
		// if (a.heatExpended(30) > 0)
		// {
		// score = score + (score * a.heatRegained(30) / a.heatExpended(30)) * .1;
		// }
		return score;
	}
}
