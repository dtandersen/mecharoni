package us.davidandersen.mecharoni.evolve;

import us.davidandersen.mecharoni.entity.MechSimulator;
import us.davidandersen.mecharoni.entity.MechSpec;
import us.davidandersen.mecharoni.entity.predicate.ClanLinkedLasersPredicate;
import us.davidandersen.mecharoni.entity.predicate.PpcPenaltyGroupPredicate;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;

public class MechFitnessFunction2
{
	private final EvolveMechConfig config;

	public MechFitnessFunction2(final EvolveMechConfig config)
	{
		this.config = config;
	}

	public double eval(final MechSpec mech)
	{
		double score = 0;
		if (mech.getTons() > config.tons)
		{
			score -= mech.getTons();
		}
		if (mech.getSlots() > config.slots)
		{
			score -= mech.getSlots();
		}
		if (mech.getEnergySlots() > config.energySlots)
		{
			score -= mech.getEnergySlots();
		}
		if (mech.getBallisticSlots() > config.ballisticSlots)
		{
			score -= mech.getBallisticSlots();
		}
		if (mech.getMissileSlots() > config.missileSlots)
		{
			score -= mech.getMissileSlots();
		}
		if (mech.getEcmSlots() > config.ecmSlots)
		{
			score -= mech.getEcmSlots();
		}
		if (mech.getAmsSlots() > config.amsSlots)
		{
			score -= mech.getAmsSlots();
		}
		if (mech.itemCount("C-HEAVY MED LASER") > 0 && mech.itemCount(new ClanLinkedLasersPredicate()) >= 4)
		{
			score -= 1;
		}
		if (mech.itemCount(new PpcPenaltyGroupPredicate()) >= 2)
		{
			score -= 1;
		}
		if (mech.uniqueWeapons() > 3)
		{
			score -= 1;
		}
		// if (a.heatExpended(30) > a.heatRegained(30) * 2.5)
		// {
		// score -= a.heatExpended(30);
		// }
		score *= 10000;
		// score += a.totalDps();
		final int time = 120;
		score += sim(mech, 0, config.range) * time * (1 / 15f);
		score += sim(mech, time, config.range);
		// score += sim(a, 60) / 4;
		// score += sim(a, 120) / 8;
		// score -= a.getSlots() * 5;
		// score -= a.getTons() * 5;
		// score = score * (1 - (a.getSlots() / config.slots) * .1);
		// score = score * (1 - (a.getTons() / config.tons) * .1);
		// score += a.getExternalHeatSinks() * 100;
		// if (a.heatExpended(30) > 0)
		// {
		// score = score + (score * a.heatRegained(30) / a.heatExpended(30)) * .1;
		// }
		return score;
	}

	private float sim(final MechSpec a, final int endTime, final int range)
	{
		final MechSimulator ms = new MechSimulator();
		ms.addMech(a);
		ms.go(endTime, range);
		final float damage = ms.damage();
		return damage;
	}
}
