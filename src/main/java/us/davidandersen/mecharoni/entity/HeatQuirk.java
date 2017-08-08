package us.davidandersen.mecharoni.entity;

public class HeatQuirk implements QuirkApplier
{
	@Override
	public void apply(final QuirkContext context, final Quirk quirk)
	{
		context.incrementHeat(quirk);
	}
}
