package us.davidandersen.mecharoni.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Slots
{
	private final List<Slot> slots;

	Slots()
	{
		slots = new ArrayList<>();
	}

	List<Component> componentsByLocation(final LocationType locationType)
	{
		return slots.stream()
				.filter(slot -> slot.hasLocation(locationType))
				.map(slot -> slot.getComponent())
				.collect(Collectors.toList());
	}

	public List<Component> componentsByLinkedGroup(final int linkedGroup)
	{
		return slots.stream()
				.filter(slot -> slot.hasLinkedGroup(linkedGroup))
				.map(slot -> slot.getComponent())
				.collect(Collectors.toList());
	}

	void addComponent(final Slot slot)
	{
		slots.add(slot);
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
		return slots.stream()
				.map(slot -> slot.getComponent())
				.collect(Collectors.toList());
	}

	public Component findFirst(final Predicate<? super Component> predicate)
	{
		return stream().filter(predicate).findFirst().get();
	}
}
