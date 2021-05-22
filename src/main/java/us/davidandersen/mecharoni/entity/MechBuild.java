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
import us.davidandersen.mecharoni.entity.Quirk.QuirkBuilder;
import us.davidandersen.mecharoni.entity.Slot.SlotBuilder;
import us.davidandersen.mecharoni.entity.predicate.MultiWeaponPredicate;
import us.davidandersen.mecharoni.io.MechPrinter.Node;
import us.davidandersen.mecharoni.sim4.Heat;

public class MechBuild
{
	private final MechSpec mechSpec;

	private final Slots slots;

	private final Quirks quirks;

	private float heat;

	public MechBuild(final MechBuildBuilder mechBuilder)
	{
		mechSpec = mechBuilder.mechSpecBuilder.build();
		slots = new Slots();
		assert mechSpec.getMaxTons() > 0;
		assert mechSpec.getMaxFreeSlots() > 0;
		assert mechSpec.getLocations().size() == 8;

		final ComponentValidator validator = new ComponentValidator(this);
		quirks = new Quirks(mechBuilder.quirks);

		for (final SlotBuilder slotBuilder : mechBuilder.slotBuilders)
		{
			final Slot slot = slotBuilder.build();
			if (!validator.isValid(slot.getLocationType(), slot.getComponent()))
			{
				continue;
			}

			slotBuilder.withComponent(new QuirkedComponent(slot.getComponent(), quirks));
			slots.addComponent(slotBuilder.build());
		}
	}

	public List<? extends Component> componentsInLocation(final LocationType locationType)
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
		return getHeatCapacity() + getHeatDisipation() + time;
	}

	public float getHeatDisipation()
	{
		return Heat.dissipation(getInternalHeatSinks(), getExternalHeatSinks());
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
		return Heat.getHeatCapacity(getInternalHeatSinks(), getExternalHeatSinks());
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

	public List<? extends Component> getWeapons()
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

	public List<? extends Component> getAmmo()
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
		final float f = getHeatDisipation() / hps();
		return f;
	}

	public int freeSlots()
	{
		return mechSpec.getMaxFreeSlots() - occupiedSlots();
	}

	int occupiedSlots()
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

	public double getMaxTons()
	{
		return mechSpec.getMaxTons();
	}

	public Location getLocation(final LocationType locationType)
	{
		return mechSpec.getLocations().get(locationType);
	}

	public Component getWeapon(final String friendlyName)
	{
		return slots.findFirst(component -> component.hasFriendlyName(friendlyName));
	}

	long hardpointsUsed(final HardpointType type, final Location location)
	{
		return componentsInLocation(location.getLocationType()).stream().filter(c -> c.getHardpointType() == type).count();
	}

	int occupiedSlots(final LocationType locationType)
	{
		final List<? extends Component> componentsInLocation = componentsInLocation(locationType);
		return componentsInLocation.stream().mapToInt(Component::getSlots).sum();
	}

	private void forEach(final Consumer<Component> action)
	{
		slots.forEach(action);
	}

	private int typeCount(final String type)
	{
		final Predicate<? super Component> predicate = component -> Objects.equals(component.getType(), type);
		return (int)slots.count(predicate);
	}

	public float getHeat()
	{
		return heat;
	}

	public void fire(final int linkedGroup)
	{
		final List<Component> weapons = slots.componentsByLinkedGroup(linkedGroup);
		heat += weapons.stream().mapToDouble(c -> c.getHeat()).sum();
	}

	public static class MechBuildBuilder
	{
		private MechSpecBuilder mechSpecBuilder = new MechSpecBuilder();

		private Map<QuirkType, Quirk> quirks = new HashMap<>();

		private final List<SlotBuilder> slotBuilders = new ArrayList<>();

		public MechBuildBuilder withSpec(final MechSpecBuilder mechSpecBuilder)
		{
			this.mechSpecBuilder = mechSpecBuilder;
			return this;
		}

		@Deprecated
		public MechBuildBuilder withComponent(final LocationType location, final Component component)
		{
			slotBuilders.add(SlotBuilder.slot()
					.withLocation(location)
					.withComponent(component));
			return this;
		}

		public MechBuildBuilder withComponent(final SlotBuilder slotBuilder)
		{
			slotBuilders.add(slotBuilder);
			return this;
		}

		@Deprecated
		public MechBuildBuilder withComponent(final LocationType location, final Component component, final int linkedGroup)
		{
			slotBuilders.add(SlotBuilder.slot()
					.withLocation(location)
					.withComponent(component)
					.withLinkedGroup(linkedGroup));
			return this;
		}

		public MechBuildBuilder withQuirks(final QuirkBuilder quirkBuilder)
		{
			final Quirk quirk = quirkBuilder.build();
			quirks.put(quirk.getQuirkType(), quirk);
			return this;
		}

		public MechBuildBuilder withQuirks(final Map<QuirkType, Quirk> quirks)
		{
			this.quirks = quirks;
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

	public void update(final float dt)
	{
		if (heat == 0)
		{
			return;
		}
		heat -= hps() * dt;
		if (heat < 0)
		{
			heat = 0;
		}
	}
}
