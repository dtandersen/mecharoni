package us.davidandersen.mecharoni.evolve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.jenetics.Genotype;
import us.davidandersen.mecharoni.entity.BasicComponent;
import us.davidandersen.mecharoni.entity.HardpointType;
import us.davidandersen.mecharoni.entity.Location;
import us.davidandersen.mecharoni.entity.Location.LocationBuilder;
import us.davidandersen.mecharoni.entity.LocationType;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.entity.MechBuild.MechBuildBuilder;
import us.davidandersen.mecharoni.entity.MechSpec.MechSpecBuilder;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;

public class MechCodec
{
	public static MechBuild toMech(final Genotype<MechGene> gt, final EvolveMechConfig config)
	{
		final MechChromosome c = gt.getChromosome()
				.as(MechChromosome.class);
		return MechCodec.toMech2(c, config);
	}

	public static MechBuild toMech2(final MechChromosome c, final EvolveMechConfig config)
	{
		final MechSpecBuilder spec = MechSpecBuilder.mech()
				.withExternalHeatSinks(config.heatSinks)
				.withEngineSinks(config.engineSinks)
				.withTons(config.tons)
				.withFreeSlots(config.slots);

		for (final Location location : config.locations.values())
		{
			spec.withLocation(LocationBuilder.location()
					.withEnergy(location.getHardpointCount(HardpointType.ENERGY))
					.withMissile(location.getHardpointCount(HardpointType.MISSILE))
					.withBallistics(location.getHardpointCount(HardpointType.BALLISTIC))
					.withAms(location.getHardpointCount(HardpointType.AMS))
					.withEcm(location.getHardpointCount(HardpointType.ECM))
					.withLocationType(location.getLocationType())
					.withSlots(location.getSlots()));
		}
		final MechBuildBuilder mechSpecBuilder = MechBuildBuilder.mech()
				.withSpec(spec);

		final List<LocationType> locationTypes = getLocationTypes(config.locations);

		for (int i = 0; i < c.length(); i++)
		{
			final MechGene g = c.getGene(i);
			final BasicComponent component = g.getAllele();
			mechSpecBuilder.withComponent(locationTypes.get(i), component);
		}

		return mechSpecBuilder.build();
	}

	private static List<LocationType> getLocationTypes(final Map<LocationType, Location> locations)
	{
		final List<LocationType> types = new ArrayList<>();
		final List<Location> sortedlocs = new ArrayList<>(locations.values());

		Collections.sort(sortedlocs, new Comparator<Location>() {
			@Override
			public int compare(final Location o1, final Location o2)
			{
				return o1.getLocationType().compareTo(o2.getLocationType());
			}
		});

		// final int total = sortedlocs.stream().mapToInt(loc -> loc.getSlots()).sum();
		for (final Location loc : sortedlocs)
		{
			for (int i = 0; i < loc.getSlots(); i++)
			{
				types.add(loc.getLocationType());
			}
		}

		return types;
	}
}
