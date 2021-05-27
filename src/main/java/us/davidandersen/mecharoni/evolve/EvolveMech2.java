package us.davidandersen.mecharoni.evolve;

import java.time.Duration;
import java.util.function.Function;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.Phenotype;
import io.jenetics.SwapMutator;
import io.jenetics.TournamentSelector;
import io.jenetics.UniformCrossover;
import io.jenetics.engine.Engine;
import io.jenetics.engine.Limits;
import io.jenetics.ext.moea.MOEA;
import io.jenetics.ext.moea.NSGA2Selector;
import io.jenetics.ext.moea.Vec;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import us.davidandersen.mecharoni.entity.Location;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;
import us.davidandersen.mecharoni.io.MechPrinter;

public class EvolveMech2
{
	public void run(final EvolveMechConfig config)
	{
		final int slots = config.locations.values().stream().mapToInt(Location::getSlots).sum();
		final Factory<Genotype<MechGene>> gtf = Genotype.of(MechChromosome.of(1, slots, config.items, config));

		final MechSimulatorFitnessFunction2 fitnessCalculator = new MechSimulatorFitnessFunction2(config);
		// final MechFitnessFunction fitnessCalculator = new
		// MechFitnessFunction(config);
		final Function<Genotype<MechGene>, Vec<double[]>> ff = gt -> fitnessCalculator.eval(MechCodec.toMech(gt, config));
		// final BasicComponent heatSink = config.items.stream().filter(item ->
		// item.getName().contains("HeatSink")).findFirst().get();
		// final BasicComponent empty = config.items.stream().filter(item ->
		// item.getName().contains("Empty")).findFirst().get();
		final Engine<MechGene, Vec<double[]>> engine = Engine.builder(ff, gtf)
				.alterers(
						new Mutator<>(),
						// new MyMutator(.25, heatSink, config.items, empty),
						// new MyMutator(.25, heatSink, config.items, empty),
						// ,
						// new SinglePointCrossover<>(),
						new UniformCrossover<>(),
						new SwapMutator<>())
				// .selector(new TournamentSelector<>())
				.offspringSelector(new TournamentSelector<>(2))
				.survivorsSelector(NSGA2Selector.ofVec())
				// .survivorsSelector(UFTournamentSelector.ofVec())
				// .selector(new BoltzmannSelector<>(2))
				.populationSize(1000)
				.build();

		final ResultPrinter2 resultPrinter = new ResultPrinter2(config);
		// final Genotype<MechGene> result =
		// engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
		final ISeq<Phenotype<MechGene, Vec<double[]>>> result = engine.stream()
				// .limit(limit.bySteadyFitness(10000))
				// .limit(limit.byFitnessConvergence(50, 100, 10E-4))
				//
				.limit(Limits.byExecutionTime(Duration.ofSeconds(120)))
				// .limit(1000)
				.peek(r -> resultPrinter.update(r))
				.collect(MOEA.toParetoSet(IntRange.of(5, 100)));
		// .collect(EvolutionResult.toBestPhenotype());
		// final Phenotype<MechGene, Double> result =
		// engine.stream().limit(100).collect(EvolutionResult.to);
		final MechPrinter mechPrinter = new MechPrinter(System.out);
		for (final Phenotype<MechGene, Vec<double[]>> x : result)
		{
			System.out.println("---");
			final MechBuild mech = MechCodec.toMech(x.genotype(), config);
			mechPrinter.printMech(mech, config);
		}
		// final MechBuild mech = resultPrinter.bestMech();
		//
		// System.out.println("generation=" + result.generation() + ", fitness="
		// + result.fitness());
		//
		// System.out.println("END");
	}
}
