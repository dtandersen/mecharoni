package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import us.davidandersen.mecharoni.entity.MechSpec.MechSpecBuilder;
import us.davidandersen.mecharoni.entity.predicate.MultiWeaponPredicate;
import us.davidandersen.mecharoni.io.MechPrinter.Node;

public class MechBuild
{
	private final MechSpec mechSpec;

	private final Slots slots;

	public MechBuild(final MechBuildBuilder mechBuilder)
	{
		mechSpec = mechBuilder.mechSpecBuilder.build();
		slots = new Slots();
		assert mechSpec.getMaxTons() > 0;
		assert mechSpec.getMaxFreeSlots() > 0;
		assert mechSpec.getLocations().size() == 8;

		for (final Slot slot : mechBuilder.slots)
		{
			addComponent(slot.getLocationType(), slot.getComponent());
		}
	}

	public List<Component> componentsInLocation(final LocationType locationType)
	{
		return slots.componentsByLocation(locationType);
	}

	public int getMaxSlots()
	{
		return mechSpec.getLocations().values().stream().mapToInt(loc -> loc.getSlots()).sum();
	}

	public int getOccupiedSlots()
	{
		return slots.occupiedSlots();
	}

	public double occupiedTons()
	{
		return slots.occupiedTons();
	}

	public float heatExpended(final float time)
	{
		return (float)slots.sum(component -> component.getHps() * time);
	}

	public float heatRegained(final float time)
	{
		return getHeatCapacity() + getDisipation() + time;
	}

	public float getDisipation()
	{
		return (float)(getInternalHeatSinks() * .2 + getExternalHeatSinks() * .15);
	}

	public float hps()
	{
		return (float)getWeapons().stream().mapToDouble(n -> n.getHps()).sum();
	}

	public int getExternalHeatSinks()
	{
		return (int)(mechSpec.getHeatSinks() + slots.count(item -> item.isHeatSink()));
	}

	public int getInternalHeatSinks()
	{
		return mechSpec.getEngineSinks();
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

	public List<Component> getWeapons()
	{
		return slots.filter(component -> component.getDamage() > 0);
	}

	public long componentCountByFriendlyName(final String friendlyName)
	{
		return slots.count(component -> component.hasFriendlyName(friendlyName));
	}

	public long componentCount(final Predicate<Component> predicate)
	{
		return slots.count(predicate);
	}

	public List<Component> getAmmo()
	{
		return slots.filter(comp -> comp.getName().contains("Ammo"));
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
		final float f = getDisipation() / hps();
		return f;
	}

	public int freeSlots()
	{
		return mechSpec.getMaxFreeSlots() - occupiedSlots();
	}

	private int occupiedSlots()
	{
		return slots.intSum(Component::getSlots);
	}

	public int maxHardpoints(final HardpointType hardpointType)
	{
		return mechSpec.getLocations().values().stream().mapToInt(location -> location.getHardpointCount(hardpointType)).sum();
	}

	public int maxFreeSlots()
	{
		return mechSpec.getMaxFreeSlots();
	}

	public Collection<Location> getLocations()
	{
		return mechSpec.getLocations().values();
	}

	public boolean hasItem(final String friendlyName)
	{
		return slots.anyMatch(component -> component.hasFriendlyName(friendlyName));
	}

	public float getFirepower()
	{
		return (float)getWeapons().stream().mapToDouble(Component::getDamage).sum();
	}

	public long itemCount(final String... names)
	{
		return componentCount(new MultiWeaponPredicate(names));
	}

	private void forEach(final Consumer<Component> action)
	{
		slots.forEach(action);
	}

	private void addComponent(final LocationType locationType, final Component component)
	{
		if (!isValid(locationType, component)) { return; }

		slots.addComponent(locationType, component);
	}

	private boolean isValid(final LocationType locationType, final Component component)
	{
		boolean valid = true;
		final ComponentValidator validator;
		if (tooHeavy(component.getTons()))
		{
			valid = false;
		}
		final Location location = mechSpec.getLocations().get(locationType);
		final boolean locationhasSlots = locationHasSlots(component, locationType);
		if (!locationhasSlots)
		{
			valid = false;
		}
		if (!hasFreeSlots(component.getSlots()))
		{
			valid = false;
		}
		if (isLocationFull(component, location))
		{
			valid = false;
		}
		return valid;
	}

	private boolean locationHasSlots(final Component component, final LocationType locationType)
	{
		final Location location = mechSpec.getLocations().get(locationType);
		// 3 - 2 = 1 >= 1
		final boolean locationhasSlots = location.getSlots() - occupiedSlots(locationType) >= component.getSlots();

		return locationhasSlots;
	}

	private boolean hasFreeSlots(final int slots)
	{
		return maxFreeSlots() - occupiedSlots() >= slots;
	}

	private boolean tooHeavy(final float tons)
	{
		final double occupiedTons = slots.occupiedTons() + tons;
		return occupiedTons > mechSpec.getMaxTons();
	}

	private int typeCount(final String type)
	{
		final Predicate<? super Component> predicate = component -> Objects.equals(component.getType(), type);
		return (int)slots.count(predicate);
	}

	private long hardpointsUsed(final HardpointType type, final Location location)
	{
		return componentsInLocation(location.getLocationType()).stream().filter(c -> c.getHardpointType() == type).count();
	}

	private int occupiedSlots(final LocationType locationType)
	{
		final List<Component> componentsInLocation = componentsInLocation(locationType);
		return componentsInLocation.stream().mapToInt(Component::getSlots).sum();
	}

	private boolean isLocationFull(final Component component, final Location location)
	{
		final long hardpointsUsed = hardpointsUsed(component.getHardpointType(), location);
		final int hardpointsMax = location.getHardpointCount(component.getHardpointType());

		final boolean isUndefinedHardpoint = component.getHardpointType() == null;
		final boolean hasRoom = hardpointsUsed < hardpointsMax;

		if (!(isUndefinedHardpoint || hasRoom)) { return true; }

		return false;
	}

	public static class MechBuildBuilder
	{
		private MechSpecBuilder mechSpecBuilder = new MechSpecBuilder();

		private final List<Slot> slots = new ArrayList<>();

		public MechBuildBuilder withSpec(final MechSpecBuilder mechSpecBuilder)
		{
			this.mechSpecBuilder = mechSpecBuilder;
			return this;
		}

		public MechBuildBuilder withComponent(final LocationType location, final Component component)
		{
			slots.add(new Slot(location, component));
			return this;
		}

		public MechBuild build()
		{
			return new MechBuild(this);
		}

		public static MechBuildBuilder mech()
		{
			return new MechBuildBuilder();
		}
	}
}
