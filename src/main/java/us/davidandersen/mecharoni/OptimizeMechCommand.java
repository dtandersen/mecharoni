package us.davidandersen.mecharoni;

import us.davidandersen.mecharoni.command.OptimizeMech;
import us.davidandersen.mecharoni.repository.ComponentRepository;
import us.davidandersen.mecharoni.repository.json.JsonComponentRepository;

public class OptimizeMechCommand
{
	public static void main(final String[] args) throws Exception
	{
		final ComponentRepository componentReader = new JsonComponentRepository();

		final OptimizeMech opt = new OptimizeMech(componentReader);
		opt.run();
	}
}
