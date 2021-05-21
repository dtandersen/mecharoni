package us.davidandersen.mecharoni.sim4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileNotFoundException;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.LocationType;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.entity.MechBuild.MechBuildBuilder;
import us.davidandersen.mecharoni.entity.PrefabMechs;
import us.davidandersen.mecharoni.entity.Slot.SlotBuilder;
import us.davidandersen.mecharoni.repository.Components;
import us.davidandersen.mecharoni.repository.json.JsonComponentRepository;
import us.davidandersen.mecharoni.sim4.WeaponStatus.WeaponStatusBuilder;

class CombatSimulatorTest
{
	private Components compCache;

	@BeforeEach
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Component> components = new JsonComponentRepository().all();
		compCache = new Components(components);
	}

	@Test
	void testFireLaser()
	{
		final CombatSimulator sim = new CombatSimulator();
		final Component llaser = component("ClanERLargeLaser");
		final MechBuild mech = MechBuildBuilder.mech()
				.withSpec(PrefabMechs.boarsHead())
				.withComponent(new SlotBuilder()
						.withComponent(llaser)
						.withLocation(LocationType.LA))
				.build();

		sim.addMech(mech);

		sim.tick();

		assertThat(sim.getStatus().getHeat(), equalTo(11.8f));
		assertThat(sim.getWeapons(), containsInAnyOrder(
				weaponMatcher(WeaponStatus.builder()
						.withCooldown(4.0f))));

		sim.tick();

		assertThat(sim.getStatus().getHeat(), equalTo(11.8f - mech.getHeatDisipation() * 1 / 30f));
	}

	private Matcher<? super WeaponStatus> weaponMatcher(final WeaponStatusBuilder builder)
	{
		final WeaponStatus weaponStatus = builder.build();

		return ComposeBuilder.of(WeaponStatus.class)
				.withDescription("a weapon with")
				.withFeature("cooldown", WeaponStatus::getCooldown, weaponStatus.getCooldown())
				.build();
	}

	private Component component(final String name)
	{
		return compCache.getComponentByName(name);
	}
}
