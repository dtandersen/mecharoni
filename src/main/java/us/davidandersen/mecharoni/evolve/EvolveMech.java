package us.davidandersen.mecharoni.evolve;

import java.util.List;
import java.util.function.Function;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.Phenotype;
import org.jenetics.SwapMutator;
import org.jenetics.TournamentSelector;
import org.jenetics.UniformCrossover;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.Mech;
import us.davidandersen.mecharoni.io.MechPrinter;

public class EvolveMech
{
	public void run(final MechSpecYaml config)
	{
		final Factory<Genotype<MechGene>> gtf = Genotype.of(MechChromosome.of(1, config.slots, config.items, config));

		final MechFitnessFunction fitnessCalculator = new MechFitnessFunction(config);
		final Function<Genotype<MechGene>, Double> ff = gt -> fitnessCalculator.eval(MechCodec.toMech(gt, config));
		final Component heatSink = config.items.stream().filter(item -> item.getName().contains("HeatSink")).findFirst().get();
		final Component empty = config.items.stream().filter(item -> item.getName().contains("Empty")).findFirst().get();
		final Engine<MechGene, Double> engine = Engine.builder(ff, gtf)
				.alterers(
						new Mutator<>(.2),
						new MyMutator(.1, heatSink, config.items, empty),
						// ,
						// new SinglePointCrossover<>(),
						new UniformCrossover<>(),
						new SwapMutator<>())
				.selector(new TournamentSelector<>())
				// .selector(new StochasticUniversalSelector<>())
				// .selector(new BoltzmannSelector<>(2))
				.populationSize(500)
				.build();

		final ResultPrinter resultPrinter = new ResultPrinter(config);
		// final Genotype<MechGene> result = engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
		final Phenotype<MechGene, Double> result = engine.stream()
				// .limit(bySteadyFitness(10000))
				.limit(50000)
				.peek(r -> resultPrinter.update(r))
				.collect(EvolutionResult.toBestPhenotype());
		// final Phenotype<MechGene, Double> result = engine.stream().limit(100).collect(EvolutionResult.to);
		final Mech mech = resultPrinter.bestMech();

		final MechPrinter mechPrinter = new MechPrinter(System.out);
		mechPrinter.printMech(mech, config);
		System.out.println("generation=" + result.getGeneration() + ", fitness=" + result.getFitness());

		System.out.println("END");
	}

	public static class MechSpecYaml
	{
		public int heatSinks;

		public int engineSinks;

		public int tons;

		public List<Component> items;

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
