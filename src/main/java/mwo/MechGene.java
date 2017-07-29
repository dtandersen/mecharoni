package mwo;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import org.jenetics.Gene;
import org.jenetics.util.RandomRegistry;

@SuppressWarnings("serial")
public class MechGene implements Gene<Item, MechGene>, Comparable<MechGene>, Serializable
{
	private final List<Item> items;

	private final Item item;

	public MechGene(final List<Item> items)
	{
		this.items = items;
		final Random r = RandomRegistry.getRandom();
		item = items.get(r.nextInt(items.size()));
	}

	public MechGene(final Item value, final List<Item> items2)
	{
		this.item = value;
		this.items = items2;
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
	public Item getAllele()
	{
		return item;
	}

	@Override
	public MechGene newInstance()
	{
		return new MechGene(getItems());
	}

	@Override
	public MechGene newInstance(final Item item)
	{
		return new MechGene(item, getItems());
	}

	@Override
	public String toString()
	{
		return item.toString();
	}

	public List<Item> getItems()
	{
		return items;
	}
}
