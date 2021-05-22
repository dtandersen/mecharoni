package us.davidandersen.mecharoni.sim4;

import java.util.List;

public class CombatSimulator
{
	private final MechStatus mech;

	final static float TICK_TIME = 1 / 30f;

	public CombatSimulator(final MechStatus mech)
	{
		this.mech = mech;
	}

	public void tick()
	{
		mech.dissipateHeat(TICK_TIME);

		for (final WeaponStatus weapon : mech.getWeapons())
		{
			if (mech.canFire(weapon))
			{
				mech.fire(weapon);
			}
		}
	}

	// public void addMech(final MechBuild mechBuild)
	// {
	// final List<WeaponStatus> weapons = new ArrayList<>();
	//
	// for (final Component weapon : mechBuild.getWeapons())
	// {
	// weapons.add(WeaponStatus.builder()
	// .withDamage(weapon.getDamage())
	// .withHeat(weapon.getHeat())
	// .withCooldown(weapon.getCooldown())
	// .build());
	// }
	//
	// mech = MechStatus.builder()
	// .withInternalHeatSinks(mechBuild.getInternalHeatSinks())
	// .withExternalHeatSinks(mechBuild.getExternalHeatSinks())
	// .withWeapons(weapons)
	// .build();
	//
	// }

	public MechStatus getStatus()
	{
		return mech;
	}

	public List<WeaponStatus> getWeapons()
	{
		return mech.getWeapons();
	}
}
