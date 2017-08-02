package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location
{
	private final List<Component> components = new ArrayList<>();

	private final LocationType locationType;

	private final Map<HardpointType, Integer> hardpoints;

	public Location(final LocationBuilder locationBuilder)
	{
		locationType = locationBuilder.locationType;
		hardpoints = locationBuilder.hardpoints;
	}

	public LocationType getLocationType()
	{
		return locationType;
	}

	public List<Component> getComponents()
	{
		return components;
	}

	public void addComponent(final Component component)
	{
		final long hardpointsUsed = hardpointsUsed(component.getHardpointType());
		final int hardpointsMax = hardpointsMax(component.getHardpointType());

		if (hardpointsUsed < hardpointsMax)
		{
			components.add(component);
		}
	}

	private long hardpointsUsed(final HardpointType type)
	{
		return components.stream().filter(c -> c.getHardpointType() == type).count();
	}

	private int hardpointsMax(final HardpointType type)
	{
		return hardpoints.get(type);
	}

	public static class LocationBuilder
	{
		private int slots;

		// private int energy;

		Map<HardpointType, Integer> hardpoints = new HashMap<>();

		private LocationType locationType;

		public Location build()
		{
			return new Location(this);
		}

		public static LocationBuilder location()
		{
			return new LocationBuilder();
		}

		public LocationBuilder withSlots(final int slots)
		{
			this.slots = slots;
			return this;
		}

		public LocationBuilder withEnergy(final int energy)
		{
			// this.energy = energy;
			hardpoints.put(HardpointType.ENERGY, energy);
			return this;
		}

		public LocationBuilder withLocationType(final LocationType locationType)
		{
			this.locationType = locationType;
			return this;
		}
	}
}
