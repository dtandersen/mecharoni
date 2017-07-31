package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimData
{
	private final List<Weapon> weapons = new ArrayList<>();

	private float damage;

	private double heat;

	private int internalHeatSinks;

	private int externalHeatSinks;

	private final Map<String, Ammo> ammunition = new HashMap<>();

	public float getDamage()
	{
		return damage;
	}

	public void addWeapon(final Weapon weapon)
	{
		weapons.add(weapon);
	}

	public List<Weapon> getWeapons()
	{
		return weapons;
	}

	public void setDamage(final float damage)
	{
		this.damage = damage;
	}

	public double getHeat()
	{
		return heat;
	}

	public void setHeat(final double heat)
	{
		this.heat = heat;
	}

	public void setInternalSinks(final int internalHeatSinks)
	{
		this.internalHeatSinks = internalHeatSinks;
	}

	public void setExternalSinks(final int externalHeatSinks)
	{
		this.externalHeatSinks = externalHeatSinks;
	}

	public int getInternalHeatSinks()
	{
		return internalHeatSinks;
	}

	public int getExternalHeatSinks()
	{
		return externalHeatSinks;
	}

	public void addAmmo(final Ammo ammo)
	{
		if (!ammunition.containsKey(ammo.getType()))
		{
			ammunition.put(ammo.getType(), ammo);
		}
		else
		{
			final Ammo ammo2 = ammunition.get(ammo.getType());
			ammo2.increment(ammo.getNumShots());
		}
	}

	public Map<String, Ammo> getAmmo()
	{
		return ammunition;
	}
}
