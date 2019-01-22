package us.davidandersen.mecharoni.entity;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import java.io.FileNotFoundException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.MechBuild.MechBuildBuilder;
import us.davidandersen.mecharoni.entity.Quirk.QuirkBuilder;
import us.davidandersen.mecharoni.repository.CompCache;
import us.davidandersen.mecharoni.repository.json.JsonComponentRepository;

public class MechBuildTest
{
	private CompCache compCache;

	@Before
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Component> components = new JsonComponentRepository().all();
		compCache = new CompCache(components);
	}

	@Test
	public void quirk()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.RA, component("LRG PULSE LASER"))
				.withQuirks(QuirkBuilder.quirk()
						.withQuirkType(QuirkType.LASER_DURATION)
						.withValue(-.1f))
				.build();

		assertThat((double)mech.getWeapon("LRG PULSE LASER").getDuration(), closeTo(0.67 * (1 - .1), .01));
	}

	private Component component(final String name)
	{
		return compCache.getComp(name);
	}
}
