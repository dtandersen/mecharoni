package us.davidandersen.mecharoni.evolve;

import org.jenetics.Genotype;
import us.davidandersen.mecharoni.entity.MechSpec;
import us.davidandersen.mecharoni.entity.MechSpec.MechSpecBuilder;
import us.davidandersen.mecharoni.evolve.EvolveMech.MechSpecYaml;

public class MechCodec
{
	public static MechSpec toMech(final Genotype<MechGene> gt, final MechSpecYaml config)
	{
		final MechChromosome c = gt.getChromosome()
				.as(MechChromosome.class);
		return MechCodec.toMech(c, config);
	}

	public static MechSpec toMech(final MechChromosome c, final MechSpecYaml config)
	{
		final MechSpecBuilder a = new MechSpecBuilder().withConfig(config);
		c.forEach(x -> a.add(x.getAllele()));

		return a.build();
	}
}
