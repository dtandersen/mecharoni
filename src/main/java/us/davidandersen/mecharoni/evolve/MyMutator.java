package us.davidandersen.mecharoni.evolve;

import static org.jenetics.internal.math.random.indexes;
import java.util.List;
import java.util.Random;
import org.jenetics.Mutator;
import org.jenetics.util.MSeq;
import org.jenetics.util.RandomRegistry;
import us.davidandersen.mecharoni.entity.Component;

public class MyMutator extends Mutator<MechGene, Double>
{
	private final Component heatSink;

	private final List<Component> items;

	private final Component empty;

	public MyMutator(final double p, final Component heatSink, final List<Component> items, final Component empty)
	{
		super(p);
		this.heatSink = heatSink;
		this.items = items;
		this.empty = empty;
	}

	@Override
	protected int mutate(final MSeq<MechGene> genes, final double p)
	{
		final Random random = RandomRegistry.getRandom();
		Component comp;
		if (random.nextDouble() > .5)
		{
			comp = heatSink;
		}
		else
		{
			comp = empty;
		}
		return (int)indexes(random, genes.length(), p)
				.peek(i -> genes.set(i, new MechGene(comp, items)))
				.count();
	}
}
