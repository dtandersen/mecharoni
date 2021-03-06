package us.davidandersen.mecharoni.entity;

import static us.davidandersen.mecharoni.entity.Location.LocationBuilder.location;
import us.davidandersen.mecharoni.entity.MechSpec.MechSpecBuilder;

public class PrefabMechs
{

	public static MechSpecBuilder genericMech()
	{
		return MechSpecBuilder.mech()
				.withTons(11)
				.withFreeSlots(10)
				.withLocation(location()
						.withLocationType(LocationType.HEAD)
						.withSlots(7)
						.withBallistics(1))
				.withLocation(location()
						.withLocationType(LocationType.CT)
						.withSlots(7)
						.withBallistics(1))
				.withLocation(location()
						.withLocationType(LocationType.RT)
						.withSlots(7)
						.withBallistics(1))
				.withLocation(location()
						.withLocationType(LocationType.LT)
						.withSlots(7)
						.withEnergy(1))
				.withLocation(location()
						.withLocationType(LocationType.RA)
						.withSlots(7)
						.withBallistics(1))
				.withLocation(location()
						.withLocationType(LocationType.LA)
						.withSlots(7)
						.withBallistics(1))
				.withLocation(location()
						.withLocationType(LocationType.RL)
						.withSlots(7)
						.withBallistics(1))
				.withLocation(location()
						.withLocationType(LocationType.LL)
						.withSlots(7)
						.withBallistics(1));
	}

	public static MechSpecBuilder cyclopsBuilder()
	{
		return MechSpecBuilder.mech()
				.withTons(40)
				.withFreeSlots(20)
				.withLocation(location()
						.withLocationType(LocationType.HEAD)
						.withSlots(1)
						.withEnergy(1))
				.withLocation(location()
						.withLocationType(LocationType.CT)
						.withSlots(2)
						.withMissile(1))
				.withLocation(location()
						.withLocationType(LocationType.RT)
						.withSlots(9)
						.withBallistics(2))
				.withLocation(location()
						.withLocationType(LocationType.LT)
						.withSlots(9)
						.withBallistics(2)
						.withAms(1))
				.withLocation(location()
						.withLocationType(LocationType.RA)
						.withSlots(8)
						.withEnergy(2))
				.withLocation(location()
						.withLocationType(LocationType.LA)
						.withSlots(8)
						.withEnergy(1))
				.withLocation(location()
						.withLocationType(LocationType.RL)
						.withSlots(2))
				.withLocation(location()
						.withLocationType(LocationType.LL)
						.withSlots(2));
	}

	public static MechSpecBuilder locustBuilder()
	{
		return MechSpecBuilder.mech()
				.withTons(9.5f)
				.withFreeSlots(17)
				.withLocation(location()
						.withLocationType(LocationType.HEAD)
						.withSlots(1))
				.withLocation(location()
						.withLocationType(LocationType.CT)
						.withSlots(2))
				.withLocation(location()
						.withLocationType(LocationType.RT)
						.withSlots(9)
						.withAms(1))
				.withLocation(location()
						.withLocationType(LocationType.LT)
						.withSlots(9))
				.withLocation(location()
						.withLocationType(LocationType.RA)
						.withSlots(10)
						.withEnergy(3))
				.withLocation(location()
						.withLocationType(LocationType.LA)
						.withSlots(10)
						.withEnergy(3))
				.withLocation(location()
						.withLocationType(LocationType.RL)
						.withSlots(2))
				.withLocation(location()
						.withLocationType(LocationType.LL)
						.withSlots(2));
	}

	public static MechSpecBuilder boarsHead()
	{
		return MechSpecBuilder.mech()
				.withTons(42)
				.withFreeSlots(27)
				.withEngineSinks(10)
				.withLocation(location()
						.withLocationType(LocationType.HEAD)
						.withSlots(1))
				.withLocation(location()
						.withLocationType(LocationType.CT)
						.withSlots(2))
				.withLocation(location()
						.withLocationType(LocationType.RT)
						.withSlots(9)
						.withBallistics(1))
				.withLocation(location()
						.withLocationType(LocationType.LT)
						.withSlots(9)
						.withMissile(1))
				.withLocation(location()
						.withLocationType(LocationType.RA)
						.withSlots(8)
						.withEnergy(3))
				.withLocation(location()
						.withLocationType(LocationType.LA)
						.withSlots(8)
						.withEnergy(3)
						.withAms(1))
				.withLocation(location()
						.withLocationType(LocationType.RL)
						.withSlots(2))
				.withLocation(location()
						.withLocationType(LocationType.LL)
						.withSlots(2));
	}

}
