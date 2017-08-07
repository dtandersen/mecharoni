package us.davidandersen.mecharoni.entity;

public class QuirkContext
{
	private Component component;

	private float duration = 0;

	private float cooldown;

	public boolean isEnergy()
	{
		return component.isEnergy();
	}

	public void incrementDuration(final Quirk quirk)
	{
		duration += quirk.getValue();
	}

	public void incrementCooldown(final Quirk quirk)
	{
		cooldown += quirk.getValue();
	}
}
