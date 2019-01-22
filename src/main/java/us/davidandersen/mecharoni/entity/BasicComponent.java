package us.davidandersen.mecharoni.entity;

import java.util.Objects;

public class BasicComponent implements Component
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

	public BasicComponent(final ComponentBuilder itemBuilder)
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

	@Override
	public final float getTons()
	{
		return tons;
	}

	@Override
	public int getSlots()
	{
		return slots;
	}

	@Override
	public float getDps()
	{
		return dps;
	}

	@Override
	public float getHps()
	{
		return hps;
	}

	@Override
	public String toString()
	{
		return friendlyName;
	}

	@Override
	public float getDamage()
	{
		return damage;
	}

	@Override
	public boolean isClan()
	{
		return name.startsWith("Clan") || empty;
	}

	@Override
	public boolean isInnerSphere()
	{
		return !name.startsWith("Clan") || empty;
	}

	@Override
	public boolean isHeatSink()
	{
		return heatSink;
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public HardpointType getHardpointType()
	{
		return HardpointType.fromType(type);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
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

		private boolean heatSink;

		private String friendlyName;

		private String id;

		private int damageMultiplier;

		private int min_range;

		private int long_range;

		private int max_range;

		public BasicComponent build()
		{
			return new BasicComponent(this);
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

	@Override
	public float getHeat()
	{
		return heat;
	}

	@Override
	public float getDuration()
	{
		return duration;
	}

	@Override
	public float getCooldown()
	{
		return cooldown;
	}

	@Override
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

	@Override
	public int getNumShots()
	{
		return num_shots;
	}

	@Override
	public int getDamageMultiplier()
	{
		return damageMultiplier;
	}

	@Override
	public String getFriendlyName()
	{
		return friendlyName;
	}

	@Override
	public int getMinRange()
	{
		return minRange;
	}

	@Override
	public int getLongRange()
	{
		return longRange;
	}

	@Override
	public int getMaxRange()
	{
		return maxRange;
	}

	@Override
	public boolean isWeapon()
	{
		return getDamage() > 0;
	}

	@Override
	public boolean isEnergy()
	{
		return "BEAM".equals(type);
	}

	@Override
	public boolean isMissile()
	{
		return "MISSLE".equals(type);
	}

	@Override
	public boolean isBallistic()
	{
		return "BALLISTIC".equals(type);
	}

	public float getDph()
	{
		return damage / heat;
	}

	@Override
	public boolean hasFriendlyName(final String friendlyName)
	{
		return Objects.equals(this.friendlyName, friendlyName);
	}
}
