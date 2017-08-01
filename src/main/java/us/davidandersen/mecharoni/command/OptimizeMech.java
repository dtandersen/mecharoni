package us.davidandersen.mecharoni.command;

import java.util.List;
import java.util.stream.Collectors;
import us.davidandersen.mecharoni.command.OptimizeMech.OptimizeMechRequestAdapter;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.evolve.EvolveMech;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.repository.ComponentRepository;

public class OptimizeMech extends BaseCommand<OptimizeMechRequestAdapter, VoidResult>
{
	private final ComponentRepository componentRepository;

	public OptimizeMech(final ComponentRepository componentRepository)
	{
		this.componentRepository = componentRepository;
	}

	@Override
	public void execute()
	{
		try
		{
			List<Component> items;
			switch (request.getFaction())
			{
			case "clan":
				items = filterItems(request.getExcludes(), componentRepository.clanComponents());
				break;
			case "innersphere":
				items = filterItems(request.getExcludes(), componentRepository.isComponents());
				break;
			default:
				throw new RuntimeException("faction must be clan or innersphere");
			}

			final EvolveMechConfig spec = new EvolveMechConfig();
			spec.slots = request.getSlots();
			spec.tons = request.getTons();
			spec.items = items;
			spec.amsSlots = request.getAmsSlots();
			spec.energySlots = request.getEnergySlots();
			spec.ballisticSlots = request.getBallisticSlots();
			spec.missileSlots = request.getMissileSlots();
			spec.ecmSlots = request.getEcmSlots();
			spec.engineSinks = request.getEngineSinks();
			spec.heatSinks = request.getHeatSinks();
			spec.range = request.getRange();

			final EvolveMech evolver = new EvolveMech();
			evolver.run(spec);
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
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
			if (item.getFriendlyName() != null && item.getFriendlyName().equalsIgnoreCase(filter)) { return true; }
		}

		return false;
	}

	public interface OptimizeMechRequestAdapter
	{
		int getSlots();

		int getRange();

		String getFaction();

		String[] getExcludes();

		float getTons();

		int getAmsSlots();

		int getEnergySlots();

		int getBallisticSlots();

		int getMissileSlots();

		int getEcmSlots();

		int getEngineSinks();

		int getHeatSinks();
	}
}
