package us.davidandersen.mecharoni.sim4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import javax.annotation.processing.Generated;

public class Mech
{
	private float heat;

	private final List<MechWeapon> weapons;

	public int internalHeatSinks;

	public int externalHeatSinks;

	private AmmoStore ammoStore;

	@Generated("SparkTools")
	private Mech(final MechStatusBuilder mechStatusBuilder)
	{
		this.heat = mechStatusBuilder.heat;
		this.weapons = mechStatusBuilder.weapons;
		this.internalHeatSinks = mechStatusBuilder.internalHeatSinks;
		this.externalHeatSinks = mechStatusBuilder.externalHeatSinks;
		this.ammoStore = new AmmoStore(mechStatusBuilder.ammo);
	}

	public Mech()
	{
		weapons = new ArrayList<MechWeapon>();
	}

	public float getHeat()
	{
		return heat;
	}

	public void setHeat(final float heat)
	{
		this.heat = heat;
	}

	public List<MechWeapon> getWeapons()
	{
		return weapons;
	}

	public void addWeapon(final MechWeapon weapon)
	{
		weapons.add(weapon);
	}

	public float getHeatDisipation()
	{
		return Heat.dissipation(internalHeatSinks, externalHeatSinks);
	}

	public void fire(final MechWeapon weapon, final TargetDummy target, final int range)
	{
		if (weapon.usesAmmo())
		{
			ammoStore.consumeAmmo(weapon);
		}

		heat += weapon.getHeat();
		weapon.fire();
		final float damage = calcDamage(weapon.getDamage(), range, weapon.getOptimalRange(), weapon.getMaxRange(), weapon.getMinRange());
		target.applyDamage(damage);
	}

	public static float calcDamage(final float damage, final int range, final int optimalRange, final int maxRange, final int minRange)
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
		final float maxMinusOpt = maxRange - optimalRange;
		final float rangeMinusOpt = range - optimalRange;
		final float mult = 1 - (rangeMinusOpt / maxMinusOpt);
		final float calculatedDamage = damage * mult;
		return calculatedDamage;
	}

	public void fire(final int slot, final TargetDummy target, final int range)
	{
		fire(weapons.get(slot), target, range);
	}

	public float getWeaponsGroupCooldown(final int heatPenaltyId)
	{
		float heatCooldown = 0;

		for (final MechWeapon weapon : weapons)
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
		for (final MechWeapon weapon : weapons)
		{
			weapon.cooldown(time);
		}
	}

	public MechWeapon getWeapon(final int slot)
	{
		return weapons.get(slot);
	}

	public float getAvailableHeat()
	{
		return Heat.getHeatCapacity(internalHeatSinks, externalHeatSinks) - heat;
	}

	public boolean isWeaponReady(final MechWeapon weapon)
	{
		final int heatPenaltyId = weapon.getHeatPenaltyId();

		if (weapon.usesAmmo())
		{
			if (!ammoStore.hasAmmo(weapon.getAmmoType()))
			{
				return false;
			}
			final int numShots = ammoStore.getAmmo(weapon.getAmmoType());
			if (numShots <= 0)
			{
				return false;
			}
		}

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
	 * Creates builder to build {@link Mech}.
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

	public int hasAmmo(final String ammoType)
	{
		if (!ammoStore.hasAmmo(ammoType))
		{
			return 0;
		}

		return ammoStore.getAmmo(ammoType);
	}

	public void ammo(final String ammoType, final int numShots)
	{
		ammoStore.setAmmo(ammoType, numShots);
	}

	/**
	 * Builder to build {@link Mech}.
	 */
	@Generated("SparkTools")
	public static final class MechStatusBuilder
	{
		public Map<String, Integer> ammo = new HashMap<>();

		private float heat;

		private List<MechWeapon> weapons = new ArrayList<>();

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

		public MechStatusBuilder withWeapons(final List<MechWeapon> weapons)
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

		public MechStatusBuilder withComponents(final List<MechComponent> components)
		{
			for (final MechComponent c : components)
			{
				if (c instanceof MechWeapon)
				{
					weapons.add((MechWeapon)c);
				}
				else if (c instanceof MechAmmo)
				{
					final MechAmmo a = (MechAmmo)c;
					if (ammo.containsKey(a.getName()))
					{
						Integer qty = ammo.get(a.getName());
						qty += a.getNumShots();
						ammo.put(a.getName(), qty);
					}
					else
					{
						ammo.put(a.getName(), a.getNumShots());
					}
				}
			}

			return this;
		}

		public Mech build()
		{
			return new Mech(this);
		}
	}
}
