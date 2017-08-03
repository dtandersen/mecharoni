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

	private final int slots;

	public Location(final LocationBuilder locationBuilder)
	{
		locationType = locationBuilder.locationType;
		hardpoints = locationBuilder.hardpoints;
		slots = locationBuilder.slots;
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
		if (component.getHardpointType() == null)
		{
			components.add(component);
		}
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

	public int hardpointsMax(final HardpointType type)
	{
		if (!hardpoints.containsKey(type)) { return 0; }

		return hardpoints.get(type);
	}

	public int maxSlots()
	{
		return slots;
	}

	public boolean hasFreeSlots(final int slots)
	{
		// 3 - 2 = 1 >= 1
		return maxSlots() - occupiedSlots() >= slots;
	}

	private int occupiedSlots()
	{
		return components.stream().mapToInt(Component::getSlots).sum();
	}

	public static class LocationBuilder
	{
		private int slots;

		private final Map<HardpointType, Integer> hardpoints = new HashMap<>();

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
			hardpoints.put(HardpointType.ENERGY, energy);
			return this;
		}

		public LocationBuilder withBallistics(final int ballistic)
		{
			hardpoints.put(HardpointType.BALLISTIC, ballistic);
			return this;
		}

		public LocationBuilder withLocationType(final LocationType locationType)
		{
			this.locationType = locationType;
			return this;
		}

		public LocationBuilder withMissile(final int missile)
		{
			hardpoints.put(HardpointType.MISSILE, missile);
			return this;
		}

		public LocationBuilder withAms(final int ams)
		{
			hardpoints.put(HardpointType.AMS, ams);
			return this;
		}

		public LocationBuilder withEcm(final int ecm)
		{
			hardpoints.put(HardpointType.ECM, ecm);
			return this;
		}
	}
}
