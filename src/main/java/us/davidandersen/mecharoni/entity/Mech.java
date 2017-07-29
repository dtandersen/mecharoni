package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import us.davidandersen.mecharoni.evolve.EvolveMech.MechSpecYaml;

public class Mech
{
	private final List<Component> items;

	private final MechSpecYaml config;

	public Mech(final MechBuilder analyzerBuilder)
	{
		this.config = analyzerBuilder.config;
		// this.items = analyzerBuilder.items;
		items = new ArrayList<>();
		analyzerBuilder.items.forEach(item -> add(item));
	}

	public void add(final Component item)
	{
		// System.out.println(item.getName() + item.getType());
		if (isValid(item))
		{
			items.add(item);
		}
	}

	private boolean isValid(final Component item)
	{
		if (slotsFull(item.getType())) { return false; }
		if (tooHeavy(item.getTons())) { return false; }
		if (notEnoughSlots(item.getSlots())) { return false; }

		return true;
	}

	private boolean notEnoughSlots(final float slots)
	{
		return getSlots() + slots > config.slots;
	}

	private boolean tooHeavy(final float tons)
	{
		return getTons() + tons > config.tons;
	}

	private boolean slotsFull(final String type)
	{
		if (type == null) { return false; }
		return typeCount(type) >= config.typeSlots(type);
	}

	public double totalDamage()
	{
		float damage = 0;
		for (final Component item : items)
		{
			damage += item.getDamage();
		}

		return damage;
	}

	public double getSlots()
	{
		return items.stream().mapToDouble(Component::getSlots).sum();
	}

	public double getTons()
	{
		return items.stream().mapToDouble(Component::getTons).sum();
	}

	public double totalDps()
	{
		return items.stream().mapToDouble(Component::getDps).sum();
	}

	public double damageOverTime(final float time)
	{
		// final float heat = getHeatCapacity() + getDisipation() * time;
		float dot = 0;
		for (final Component item : items)
		{
			dot += item.getDps() * time;
		}

		return dot;
	}

	public float heatExpended(final float time)
	{
		float dot = 0;
		for (final Component item : items)
		{
			dot += item.getHps() * time;
		}

		return dot;
	}

	public float heatRegained(final float time)
	{
		return getHeatCapacity() + getDisipation() + time;
	}

	private float getDisipation()
	{
		return (float)(getInternalHeatSinks() * 2 + getExternalHeatSinks() * 1.5);
	}

	public int getExternalHeatSinks()
	{
		return (int)(config.heatSinks + items.stream().filter(item -> item.isHeatSink()).count());
	}

	public int getInternalHeatSinks()
	{
		return config.engineSinks;
	}

	private float getHeatCapacity()
	{
		return (float)(30 + getInternalHeatSinks() * .2 + getExternalHeatSinks() * .15);
	}

	public int getEnergySlots()
	{
		return typeCount("BEAM");
	}

	public int getBallisticSlots()
	{
		return typeCount("BALLISTIC");
	}

	public int getMissileSlots()
	{
		return typeCount("MISSLE");
	}

	public int getEcmSlots()
	{
		return typeCount("ECM");
	}

	public int getAmsSlots()
	{
		return typeCount("AMS");
	}

	private int typeCount(final String b)
	{
		return (int)items.stream()
				.filter(item -> Objects.equals(item.type, b))
				.count();
	}

	public void forEach(final Consumer<Component> action)
	{
		items.forEach(action);
	}

	public static class MechBuilder
	{
		private MechSpecYaml config;

		private final List<Component> items = new ArrayList<>();

		public MechBuilder add(final Component item)
		{
			items.add(item);
			return this;
		}

		public MechBuilder withConfig(final MechSpecYaml config)
		{
			this.config = config;
			return this;
		}

		public Mech build()
		{
			return new Mech(this);
		}
	}
}
