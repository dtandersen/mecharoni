package us.davidandersen.mecharoni.entity;

public class QuirkedComponent implements Component
{
	private final Component component;

	private final Quirks quirks;

	public QuirkedComponent(final Component component, final Quirks quirks)
	{
		this.component = component;
		this.quirks = quirks;
	}

	@Override
	public String getFriendlyName()
	{
		return component.getFriendlyName();
	}

	@Override
	public boolean isEmpty()
	{
		return component.isEmpty();
	}

	@Override
	public int getSlots()
	{
		return component.getSlots();
	}

	@Override
	public HardpointType getHardpointType()
	{
		return component.getHardpointType();
	}

	@Override
	public boolean hasFriendlyName(final String friendlyName)
	{
		return component.hasFriendlyName(friendlyName);
	}

	@Override
	public float getTons()
	{
		return component.getTons();
	}

	@Override
	public float getDamage()
	{
		return component.getDamage();
	}

	@Override
	public String getName()
	{
		return component.getName();
	}

	@Override
	public float getHps()
	{
		return component.getHps();
	}

	@Override
	public float getHeat()
	{
		return component.getHeat();
	}

	@Override
	public float getDuration()
	{
		return component.getDuration() * (1 + quirks.get(QuirkType.LASER_DURATION));
	}

	@Override
	public float getCooldown()
	{
		return component.getCooldown();
	}

	@Override
	public String getAmmoType()
	{
		return component.getAmmoType();
	}

	@Override
	public int getDamageMultiplier()
	{
		return component.getDamageMultiplier();
	}

	@Override
	public int getMinRange()
	{
		return component.getMinRange();
	}

	@Override
	public int getLongRange()
	{
		return component.getLongRange();
	}

	@Override
	public int getMaxRange()
	{
		return component.getMaxRange();
	}

	@Override
	public int getNumShots()
	{
		return component.getNumShots();
	}

	@Override
	public float getDps()
	{
		return component.getDps();
	}

	@Override
	public boolean isWeapon()
	{
		return component.isWeapon();
	}

	@Override
	public boolean isBallistic()
	{
		return component.isBallistic();
	}

	@Override
	public boolean isMissile()
	{
		return component.isMissile();
	}

	@Override
	public boolean isEnergy()
	{
		return component.isEnergy();
	}

	@Override
	public String getType()
	{
		return component.getType();
	}

	@Override
	public boolean isHeatSink()
	{
		return component.isHeatSink();
	}
}
