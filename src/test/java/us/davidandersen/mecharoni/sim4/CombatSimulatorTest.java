package us.davidandersen.mecharoni.sim4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Component;
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
		final MechStatus mech = MechStatus.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("ClanERLargeLaser"))
				.build();

		final CombatSimulator sim = new CombatSimulator(mech);

		sim.tick();

		assertThat("should have heat of a ClanERLargeLaser",
				sim.getStatus().getHeat(), equalTo(11.8f));

		assertThat("weapon should be on cooldown",
				sim.getWeapons(), containsInAnyOrder(
						weaponMatching(WeaponStatus.builder()
								.withCooldown(4.0f))));

		sim.tick();

		assertThat("should dissipate 1/30th of heat",
				sim.getStatus().getHeat(), equalTo(11.8f - mech.getHeatDisipation() * 1 / 30f));
	}

	private List<WeaponStatus> weapons(final String name)
	{
		final Component llaser = component(name);

		final WeaponStatus weapon2 = WeaponStatus.builder()
				.withDamage(llaser.getDamage())
				.withHeat(llaser.getHeat())
				.withCooldown(llaser.getCooldown())
				.build();

		final List<WeaponStatus> weapons = new ArrayList<WeaponStatus>();
		weapons.add(weapon2);
		return weapons;
	}

	private Matcher<WeaponStatus> weaponMatching(final WeaponStatusBuilder builder)
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
