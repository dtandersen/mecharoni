package us.davidandersen.mecharoni.entity;

public class Ammo
{
	private final String type;

	private int num_shots;

	public Ammo(final AmmoBuilder ammoBuilder)
	{
		num_shots = ammoBuilder.num_shots;
		type = ammoBuilder.type;
	}

	static class AmmoBuilder
	{
		private String type;

		private int num_shots;

		public AmmoBuilder from(final Component component)
		{
			num_shots = component.getNumShots();
			type = component.getName();
			return this;
		}

		public Ammo build()
		{
			return new Ammo(this);
		}
	}

	public String getType()
	{
		return type;
	}

	public int getNumShots()
	{
		return num_shots;
	}

	public void increment(final int numShots)
	{
		this.num_shots += numShots;
	}

	public void decrement(final int damageMultiplier)
	{
		this.num_shots -= damageMultiplier;
	}
}
