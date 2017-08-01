package us.davidandersen.mecharoni.entity.predicate;

import java.util.Arrays;
import java.util.function.Predicate;
import us.davidandersen.mecharoni.entity.Component;

public class PpcPenaltyGroupPredicate implements Predicate<Component>
{
	@Override
	public boolean test(final Component component)
	{
		if (component.getFriendlyName() == null) { return false; }

		final String[] lasers = new String[] {
				"ER PPC",
				"PPC",
				"GAUSS RIFLE",
				"HEAVY GAUSS RIFLE",
				"LIGHT PPC",
				"HEAVY PPC",
				"SNUB-NOSE PPC",
				"C-GAUSS RIFLE",
				"C-ER PPC",
		};

		return Arrays.asList(lasers).stream().anyMatch(x -> component.getFriendlyName().equalsIgnoreCase(x));
	}
}
