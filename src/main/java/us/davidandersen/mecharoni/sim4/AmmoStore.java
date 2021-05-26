package us.davidandersen.mecharoni.sim4;

import java.util.Map;

public class AmmoStore
{
	private final Map<String, Integer> ammo;

	public AmmoStore(final Map<String, Integer> ammo)
	{
		this.ammo = ammo;
	}

	void consumeAmmo(final MechWeapon weapon)
	{
		int ammoQty = ammo.get(weapon.getAmmoType());
		ammoQty -= weapon.getAmmoPerShot();
		ammo.put(weapon.getAmmoType(), ammoQty);
	}

	void setAmmo(final String ammoType, final int numShots)
	{
		ammo.put(ammoType, numShots);
	}

	boolean hasAmmo(final String ammoType)
	{
		return ammo.containsKey(ammoType);
	}

	Integer getAmmo(final String ammoType)
	{
		return ammo.get(ammoType);
	}
}
