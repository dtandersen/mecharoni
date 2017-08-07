package us.davidandersen.mecharoni.entity;

import java.util.HashMap;
import java.util.Map;

public class Location
{
	private final LocationType locationType;

	private final Map<HardpointType, Integer> hardpointCount;

	private final int slots;

	public Location(final LocationBuilder locationBuilder)
	{
		locationType = locationBuilder.locationType;
		hardpointCount = locationBuilder.hardpointCount;
		slots = locationBuilder.slots;
	}

	public LocationType getLocationType()
	{
		return locationType;
	}

	public int getHardpointCount(final HardpointType type)
	{
		if (!hardpointCount.containsKey(type)) { return 0; }

		return hardpointCount.get(type);
	}

	public int getSlots()
	{
		return slots;
	}

	public static class LocationBuilder
	{
		private int slots;

		private final Map<HardpointType, Integer> hardpointCount = new HashMap<>();

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
			hardpointCount.put(HardpointType.ENERGY, energy);
			return this;
		}

		public LocationBuilder withBallistics(final int ballistic)
		{
			hardpointCount.put(HardpointType.BALLISTIC, ballistic);
			return this;
		}

		public LocationBuilder withLocationType(final LocationType locationType)
		{
			this.locationType = locationType;
			return this;
		}

		public LocationBuilder withMissile(final int missile)
		{
			hardpointCount.put(HardpointType.MISSILE, missile);
			return this;
		}

		public LocationBuilder withAms(final int ams)
		{
			hardpointCount.put(HardpointType.AMS, ams);
			return this;
		}

		public LocationBuilder withEcm(final int ecm)
		{
			hardpointCount.put(HardpointType.ECM, ecm);
			return this;
		}
	}
}
