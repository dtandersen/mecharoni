package us.davidandersen.mecharoni.entity.predicate;

import java.util.Arrays;
import java.util.function.Predicate;
import us.davidandersen.mecharoni.entity.Component;

public class MultiWeaponPredicate implements Predicate<Component>
{
	private final String[] names;

	MultiWeaponPredicate(final String... names)
	{
		this.names = names;
	}

	@Override
	public boolean test(final Component component)
	{
		return Arrays.asList(names).stream().anyMatch(x -> component.getFriendlyName().equalsIgnoreCase(x));
	}
}
