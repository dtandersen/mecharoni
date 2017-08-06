package us.davidandersen.mecharoni.entity;

import java.util.List;

public class MechSim2
{
	private MechSpec mech;

	private float total = 0;

	public void addMech(final MechSpec mech)
	{
		this.mech = mech;
	}

	public void run(final float time)
	{
		final float capacity = mech.getHeatCapacity();
		final float disipation = mech.getDisipation();
		final float heatAvailableToMech = 2 * (capacity + disipation * time);
		final List<Component> weapons = mech.getWeapons();
		final float totalHps = (float)weapons.stream().filter(c -> c.isWeapon()).mapToDouble(Component::getHps).sum();
		for (final Component weapon : weapons)
		{
			final float hps = weapon.getHps();
			if (hps > 0)
			{
				final float share = hps / totalHps;
				final float heatAvailableToWeapon = heatAvailableToMech * share;
				final float maxTime = heatAvailableToWeapon / hps;
				// final float t = Math.min(time, maxTime);
				final float t = maxTime;
				final float damage = weapon.getDps() * t;

				total += damage;
			}
			else
			{
				total += weapon.getDps() * time;
			}
		}
	}

	public float getScore()
	{
		return total;
	}
}
