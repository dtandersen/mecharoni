package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Slots
{
	private final List<? super Component> components;

	private final Map<LocationType, List<? super Component>> componentsByLocation;

	Slots()
	{
		this.components = new ArrayList<>();
		this.componentsByLocation = new HashMap<>();
	}

	List<? extends Component> componentsByLocation(final LocationType locationType)
	{
		final List<? super Component> list = componentsByLocation.get(locationType);
		if (list == null) { return new ArrayList<>(); }

		return (List<? extends Component>)list;
	}

	void addComponent(final LocationType locationType, final Component component)
	{
		components.add(component);
		final List<? super Component> list = componentsByLocation.get(locationType);
		List<? super Component> compsInLoc = list;
		if (compsInLoc == null)
		{
			compsInLoc = new ArrayList<>();
			final List<? super Component> compsInLoc1 = compsInLoc;
			componentsByLocation.put(locationType, compsInLoc1);
		}
		compsInLoc.add(component);
	}

	int occupiedSlots()
	{
		return intSum(Component::getSlots);
	}

	double occupiedTons()
	{
		return sum(Component::getTons);
	}

	boolean anyMatch(final Predicate<? super Component> predicate)
	{
		return stream().anyMatch(predicate);
	}

	List<? extends Component> filter(final Predicate<? super Component> predicate)
	{
		return stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}

	long count(final Predicate<? super Component> predicate)
	{
		return stream().filter(predicate).count();
	}

	void forEach(final Consumer<Component> action)
	{
		getComponents().forEach(action);
	}

	double sum(final ToDoubleFunction<? super Component> mapper)
	{
		return stream().mapToDouble(mapper).sum();
	}

	int intSum(final ToIntFunction<? super Component> mapper)
	{
		return stream().mapToInt(mapper).sum();
	}

	private Stream<? extends Component> stream()
	{
		return getComponents().stream();
	}

	private List<? extends Component> getComponents()
	{
		return (List<? extends Component>)components;
	}

	public Component findFirst(final Predicate<? super Component> predicate)
	{
		return stream().filter(predicate).findFirst().get();
	}
}
