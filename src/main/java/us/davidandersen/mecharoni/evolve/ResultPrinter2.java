package us.davidandersen.mecharoni.evolve;

import io.jenetics.Phenotype;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.ext.moea.Vec;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.io.MechPrinter;

public class ResultPrinter2
{
	private final EvolveMechConfig config;

	private final MechPrinter mechPrinter;

	private Phenotype<MechGene, Vec<double[]>> best = null;

	public ResultPrinter2(final EvolveMechConfig config)
	{
		this.config = config;
		mechPrinter = new MechPrinter(System.out);
	}

	public void update(final EvolutionResult<MechGene, Vec<double[]>> result)
	{
		if (best == null || best.compareTo(result.bestPhenotype()) < 0)
		{
			this.best = result.bestPhenotype();
			final MechBuild a = MechCodec.toMech2((MechChromosome)best.genotype().chromosome(), config);

			mechPrinter.printMech(a, config);
			System.out.println("generation=" + best.generation() + ", fitness=" + best.fitness());
		}
	}

	public MechBuild bestMech()
	{
		return MechCodec.toMech2((MechChromosome)best.genotype().chromosome(), config);
	}
}
