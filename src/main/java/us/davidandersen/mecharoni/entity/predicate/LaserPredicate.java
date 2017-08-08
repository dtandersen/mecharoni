package us.davidandersen.mecharoni.entity.predicate;

import java.util.function.Predicate;
import us.davidandersen.mecharoni.entity.Component;

public class LaserPredicate implements Predicate<Component>
{
	@Override
	public boolean test(final Component t)
	{
		return t.isEnergy();
	}
}
