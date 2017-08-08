package us.davidandersen.mecharoni.entity;

public class CooldownQuirk implements QuirkApplier
{
	@Override
	public void apply(final QuirkContext context, final Quirk quirk)
	{
		context.incrementCooldown(quirk);
	}
}
