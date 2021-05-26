package us.davidandersen.mecharoni.sim4;

import javax.annotation.processing.Generated;

public class MechWeapon extends MechComponent
{
	private final String name;

	private final float damage;

	private final float heat;

	private final float maxCooldown;

	private float cooldown;

	private final int heatPenaltyId;

	private final int minHeatPenaltyLevel;

	private float heatPenaltyCooldown;

	private final int optimalRange;

	private final int maxRange;

	private final int minRange;

	private final String ammoType;

	private final int ammoPerShot;

	@Generated("SparkTools")
	private MechWeapon(final WeaponStatusBuilder weaponStatusBuilder)
	{
		this.name = weaponStatusBuilder.name;
		this.damage = weaponStatusBuilder.damage;
		this.heat = weaponStatusBuilder.heat;
		this.maxCooldown = weaponStatusBuilder.maxCooldown;
		this.cooldown = weaponStatusBuilder.cooldown;
		this.heatPenaltyId = weaponStatusBuilder.heatPenaltyId;
		this.minHeatPenaltyLevel = weaponStatusBuilder.minHeatPenaltyLevel;
		this.heatPenaltyCooldown = weaponStatusBuilder.heatPenaltyCooldown;
		this.optimalRange = weaponStatusBuilder.optimalRange;
		this.maxRange = weaponStatusBuilder.maxRange;
		this.minRange = weaponStatusBuilder.minRange;
		this.ammoType = weaponStatusBuilder.ammoType;
		this.ammoPerShot = weaponStatusBuilder.ammoPerShot;
	}

	public String getName()
	{
		return name;
	}

	public float getHeat()
	{
		return heat;
	}

	public float getCooldown()
	{
		return cooldown;
	}

	public boolean hasHeatPenaltyId(final int heatPenaltyId)
	{
		return this.heatPenaltyId == heatPenaltyId;
	}

	public void fire()
	{
		cooldown = maxCooldown;
		heatPenaltyCooldown = 0.5f;
	}

	@Override
	public String toString()
	{
		return "MechWeapon [name=" + name + ", damage=" + damage + ", heat=" + heat + ", maxCooldown=" + maxCooldown + ", cooldown=" + cooldown +
				", heatPenaltyId=" + heatPenaltyId + ", minHeatPenaltyLevel=" + minHeatPenaltyLevel + ", heatPenaltyCooldown=" + heatPenaltyCooldown +
				", optimalRange=" + optimalRange + ", maxRange=" + maxRange + ", minRange=" + minRange + ", ammoType=" + ammoType + ", ammoPerShot=" +
				ammoPerShot + "]";
	}

	public float getHeatCooldown()
	{
		return heatPenaltyCooldown;
	}

	public void cooldown(final float time)
	{
		cooldown -= time;
		if (cooldown < 0)
		{
			cooldown = 0;
		}
		heatPenaltyCooldown -= time;
		if (heatPenaltyCooldown < 0)
		{
			heatPenaltyCooldown = 0;
		}
	}

	public boolean isReady()
	{
		return heatPenaltyCooldown == 0 && cooldown == 0;
	}

	public float getDamage()
	{
		return damage;
	}

	public int getHeatPenaltyId()
	{
		return heatPenaltyId;
	}

	public boolean isHeatPenaltyCooldown()
	{
		return heatPenaltyCooldown > 0;
	}

	public int getMinHeatPenaltyLevel()
	{
		return minHeatPenaltyLevel;
	}

	public int getOptimalRange()
	{
		return optimalRange;
	}

	public int getMaxRange()
	{
		return maxRange;
	}

	public int getMinRange()
	{
		return minRange;
	}

	public String getAmmoType()
	{
		return ammoType;
	}

	public int getAmmoPerShot()
	{
		return ammoPerShot;
	}

	boolean usesAmmo()
	{
		boolean usesAmmo = getAmmoType() != null && !getAmmoType().isEmpty();
		return usesAmmo;
	}

	/**
	 * Creates builder to build {@link MechWeapon}.
	 *
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static WeaponStatusBuilder builder()
	{
		return new WeaponStatusBuilder();
	}

	/**
	 * Builder to build {@link MechWeapon}.
	 */
	@Generated("SparkTools")
	public static final class WeaponStatusBuilder
	{
		public String name;

		private int ammoPerShot;

		private String ammoType;

		private float damage;

		private float heat;

		private float maxCooldown;

		private float cooldown;

		private int heatPenaltyId;

		private int minHeatPenaltyLevel;

		private float heatPenaltyCooldown;

		private int optimalRange;

		private int maxRange;

		private int minRange;

		private WeaponStatusBuilder()
		{
		}

		public WeaponStatusBuilder withDamage(final float damage)
		{
			this.damage = damage;
			return this;
		}

		public WeaponStatusBuilder withHeat(final float heat)
		{
			this.heat = heat;
			return this;
		}

		public WeaponStatusBuilder withMaxCooldown(final float maxCooldown)
		{
			this.maxCooldown = maxCooldown;
			return this;
		}

		public WeaponStatusBuilder withCooldown(final float cooldown)
		{
			this.cooldown = cooldown;
			return this;
		}

		public WeaponStatusBuilder withHeatPenaltyId(final int heatPenaltyId)
		{
			this.heatPenaltyId = heatPenaltyId;
			return this;
		}

		public WeaponStatusBuilder withMinHeatPenaltyLevel(final int minHeatPenaltyLevel)
		{
			this.minHeatPenaltyLevel = minHeatPenaltyLevel;
			return this;
		}

		public WeaponStatusBuilder withHeatPenaltyCooldown(final float heatPenaltyCooldown)
		{
			this.heatPenaltyCooldown = heatPenaltyCooldown;
			return this;
		}

		public WeaponStatusBuilder withOptimalRange(final int optimalRange)
		{
			this.optimalRange = optimalRange;
			return this;
		}

		public WeaponStatusBuilder withMaxRange(final int maxRange)
		{
			this.maxRange = maxRange;
			return this;
		}

		public WeaponStatusBuilder withMinRange(final int minRange)
		{
			this.minRange = minRange;
			return this;
		}

		public MechWeapon build()
		{
			return new MechWeapon(this);
		}

		public WeaponStatusBuilder withAmmoType(final String ammoType)
		{
			this.ammoType = ammoType;
			return this;
		}

		public WeaponStatusBuilder withAmmoPerShot(final int ammoPerShot)
		{
			this.ammoPerShot = ammoPerShot;
			return this;
		}

		public WeaponStatusBuilder withName(final String name)
		{
			this.name = name;
			return this;
		}
	}
}
