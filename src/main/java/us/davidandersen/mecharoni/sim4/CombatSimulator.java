package us.davidandersen.mecharoni.sim4;

import java.util.List;

public class CombatSimulator
{
	private final MechStatus mech;

	private final TargetDummy target;

	private float time;

	final static float TICK_TIME = 1 / 30f;

	public CombatSimulator(final MechStatus mech)
	{
		this.mech = mech;
		this.target = new TargetDummy();
	}

	public void run(final float endTime)
	{
		while (time < endTime)
		{
			tick();
			time += TICK_TIME;
		}
	}

	public void tick()
	{
		mech.regen(TICK_TIME);

		for (final WeaponStatus weapon : mech.getWeapons())
		{
			if (mech.isWeaponReady(weapon))
			{
				mech.fire(weapon, target);
			}
		}
	}

	public MechStatus getStatus()
	{
		return mech;
	}

	public List<WeaponStatus> getWeapons()
	{
		return mech.getWeapons();
	}

	public TargetDummy getTarget()
	{
		return target;
	}
}
