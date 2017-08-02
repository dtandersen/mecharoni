package us.davidandersen.mecharoni.evolve;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static us.davidandersen.mecharoni.entity.Location.LocationBuilder.location;
import java.io.FileNotFoundException;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.LocationType;
import us.davidandersen.mecharoni.entity.MechSpec;
import us.davidandersen.mecharoni.entity.MechSpec.MechSpecBuilder;
import us.davidandersen.mecharoni.repository.CompCache;
import us.davidandersen.mecharoni.repository.json.JsonComponentRepository;
import us.davidandersen.mecharoni.util.ComposeBuilder;

public class AssembleMechTest
{
	private CompCache compCache;

	@Before
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final JsonComponentRepository repo = new JsonComponentRepository();
		final List<Component> comps = repo.all();
		compCache = new CompCache(comps);
	}

	@Test
	public void abc()
	{
		final MechSpec mech = MechSpecBuilder.mech()
				.withLocation(location()
						.withLocationType(LocationType.LT)
						.withSlots(8)
						.withEnergy(1))
				.withComponent(LocationType.LT, component("C-ER LRG LASER"))
				.build();

		assertThat(mech.componentsInLocation(LocationType.LT), hasComponents("C-ER LRG LASER"));
	}

	@Test
	public void wontFit()
	{
		final MechSpec mech = MechSpecBuilder.mech()
				.withLocation(location()
						.withLocationType(LocationType.RT)
						.withSlots(8)
						.withEnergy(1))
				.withComponent(LocationType.RT, component("C-SRM 6"))
				.build();

		assertThat(mech.componentsInLocation(LocationType.RT), empty());
	}

	private Matcher<Iterable<? extends Component>> hasComponents(final String string)
	{
		final Matcher<Component> matcher = ComposeBuilder.compose(Component.class)
				.withFeature("friendlyName", Component::getFriendlyName, string)
				.build();
		return contains(matcher);
	}

	private Component component(final String string)
	{
		return compCache.getComp(string);
	}
}
