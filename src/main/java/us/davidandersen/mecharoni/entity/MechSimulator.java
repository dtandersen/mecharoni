package us.davidandersen.mecharoni.entity;

import java.util.Collections;
import java.util.Comparator;
import us.davidandersen.mecharoni.entity.Ammo.AmmoBuilder;
import us.davidandersen.mecharoni.entity.Weapon.WeaponBuilder;

public class MechSimulator
{
	private final SimData data = new SimData();

	private final FiringStrategy firingStrategy;

	public MechSimulator(final FiringStrategy firingStrategy)
	{
		this.firingStrategy = firingStrategy;
	}

	public MechSimulator()
	{
		this(new AlwaysFireStrategy());
	}

	public void addMech(final MechSpec mech)
	{
		for (final Component component : mech.getWeapons())
		{
			data.addWeapon(new WeaponBuilder()
					.from(component)
					.build());
		}
		for (final Component component : mech.getAmmo())
		{
			data.addAmmo(new AmmoBuilder()
					.from(component)
					.build());
		}
		data.setInternalSinks(mech.getInternalHeatSinks());
		data.setExternalSinks(mech.getExternalHeatSinks());
	}

	// public void go(final float endTime, final int range)
	// {
	// go(endTime, range, new FireAlwaysStrategy());
	// }

	public void go(final float endTime, final int range)
	{
		double curTime = 0;
		final double delta = .1;
		// if (sort.equals("highdph"))
		// {
		// high hph
		Collections.sort(data.getWeapons(), new Comparator<Weapon>() {
			@Override
			public int compare(final Weapon o1, final Weapon o2)
			{
				if (o1.getDph() == o2.getDph()) { return 0; }
				return new Float(o2.getDph()).compareTo(o1.getDph());
			}
		});
		// }
		// else
		// {
		// // high damage
		// Collections.sort(data.getWeapons(), new Comparator<Weapon>() {
		// @Override
		// public int compare(final Weapon o1, final Weapon o2)
		// {
		// if (o1.getDph() == o2.getDph()) { return 0; }
		// return new Float(o2.getSpecDamage()).compareTo(o1.getSpecDamage());
		// }
		// });
		// }
		// Collections.shuffle(data.getWeapons());
		// for (final Weapon weapon : data.getWeapons())
		// {
		// System.out.println(weapon.getDph());
		//
		// }
		final double totalHeat = data.getWeapons().stream().mapToDouble(Weapon::getSpecHeat).sum();

		while (curTime <= endTime)
		{
			// System.out.println("time: " + curTime + " | heat: " + data.getHeat() + "/" + getHeatCapacity());
			disipateHeat(delta);
			for (final Weapon weapon : data.getWeapons())
			{
				weapon.cooldown(delta);
				// System.out.println("cooldown: " + weapon.getCooldown());
				final boolean fire = firingStrategy.fire(data.getHeat(), getAvailableHeat(), totalHeat);
				final boolean ready = weapon.isReady();
				final boolean b = getAvailableHeat() > weapon.getSpecHeat();
				final boolean hasAmmo = hasAmmo(weapon);
				if (fire && ready && b && hasAmmo)
				{
					weapon.fire();
					if (weapon.isAmmoConsumer())
					{
						expendAmmo(weapon.getAmmoType(), weapon.getDamageMultiplier());
					}
					addDamage(weapon.damageAtRange(range));
					addHeat(weapon.getSpecHeat());
					weapon.cooldown(delta);
				}
			}

			curTime += delta;
		}
	}

	private void expendAmmo(final String ammoType, final int damageMultiplier)
	{
		final Ammo ammo = data.getAmmo().get(ammoType);
		ammo.decrement(damageMultiplier);
	}

	private boolean hasAmmo(final Weapon weapon)
	{
		if (!weapon.isAmmoConsumer()) { return true; }

		final Ammo ammo = data.getAmmo().get(weapon.getAmmoType());
		if (ammo == null) { return false; }

		return weapon.getDamageMultiplier() <= ammo.getNumShots();
	}

	private void disipateHeat(final double delta)
	{
		double heat = data.getHeat() - getHeatDisipation() * delta;
		if (heat < 0)
		{
			heat = 0;
		}
		data.setHeat(heat);
	}

	private float getHeatDisipation()
	{
		return (float)(data.getInternalHeatSinks() * .2 + data.getExternalHeatSinks() * .15);
	}

	private double getAvailableHeat()
	{
		return getHeatCapacity() - data.getHeat();
	}

	private float getHeatCapacity()
	{
		return (float)(30 + data.getInternalHeatSinks() * 2 + data.getExternalHeatSinks() * 1.5);
	}

	private void addHeat(final float specHeat)
	{
		data.setHeat(data.getHeat() + specHeat);
	}

	private void addDamage(final float specDamage)
	{
		data.setDamage(data.getDamage() + specDamage);
	}

	public float damage()
	{
		return data.getDamage();
	}
}
