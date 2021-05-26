package us.davidandersen.mecharoni.evolve;

import java.util.ArrayList;
import java.util.List;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.sim4.CombatSimulator;
import us.davidandersen.mecharoni.sim4.Mech;
import us.davidandersen.mecharoni.sim4.MechAmmo;
import us.davidandersen.mecharoni.sim4.MechComponent;
import us.davidandersen.mecharoni.sim4.MechWeapon;

public class MechSimulatorFitnessFunction
{
	private final EvolveMechConfig config;

	private final float TIME = 15f;

	public MechSimulatorFitnessFunction(final EvolveMechConfig config)
	{
		this.config = config;
	}

	public double eval(final MechBuild mechBuild)
	{
		final double fitness = 0;
		final double fitness1 = simulate(mechBuild, 2);
		// fitness += 8 * simulate(mechBuild, 15);
		final double fitness2 = simulate(mechBuild, 120) / 120;
		// fitness /= 3f;
		// fitness += 100 * mechBuild.getHeatDisipation();
		return Math.pow(fitness1 + mechBuild.getExternalHeatSinks(), 2) + Math.pow(fitness2, 2);
	}

	private double simulate(final MechBuild mechBuild, final int time)
	{
		double score = 0;

		final Mech mech = Mech.builder()
				.withInternalHeatSinks(mechBuild.getInternalHeatSinks())
				.withExternalHeatSinks(mechBuild.getExternalHeatSinks())
				.withComponents(weapons(mechBuild))
				.build();
		final CombatSimulator sim = new CombatSimulator(mech, config.range);

		final long energyCount = mechBuild.getWeapons().stream()
				.filter(w -> w.isEnergy())
				// .filter(w -> w.isWeapon())
				.map(w -> w.getName())
				.distinct()
				.count();
		final long totalCount = mechBuild.getWeapons().stream()
				// .filter(w -> w.isEnergy())
				.filter(w -> w.isWeapon())
				.map(w -> w.getName())
				.distinct()
				.count();

		sim.run(time);
		final float damage = sim.getTarget().getDamage();
		score = damage;
		// final double alpha = mech.getWeapons().stream().mapToDouble(w ->
		// w.getDamage()).sum();
		// score = damage + mech.getHeatDisipation();
		if (energyCount > 1)
		{
			score *= .9;
		}
		if (totalCount > 3)
		{
			score *= .9;
		}
		return score;
	}

	private List<MechComponent> weapons(final MechBuild mechBuild)
	{
		final List<MechComponent> weapons = new ArrayList<>();

		for (final Component component : mechBuild.getWeapons())
		{
			final MechWeapon weapon = MechWeapon.builder()
					.withName(component.getName())
					.withDamage(component.getDamage() * component.getNumFiring())
					.withHeat(component.getHeat())
					.withMaxCooldown(component.getCooldown() + component.getDuration())
					.withHeatPenaltyId(component.getHeatPenaltyId())
					.withMinHeatPenaltyLevel(component.getMinHeatPenaltyLevel())
					.withOptimalRange(component.getLongRange())
					.withMaxRange(component.getMaxRange())
					.withMinRange(component.getMinRange())
					.withAmmoType(component.getAmmoType())
					.withAmmoPerShot(component.getAmmoPerShot())
					.build();

			weapons.add(weapon);
		}
		for (final Component component : mechBuild.getAmmo())
		{
			final MechAmmo weapon = MechAmmo.builder()
					.withName(component.getName())
					.withNumShots(component.getNumShots())
					.build();

			weapons.add(weapon);
		}
		return weapons;
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
