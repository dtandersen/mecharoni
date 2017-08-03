package us.davidandersen.mecharoni.evolve;

import us.davidandersen.mecharoni.entity.MechSimulator;
import us.davidandersen.mecharoni.entity.MechSpec;
import us.davidandersen.mecharoni.entity.predicate.BeamsPredicate;
import us.davidandersen.mecharoni.entity.predicate.ClanLinkedLasersPredicate;
import us.davidandersen.mecharoni.entity.predicate.PpcPenaltyGroupPredicate;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;

public class MechFitnessFunction
{
	private final EvolveMechConfig config;

	public MechFitnessFunction(final EvolveMechConfig config)
	{
		this.config = config;
	}

	public double eval(final MechSpec mech)
	{
		double score = 0;
		if (mech.itemCount("C-HEAVY MED LASER") > 0 && mech.itemCount(new ClanLinkedLasersPredicate()) >= 4)
		{
			score -= 1;
		}
		if (mech.itemCount(new PpcPenaltyGroupPredicate()) >= 2)
		{
			score -= 1;
		}
		if (mech.itemCount(new BeamsPredicate()) < 1)
		{
			score -= 1;
		}
		if (mech.uniqueWeapons() > 3)
		{
			score -= 1;
		}
		score *= 10000;
		final int time = 120;
		score += sim(mech, 0, config.range) * time / 15;
		score += sim(mech, time, config.range);

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
