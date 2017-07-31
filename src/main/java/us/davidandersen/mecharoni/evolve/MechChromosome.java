package us.davidandersen.mecharoni.evolve;

import java.io.Serializable;
import java.util.List;
import org.jenetics.AbstractChromosome;
import org.jenetics.Chromosome;
import org.jenetics.util.ISeq;
import org.jenetics.util.MSeq;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;

@SuppressWarnings("serial")
public class MechChromosome extends
		AbstractChromosome<MechGene> implements Chromosome<MechGene>, Serializable
{
	private final EvolveMechConfig config;

	protected MechChromosome(final ISeq<MechGene> genes, final EvolveMechConfig config)
	{
		super(genes);
		this.config = config;
	}

	@Override
	public Chromosome<MechGene> newInstance()
	{
		return MechChromosome.of(_genes.size(), config.items, config);
	}

	@Override
	public Chromosome<MechGene> newInstance(final ISeq<MechGene> genes)
	{
		return new MechChromosome(genes, config);
	}

	public static MechChromosome of(final int geneCount, final List<Component> items, final EvolveMechConfig config)
	{
		final MechChromosome mechChromosome = new MechChromosome(MSeq.<MechGene> ofLength(geneCount).fill(() -> new MechGene(items))
				.toISeq(), config);
		return mechChromosome;
	}

	public static ISeq<MechChromosome> of(final int chromosomeCount, final int geneCount, final List<Component> items, final EvolveMechConfig config)
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
