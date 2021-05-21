package us.davidandersen.mecharoni.sim4;

import java.util.ArrayList;
import java.util.List;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.MechBuild;

public class CombatSimulator
{
	private MechBuild mech;

	private final MechStatus status;

	private final List<WeaponStatus> weaponStatuses;

	private final float TICK_TIME = 1 / 30f;

	public CombatSimulator()
	{
		status = new MechStatus();
		weaponStatuses = new ArrayList<WeaponStatus>();
	}

	public void tick()
	{
		float heat = status.getHeat();
		heat -= mech.getHeatDisipation() * TICK_TIME;
		if (heat < 0)
		{
			heat = 0;
		}

		for (final WeaponStatus weapon : weaponStatuses)
		{
			if (weapon.isOffCooldown())
			{
				heat += weapon.getHeat();
				weapon.fire();
			}
		}

		status.setHeat(heat);
	}

	public void addMech(final MechBuild mech)
	{
		this.mech = mech;
		for (final Component weapon : mech.getWeapons())
		{
			weaponStatuses.add(WeaponStatus.builder()
					.withDamage(weapon.getDamage())
					.withHeat(weapon.getHeat())
					.withCooldown(weapon.getCooldown())
					.build());
		}
	}

	public MechStatus getStatus()
	{
		return status;
	}

	public List<WeaponStatus> getWeapons()
	{
		return weaponStatuses;
	}
}
