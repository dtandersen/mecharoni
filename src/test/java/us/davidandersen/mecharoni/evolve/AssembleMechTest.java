package us.davidandersen.mecharoni.evolve;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.HardpointType;
import us.davidandersen.mecharoni.entity.LocationType;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.entity.MechBuild.MechBuildBuilder;
import us.davidandersen.mecharoni.entity.PrefabMechs;
import us.davidandersen.mecharoni.repository.CompCache;
import us.davidandersen.mecharoni.repository.json.JsonComponentRepository;
import us.davidandersen.mecharoni.util.ComposeBuilder;

public class AssembleMechTest
{
	private CompCache compCache;

	@BeforeEach
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final JsonComponentRepository repo = new JsonComponentRepository();
		final List<Component> comps = repo.all();
		compCache = new CompCache(comps);
	}

	@Test
	public void abc()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.genericMech())
				.withComponent(LocationType.LT, component("C-ER LRG LASER"))
				.build();

		assertThat(mech.componentsInLocation(LocationType.LT), hasComponents("C-ER LRG LASER"));
	}

	@Test
	public void noHardpointDefined()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.genericMech())
				.withComponent(LocationType.RT, component("C-SRM 6"))
				.build();

		assertThat(mech.componentsInLocation(LocationType.RT), empty());
	}

	@Test
	public void tooHeavy()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.genericMech())
				.withComponent(LocationType.RT, component("AC/10"))
				.build();

		assertThat(mech.componentsInLocation(LocationType.RT), empty());
	}

	@Test
	public void cyclops()
	{
		final MechBuild mech = MechBuildBuilder.mech().withSpec(
				PrefabMechs.cyclopsBuilder())
				.withComponent(LocationType.RT, component("AC/10"))
				.build();

		assertThat(mech.componentsInLocation(LocationType.RT), hasComponents("AC/10"));
		assertThat(mech.getMaxSlots(), equalTo(41));
		assertThat(mech.maxHardpoints(HardpointType.ENERGY), equalTo(4));
		assertThat(mech.maxHardpoints(HardpointType.MISSILE), equalTo(1));
		assertThat(mech.maxHardpoints(HardpointType.BALLISTIC), equalTo(4));
		assertThat(mech.maxHardpoints(HardpointType.AMS), equalTo(1));
		assertThat(mech.maxHardpoints(HardpointType.ECM), equalTo(0));
	}

	/**
	 * LT: AC/2 AMMO
	 * LT: Double Heat Sink
	 * LT: Double Heat Sink
	 * LT: AC/2 AMMO
	 * RA: Double Heat Sink
	 * RA: LRG PULSE LASER
	 * RA: LRG PULSE LASER
	 * RT: Double Heat Sink
	 * RT: Double Heat Sink
	 * RT: AC/2
	 * LA: Double Heat Sink
	 * LA: HEAVY PPC
	 */
	@Test
	public void sleipnir()
	{
		final MechBuild mech = MechBuildBuilder.mech().withSpec(
				PrefabMechs.cyclopsBuilder()
						.withTons(40)
						.withFreeSlots(27))
				.withComponent(LocationType.LT, component("AC/2 AMMO"))
				.withComponent(LocationType.LT, component("Double Heat Sink"))
				.withComponent(LocationType.LT, component("Double Heat Sink"))
				.withComponent(LocationType.LT, component("AC/2 AMMO"))
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RA, component("LRG PULSE LASER"))
				.withComponent(LocationType.RA, component("LRG PULSE LASER"))
				.withComponent(LocationType.RT, component("Double Heat Sink"))
				.withComponent(LocationType.RT, component("Double Heat Sink"))
				.withComponent(LocationType.RT, component("AC/2 AMMO"))
				.withComponent(LocationType.LA, component("Double Heat Sink"))
				.withComponent(LocationType.LA, component("HEAVY PPC"))
				.build();

		assertThat(mech.freeSlots(), equalTo(2));
		assertThat(mech.componentsInLocation(LocationType.LA), hasComponents("Double Heat Sink"));
	}

	@Test
	public void locationOverflow()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.locustBuilder())
				.withComponent(LocationType.HEAD, component("Double Heat Sink"))
				.build();

		assertThat(mech.freeSlots(), equalTo(17));
	}

	@Test
	public void fillLocation()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.locustBuilder())
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.build();

		assertThat(mech.componentsInLocation(LocationType.RA), hasComponents("Double Heat Sink", "Double Heat Sink", "Double Heat Sink"));
		assertThat(mech.freeSlots(), equalTo(8));
	}

	@Test
	public void fillMech()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.locustBuilder())
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RA, component("Double Heat Sink"))
				.withComponent(LocationType.RT, component("Double Heat Sink"))
				.withComponent(LocationType.RT, component("Double Heat Sink"))
				.build();
		assertThat(mech.componentsInLocation(LocationType.RA), hasComponents("Double Heat Sink", "Double Heat Sink", "Double Heat Sink"));
		assertThat(mech.componentsInLocation(LocationType.RT), hasComponents("Double Heat Sink", "Double Heat Sink"));
		assertThat(mech.freeSlots(), equalTo(2));
	}

	@Test
	public void fillAllSlotsInLocation()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.locustBuilder()
						.withTons(9.5f)
						.withFreeSlots(17))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.build();
		assertThat(mech.componentsInLocation(LocationType.RA), Matchers.hasSize(10));
		assertThat(mech.componentsInLocation(LocationType.RA), hasComponents(
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)"));
		assertThat(mech.freeSlots(), equalTo(7));

	}

	@Test
	public void fillAllSlots()
	{
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.locustBuilder()
						.withTons(9.5f)
						.withFreeSlots(17))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RA, component("SRM AMMO (1/2)"))

				.withComponent(LocationType.RT, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RT, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RT, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RT, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RT, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RT, component("SRM AMMO (1/2)"))
				.withComponent(LocationType.RT, component("SRM AMMO (1/2)"))
				.build();
		assertThat(mech.componentsInLocation(LocationType.RA), Matchers.hasSize(10));
		assertThat(mech.componentsInLocation(LocationType.RT), Matchers.hasSize(7));

		assertThat(mech.componentsInLocation(LocationType.RA), hasComponents(
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)"));
		assertThat(mech.componentsInLocation(LocationType.RT), hasComponents(
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)",
				"SRM AMMO (1/2)"));
		assertThat(mech.freeSlots(), equalTo(0));

	}

	private Matcher<Iterable<? extends Component>> hasComponents(final String... friendlyNames)
	{
		final List<Matcher<? super Component>> matchers = Arrays.stream(friendlyNames).map(friendlyName -> componentMatcher(friendlyName)).collect(Collectors.toList());
		return contains(matchers);
	}

	private Matcher<? super Component> componentMatcher(final String friendlyName)
	{
		final Matcher<Component> matcher = ComposeBuilder.compose(Component.class)
				.withFeature("friendlyName", Component::getFriendlyName, friendlyName)
				.build();
		return matcher;
	}

	private Component component(final String string)
	{
		return compCache.getComp(string);
	}
}
