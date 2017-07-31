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

	private final String type;

	private final float hps;

	private final float heat;

	private final boolean heatSink;

	private String ammoType;

	private final String id;

	private final int num_shots;

	private final int damageMultiplier;

	private final String friendlyName;

	private final int minRange;

	private final int longRange;

	private final int maxRange;

	public Component(final ComponentBuilder itemBuilder)
	{
		id = itemBuilder.id;
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
		num_shots = itemBuilder.num_shots;
		damageMultiplier = itemBuilder.damageMultiplier;
		friendlyName = itemBuilder.friendlyName;
		minRange = itemBuilder.min_range;
		longRange = itemBuilder.long_range;
		maxRange = itemBuilder.max_range;
		// heatCapacity = itemBuilder.heatCapacity;
		// heatCapacity = itemBuilder.heatCapacity;
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

	public boolean isInnerSphere()
	{
		return !name.startsWith("Clan") || empty;
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

	public boolean isEmpty()
	{
		return empty;
	}

	public static class ComponentBuilder
	{
		public int num_shots;

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

		private int damageMultiplier;

		private int min_range;

		private int long_range;

		private int max_range;

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

		public ComponentBuilder withNumShots(final int num_shots)
		{
			this.num_shots = num_shots;
			return this;
		}

		public ComponentBuilder withDamageMultiplier(final int damageMultiplier)
		{
			this.damageMultiplier = damageMultiplier;
			return this;
		}

		public ComponentBuilder withMinRange(final int min_range)
		{
			this.min_range = min_range;
			return this;
		}

		public ComponentBuilder withLongRange(final int long_range)
		{
			this.long_range = long_range;
			return this;
		}

		public ComponentBuilder withMaxRange(final int max_range)
		{
			this.max_range = max_range;
			return this;
		}
	}

	public float getHeat()
	{
		return heat;
	}

	public float getDuration()
	{
		return duration;
	}

	public float getCooldown()
	{
		return cooldown;
	}

	public String getAmmoType()
	{
		return ammoType;
	}

	public String getId()
	{
		return id;
	}

	public void setAmmoType(final String ammoType)
	{
		this.ammoType = ammoType;
	}

	public int getNumShots()
	{
		return num_shots;
	}

	public int getDamageMultiplier()
	{
		return damageMultiplier;
	}

	public String getFriendlyName()
	{
		return friendlyName;
	}

	public int getMinRange()
	{
		return minRange;
	}

	public int getLongRange()
	{
		return longRange;
	}

	public int getMaxRange()
	{
		return maxRange;
	}
}
