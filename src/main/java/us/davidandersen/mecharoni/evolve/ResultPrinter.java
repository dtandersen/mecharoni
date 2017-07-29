package us.davidandersen.mecharoni.evolve;

import org.jenetics.Phenotype;
import org.jenetics.engine.EvolutionResult;
import us.davidandersen.mecharoni.entity.Mech;
import us.davidandersen.mecharoni.evolve.EvolveMech.MechSpecYaml;
import us.davidandersen.mecharoni.io.MechPrinter;

public class ResultPrinter
{
	private final MechSpecYaml config;

	private final MechPrinter mechPrinter;

	private Phenotype<MechGene, Double> best = null;

	public ResultPrinter(final MechSpecYaml config)
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

			mechPrinter.printMech(a, config);
			System.out.println("generation=" + best.getGeneration() + ", fitness=" + best.getFitness());
		}
	}

	public Mech bestMech()
	{
		return MechCodec.toMech((MechChromosome)best.getGenotype().getChromosome(), config);
	}
}
