package us.davidandersen.mecharoni.entity;

public class DurationQuirk implements QuirkApplier
{
	@Override
	public void apply(final QuirkContext context, final Quirk quirk)
	{
		context.incrementDuration(quirk);
	}
}
