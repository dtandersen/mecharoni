package us.davidandersen.mecharoni.entity;

import java.util.HashMap;
import java.util.Map;
import us.davidandersen.mecharoni.entity.Location.LocationBuilder;

public class MechSpec
{
	private final int maxFreeSlots;

	private final int engineSinks;

	private final int heatSinks;

	private final float maxTons;

	private Map<LocationType, Location> locations = new HashMap<>();

	public MechSpec(final MechSpecBuilder mechSpecBuilder)
	{
		maxFreeSlots = mechSpecBuilder.maxFreeSlots;
		engineSinks = mechSpecBuilder.engineSinks;
		heatSinks = mechSpecBuilder.heatSinks;
		maxTons = mechSpecBuilder.maxTons;
		locations = mechSpecBuilder.locations;
	}

	public int getMaxFreeSlots()
	{
		return maxFreeSlots;
	}

	public int getEngineSinks()
	{
		return engineSinks;
	}

	public int getHeatSinks()
	{
		return heatSinks;
	}

	public float getMaxTons()
	{
		return maxTons;
	}

	public Map<LocationType, Location> getLocations()
	{
		return locations;
	}

	public static class MechSpecBuilder
	{
		private int heatSinks;

		private int engineSinks;

		private float maxTons;

		private int maxFreeSlots;

		private final Map<LocationType, Location> locations = new HashMap<>();

		public MechSpecBuilder withTons(final float tons)
		{
			this.maxTons = tons;
			return this;
		}

		public MechSpecBuilder withEngineSinks(final int engineSinks)
		{
			this.engineSinks = engineSinks;
			return this;
		}

		public MechSpecBuilder withExternalHeatSinks(final int externalHeatSinks)
		{
			heatSinks = externalHeatSinks;
			return this;
		}

		public MechSpecBuilder withLocation(final LocationBuilder locationBuilder)
		{
			final Location location = locationBuilder.build();
			locations.put(location.getLocationType(), location);
			return this;
		}

		public MechSpecBuilder withFreeSlots(final int freeSlots)
		{
			this.maxFreeSlots = freeSlots;
			return this;
		}

		public MechSpec build()
		{
			return new MechSpec(this);
		}

		public static MechSpecBuilder mech()
		{
			return new MechSpecBuilder();
		}
	}
}
