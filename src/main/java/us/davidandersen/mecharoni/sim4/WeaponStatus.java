package us.davidandersen.mecharoni.sim4;

import javax.annotation.processing.Generated;

public class WeaponStatus
{
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
		this.optimalRange = weaponStatusBuilder.optimalRange;
		this.maxRange = weaponStatusBuilder.maxRange;
		this.minRange = weaponStatusBuilder.minRange;
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

		public WeaponStatus build()
		{
			return new WeaponStatus(this);
		}
	}
}
