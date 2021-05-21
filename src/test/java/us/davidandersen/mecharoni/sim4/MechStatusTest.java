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

class MechStatusTest
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

		mech.fire(0);

		assertThat("should have heat of a ClanERLargeLaser",
				mech.getHeat(), equalTo(11.8f));

		assertThat("weapon should be on cooldown",
				mech.getWeapons(), containsInAnyOrder(
						weaponMatching(WeaponStatus.builder()
								.withCooldown(4.0f))));

		assertThat("weapon group should be on cooldown",
				mech.getWeaponsGroupCooldown(3), equalTo(0.5f));

		mech.regen();

		assertThat("should have heat of a ClanERLargeLaser",
				mech.getHeat(), equalTo(11.8f - Heat.dissipation(4, 0) * 1 / 30f));

		assertThat("weapon should be on cooldown",
				mech.getWeapons(), containsInAnyOrder(
						weaponMatching(WeaponStatus.builder()
								.withCooldown(4.0f - 1 / 30f))));

		assertThat("weapon group should be on cooldown",
				mech.getWeaponsGroupCooldown(3), equalTo(0.5f - 1 / 30f));
	}

	@Test
	void testNoNegativeCooldowns()
	{
		final MechStatus mech = MechStatus.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("ClanERLargeLaser"))
				.build();

		mech.regen();

		assertThat("should have no heat",
				mech.getHeat(), equalTo(0f));

		assertThat("weapon shouldn't be on",
				mech.getWeapons(), containsInAnyOrder(
						weaponMatching(WeaponStatus.builder()
								.withCooldown(0))));

		assertThat("weapon group shouldn't be on cooldown",
				mech.getWeaponsGroupCooldown(3), equalTo(0f));
	}

	private List<WeaponStatus> weapons(final String name)
	{
		final Component llaser = component(name);

		final WeaponStatus weapon2 = WeaponStatus.builder()
				.withDamage(llaser.getDamage())
				.withHeat(llaser.getHeat())
				.withMaxCooldown(llaser.getCooldown())
				.withHeatPenaltyId(llaser.getHeatPenaltyId())
				.withMinHeatPenaltyLevel(llaser.getMinHeatPenaltyLevel())
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
