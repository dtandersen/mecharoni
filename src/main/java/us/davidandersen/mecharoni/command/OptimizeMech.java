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
		final String[] excludes = new String[] { "ClanFlamer", "ClanLRM", "ClanMicro", "ClanERMicroLaser" };
		final List<Component> items = filterItems(excludes, weaponReader.clanComponents());

		final MechSpecYaml spec = new MechSpecYaml();
		spec.slots = 33;
		spec.tons = 17;
		spec.items = items;
		spec.amsSlots = 0;
		spec.energySlots = 8;
		spec.ballisticSlots = 0;
		spec.missileSlots = 2;
		spec.ecmSlots = 0;
		spec.engineSinks = 10;
		spec.heatSinks = 4;

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
			if (item.getName().startsWith(filter)) { return true; }
		}

		return false;
	}
}
