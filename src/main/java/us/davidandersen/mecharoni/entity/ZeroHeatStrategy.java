package us.davidandersen.mecharoni.entity;

public class ZeroHeatStrategy implements FiringStrategy
{
	@Override
	public boolean fire(final double heat, final double availableHeat, final double totalHeat)
	{
		return heat <= 0;
	}
}
