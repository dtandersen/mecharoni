package us.davidandersen.mecharoni.entity;

public class WeaponData
{
	public Spec spec = new Spec();

	public double cooldown;

	public int damageMultiplier;

	static class Spec
	{
		public float damage;

		public float heat;

		public float duration;

		public float cooldown;

		public String ammoType;

		public int minRange;

		public int longRange;

		public int maxRange;
	}
}
