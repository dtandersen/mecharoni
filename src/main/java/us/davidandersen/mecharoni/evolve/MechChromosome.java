package us.davidandersen.mecharoni.evolve;

import java.io.Serializable;
import java.util.List;
import org.jenetics.AbstractChromosome;
import org.jenetics.Chromosome;
import org.jenetics.util.ISeq;
import org.jenetics.util.MSeq;
import us.davidandersen.mecharoni.entity.Item;
import us.davidandersen.mecharoni.evolve.EvolveMech.FitnessCheckerConfig;

@SuppressWarnings("serial")
public class MechChromosome extends
		AbstractChromosome<MechGene> implements Chromosome<MechGene>, Comparable<MechChromosome>, Serializable
{
	private final FitnessCheckerConfig config;

	protected MechChromosome(final ISeq<MechGene> genes, final FitnessCheckerConfig config)
	{
		super(genes);
		this.config = config;
	}

	@Override
	public Chromosome<MechGene> newInstance()
	{
		return MechChromosome.of(_genes.size(), _genes.get(0).getItems(), config);
		// return new MechChromosome(_genes, config);
	}

	@Override
	public int compareTo(final MechChromosome o)
	{
		return 0;
	}

	@Override
	public Chromosome<MechGene> newInstance(final ISeq<MechGene> genes)
	{
		// System.out.println("new");
		return new MechChromosome(genes, config);
	}

	public static MechChromosome of(final int geneCount, final List<Item> items, final FitnessCheckerConfig config)
	{
		final MechChromosome mechChromosome = new MechChromosome(MSeq.<MechGene> ofLength(geneCount).fill(() -> new MechGene(items))
				.toISeq(), config);
		// System.out.println(mechChromosome);
		return mechChromosome;
	}

	public static ISeq<MechChromosome> of(final int chromosomeCount, final int geneCount, final List<Item> items, final FitnessCheckerConfig config)
	{
		return MSeq.<MechChromosome> ofLength(chromosomeCount).fill(() -> MechChromosome.of(geneCount, items, config)).toISeq();
	}

	@Override
	public boolean isValid()
	{
		// final Analyzer a = FitnessChecker.toAnalyzer(this);
		// if (a.getTons() > config.tons) { return false; }
		// if (a.getSlots() > config.slots) { return false; }
		// if (a.getEnergySlots() > config.energySlots) { return false; }
		// if (a.getBallisticSlots() > config.ballisticSlots) { return false; }
		// if (a.getMissileSlots() > config.missileSlots) { return false; }
		// if (a.getEcmSlots() > config.ecmSlots) { return false; }
		// if (a.getAmsSlots() > config.amsSlots) { return false; }
		return super.isValid();
	}
}
