package us.davidandersen.mecharoni.evolve;

import org.jenetics.Phenotype;
import org.jenetics.engine.EvolutionResult;
import us.davidandersen.mecharoni.entity.Mech;
import us.davidandersen.mecharoni.evolve.EvolveMech.FitnessCheckerConfig;
import us.davidandersen.mecharoni.io.MechPrinter;

public class ResultPrinter
{
	private final FitnessCheckerConfig config;

	private final MechPrinter mechPrinter;

	private Phenotype<MechGene, Double> best = null;

	public ResultPrinter(final FitnessCheckerConfig config)
	{
		this.config = config;
		mechPrinter = new MechPrinter(System.out);
	}

	public void update(final EvolutionResult<MechGene, Double> result)
	{
		if (best == null || best.compareTo(result.getBestPhenotype()) < 0)
		{
			this.best = result.getBestPhenotype();
			final Mech a = MechCodec.toMech((MechChromosome)best.getGenotype().getChromosome(), config);

			System.out.println(result);
			mechPrinter.printMech(a, config);
		}
	}

	public Mech bestMech()
	{
		return MechCodec.toMech((MechChromosome)best.getGenotype().getChromosome(), config);
	}
}
