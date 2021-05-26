package us.davidandersen.mecharoni.entity;

public class QuirkedComponent implements Component
{
	private final Component component;

	private final QuirkContext quirkContext;

	public QuirkedComponent(final Component component, final Quirks quirks)
	{
		this.component = component;
		quirkContext = new QuirkContext();
		quirks.apply(component, quirkContext);
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
		return component.getHeat() * (1 + quirkContext.getHeat());
	}

	@Override
	public float getDuration()
	{
		return component.getDuration() * (1 + quirkContext.getDuration());
	}

	@Override
	public float getCooldown()
	{
		return component.getCooldown() * (1 + quirkContext.getCooldown());
	}

	@Override
	public String getAmmoType()
	{
		return component.getAmmoType();
	}

	@Override
	public int getNumFiring()
	{
		return component.getNumFiring();
	}

	@Override
	public int getAmmoPerShot()
	{
		return component.getAmmoPerShot();
	}

	@Override
	public int getMinRange()
	{
		return (int)(component.getMinRange() * (1 + quirkContext.getRange()));
	}

	@Override
	public int getLongRange()
	{
		return (int)(component.getLongRange() * (1 + quirkContext.getRange()));
	}

	@Override
	public int getMaxRange()
	{
		return (int)(component.getMaxRange() * (1 + quirkContext.getRange()));
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

	@Override
	public boolean isClan()
	{
		return component.isClan();
	}

	@Override
	public boolean isInnerSphere()
	{
		return component.isInnerSphere();
	}

	@Override
	public int getMinHeatPenaltyLevel()
	{
		return component.getMinHeatPenaltyLevel();
	}

	@Override
	public float getHeatPenalty()
	{
		return component.getHeatPenalty();
	}

	@Override
	public int getHeatPenaltyId()
	{
		return component.getHeatPenaltyId();
	}
}
