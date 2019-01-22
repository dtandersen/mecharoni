package us.davidandersen.mecharoni.evolve;

import io.jenetics.Phenotype;
import io.jenetics.engine.EvolutionResult;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.io.MechPrinter;

public class ResultPrinter
{
	private final EvolveMechConfig config;

	private final MechPrinter mechPrinter;

	private Phenotype<MechGene, Double> best = null;

	public ResultPrinter(final EvolveMechConfig config)
	{
		this.config = config;
		mechPrinter = new MechPrinter(System.out);
	}

	public void update(final EvolutionResult<MechGene, Double> result)
	{
		if (best == null || best.compareTo(result.getBestPhenotype()) < 0)
		{
			this.best = result.getBestPhenotype();
			final MechBuild a = MechCodec.toMech2((MechChromosome)best.getGenotype().getChromosome(), config);

			mechPrinter.printMech(a, config);
			System.out.println("generation=" + best.getGeneration() + ", fitness=" + best.getFitness());
		}
	}

	public MechBuild bestMech()
	{
		return MechCodec.toMech2((MechChromosome)best.getGenotype().getChromosome(), config);
	}
}
