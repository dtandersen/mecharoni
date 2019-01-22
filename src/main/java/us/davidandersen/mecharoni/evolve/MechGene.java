package us.davidandersen.mecharoni.evolve;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import io.jenetics.Gene;
import io.jenetics.util.RandomRegistry;
import us.davidandersen.mecharoni.entity.Component;

@SuppressWarnings("serial")
public class MechGene implements Gene<Component, MechGene>, Comparable<MechGene>, Serializable
{
	private final List<Component> items;

	private final Component item;

	public MechGene(final List<Component> items)
	{
		this.items = items;
		final Random r = RandomRegistry.getRandom();
		item = items.get(r.nextInt(items.size()));
	}

	public MechGene(final Component component, final List<Component> items)
	{
		this.item = component;
		this.items = items;
	}

	@Override
	public boolean isValid()
	{
		return true;
	}

	@Override
	public int compareTo(final MechGene o)
	{
		return 0;
	}

	@Override
	public Component getAllele()
	{
		return item;
	}

	@Override
	public MechGene newInstance()
	{
		return new MechGene(getItems());
	}

	@Override
	public MechGene newInstance(final Component item)
	{
		return new MechGene(item, getItems());
	}

	@Override
	public String toString()
	{
		return item.toString();
	}

	public List<Component> getItems()
	{
		return items;
	}
}
