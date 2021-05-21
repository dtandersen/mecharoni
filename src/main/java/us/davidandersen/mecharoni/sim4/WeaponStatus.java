package us.davidandersen.mecharoni.sim4;

import javax.annotation.processing.Generated;

public class WeaponStatus
{
	private final float damage;

	private final float heat;

	private final float maxCooldown;

	private float currentCooldown;

	@Generated("SparkTools")
	private WeaponStatus(final WeaponStatusBuilder weaponStatusBuilder)
	{
		this.damage = weaponStatusBuilder.damage;
		this.heat = weaponStatusBuilder.heat;
		this.maxCooldown = weaponStatusBuilder.cooldown;
	}

	public float getHeat()
	{
		return heat;
	}

	public float getCooldown()
	{
		return maxCooldown;
	}

	public void fire()
	{
		currentCooldown = maxCooldown;
	}

	public boolean isOffCooldown()
	{
		return currentCooldown == 0;
	}

	@Override
	public String toString()
	{
		return "WeaponStatus [damage=" + damage + ", heat=" + getHeat() + ", cooldown=" + maxCooldown + "]";
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

		private float cooldown;

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

		public WeaponStatusBuilder withCooldown(final float cooldown)
		{
			this.cooldown = cooldown;
			return this;
		}

		public WeaponStatus build()
		{
			return new WeaponStatus(this);
		}
	}
}
