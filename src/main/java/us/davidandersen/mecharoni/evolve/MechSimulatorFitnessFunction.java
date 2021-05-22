package us.davidandersen.mecharoni.evolve;

import java.util.ArrayList;
import java.util.List;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.sim4.CombatSimulator;
import us.davidandersen.mecharoni.sim4.MechStatus;
import us.davidandersen.mecharoni.sim4.WeaponStatus;

public class MechSimulatorFitnessFunction
{
	private final EvolveMechConfig config;

	public MechSimulatorFitnessFunction(final EvolveMechConfig config)
	{
		this.config = config;
	}

	public double eval(final MechBuild mechBuild)
	{
		double score = 0;

		final MechStatus mech = MechStatus.builder()
				.withInternalHeatSinks(mechBuild.getInternalHeatSinks())
				.withExternalHeatSinks(mechBuild.getExternalHeatSinks())
				.withWeapons(weapons(mechBuild))
				.build();
		final CombatSimulator sim = new CombatSimulator(mech);

		sim.run(1);
		score = sim.getTarget().getDamage();
		return score;
	}

	private List<WeaponStatus> weapons(final MechBuild mechBuild)
	{
		final List<WeaponStatus> weapons = new ArrayList<WeaponStatus>();

		for (final Component component : mechBuild.getWeapons())
		{
			final WeaponStatus weapon = WeaponStatus.builder()
					.withDamage(component.getDamage())
					.withHeat(component.getHeat())
					.withMaxCooldown(component.getCooldown())
					.withHeatPenaltyId(component.getHeatPenaltyId())
					.withMinHeatPenaltyLevel(component.getMinHeatPenaltyLevel())
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