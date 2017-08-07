package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.davidandersen.mecharoni.entity.Location.LocationBuilder;
import us.davidandersen.mecharoni.entity.predicate.MultiWeaponPredicate;
import us.davidandersen.mecharoni.io.MechPrinter.Node;

public class MechSpec
{
	private final Map<LocationType, Location> locations = new HashMap<>();

	private final int maxFreeSlots;

	private final int engineSinks;

	private final int heatSinks;

	private final float maxTons;

	private final List<Slot> components = new ArrayList<>();

	public MechSpec(final MechSpecBuilder mechBuilder)
	{
		assert mechBuilder.maxTons > 0;
		assert mechBuilder.maxFreeSlots > 0;
		assert mechBuilder.locations.size() == 8;

		engineSinks = mechBuilder.engineSinks;
		heatSinks = mechBuilder.heatSinks;
		maxTons = mechBuilder.maxTons;
		maxFreeSlots = mechBuilder.maxFreeSlots;

		for (final Location location : mechBuilder.locations)
		{
			locations.put(location.getLocationType(), location);
		}
		for (final Slot slot : mechBuilder.slots)
		{
			addComponent(slot.getLocationType(), slot.getComponent());
		}
	}

	private void addComponent(final LocationType locationType, final Component component)
	{
		if (tooHeavy(component.getTons())) { return; }

		final Location location = locations.get(locationType);
		final boolean locationhasSlots = locationHasSlots(component, locationType);
		if (!locationhasSlots) { return; }
		if (!hasFreeSlots(component.getSlots())) { return; }
		if (isLocationFull(component, location)) { return; }

		components.add(new Slot(locationType, component));
	}

	private boolean locationHasSlots(final Component component, final LocationType locationType)
	{
		final Location location = locations.get(locationType);
		// 3 - 2 = 1 >= 1
		final boolean locationhasSlots = location.getSlots() - occupiedSlots(locationType) >= component.getSlots();

		return locationhasSlots;
	}

	public List<Component> componentsInLocation(final LocationType locationType)
	{
		return components.stream()
				.filter(s -> s.getLocationType() == locationType)
				.map(s -> s.getComponent())
				.collect(Collectors.toList());
	}

	private boolean hasFreeSlots(final int slots)
	{
		return maxFreeSlots() - occupiedSlots() >= slots;
	}

	public int getMaxSlots()
	{
		return locations.values().stream().mapToInt(loc -> loc.getSlots()).sum();
	}

	private boolean tooHeavy(final float tons)
	{
		final double occupiedTons = occupiedTons() + tons;
		return occupiedTons > maxTons;
	}

	public double totalDamage()
	{
		float damage = 0;
		for (final Component item : getComponents())
		{
			damage += item.getDamage();
		}

		return damage;
	}

	public int getSlots()
	{
		return getComponents().stream().mapToInt(Component::getSlots).sum();
	}

	List<Component> getComponents()
	{
		return components.stream().map(s -> s.getComponent()).collect(Collectors.toList());
	}

	public double occupiedTons()
	{
		return getComponents().stream().mapToDouble(Component::getTons).sum();
	}

	public double totalDps()
	{
		return getComponents().stream().mapToDouble(Component::getDps).sum();
	}

	public double damageOverTime(final float time)
	{
		float dot = 0;
		for (final Component item : getComponents())
		{
			dot += item.getDps() * time;
		}

		return dot;
	}

	public float heatExpended(final float time)
	{
		float dot = 0;
		for (final Component item : getComponents())
		{
			dot += item.getHps() * time;
		}

		return dot;
	}

	public float heatRegained(final float time)
	{
		return getHeatCapacity() + getDisipation() + time;
	}

	float getDisipation()
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
		return (int)(heatSinks + getComponents().stream().filter(item -> item.isHeatSink()).count());
	}

	public int getInternalHeatSinks()
	{
		return engineSinks;
	}

	public float getHeatCapacity()
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
		return (int)getComponents().stream()
				.filter(item -> Objects.equals(item.getType(), b))
				.count();
	}

	public void forEach(final Consumer<Component> action)
	{
		getComponents().forEach(action);
	}

	public List<Component> getWeapons()
	{
		return getComponents().stream()
				.filter(comp -> comp.getDamage() > 0)
				.collect(Collectors.toList());
	}

	public long itemCount(final String string)
	{
		return getComponents().stream().filter(c -> Objects.equals(c.getFriendlyName(), string)).count();
	}

	public long itemCount(final Predicate<Component> predicate)
	{
		return getComponents().stream().filter(predicate).count();
	}

	public List<Component> getAmmo()
	{
		return getComponents().stream()
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

	public int freeSlots()
	{
		return maxFreeSlots - occupiedSlots();
	}

	private int occupiedSlots()
	{
		return getComponents().stream().mapToInt(comp -> comp.getSlots()).sum();
	}

	public int maxHardpoints(final HardpointType hardpointType)
	{
		return locations.values().stream().mapToInt(location -> location.getHardpointCount(hardpointType)).sum();
	}

	public int maxFreeSlots()
	{
		return maxFreeSlots;
	}

	public Collection<Location> getLocations()
	{
		return locations.values();
	}

	public boolean hasItem(final String string)
	{
		return getComponents().stream().anyMatch(c -> c.getFriendlyName().equals(string));
	}

	public float getFirepower()
	{
		return (float)getWeapons().stream().mapToDouble(Component::getDamage).sum();
	}

	public long itemCount(final String... names)
	{
		return itemCount(new MultiWeaponPredicate(names));
	}

	long hardpointsUsed(final HardpointType type, final Location location)
	{
		return componentsInLocation(location.getLocationType()).stream().filter(c -> c.getHardpointType() == type).count();
	}

	private int occupiedSlots(final LocationType locationType)
	{
		return componentsInLocation(locationType).stream().mapToInt(Component::getSlots).sum();
	}

	boolean isLocationFull(final Component component, final Location location)
	{
		final long hardpointsUsed = hardpointsUsed(component.getHardpointType(), location);
		final int hardpointsMax = location.getHardpointCount(component.getHardpointType());

		final boolean isUndefinedHardpoint = component.getHardpointType() == null;
		final boolean hasRoom = hardpointsUsed < hardpointsMax;

		if (!(isUndefinedHardpoint || hasRoom)) { return true; }

		return false;
	}

	public static class MechSpecBuilder
	{
		private int heatSinks;

		private int engineSinks;

		private float maxTons;

		private final List<Slot> slots = new ArrayList<>();

		private final List<Location> locations = new ArrayList<>();

		private int maxFreeSlots;

		public MechSpecBuilder withTons(final float tons)
		{
			this.maxTons = tons;
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

		public MechSpecBuilder withComponent(final LocationType location, final Component component)
		{
			slots.add(new Slot(location, component));
			return this;
		}

		public MechSpecBuilder withLocation(final LocationBuilder locationBuilder)
		{
			locations.add(locationBuilder.build());
			return this;
		}

		public MechSpecBuilder withLocations(final Collection<Location> values)
		{
			locations.addAll(values);
			return this;
		}

		public MechSpecBuilder withFreeSlots(final int freeSlots)
		{
			this.maxFreeSlots = freeSlots;
			return this;
		}

		public MechSpec build()
		{
			return new MechSpec(this);
		}

		public static MechSpecBuilder mech()
		{
			return new MechSpecBuilder();
		}
	}
}
