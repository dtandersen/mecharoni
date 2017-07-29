package us.davidandersen.mecharoni.evolve;

import org.jenetics.Genotype;
import us.davidandersen.mecharoni.entity.Mech;
import us.davidandersen.mecharoni.entity.Mech.MechBuilder;
import us.davidandersen.mecharoni.evolve.EvolveMech.FitnessCheckerConfig;

public class MechCodec
{
	public static Mech toMech(final Genotype<MechGene> gt, final FitnessCheckerConfig config)
	{
		final MechChromosome c = gt.getChromosome()
				.as(MechChromosome.class);
		return MechCodec.toMech(c, config);
	}

	public static Mech toMech(final MechChromosome c, final FitnessCheckerConfig config)
	{
		final MechBuilder a = new MechBuilder().withConfig(config);
		c.forEach(x -> a.add(x.getAllele()));

		return a.build();
	}
}
