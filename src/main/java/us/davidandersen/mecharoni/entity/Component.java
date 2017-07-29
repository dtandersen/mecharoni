package us.davidandersen.mecharoni.entity;

public class Component
{
	private final float tons;

	private final int slots;

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

	public Component(final ComponentBuilder itemBuilder)
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

	public int getSlots()
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
		return name.startsWith("Clan") || empty;
	}

	public boolean isHeatSink()
	{
		return heatSink;
	}

	public String getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public static class ComponentBuilder
	{
		public float heat;

		private String type;

		private float tons;

		private int slots;

		private String name;

		private float damage;

		private boolean empty;

		private float cooldown;

		private float duration;

		private double heatCapacity;

		private double heatDisipation;

		private boolean heatSink;

		private String friendlyName;

		private String id;

		public Component build()
		{
			return new Component(this);
		}

		public ComponentBuilder withTons(final float tons)
		{
			this.tons = tons;
			return this;
		}

		public ComponentBuilder withSlots(final int slots)
		{
			this.slots = slots;
			return this;
		}

		public ComponentBuilder withName(final String name)
		{
			this.name = name;
			return this;
		}

		public ComponentBuilder withDamage(final float damage)
		{
			this.damage = damage;
			return this;
		}

		public ComponentBuilder empty()
		{
			empty = true;
			return this;
		}

		public ComponentBuilder withCooldown(final float cooldown)
		{
			this.cooldown = cooldown;
			return this;
		}

		public ComponentBuilder withDuration(final float duration)
		{
			this.duration = duration;
			return this;
		}

		public ComponentBuilder withType(final String type)
		{
			this.type = type;
			return this;
		}

		public ComponentBuilder withHeat(final float heat)
		{
			this.heat = heat;
			return this;
		}

		public ComponentBuilder heatSink()
		{
			this.heatSink = true;
			return this;
		}

		public ComponentBuilder withHeatCapacity(final double heatCapacity)
		{
			this.heatCapacity = heatCapacity;
			return this;
		}

		public ComponentBuilder withDisipation(final double heatDisipation)
		{
			this.heatDisipation = heatDisipation;
			return this;
		}

		public static ComponentBuilder component()
		{
			return new ComponentBuilder();
		}

		public ComponentBuilder withFriendlyName(final String friendlyName)
		{
			this.friendlyName = friendlyName;
			return this;
		}

		public ComponentBuilder withId(final String id)
		{
			this.id = id;
			return this;
		}
	}

	public boolean isEmpty()
	{
		return empty;
	}
}
