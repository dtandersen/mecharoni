package us.davidandersen.mecharoni.sim4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import javax.annotation.processing.Generated;

public class MechStatus
{
	private float heat;

	private final List<WeaponStatus> weapons;

	public int internalHeatSinks;

	public int externalHeatSinks;

	@Generated("SparkTools")
	private MechStatus(final MechStatusBuilder mechStatusBuilder)
	{
		this.heat = mechStatusBuilder.heat;
		this.weapons = mechStatusBuilder.weapons;
		this.internalHeatSinks = mechStatusBuilder.internalHeatSinks;
		this.externalHeatSinks = mechStatusBuilder.externalHeatSinks;
	}

	public MechStatus()
	{
		weapons = new ArrayList<WeaponStatus>();
	}

	public float getHeat()
	{
		return heat;
	}

	public void setHeat(final float heat)
	{
		this.heat = heat;
	}

	public List<WeaponStatus> getWeapons()
	{
		return weapons;
	}

	public void addWeapon(final WeaponStatus weapon)
	{
		weapons.add(weapon);
	}

	public float getHeatDisipation()
	{
		return Heat.dissipation(internalHeatSinks, externalHeatSinks);
	}

	public void fire(final WeaponStatus weapon, final TargetDummy target, final int range)
	{
		heat += weapon.getHeat();
		weapon.fire();
		target.applyDamage(calcDamage(weapon.getDamage(), range, weapon.getOptimalRange(), weapon.getMaxRange(), weapon.getMinRange()));
	}

	private float calcDamage(final float damage, final int range, final int optimalRange, final int maxRange, final int minRange)
	{
		if (range < minRange)
		{
			return 0;
		}
		if (range <= optimalRange)
		{
			return damage;
		}
		if (range > maxRange)
		{
			return 0;
		}

		// 1000 - 500 / 1000 - 500
		final float calculatedDamage = damage * (range - optimalRange) / (maxRange - optimalRange);
		return calculatedDamage;
	}

	public void fire(final int slot, final TargetDummy target, final int range)
	{
		fire(weapons.get(slot), target, range);
	}

	public float getWeaponsGroupCooldown(final int heatPenaltyId)
	{
		float heatCooldown = 0;

		for (final WeaponStatus weapon : weapons)
		{
			if (weapon.hasHeatPenaltyId(heatPenaltyId))
			{
				if (weapon.getHeatCooldown() > heatCooldown)
				{
					heatCooldown = weapon.getHeatCooldown();
				}
			}
		}

		return heatCooldown;
	}

	public void regen(final float time)
	{
		heat -= Heat.dissipation(internalHeatSinks, externalHeatSinks) * time;
		if (heat < 0)
		{
			heat = 0;
		}
		for (final WeaponStatus weapon : weapons)
		{
			weapon.cooldown(time);
		}
	}

	public WeaponStatus getWeapon(final int slot)
	{
		return weapons.get(slot);
	}

	public float getAvailableHeat()
	{
		return Heat.getHeatCapacity(internalHeatSinks, externalHeatSinks) - heat;
	}

	public boolean isWeaponReady(final WeaponStatus weapon)
	{
		final int heatPenaltyId = weapon.getHeatPenaltyId();

		if (getAvailableHeat() < weapon.getHeat())
		{
			return false;
		}
		if (!weapon.isReady())
		{
			return false;
		}
		final int heatPenaltyGroupCooldownCount = getHeatPenaltyGroupCooldownCount(heatPenaltyId);
		final int minHeatPenaltyLevel = weapon.getMinHeatPenaltyLevel();
		if ((heatPenaltyGroupCooldownCount + 1) < minHeatPenaltyLevel)
		{
			return true;
		}

		return getWeaponsGroupCooldown(heatPenaltyId) == 0;
	}

	public boolean isWeaponReady(final int slot)
	{
		return isWeaponReady(weapons.get(slot));
	}

	/**
	 * Creates builder to build {@link MechStatus}.
	 *
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static MechStatusBuilder builder()
	{
		return new MechStatusBuilder();
	}

	public float getHeatPenaltyGroupCooldown(final int heatPenaltyId)
	{
		final OptionalDouble maxCooldown = weapons.stream().filter(w -> w.hasHeatPenaltyId(heatPenaltyId)).mapToDouble(w -> w.getHeatCooldown())
				.max();

		return (float)maxCooldown.orElse(0);
	}

	public int getHeatPenaltyGroupCooldownCount(final int heatPenaltyId)
	{
		final long maxCooldown = weapons.stream()
				.filter(w -> w.hasHeatPenaltyId(heatPenaltyId))
				.filter(w -> w.isHeatPenaltyCooldown())
				.count();

		return (int)maxCooldown;
	}

	/**
	 * Builder to build {@link MechStatus}.
	 */
	@Generated("SparkTools")
	public static final class MechStatusBuilder
	{
		private float heat;

		private List<WeaponStatus> weapons = Collections.emptyList();

		private int internalHeatSinks;

		private int externalHeatSinks;

		private MechStatusBuilder()
		{
		}

		public MechStatusBuilder withHeat(final float heat)
		{
			this.heat = heat;
			return this;
		}

		public MechStatusBuilder withWeapons(final List<WeaponStatus> weapons)
		{
			this.weapons = weapons;
			return this;
		}

		public MechStatusBuilder withInternalHeatSinks(final int internalHeatSinks)
		{
			this.internalHeatSinks = internalHeatSinks;
			return this;
		}

		public MechStatusBuilder withExternalHeatSinks(final int externalHeatSinks)
		{
			this.externalHeatSinks = externalHeatSinks;
			return this;
		}

		public MechStatus build()
		{
			return new MechStatus(this);
		}
	}
}
