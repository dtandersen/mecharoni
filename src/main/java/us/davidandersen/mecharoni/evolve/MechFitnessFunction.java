package us.davidandersen.mecharoni.evolve;

import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.entity.MechSim3;
import us.davidandersen.mecharoni.entity.predicate.BallisticPredicate;
import us.davidandersen.mecharoni.entity.predicate.ClanLargeLasersPredicate;
import us.davidandersen.mecharoni.entity.predicate.ClanLinkedLasersPredicate;
import us.davidandersen.mecharoni.entity.predicate.EnergyPredicate;
import us.davidandersen.mecharoni.entity.predicate.MissilePredicate;
import us.davidandersen.mecharoni.entity.predicate.SrmPenaltyGroupPredicate;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;

public class MechFitnessFunction
{
	private final EvolveMechConfig config;

	public MechFitnessFunction(final EvolveMechConfig config)
	{
		this.config = config;
	}

	public double eval(final MechBuild mech)
	{
		double score = 0;
		final long llCount = mech.componentCount(new ClanLargeLasersPredicate());
		if (llCount >= 3)
		{
			score -= 1;
		}
		else
			if (mech.itemCount("C-HEAVY LRG LASER", "C-LRG PULSE LASER", "C-ER LRG LASER") > 0 && llCount >= 2)
			{
				score -= 1;
			}

		final long lCount = mech.componentCount(new ClanLinkedLasersPredicate());
		if (mech.componentCountByFriendlyName("C-HEAVY MED LASER") > 0 && lCount >= 4)
		{
			score -= 1;
		}
		// if (mech.itemCount(new PpcPenaltyGroupPredicate()) >= 2)
		// {
		// score -= 1;
		// }

		final long srmCount = mech.componentCount(new SrmPenaltyGroupPredicate());
		if (srmCount > 4)
		{
			score -= 1;
		}
		else
			if (srmCount > 1 && mech.hasItem("MRM 40"))
			{
				score -= 1;
			}
			else
				if (srmCount > 2 && mech.hasItem("MRM 30"))
				{
					score -= 1;
				}
				else
					if (srmCount > 3 && mech.hasItem("MRM 20"))
					{
						score -= 1;
					}

		if (mech.componentCount(new EnergyPredicate()) < 0)
		{
			score -= 1;
		}
		if (mech.componentCount(new MissilePredicate()) < 0)
		{
			score -= 1;
		}
		if (mech.componentCount(new BallisticPredicate()) > 0)
		{
			score -= 1;
		}
		if (mech.uniqueWeapons() > 3)
		{
			score -= 1;
		}
		score *= 10000;
		// final int time = 120;
		// score += mech.getFirepower();
		// score += sim(mech, 15, config.range, new AlphaStrategy()) * (time /
		// 15f);
		// score += sim(mech, time, config.range, new AlwaysFireStrategy());
		// score -= mech.uniqueWeapons();
		// score *= (1 + .1 * (mech.occupiedTons() / config.tons));
		// for (int i = 0; i < 5; i++)
		// {
		// score += sim(mech, 120, config.range);
		// }

		// final MechSim2 sim2 = new MechSim2();
		// sim2.addMech(mech);
		// sim2.run(30);
		// score += sim2.getScore();

		final MechSim3 sim3 = new MechSim3();
		sim3.addMech(mech);
		sim3.run(15, config.range);
		score += sim3.getDamage();
		// if (sim3.getHeatOver() > 0)
		// {
		// score -= (sim3.getHeatOver() * sim3.getDph());
		// }

		// try
		// {
		// Thread.sleep(0);
		// }
		// catch (final InterruptedException e)
		// {
		// e.printStackTrace();
		// }

		return score;
	}

	// private float sim(final MechBuild a, final int endTime, final int range,
	// final FiringStrategy firingStrategy)
	// {
	// final MechSimulator ms = new MechSimulator(firingStrategy);
	// ms.addMech(a);
	// ms.go(endTime, range);
	// final float damage = ms.damage();
	// return damage;
	// }
}
