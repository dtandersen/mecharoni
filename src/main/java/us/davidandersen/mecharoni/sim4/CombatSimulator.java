package us.davidandersen.mecharoni.sim4;

import java.util.List;

public class CombatSimulator
{
	private final Mech mech;

	private final TargetDummy target;

	private float time;

	final static float TICK_TIME = 1 / 30f;

	private final int range;

	public CombatSimulator(final Mech mech, final int range)
	{
		this.mech = mech;
		this.range = range;
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

		for (final MechWeapon weapon : mech.getWeapons())
		{
			if (mech.isWeaponReady(weapon))
			{
				// if (Objects.equals(weapon.getName(), "MRM40"))
				// {
				// System.out.println("fires " + weapon.getName());
				// }
				mech.fire(weapon, target, range);
			}
		}
	}

	public Mech getStatus()
	{
		return mech;
	}

	public List<MechWeapon> getWeapons()
	{
		return mech.getWeapons();
	}

	public TargetDummy getTarget()
	{
		return target;
	}
}
