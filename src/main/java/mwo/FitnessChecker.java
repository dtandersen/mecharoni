package mwo;

import static org.jenetics.engine.limit.bySteadyFitness;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.Phenotype;
import org.jenetics.StochasticUniversalSelector;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import mwo.Item.ItemBuilder;
import mwo.Mech.MechBuilder;

public class FitnessChecker
{
	private static FitnessCheckerConfig config;

	public void run(final FitnessCheckerConfig config2)
	{
		config = config2;
		final Factory<Genotype<MechGene>> gtf = Genotype.of(MechChromosome.of(1, config.slots, config.items, config));

		final FitnessCalculator fitnessCalculator = new FitnessCalculator(config);
		final Function<Genotype<MechGene>, Double> ff = gt -> fitnessCalculator.eval(toAnalyzer(gt));
		final Engine<MechGene, Double> engine = Engine.builder(ff, gtf)
				.alterers(
						new Mutator<>(.5)
				// ,
				// new SinglePointCrossover<>(0.06),
				// new SwapMutator<>())
				)
				// .selector(new TournamentSelector<>())
				.selector(new StochasticUniversalSelector<>())
				// .selector(new BoltzmannSelector<>(2))
				.populationSize(500)
				.build();

		// final Genotype<MechGene> result = engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
		final Phenotype<MechGene, Double> result = engine.stream()
				.limit(bySteadyFitness(10000))
				.limit(50000)
				.peek(FitnessChecker::update)
				.collect(EvolutionResult.toBestPhenotype());
		// final Phenotype<MechGene, Double> result = engine.stream().limit(100).collect(EvolutionResult.to);
		final Mech a = toAnalyzer((MechChromosome)best.getGenotype().getChromosome());

		System.out.println(result);
		System.out.println("Tons: " + a.getTons() + "/" + config.tons);
		System.out.println("Slots: " + a.getSlots() + "/" + config.slots);
		System.out.println("Energy: " + a.getEnergySlots() + "/" + config.energySlots);
		System.out.println("Ballistic: " + a.getBallisticSlots() + "/" + config.ballisticSlots);
		System.out.println("Missile: " + a.getMissileSlots() + "/" + config.missileSlots);
		System.out.println("ECM: " + a.getEcmSlots() + "/" + config.ecmSlots);
		System.out.println("AMS: " + a.getAmsSlots() + "/" + config.amsSlots);
		System.out.println("Damage: " + a.damageOverTime(30));
		System.out.println("Heat: " + a.heatExpended(30) + "/" + a.heatRegained(30));
		a.forEach(item -> System.out.println(item.getName() + ", dps=" + item.getDps() + ", hps=" + item.getHps()));

		System.out.println("END");
	}

	static Phenotype<MechGene, Double> best = null;

	private static void update(final EvolutionResult<MechGene, Double> result)
	{
		if (best == null || best.compareTo(result.getBestPhenotype()) < 0)
		{
			best = result.getBestPhenotype();
			final Mech a = toAnalyzer((MechChromosome)best.getGenotype().getChromosome());

			System.out.println(result);
			System.out.println("Tons: " + a.getTons() + "/" + config.tons);
			System.out.println("Slots: " + a.getSlots() + "/" + config.slots);
			System.out.println("Energy: " + a.getEnergySlots() + "/" + config.energySlots);
			System.out.println("Ballistic: " + a.getBallisticSlots() + "/" + config.ballisticSlots);
			System.out.println("Missile: " + a.getMissileSlots() + "/" + config.missileSlots);
			System.out.println("ECM: " + a.getEcmSlots() + "/" + config.ecmSlots);
			System.out.println("AMS: " + a.getAmsSlots() + "/" + config.amsSlots);
			System.out.println("Damage: " + a.damageOverTime(30));
			System.out.println("Heat: " + a.heatExpended(30) + "/" + a.heatRegained(30));
			System.out.println("Heat Sinks: " + a.getInternalHeatSinks() + "/" + a.getExternalHeatSinks());
			a.forEach(item -> System.out.println(item.getName() + ", dps=" + item.getDps() + ", hps=" + item.getHps()));
		}
	}

	private static Mech toAnalyzer(final Genotype<MechGene> gt)
	{
		final MechChromosome c = gt.getChromosome()
				.as(MechChromosome.class);
		return toAnalyzer(c);
	}

	static Mech toAnalyzer(final MechChromosome c)
	{
		final MechBuilder a = new MechBuilder().withConfig(config);
		c.forEach(x -> a.add(x.getAllele()));

		return a.build();
	}

	public static void main(final String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Item> items = clanItems().stream()
				.filter(item -> !item.getName().startsWith("ClanFlamer"))
				.filter(item -> !item.getName().startsWith("ClanLRM"))
				.filter(item -> !item.getName().startsWith("ClanMicro"))
				.filter(item -> !item.getName().startsWith("ClanERMicroLaser"))
				.collect(Collectors.toList());
		// items.forEach(i -> System.out.println(i.slots));
		final FitnessCheckerConfig config = new FitnessCheckerConfig();
		config.slots = 33;
		config.tons = 17;
		config.items = items;
		config.amsSlots = 0;
		config.ballisticSlots = 0;
		config.energySlots = 5;
		config.missileSlots = 2;
		config.ecmSlots = 0;
		config.engineSinks = 10;
		config.heatSinks = 4;

		final FitnessChecker hw = new FitnessChecker();
		hw.run(config);
	}

	private static List<Item> readItems() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final Gson gson = new Gson();
		final Type listType = new TypeToken<HashMap<String, ItemJson>>() {}.getType();
		final HashMap<String, ItemJson> i = gson.fromJson(new InputStreamReader(FitnessChecker.class.getResourceAsStream("/weapons.json")), listType);

		final List<Item> collect = i.values().stream()
				.map(it -> new ItemBuilder()
						.withTons(it.tons)
						.withSlots(it.slots)
						.withName(it.name)
						.withDamage(it.calc_stats.dmg)
						.withCooldown(it.cooldown)
						.withDuration(it.duration)
						.withType(it.type)
						.withHeat(it.heat)
						.build())
				.collect(Collectors.toList());
		collect.add(new ItemBuilder().withName("Empty Slot").empty().build());
		collect.add(new ItemBuilder()
				.withName("Double Heat Sink")
				.heatSink()
				.withHeatCapacity(1.5)
				.withDisipation(0.15)
				.withSlots(2)
				.withTons(1)
				.build());
		return Collections.unmodifiableList(collect);
	}

	static List<Item> clanItems() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Item> clanItems = readItems().stream()
				.filter(item -> item.isClan())
				.collect(Collectors.toList());

		return Collections.unmodifiableList(clanItems);
	}

	static class FitnessCheckerConfig
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

	static class ItemJson
	{
		public String type;

		public float tons;

		public float slots;

		public float cooldown;

		public float duration;

		public String name;

		public float heat;

		public Stats calc_stats;

		static class Stats
		{
			float dmg;
		}
	}
}
