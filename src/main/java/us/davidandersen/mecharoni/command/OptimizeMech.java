package us.davidandersen.mecharoni.command;

import java.util.List;
import java.util.stream.Collectors;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.evolve.EvolveMech;
import us.davidandersen.mecharoni.evolve.EvolveMech.MechSpecYaml;
import us.davidandersen.mecharoni.repository.ComponentRepository;

public class OptimizeMech
{
	private final ComponentRepository weaponReader;

	public OptimizeMech(final ComponentRepository weaponReader)
	{
		this.weaponReader = weaponReader;
	}

	public void run() throws Exception
	{
		final String[] excludes = new String[] {
				"ClanFlamer",
				"RocketLauncher10", "RocketLauncher15", "RocketLauncher20",
				"ClanLRM5", "ClanLRM10", "ClanLRM15", "ClanLRM20",
				"ClanLRM5_Artemis", "ClanLRM10_Artemis", "ClanLRM15_Artemis", "ClanLRM20_Artemis",
				// "ClanSRM2", "ClanSRM4",
				"ClanSRM6",
				"ClanSRM2_Artemis", "ClanSRM4_Artemis",
				"ClanATM3", "ClanATM6", "ClanATM9", "ClanATM12",
				"ClanStreakSRM2", "ClanStreakSRM4", "ClanStreakSRM6" };
		// final List<Component> items = filterItems(excludes, weaponReader.isComponents());
		final List<Component> items = filterItems(excludes, weaponReader.clanComponents());

		final MechSpecYaml spec = new MechSpecYaml();
		spec.slots = 21;
		spec.tons = 7.5f;
		spec.items = items;
		spec.amsSlots = 0;
		spec.energySlots = 6;
		spec.ballisticSlots = 0;
		spec.missileSlots = 1;
		spec.ecmSlots = 0;
		spec.engineSinks = 10;
		spec.heatSinks = 0;

		final EvolveMech evolver = new EvolveMech();
		evolver.run(spec);
	}

	private static List<Component> filterItems(final String[] filters, final List<Component> readItems)
	{
		final List<Component> items = readItems.stream()
				.filter(item -> !matches(item, filters))
				.collect(Collectors.toList());

		return items;
	}

	private static boolean matches(final Component item, final String[] filters)
	{
		for (final String filter : filters)
		{
			if (item.getName().equalsIgnoreCase(filter)) { return true; }
			// if (item.getFriendlyName().equalsIgnoreCase(filter)) { return true; }
		}

		return false;
	}
}
