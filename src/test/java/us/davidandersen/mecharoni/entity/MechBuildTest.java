package us.davidandersen.mecharoni.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.MechBuild.MechBuildBuilder;
import us.davidandersen.mecharoni.entity.Quirk.QuirkBuilder;
import us.davidandersen.mecharoni.repository.Components;
import us.davidandersen.mecharoni.repository.json.SmurfyComponentRepository;

public class MechBuildTest
{
	private Components components;

	private MechBuild boarsHead;

	@BeforeEach
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		components = new Components(new SmurfyComponentRepository().all());
		boarsHead = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(LocationType.RA, component("LRG PULSE LASER"), 1)
				.withComponent(LocationType.LA, component("MEDIUM LASER"), 2)
				.withComponent(LocationType.LA, component("MEDIUM LASER"), 2)
				.withQuirks(QuirkBuilder.quirk()
						.withQuirkType(QuirkType.LASER_DURATION)
						.withValue(-.1f))
				.build();
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

	@Test
	public void startsAtZeroHeat()
	{
		assertThat(boarsHead.getHeat(), is(0f));
	}

	@Test
	public void firingRaisesHeat()
	{
		boarsHead.fire(1);
		assertThat(boarsHead.getHeat(), is(component("LRG PULSE LASER").getHeat()));
	}

	@Test
	public void fireTwoWeapons()
	{
		boarsHead.fire(2);
		assertThat(boarsHead.getHeat(), is(component("MEDIUM LASER").getHeat() * 2));
	}

	@Test
	public void cooldown()
	{
		boarsHead.fire(2);
		boarsHead.update(.1f);
		assertThat((double)boarsHead.getHeat(), closeTo(component("MEDIUM LASER").getHeat() * 2 - boarsHead.hps() * .1, .0000001));
	}

	@Test
	public void cantCoolBelowZero()
	{
		boarsHead.fire(2);
		boarsHead.update(5f);
		assertThat(boarsHead.getHeat(), is(0f));
	}

	private Component component(final String name)
	{
		return components.getComponentByName(name);
	}
}
