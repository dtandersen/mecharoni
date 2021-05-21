package us.davidandersen.mecharoni.sim4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

	boolean canFire(final WeaponStatus weapon)
	{
		return weapon.isOffCooldown();
	}

	public float getHeatDisipation()
	{
		return Heat.dissipation(internalHeatSinks, externalHeatSinks);
	}

	public void fire(final WeaponStatus weapon)
	{
		heat += weapon.getHeat();
		weapon.fire();
	}

	public void fire(final int slot)
	{
		fire(weapons.get(slot));
	}

	public void dissipateHeat(final float time)
	{
		heat -= getHeatDisipation() * time;

		if (heat < 0)
		{
			heat = 0;
		}
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

	public void regen()
	{
		heat -= Heat.dissipation(internalHeatSinks, externalHeatSinks) * 1 / 30f;
		if (heat < 0)
		{
			heat = 0;
		}
		for (final WeaponStatus weapon : weapons)
		{
			weapon.cooldown(1 / 30f);
		}
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
