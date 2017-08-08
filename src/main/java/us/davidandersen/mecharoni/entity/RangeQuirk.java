package us.davidandersen.mecharoni.entity;

public class RangeQuirk implements QuirkApplier
{
	@Override
	public void apply(final QuirkContext context, final Quirk quirk)
	{
		context.incrementRange(quirk);
	}
}
