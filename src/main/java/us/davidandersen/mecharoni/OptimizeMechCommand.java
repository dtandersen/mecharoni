package us.davidandersen.mecharoni;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import us.davidandersen.mecharoni.OptimizeMechCommand.MechSpecificationYaml.LocationYaml;
import us.davidandersen.mecharoni.command.OptimizeMech;
import us.davidandersen.mecharoni.command.OptimizeMech.OptimizeMechRequest;
import us.davidandersen.mecharoni.entity.Location;
import us.davidandersen.mecharoni.entity.Location.LocationBuilder;
import us.davidandersen.mecharoni.entity.LocationType;
import us.davidandersen.mecharoni.entity.Quirk;
import us.davidandersen.mecharoni.entity.Quirk.QuirkBuilder;
import us.davidandersen.mecharoni.entity.QuirkType;
import us.davidandersen.mecharoni.repository.ComponentRepository;
import us.davidandersen.mecharoni.repository.json.PgiComponentRepository;

public class OptimizeMechCommand
{
	private static final String DEFAULT_CONFIG = "top-dog.yaml";

	public static void main(final String[] args) throws Exception
	{
		final String file = args.length > 0 ? args[0] : DEFAULT_CONFIG;
		final InputStream input = new FileInputStream(new File(file));
		System.out.println("Loading " + file);

		final Yaml yaml = new Yaml();
		final MechSpecificationYaml data = yaml.loadAs(input, MechSpecificationYaml.class);

		final ComponentRepository componentReader = new PgiComponentRepository();

		final OptimizeMech optimizer = new OptimizeMech(componentReader);
		optimizer.setRequest(new OptimizeMechRequestYamlAdapter(data));
		optimizer.execute();
	}

	private static final class OptimizeMechRequestYamlAdapter implements OptimizeMechRequest
	{
		private final MechSpecificationYaml specYaml;

		public OptimizeMechRequestYamlAdapter(final MechSpecificationYaml data)
		{
			this.specYaml = data;
		}

		@Override
		public float getTons()
		{
			return specYaml.tons;
		}

		@Override
		public int getEngineSinks()
		{
			return specYaml.engineSinks;
		}

		@Override
		public int getHeatSinks()
		{
			return specYaml.heatSinks;
		}

		@Override
		public String[] getExcludes()
		{
			return specYaml.excludes;
		}

		@Override
		public String getFaction()
		{
			return specYaml.faction;
		}

		@Override
		public int getRange()
		{
			return specYaml.range;
		}

		@Override
		public Map<LocationType, Location> getLocations()
		{
			final Map<LocationType, Location> locations = new HashMap<>();
			for (final String locationType : specYaml.locations.keySet())
			{
				final LocationYaml location = specYaml.locations.get(locationType);
				final LocationType valueOf = LocationType.valueOf(locationType);
				locations.put(valueOf, LocationBuilder.location()
						.withLocationType(valueOf)
						.withSlots(location.slots)
						.withEnergy(location.energy)
						.withBallistics(location.ballistic)
						.withMissile(location.missile)
						.build());
			}
			return locations;
		}

		@Override
		public int getSlots()
		{
			return specYaml.slots;
		}

		@Override
		public Map<QuirkType, Quirk> getQuirks()
		{
			final Map<QuirkType, Quirk> quirks = new HashMap<>();
			if (specYaml.enhancements == null)
			{
				return new HashMap<>();
			}

			for (final String quirkName : specYaml.enhancements.keySet())
			{
				final QuirkType quirkType = QuirkType.find(quirkName);
				quirks.put(quirkType, new QuirkBuilder()
						.withQuirkType(quirkType)
						.withValue(specYaml.enhancements.get(quirkName) / 100f)
						.build());
			}

			return quirks;
		}
	}

	static final class MechSpecificationYaml
	{
		public String faction;

		public String[] excludes;

		public int heatSinks;

		public int engineSinks;

		public float tons;

		public int slots;

		public int range;

		public Map<String, LocationYaml> locations;

		public Map<String, Object> userData;

		public Map<String, Float> enhancements;

		static final class LocationYaml
		{
			public int slots;

			public int energy;

			public int missile;

			public int ballistic;
		}
	}
}
