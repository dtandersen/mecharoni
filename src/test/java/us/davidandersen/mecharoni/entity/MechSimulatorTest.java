package us.davidandersen.mecharoni.entity;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import java.io.FileNotFoundException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.MechBuild.MechBuildBuilder;
import us.davidandersen.mecharoni.repository.CompCache;
import us.davidandersen.mecharoni.repository.json.JsonComponentRepository;

public class MechSimulatorTest
{
	private CompCache compCache;

	@Before
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Component> components = new JsonComponentRepository().all();
		compCache = new CompCache(components);
	}

	@Test
	public void test()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.RA, component("ClanERLargeLaser"))
				.withComponent(LocationType.RA, component("ClanERLargeLaser"))
				.build());
		sim.go(3.75f + 1.35f, 500);
		assertThat(sim.damage(), equalTo(44f));
	}

	@Test
	public void tooMuchHeat()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.RA, component("SnubNosePPC"))
				.withComponent(LocationType.RA, component("SnubNosePPC"))
				.build());
		sim.go(20f, 200);
		assertThat(sim.damage(), equalTo(80f));
	}

	@Test
	public void noAmmo()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.LT, component("SRM6"))
				.build());
		sim.go(0f, 200);
		assertThat(sim.damage(), equalTo(0f));
	}

	@Test
	public void useAllAmmo()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.LT, component("SRM4"))
				.withComponent(LocationType.LT, component("SRMAmmo"))
				.withComponent(LocationType.LT, component("SRMAmmo"))
				.build());
		sim.go(3 * 55, 200);
		assertThat((double)sim.damage(), closeTo(8.6 * 50, .001));
	}

	@Test
	public void outOfRange()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.LT, component("SRM4"))
				.withComponent(LocationType.LT, component("SRMAmmo"))
				.build());
		sim.go(0, 271);
		assertThat((double)sim.damage(), is(0d));
	}

	@Test
	public void tooClose()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.RA, component("LightPPC"))
				.build());
		sim.go(0, 0);
		assertThat((double)sim.damage(), is(0d));
	}

	@Test
	public void longRange()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.RA, component("ClanERLargeLaser"))
				.build());
		sim.go(0, 1110);
		assertThat(sim.damage(), equalTo(11 / 2f));
	}

	@Test
	public void longRange2()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.LA, component("ERMediumLaser"))
				.build());
		sim.go(0, 700);
		assertThat((double)sim.damage(), closeTo(.277, .001));
	}

	@Test
	public void machinegun()
	{
		final MechSimulator sim = new MechSimulator();
		sim.addMech(MechBuildBuilder.mech()
				.withSpec(PrefabMechs.cyclopsBuilder())
				.withComponent(LocationType.RT, component("MACHINE GUN"))
				.withComponent(LocationType.LT, component("MACHINE GUN"))
				.withComponent(LocationType.LT, component("MACHINE GUN AMMO"))
				.build());
		sim.go(.1f, 100);
		assertThat((double)sim.damage(), closeTo(.4, .001));
	}

	private Component component(final String name)
	{
		return compCache.getComp(name);
	}
}
