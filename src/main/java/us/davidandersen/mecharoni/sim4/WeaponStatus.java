package us.davidandersen.mecharoni.sim4;

import javax.annotation.processing.Generated;

public class WeaponStatus
{
	private final float damage;

	private final float heat;

	private final float maxCooldown;

	private float cooldown;

	private final int heatPenaltyId;

	private final float minHeatPenaltyLevel;

	private float heatPenaltyCooldown;

	@Generated("SparkTools")
	private WeaponStatus(final WeaponStatusBuilder weaponStatusBuilder)
	{
		this.damage = weaponStatusBuilder.damage;
		this.heat = weaponStatusBuilder.heat;
		this.maxCooldown = weaponStatusBuilder.maxCooldown;
		this.cooldown = weaponStatusBuilder.cooldown;
		this.heatPenaltyId = weaponStatusBuilder.heatPenaltyId;
		this.minHeatPenaltyLevel = weaponStatusBuilder.minHeatPenaltyLevel;
		this.heatPenaltyCooldown = weaponStatusBuilder.heatPenaltyCooldown;
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

	public boolean isOffCooldown()
	{
		return cooldown == 0;
	}

	@Override
	public String toString()
	{
		return "WeaponStatus [damage=" + damage + ", heat=" + heat + ", maxCooldown=" + maxCooldown + ", currentCooldown=" + cooldown +
				", heatPenaltyId=" + heatPenaltyId + ", minHeatPenaltyLevel=" + minHeatPenaltyLevel + "]";
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

	/**
	 * Creates builder to build {@link WeaponStatus}.
	 *
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static WeaponStatusBuilder builder()
	{
		return new WeaponStatusBuilder();
	}

	/**
	 * Builder to build {@link WeaponStatus}.
	 */
	@Generated("SparkTools")
	public static final class WeaponStatusBuilder
	{
		private float damage;

		private float heat;

		private float maxCooldown;

		private float cooldown;

		private int heatPenaltyId;

		private float minHeatPenaltyLevel;

		private float heatPenaltyCooldown;

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

		public WeaponStatusBuilder withMinHeatPenaltyLevel(final float minHeatPenaltyLevel)
		{
			this.minHeatPenaltyLevel = minHeatPenaltyLevel;
			return this;
		}

		public WeaponStatusBuilder withHeatPenaltyCooldown(final float heatPenaltyCooldown)
		{
			this.heatPenaltyCooldown = heatPenaltyCooldown;
			return this;
		}

		public WeaponStatus build()
		{
			return new WeaponStatus(this);
		}
	}
}
