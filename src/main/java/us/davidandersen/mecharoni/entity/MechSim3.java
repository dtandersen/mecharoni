package us.davidandersen.mecharoni.entity;

import java.util.List;
import us.davidandersen.mecharoni.entity.Weapon.WeaponBuilder;

public class MechSim3
{
	private MechBuild mech;

	private float damage = 0;

	private float heat;

	private float heatAvailableToMech;

	private float heatOver;

	public void addMech(final MechBuild mech)
	{
		this.mech = mech;
	}

	public void run(final float time, final int range)
	{
		final float capacity = mech.getHeatCapacity();
		final float disipation = mech.getHeatDisipation();
		heatAvailableToMech = capacity + disipation * time;
		final List<? extends Component> weapons = mech.getWeapons();
		// final float totalHps = (float)weapons.stream().filter(c -> c.isWeapon()).mapToDouble(Component::getHps).sum();
		for (final Component weapon : weapons)
		{
			final float hps = weapon.getHps();
			if (hps > 0)
			{
				// final float share = hps / totalHps;
				// final float heatAvailableToWeapon = heatAvailableToMech * share;
				// final float maxTime = heatAvailableToWeapon / hps;
				// // final float t = Math.min(time, maxTime);
				// final float t = maxTime;
				// final float damage = weapon.getDps() * t;
				final Weapon weapon2 = new WeaponBuilder().from(weapon).build();
				damage += weapon2.damageAtRange(range) * time;
				heat += weapon.getHps() * time;
			}
			else
			{
				damage += weapon.getDps() * time;
			}
		}

		this.heatOver = heat - heatAvailableToMech * 2;
	}

	public float getDamage()
	{
		return damage;
	}

	public float getHeatOver()
	{
		return heatOver;
	}

	public float getDph()
	{
		return damage / heat;
	}
}
