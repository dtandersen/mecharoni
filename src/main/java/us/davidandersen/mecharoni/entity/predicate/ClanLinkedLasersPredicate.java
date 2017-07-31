package us.davidandersen.mecharoni.entity.predicate;

import java.util.Arrays;
import java.util.function.Predicate;
import us.davidandersen.mecharoni.entity.Component;

public class ClanLinkedLasersPredicate implements Predicate<Component>
{
	@Override
	public boolean test(final Component component)
	{
		if (component.getFriendlyName() == null) { return false; }

		final String[] lasers = new String[] {
				"C-ER SML LASER",
				"C-ER MED LASER",
				"C-MICRO PULSE LASER",
				"C-SML PULSE LASER",
				"C-MED PULSE LASER",
				"C-ER MICRO LASER",
				"C-HEAVY SML LASER",
				"C-HEAVY MED LASER"
		};

		return Arrays.asList(lasers).stream().anyMatch(x -> component.getFriendlyName().equalsIgnoreCase(x));
	}
}
