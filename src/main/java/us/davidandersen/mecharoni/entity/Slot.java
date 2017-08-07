package us.davidandersen.mecharoni.entity;

public class Slot
{
	private final LocationType location;

	private final BasicComponent component;

	public Slot(final LocationType location, final BasicComponent component)
	{
		this.location = location;
		this.component = component;
	}

	public LocationType getLocationType()
	{
		return location;
	}

	public BasicComponent getComponent()
	{
		return component;
	}

	@Override
	public String toString()
	{
		return location.toString() + ":" + component.getFriendlyName();
	}
}
