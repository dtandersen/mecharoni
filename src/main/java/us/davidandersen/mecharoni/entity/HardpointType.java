package us.davidandersen.mecharoni.entity;

import java.util.Objects;

public enum HardpointType
{
	ENERGY("BEAM"),
	MISSILE("MISSLE"),
	BALLISTIC("BALLISTIC"),
	AMS("AMS"),
	ECM("ECM");

	private final String type;

	HardpointType(final String type)
	{
		this.type = type;
	}

	public static HardpointType fromType(final String type)
	{
		for (final HardpointType hardpointType : HardpointType.values())
		{
			if (Objects.equals(hardpointType.type, type)) { return hardpointType; }
		}

		return null;
	}
}
