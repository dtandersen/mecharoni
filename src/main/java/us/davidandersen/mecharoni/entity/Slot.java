package us.davidandersen.mecharoni.entity;

public class Slot
{
	private final LocationType location;

	private final Component component;

	private final int linkedGroup;

	@Deprecated
	public Slot(final LocationType location, final Component component)
	{
		this.location = location;
		this.component = component;
		linkedGroup = 0;
	}

	public Slot(final SlotBuilder slotBuilder)
	{
		this.location = slotBuilder.location;
		this.component = slotBuilder.component;
		linkedGroup = slotBuilder.linkedGroup;
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

	public boolean hasLocation(final LocationType locationType)
	{
		return this.location == locationType;
	}

	public boolean hasLinkedGroup(final int linkedGroup)
	{
		return this.linkedGroup == linkedGroup;
	}

	@Override
	public String toString()
	{
		return location.toString() + ":" + component.getFriendlyName() + " [" + linkedGroup + "]";
	}

	public static class SlotBuilder
	{
		private int linkedGroup;

		private Component component;

		private LocationType location;

		public SlotBuilder withLocation(final LocationType location)
		{
			this.location = location;
			return this;
		}

		public SlotBuilder withComponent(final Component component)
		{
			this.component = component;
			return this;
		}

		public SlotBuilder withLinkedGroup(final int linkedGroup)
		{
			this.linkedGroup = linkedGroup;
			return this;
		}

		Slot build()
		{
			return new Slot(this);
		}

		public static SlotBuilder slot()
		{
			return new SlotBuilder();
		}
	}
}
