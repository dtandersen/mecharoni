package us.davidandersen.mecharoni.entity;

public class Weapon
{
	private final WeaponData data;

	public Weapon(final WeaponBuilder weaponBuilder)
	{
		data = new WeaponData();
		data.spec.damage = weaponBuilder.damage;
		data.spec.heat = weaponBuilder.heat;
		data.spec.duration = weaponBuilder.duration;
		data.spec.cooldown = weaponBuilder.cooldown;
		data.spec.ammoType = weaponBuilder.ammoType;
		data.damageMultiplier = weaponBuilder.damageMultiplier;
		data.spec.minRange = weaponBuilder.minRange;
		data.spec.longRange = weaponBuilder.longRange;
		data.spec.maxRange = weaponBuilder.maxRange;
	}

	static class WeaponBuilder
	{
		public String ammoType;

		private float damage;

		private float heat;

		private float duration;

		private float cooldown;

		public int damageMultiplier;

		private int minRange;

		private int longRange;

		private int maxRange;

		public Weapon build()
		{
			return new Weapon(this);
		}

		public WeaponBuilder from(final Component comp)
		{
			damage = comp.getDamage();
			heat = comp.getHeat();
			duration = comp.getDuration();
			cooldown = comp.getCooldown();
			ammoType = comp.getAmmoType();
			damageMultiplier = comp.getDamageMultiplier();
			minRange = comp.getMinRange();
			longRange = comp.getLongRange();
			maxRange = comp.getMaxRange();
			return this;
		}
	}

	public float getSpecDamage()
	{
		return data.spec.damage;
	}

	public boolean isOnCooldown()
	{
		return data.cooldown > 0;
	}

	public boolean isReady()
	{
		return data.cooldown == 0;
	}

	public void fire()
	{
		data.cooldown = data.spec.cooldown + data.spec.duration;
	}

	public void cooldown(final double delta)
	{
		if (isOnCooldown())
		{
			data.cooldown -= delta;
			if (data.cooldown < 0)
			{
				data.cooldown = 0;
			}
		}
	}

	public double getCooldown()
	{
		return data.cooldown;
	}

	public float getSpecHeat()
	{
		return data.spec.heat;
	}

	public float getDph()
	{
		if (data.spec.heat == 0) { return 0; }
		return data.spec.damage / data.spec.heat;
	}

	public boolean isAmmoConsumer()
	{
		return data.spec.ammoType != null;
	}

	public String getAmmoType()
	{
		return data.spec.ammoType;
	}

	public int getDamageMultiplier()
	{
		return data.damageMultiplier;
	}

	public float damageAtRange(final int range)
	{
		if (range <= data.spec.minRange) { return 0; }
		if (range <= data.spec.longRange) { return data.spec.damage; }
		if (range >= data.spec.maxRange) { return 0; }

		final float i = 1 - (range - data.spec.longRange) / (float)(data.spec.maxRange - data.spec.longRange);
		final float f = data.spec.damage * i;
		return f;
	}
}
