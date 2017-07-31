package us.davidandersen.mecharoni;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;
import us.davidandersen.mecharoni.command.OptimizeMech;
import us.davidandersen.mecharoni.command.OptimizeMech.OptimizeMechRequestAdapter;
import us.davidandersen.mecharoni.repository.ComponentRepository;
import us.davidandersen.mecharoni.repository.json.JsonComponentRepository;

public class OptimizeMechCommand
{
	private static final String DEFAULT_CONFIG = "mech.yaml";

	public static void main(final String[] args) throws Exception
	{
		final ComponentRepository componentReader = new JsonComponentRepository();

		final OptimizeMech optimizer = new OptimizeMech(componentReader);
		final InputStream input = new FileInputStream(new File(DEFAULT_CONFIG));
		final Yaml yaml = new Yaml();
		final MechSpecificationYaml data = yaml.loadAs(input, MechSpecificationYaml.class);
		optimizer.setRequest(new OptimizeMechRequestYamlAdapter(data));
		optimizer.execute();
	}

	private static final class OptimizeMechRequestYamlAdapter implements OptimizeMechRequestAdapter
	{
		private final MechSpecificationYaml specYaml;

		public OptimizeMechRequestYamlAdapter(final MechSpecificationYaml data)
		{
			this.specYaml = data;
		}

		@Override
		public int getSlots()
		{
			return specYaml.slots;
		}

		@Override
		public float getTons()
		{
			return specYaml.tons;
		}

		@Override
		public int getAmsSlots()
		{
			return specYaml.amsSlots;
		}

		@Override
		public int getEnergySlots()
		{
			return specYaml.energySlots;
		}

		@Override
		public int getBallisticSlots()
		{
			return specYaml.ballisticSlots;
		}

		@Override
		public int getMissileSlots()
		{
			return specYaml.missileSlots;
		}

		@Override
		public int getEcmSlots()
		{
			return specYaml.ecmSlots;
		}

		@Override
		public int getEngineSinks()
		{
			return specYaml.engineSinks;
		}

		@Override
		public int getHeatSinks()
		{
			return specYaml.heatSinks;
		}

		@Override
		public String[] getExcludes()
		{
			return specYaml.excludes;
		}

		@Override
		public String getFaction()
		{
			return specYaml.faction;
		}

		@Override
		public int getRange()
		{
			return specYaml.range;
		}
	}

	private static final class MechSpecificationYaml
	{
		public String faction;

		public String[] excludes;

		public int heatSinks;

		public int engineSinks;

		public float tons;

		public int slots;

		public int energySlots;

		public int ballisticSlots;

		public int missileSlots;

		public int ecmSlots;

		public int amsSlots;

		public int range;
	}
}
