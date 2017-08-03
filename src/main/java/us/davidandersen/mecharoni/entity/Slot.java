package us.davidandersen.mecharoni.entity;

public class Slot
{
	private final LocationType location;

	private final Component component;

	public Slot(final LocationType location, final Component component)
	{
		this.location = location;
		this.component = component;
	}

	public LocationType getLocationType()
	{
		return location;
	}

	public Component getComponent()
	{
		return component;
	}

	@Override
	public String toString()
	{
		return location.toString() + ":" + component.getFriendlyName();
	}
}
