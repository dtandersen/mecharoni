package us.davidandersen.mecharoni.evolve;

import java.util.ArrayList;
import java.util.List;
import io.jenetics.ext.moea.Vec;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.sim4.CombatSimulator;
import us.davidandersen.mecharoni.sim4.Mech;
import us.davidandersen.mecharoni.sim4.MechAmmo;
import us.davidandersen.mecharoni.sim4.MechComponent;
import us.davidandersen.mecharoni.sim4.MechWeapon;

public class MechSimulatorFitnessFunction2
{
	private final EvolveMechConfig config;

	private final float TIME = 15f;

	public MechSimulatorFitnessFunction2(final EvolveMechConfig config)
	{
		this.config = config;
	}

	public Vec<double[]> eval(final MechBuild mechBuild)
	{
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
		if (energyCount > 2)
		{
			return Vec.of(0d, 0d, 0d);
		}
		if (totalCount > 3)
		{
			return Vec.of(0d, 0d, 0d);
		}
		final double fitness2 = simulate(mechBuild, 2);
		// final double fitness15 = simulate(mechBuild, 15);
		// final double fitness30 = simulate(mechBuild, 30);
		final double fitness120 = simulate(mechBuild, 120);

		final double f2 = Math.pow(fitness2, 2);
		// final double f15 = Math.pow(fitness15, 2);
		// final double f30 = Math.pow(fitness30, 2);
		final double f120 = Math.pow(fitness120, 2);

		final double alpha = mechBuild.getWeapons().stream().mapToDouble(w -> w.getDamage() * w.getNumFiring()).sum();
		final double heat = mechBuild.getWeapons().stream().mapToDouble(w -> w.getHeat()).sum();
		final double ah = alpha / heat;
		return Vec.of(f2, f120, Math.pow(alpha, 2));
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

		sim.run(time);
		final float damage = sim.getTarget().getDamage();
		score = damage;
		// final double alpha = mech.getWeapons().stream().mapToDouble(w ->
		// w.getDamage()).sum();
		// score = damage + mech.getHeatDisipation();

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
					.withName(component.getAmmoType())
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
