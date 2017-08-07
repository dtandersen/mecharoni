package us.davidandersen.mecharoni.entity;

public class ComponentValidator
{
	private final MechBuild mech;

	public ComponentValidator(final MechBuild mech)
	{
		this.mech = mech;
	}

	boolean isValid(final LocationType locationType, final Component component)
	{
		if (tooHeavy(component.getTons())) { return false; }
		if (!hasFreeSlots(component.getSlots())) { return false; }

		final Location location = mech.getLocation(locationType);
		if (!locationHasSlots(component, location)) { return false; }
		if (isLocationFull(component, location)) { return false; }

		return true;
	}

	private boolean tooHeavy(final float tons)
	{
		final double occupiedTons = mech.occupiedTons() + tons;
		return occupiedTons > mech.getMaxTons();
	}

	private boolean locationHasSlots(final Component component, final Location location)
	{
		// 3 - 2 = 1 >= 1
		final boolean locationhasSlots = location.getSlots() - mech.occupiedSlots(location.getLocationType()) >= component.getSlots();

		return locationhasSlots;
	}

	private boolean hasFreeSlots(final int slots)
	{
		return mech.maxFreeSlots() - mech.occupiedSlots() >= slots;
	}

	private boolean isLocationFull(final Component component, final Location location)
	{
		final long hardpointsUsed = mech.hardpointsUsed(component.getHardpointType(), location);
		final int hardpointsMax = location.getHardpointCount(component.getHardpointType());

		final boolean isUndefinedHardpoint = component.getHardpointType() == null;
		final boolean hasRoom = hardpointsUsed < hardpointsMax;

		if (!(isUndefinedHardpoint || hasRoom)) { return true; }

		return false;
	}
}
