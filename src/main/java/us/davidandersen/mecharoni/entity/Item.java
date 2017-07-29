package us.davidandersen.mecharoni.entity;

public class Item
{
	private final float tons;

	private final float slots;

	private final String name;

	private final float damage;

	private final boolean empty;

	private final float cooldown;

	private final float duration;

	private final float dps;

	public final String type;

	private final float hps;

	private final float heat;

	private final boolean heatSink;

	// private final double heatCapacity;

	// private final double heatDisipation;

	public Item(final ItemBuilder itemBuilder)
	{
		tons = itemBuilder.tons;
		slots = itemBuilder.slots;
		name = itemBuilder.name;
		damage = itemBuilder.damage;
		empty = itemBuilder.empty;
		cooldown = itemBuilder.cooldown;
		duration = itemBuilder.duration;
		type = itemBuilder.type;
		heat = itemBuilder.heat;
		heatSink = itemBuilder.heatSink;
		// heatCapacity = itemBuilder.heatCapacity;
		// heatDisipation = itemBuilder.heatDisipation;
		if (cooldown + duration == 0)
		{
			dps = 0;
			hps = 0;
		}
		else
		{
			dps = damage / (cooldown + duration);
			hps = heat / (cooldown + duration);
		}
	}

	public final float getTons()
	{
		return tons;
	}

	public float getSlots()
	{
		return slots;
	}

	public float getDps()
	{
		return dps;
	}

	public float getHps()
	{
		return hps;
	}

	@Override
	public String toString()
	{
		// return name + "[tons=" + tons + ", slots=" + slots + "]";
		return name;
	}

	public float getDamage()
	{
		return damage;
	}

	public boolean isClan()
	{
		return name.startsWith("Clan") || empty || heatSink;
	}

	public String getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public static class ItemBuilder
	{
		public float heat;

		private String type;

		private float slots;

		private float tons;

		private String name;

		private float damage;

		private boolean empty;

		private float cooldown;

		private float duration;

		private double heatCapacity;

		private double heatDisipation;

		private boolean heatSink;

		public Item build()
		{
			return new Item(this);
		}

		public ItemBuilder withTons(final float tons)
		{
			this.tons = tons;
			return this;
		}

		public ItemBuilder withSlots(final float slots)
		{
			this.slots = slots;
			return this;
		}

		public ItemBuilder withName(final String name)
		{
			this.name = name;
			return this;
		}

		public ItemBuilder withDamage(final float damage)
		{
			this.damage = damage;
			return this;
		}

		public ItemBuilder empty()
		{
			empty = true;
			return this;
		}

		public ItemBuilder withCooldown(final float cooldown)
		{
			this.cooldown = cooldown;
			return this;
		}

		public ItemBuilder withDuration(final float duration)
		{
			this.duration = duration;
			return this;
		}

		public ItemBuilder withType(final String type)
		{
			this.type = type;
			return this;
		}

		public ItemBuilder withHeat(final float heat)
		{
			this.heat = heat;
			return this;
		}

		public ItemBuilder heatSink()
		{
			this.heatSink = true;
			return this;
		}

		public ItemBuilder withHeatCapacity(final double heatCapacity)
		{
			this.heatCapacity = heatCapacity;
			return this;
		}

		public ItemBuilder withDisipation(final double heatDisipation)
		{
			this.heatDisipation = heatDisipation;
			return this;
		}
	}

	public boolean isHeatSink()
	{
		return heatSink;
	}
}
