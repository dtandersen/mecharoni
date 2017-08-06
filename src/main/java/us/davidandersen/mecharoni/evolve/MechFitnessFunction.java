package us.davidandersen.mecharoni.evolve;

import us.davidandersen.mecharoni.entity.AlphaStrategy;
import us.davidandersen.mecharoni.entity.FiringStrategy;
import us.davidandersen.mecharoni.entity.MechSimulator;
import us.davidandersen.mecharoni.entity.MechSpec;
import us.davidandersen.mecharoni.entity.predicate.BallisticPredicate;
import us.davidandersen.mecharoni.entity.predicate.ClanLinkedLasersPredicate;
import us.davidandersen.mecharoni.entity.predicate.EnergyPredicate;
import us.davidandersen.mecharoni.entity.predicate.MissilePredicate;
import us.davidandersen.mecharoni.entity.predicate.PpcPenaltyGroupPredicate;
import us.davidandersen.mecharoni.entity.predicate.SrmPenaltyGroupPredicate;
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

		final long srmCount = mech.itemCount(new SrmPenaltyGroupPredicate());
		if (srmCount > 4)
		{
			score -= 1;
		}
		else if (srmCount > 1 && mech.hasItem("MRM 40"))
		{
			score -= 1;
		}
		else if (srmCount > 2 && mech.hasItem("MRM 30"))
		{
			score -= 1;
		}
		else if (srmCount > 3 && mech.hasItem("MRM 20"))
		{
			score -= 1;
		}

		if (mech.itemCount(new EnergyPredicate()) < 0)
		{
			score -= 1;
		}
		if (mech.itemCount(new MissilePredicate()) < 0)
		{
			score -= 1;
		}
		if (mech.itemCount(new BallisticPredicate()) < 0)
		{
			score -= 1;
		}
		if (mech.uniqueWeapons() > 2)
		{
			score -= 1;
		}
		score *= 10000;
		final int time = 120;
		// score += mech.getFirepower() * 30;
		score += sim(mech, 0, config.range, new AlphaStrategy()) * 30;
		// score += sim(mech, time, config.range, new AlwaysFireStrategy());
		score -= mech.uniqueWeapons() / 10;
		// score *= (1 + .1 * (mech.occupiedTons() / config.tons));
		// for (int i = 0; i < 5; i++)
		// {
		// score += sim(mech, 120, config.range);
		// }

		return score;
	}

	private float sim(final MechSpec a, final int endTime, final int range, final FiringStrategy firingStrategy)
	{
		final MechSimulator ms = new MechSimulator(firingStrategy);
		ms.addMech(a);
		ms.go(endTime, range);
		final float damage = ms.damage();
		return damage;
	}
}
