package us.davidandersen.mecharoni.entity;

public enum QuirkType
{
	LASER_DURATION
	{
		@Override
		void apply(final QuirkContext context, final Quirk quirk)
		{
			if (context.isEnergy())
			{
				context.incrementDuration(quirk);
			}
		}
	},
	COOLDOWN
	{
		@Override
		void apply(final QuirkContext context, final Quirk quirk)
		{
			context.incrementCooldown(quirk);
		}
	};

	abstract void apply(final QuirkContext context, Quirk quirk);
}
