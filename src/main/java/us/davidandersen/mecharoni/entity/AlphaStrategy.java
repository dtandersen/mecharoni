package us.davidandersen.mecharoni.entity;

public class AlphaStrategy implements FiringStrategy
{
	@Override
	public boolean fire(final double heat, final double availableHeat, final double totalHeat)
	{
		return availableHeat >= totalHeat;
	}
}
