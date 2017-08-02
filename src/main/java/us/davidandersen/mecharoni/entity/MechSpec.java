package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.davidandersen.mecharoni.entity.Location.LocationBuilder;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.io.MechPrinter.Node;

public class MechSpec
{
	private final MechSpecData data;

	private final Map<LocationType, Location> locations = new HashMap<>();

	public MechSpec(final MechSpecBuilder mechBuilder)
	{
		data = new MechSpecData();
		data.amsSlots = mechBuilder.amsSlots;
		data.ballisticSlots = mechBuilder.ballisticSlots;
		data.ecmSlots = mechBuilder.ecmSlots;
		data.energySlots = mechBuilder.energySlots;
		data.engineSinks = mechBuilder.engineSinks;
		data.heatSinks = mechBuilder.heatSinks;
		data.missileSlots = mechBuilder.missileSlots;
		data.slots = mechBuilder.slots;
		data.tons = mechBuilder.tons;
		data.components = new ArrayList<>();
		mechBuilder.items.forEach(item -> add(item));
		for (final Location location : mechBuilder.locations)
		{
			locations.put(location.getLocationType(), location);
		}
		for (final Slot slot : mechBuilder.slots2)
		{
			locations.get(slot.getLocationType()).addComponent(slot.getComponent());
		}
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
		return (float)(getInternalHeatSinks() * .2 + getExternalHeatSinks() * .15);
	}

	public float hps()
	{
		return (float)getWeapons().stream().mapToDouble(n -> n.getHps()).sum();
	}

	public float disipation()
	{
		return getDisipation();
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
		return (float)(30 + getInternalHeatSinks() * 2 + getExternalHeatSinks() * 1.5);
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

	public Map<String, Node> combineItems()
	{
		final Map<String, Node> it = new HashMap<>();
		forEach(item -> {
			if (!it.containsKey(item.getFriendlyName()))
			{
				it.put(item.getFriendlyName(), new Node(item, 1));
			}
			else
			{
				final Node n = it.get(item.getFriendlyName());
				n.increment();
			}
		});
		return it;
	}

	public long uniqueWeapons()
	{
		return combineItems().values().stream().filter(n -> n.getItem().isWeapon()).count();
	}

	public float heatEfficiency()
	{
		final float f = disipation() / hps();
		return f;
	}

	public List<Component> componentsInLocation(final LocationType locationType)
	{
		return locations.get(locationType).getComponents();
	}

	public static class MechSpecBuilder
	{
		private EvolveMechConfig config;

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

		private final List<Slot> slots2 = new ArrayList<>();

		private final List<Location> locations = new ArrayList<>();

		public MechSpecBuilder add(final Component item)
		{
			items.add(item);
			return this;
		}

		public MechSpecBuilder withConfig(final EvolveMechConfig config)
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

		public static MechSpecBuilder mech()
		{
			return new MechSpecBuilder();
		}

		public MechSpecBuilder withComponent(final LocationType location, final Component component)
		{
			slots2.add(new Slot(location, component));
			return this;
		}

		public MechSpecBuilder withLocation(final LocationBuilder locationBuilder)
		{
			locations.add(locationBuilder.build());
			return this;
		}
	}
}
