package us.davidandersen.mecharoni.evolve;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import org.jenetics.Gene;
import org.jenetics.util.RandomRegistry;
import us.davidandersen.mecharoni.entity.BasicComponent;

@SuppressWarnings("serial")
public class MechGene implements Gene<BasicComponent, MechGene>, Comparable<MechGene>, Serializable
{
	private final List<BasicComponent> items;

	private final BasicComponent item;

	public MechGene(final List<BasicComponent> items)
	{
		this.items = items;
		final Random r = RandomRegistry.getRandom();
		item = items.get(r.nextInt(items.size()));
	}

	public MechGene(final BasicComponent component, final List<BasicComponent> items)
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
	public BasicComponent getAllele()
	{
		return item;
	}

	@Override
	public MechGene newInstance()
	{
		return new MechGene(getItems());
	}

	@Override
	public MechGene newInstance(final BasicComponent item)
	{
		return new MechGene(item, getItems());
	}

	@Override
	public String toString()
	{
		return item.toString();
	}

	public List<BasicComponent> getItems()
	{
		return items;
	}
}
