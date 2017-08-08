package us.davidandersen.mecharoni.entity;

public class QuirkContext
{
	private float duration = 0;

	private float cooldown;

	private float heat;

	private float range;

	public void incrementDuration(final Quirk quirk)
	{
		duration += quirk.getValue();
	}

	public void incrementCooldown(final Quirk quirk)
	{
		cooldown += quirk.getValue();
	}

	public void incrementHeat(final Quirk quirk)
	{
		heat += quirk.getValue();
	}

	public void incrementRange(final Quirk quirk)
	{
		range += quirk.getValue();
	}

	public float getDuration()
	{
		return duration;
	}

	public float getCooldown()
	{
		return cooldown;
	}

	public float getRange()
	{
		return range;
	}

	public float getHeat()
	{
		return heat;
	}
}
