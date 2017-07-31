package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.davidandersen.mecharoni.evolve.EvolveMech.MechSpecYaml;

public class MechSpec
{
	private final MechSpecData data;

	public MechSpec(final MechSpecBuilder analyzerBuilder)
	{
		data = new MechSpecData();
		data.amsSlots = analyzerBuilder.amsSlots;
		data.ballisticSlots = analyzerBuilder.ballisticSlots;
		data.ecmSlots = analyzerBuilder.ecmSlots;
		data.energySlots = analyzerBuilder.energySlots;
		data.engineSinks = analyzerBuilder.engineSinks;
		data.heatSinks = analyzerBuilder.heatSinks;
		data.missileSlots = analyzerBuilder.missileSlots;
		data.slots = analyzerBuilder.slots;
		data.tons = analyzerBuilder.tons;
		data.components = new ArrayList<>();
		analyzerBuilder.items.forEach(item -> add(item));
	}

	public void add(final Component item)
	{
		if (isValid(item))
		{
			data.components.add(item);
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
		return getSlots() + slots > data.slots;
	}

	private boolean tooHeavy(final float tons)
	{
		return getTons() + tons > data.tons;
	}

	private boolean slotsFull(final String type)
	{
		if (type == null) { return false; }
		return typeCount(type) >= typeSlots(type);
	}

	private int typeSlots(final String type)
	{
		switch (type)
		{
		case "BEAM":
			return data.energySlots;
		case "MISSLE":
			return data.missileSlots;
		case "BALLISTIC":
			return data.ballisticSlots;
		case "AMS":
			return data.amsSlots;
		case "ECM":
			return data.ecmSlots;
		}

		throw new RuntimeException();
	}

	public double totalDamage()
	{
		float damage = 0;
		for (final Component item : data.components)
		{
			damage += item.getDamage();
		}

		return damage;
	}

	public double getSlots()
	{
		return data.components.stream().mapToDouble(Component::getSlots).sum();
	}

	public double getTons()
	{
		return data.components.stream().mapToDouble(Component::getTons).sum();
	}

	public double totalDps()
	{
		return data.components.stream().mapToDouble(Component::getDps).sum();
	}

	public double damageOverTime(final float time)
	{
		float dot = 0;
		for (final Component item : data.components)
		{
			dot += item.getDps() * time;
		}

		return dot;
	}

	public float heatExpended(final float time)
	{
		float dot = 0;
		for (final Component item : data.components)
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
		return (int)(data.heatSinks + data.components.stream().filter(item -> item.isHeatSink()).count());
	}

	public int getInternalHeatSinks()
	{
		return data.engineSinks;
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
		return (int)data.components.stream()
				.filter(item -> Objects.equals(item.getType(), b))
				.count();
	}

	public void forEach(final Consumer<Component> action)
	{
		data.components.forEach(action);
	}

	public List<Component> getWeapons()
	{
		return data.components.stream()
				.filter(comp -> comp.getDamage() > 0)
				.collect(Collectors.toList());
	}

	public long itemCount(final String string)
	{
		return data.components.stream().filter(c -> Objects.equals(c.getFriendlyName(), string)).count();
	}

	public long itemCount(final Predicate<Component> predicate)
	{
		return data.components.stream().filter(predicate).count();
	}

	public List<Component> getAmmo()
	{
		return data.components.stream()
				.filter(comp -> comp.getName().contains("Ammo"))
				.collect(Collectors.toList());
	}

	public static class MechSpecBuilder
	{
		private MechSpecYaml config;

		private final List<Component> items = new ArrayList<>();

		private int heatSinks;

		private int engineSinks;

		private float tons;

		private int slots;

		private int energySlots;

		private int ballisticSlots;

		private int missileSlots;

		private int ecmSlots;

		private int amsSlots;

		public MechSpecBuilder add(final Component item)
		{
			items.add(item);
			return this;
		}

		public MechSpecBuilder withConfig(final MechSpecYaml config)
		{
			heatSinks = config.heatSinks;
			engineSinks = config.engineSinks;
			tons = config.tons;
			slots = config.slots;
			energySlots = config.energySlots;
			ballisticSlots = config.ballisticSlots;
			missileSlots = config.missileSlots;
			ecmSlots = config.ecmSlots;
			amsSlots = config.amsSlots;
			this.config = config;
			return this;
		}

		public MechSpecBuilder withComponent(final Component component)
		{
			items.add(component);
			return this;
		}

		public MechSpec build()
		{
			return new MechSpec(this);
		}

		public MechSpecBuilder withSlots(final int slots)
		{
			this.slots = slots;
			return this;
		}

		public MechSpecBuilder withTons(final int tons)
		{
			this.tons = tons;
			return this;
		}

		public MechSpecBuilder withEnergySlots(final int energySlots)
		{
			this.energySlots = energySlots;
			return this;
		}

		public MechSpecBuilder withEngineSinks(final int engineSinks)
		{
			this.engineSinks = engineSinks;
			return this;
		}

		public MechSpecBuilder withExternalHeatSinks(final int externalHeatSinks)
		{
			heatSinks = externalHeatSinks;
			return this;
		}

		public MechSpecBuilder withMissileSlots(final int missileSlots)
		{
			this.missileSlots = missileSlots;
			return this;
		}
	}
}
