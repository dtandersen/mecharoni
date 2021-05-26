package us.davidandersen.mecharoni.sim4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
import us.davidandersen.mecharoni.repository.json.PgiComponentRepository;
import us.davidandersen.mecharoni.sim4.MechWeapon.WeaponStatusBuilder;

class CombatSimulatorTest
{
	private Components compCache;

	@BeforeEach
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Component> components = new PgiComponentRepository().all();
		compCache = new Components(components);
	}

	@Test
	void testFireLaser()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("ClanERLargeLaser"))
				.build();

		final CombatSimulator sim = new CombatSimulator(mech, 50);

		sim.tick();

		assertThat("should have heat of a ClanERLargeLaser",
				sim.getStatus().getHeat(), equalTo(10f));

		assertThat("weapon should be on cooldown",
				sim.getWeapons(), containsInAnyOrder(
						weaponMatching(MechWeapon.builder()
								.withCooldown(4.5f))));

		assertThat("target should take damage",
				sim.getTarget().getDamage(), is(11f));

		sim.tick();

		assertThat("should dissipate one tick of heat",
				sim.getStatus().getHeat(), equalTo(10f - mech.getHeatDisipation() * CombatSimulator.TICK_TIME));
	}

	private List<MechWeapon> weapons(final String name)
	{
		final Component llaser = component(name);

		final MechWeapon weapon2 = MechWeapon.builder()
				.withDamage(llaser.getDamage())
				.withHeat(llaser.getHeat())
				.withMaxCooldown(llaser.getCooldown())
				.withOptimalRange(llaser.getLongRange())
				.withMaxRange(llaser.getMaxRange())
				.withMinRange(llaser.getMinRange())
				.build();

		final List<MechWeapon> weapons = new ArrayList<MechWeapon>();
		weapons.add(weapon2);
		return weapons;
	}

	private Matcher<MechWeapon> weaponMatching(final WeaponStatusBuilder builder)
	{
		final MechWeapon weaponStatus = builder.build();

		return ComposeBuilder.of(MechWeapon.class)
				.withDescription("a weapon with")
				.withFeature("cooldown", MechWeapon::getCooldown, weaponStatus.getCooldown())
				.build();
	}

	private Component component(final String name)
	{
		return compCache.getComponentByName(name);
	}
}
