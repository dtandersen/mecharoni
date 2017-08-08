package us.davidandersen.mecharoni.entity;

import java.util.Objects;
import java.util.function.Predicate;
import us.davidandersen.mecharoni.entity.predicate.LaserPredicate;

public enum QuirkType
{
	LASER_DURATION(new LaserPredicate(), new DurationQuirk()),
	COOLDOWN(c -> c.isWeapon(), new CooldownQuirk()),
	MISSILE_COOLDOWN(c -> c.isMissile(), new CooldownQuirk()),
	HEAT(c -> c.isWeapon(), new HeatQuirk()),
	BALLISTIC_COOLDOWN(c -> c.isBallistic(), new CooldownQuirk()),
	RANGE(c -> c.isWeapon(), new RangeQuirk());

	private final Predicate<Component> predicate;

	private final QuirkApplier quirkApplier;

	QuirkType(final Predicate<Component> predicate, final QuirkApplier quirkApplier)
	{
		this.predicate = predicate;
		this.quirkApplier = quirkApplier;
	}

	public boolean matches(final Component component)
	{
		return predicate.test(component);
	}

	public void applier(final QuirkContext quirkContext, final Quirk quirk)
	{
		quirkApplier.apply(quirkContext, quirk);
	}

	public static QuirkType find(final String quirkName)
	{
		for (final QuirkType quirkType : values())
		{
			if (Objects.equals(quirkName.toUpperCase(), quirkType.name().replaceAll("_", " "))) { return quirkType; }
		}

		return null;
	}
}
