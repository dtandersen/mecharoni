package us.davidandersen.mecharoni.evolve;

import static org.jenetics.engine.limit.bySteadyFitness;
import java.util.List;
import java.util.function.Function;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.Phenotype;
import org.jenetics.SinglePointCrossover;
import org.jenetics.StochasticUniversalSelector;
import org.jenetics.SwapMutator;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import us.davidandersen.mecharoni.entity.Item;
import us.davidandersen.mecharoni.entity.Mech;
import us.davidandersen.mecharoni.io.MechPrinter;

public class EvolveMech
{
	public void run(final FitnessCheckerConfig config)
	{
		final Factory<Genotype<MechGene>> gtf = Genotype.of(MechChromosome.of(1, config.slots, config.items, config));

		final MechFitnessFunction fitnessCalculator = new MechFitnessFunction(config);
		final Function<Genotype<MechGene>, Double> ff = gt -> fitnessCalculator.eval(MechCodec.toMech(gt, config));
		final Engine<MechGene, Double> engine = Engine.builder(ff, gtf)
				.alterers(
						new Mutator<>(.5),
						// ,
						new SinglePointCrossover<>(0.06),
						new SwapMutator<>())
				// .selector(new TournamentSelector<>())
				.selector(new StochasticUniversalSelector<>())
				// .selector(new BoltzmannSelector<>(2))
				.populationSize(500)
				.build();

		final ResultPrinter resultPrinter = new ResultPrinter(config);
		// final Genotype<MechGene> result = engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
		final Phenotype<MechGene, Double> result = engine.stream()
				.limit(bySteadyFitness(10000))
				// .limit(50000)
				.peek(r -> resultPrinter.update(r))
				.collect(EvolutionResult.toBestPhenotype());
		// final Phenotype<MechGene, Double> result = engine.stream().limit(100).collect(EvolutionResult.to);
		final Mech mech = resultPrinter.bestMech();

		System.out.println(result);
		final MechPrinter mechPrinter = new MechPrinter(System.out);
		mechPrinter.printMech(mech, config);

		System.out.println("END");
	}

	public static class FitnessCheckerConfig
	{
		public int heatSinks;

		public int engineSinks;

		public int tons;

		public List<Item> items;

		public int slots;

		public int energySlots;

		public int ballisticSlots;

		public int missileSlots;

		public int ecmSlots;

		public int amsSlots;

		public int typeSlots(final String type)
		{
			switch (type)
			{
			case "BEAM":
				return energySlots;
			case "MISSLE":
				return missileSlots;
			case "BALLISTIC":
				return ballisticSlots;
			case "AMS":
				return amsSlots;
			case "ECM":
				return ecmSlots;
			}

			throw new RuntimeException();
		}
	}
}
