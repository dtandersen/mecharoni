package us.davidandersen.mecharoni.entity;

public class Slot
{
	private final LocationType location;

	private final Component component;

	private final int linkedGroup;

	public Slot(final LocationType location, final Component component)
	{
		this.location = location;
		this.component = component;
		linkedGroup = 0;
	}

	public Slot(final LocationType location, final Component component, final int linkedGroup)
	{
		this.location = location;
		this.component = component;
		this.linkedGroup = linkedGroup;
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
		return location.toString() + ":" + component.getFriendlyName() + " [" + linkedGroup + "]";
	}
}
