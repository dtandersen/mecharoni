package us.davidandersen.mecharoni.evolve;

import java.util.List;
import java.util.function.Function;
import org.jenetics.BoltzmannSelector;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.Phenotype;
import org.jenetics.SwapMutator;
import org.jenetics.UniformCrossover;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.limit;
import org.jenetics.util.Factory;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.MechSpec;
import us.davidandersen.mecharoni.io.MechPrinter;

public class EvolveMech
{
	public void run(final MechSpecYaml config)
	{
		final Factory<Genotype<MechGene>> gtf = Genotype.of(MechChromosome.of(1, config.slots, config.items, config));

		final MechFitnessFunction2 fitnessCalculator = new MechFitnessFunction2(config);
		final Function<Genotype<MechGene>, Double> ff = gt -> fitnessCalculator.eval(MechCodec.toMech(gt, config));
		final Component heatSink = config.items.stream().filter(item -> item.getName().contains("HeatSink")).findFirst().get();
		final Component empty = config.items.stream().filter(item -> item.getName().contains("Empty")).findFirst().get();
		final Engine<MechGene, Double> engine = Engine.builder(ff, gtf)
				.alterers(
						new Mutator<>(),
						new MyMutator(.25, heatSink, config.items, empty),
						// ,
						// new SinglePointCrossover<>(),
						new UniformCrossover<>(),
						new SwapMutator<>())
				// .selector(new TournamentSelector<>())
				// .selector(new StochasticUniversalSelector<>())
				.selector(new BoltzmannSelector<>(2))
				.populationSize(500)
				.build();

		final ResultPrinter resultPrinter = new ResultPrinter(config);
		// final Genotype<MechGene> result = engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
		final Phenotype<MechGene, Double> result = engine.stream()
				.limit(limit.bySteadyFitness(10000))
				// .limit(limit.byFitnessConvergence(50, 100, 10E-4))
				// .limit(50000)
				.peek(r -> resultPrinter.update(r))
				.collect(EvolutionResult.toBestPhenotype());
		// final Phenotype<MechGene, Double> result = engine.stream().limit(100).collect(EvolutionResult.to);
		final MechSpec mech = resultPrinter.bestMech();

		final MechPrinter mechPrinter = new MechPrinter(System.out);
		mechPrinter.printMech(mech, config);
		System.out.println("generation=" + result.getGeneration() + ", fitness=" + result.getFitness());

		System.out.println("END");
	}

	public static class MechSpecYaml
	{
		public int heatSinks;

		public int engineSinks;

		public float tons;

		public List<Component> items;

		public int slots;

		public int energySlots;

		public int ballisticSlots;

		public int missileSlots;

		public int ecmSlots;

		public int amsSlots;
	}
}
