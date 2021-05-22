package us.davidandersen.mecharoni.sim4;

public class TargetDummy
{
	private float damage;

	public float getDamage()
	{
		return damage;
	}

	public void applyDamage(final float damage)
	{
		this.damage += damage;
	}
}
