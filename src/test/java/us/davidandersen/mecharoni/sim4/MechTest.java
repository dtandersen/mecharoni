package us.davidandersen.mecharoni.sim4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
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

class MechTest
{
	private Components compCache;

	private TargetDummy target;

	@BeforeEach
	public void setUp() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Component> components = new PgiComponentRepository().all();
		compCache = new Components(components);
		target = new TargetDummy();
	}

	@Test
	void testFireLaser()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				// .withWeapons(weapons("ClanERLargeLaser"))
				.withComponents(components("ClanERLargeLaser"))
				.build();

		assertThat(mech.getWeapon(0).isReady(), is(true));

		assertThat("mech has suffient heat to fire",
				mech.isWeaponReady(0), is(true));

		mech.fire(0, target, 50);

		assertThat("target should take damage",
				target.getDamage(), is(11f));

		assertThat("should have heat of a ClanERLargeLaser",
				mech.getHeat(), equalTo(10f));

		assertThat("weapon should be on cooldown",
				mech.getWeapons(), containsInAnyOrder(
						weaponMatching(MechWeapon.builder()
								.withCooldown(4.5f))));

		assertThat("weapon group should be on cooldown",
				mech.getWeaponsGroupCooldown(3), equalTo(0.5f));

		mech.regen(CombatSimulator.TICK_TIME);

		assertThat("should have heat of a ClanERLargeLaser",
				mech.getHeat(), equalTo(10f - Heat.dissipation(4, 0) * CombatSimulator.TICK_TIME));

		assertThat("weapon should be on cooldown",
				mech.getWeapons(), containsInAnyOrder(
						weaponMatching(MechWeapon.builder()
								.withCooldown(4.5f - CombatSimulator.TICK_TIME))));

		assertThat("weapon group should be on cooldown",
				mech.getWeaponsGroupCooldown(3), equalTo(0.5f - CombatSimulator.TICK_TIME));
	}

	@Test
	void testWeaponOnCooldown()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("ClanERLargeLaser"))
				.build();

		assertThat(mech.getWeapon(0).isReady(), is(true));

		mech.fire(0, target, 50);

		assertThat(mech.getWeapon(0).isReady(), is(false));

		mech.regen(0.5f);

		assertThat(mech.getWeapon(0).isReady(), is(false));

		mech.regen(4f);

		assertThat(mech.getWeapon(0).isReady(), is(true));
	}

	@Test
	void testInsufficientHeatCapacity()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withHeat(28.1f)
				.withWeapons(weapons("ClanERLargeLaser"))
				.build();

		assertThat(mech.getAvailableHeat(), lessThan(10f));

		assertThat("weapon should be off cooldown",
				mech.getWeapon(0).isReady(), is(true));

		assertThat("mech has insuffient heat to fire",
				mech.isWeaponReady(0), is(false));

		mech.regen(1f);

		assertThat("sufficient heat to fire",
				mech.isWeaponReady(0), is(true));
	}

	@Test
	void testAc20HeatPenaltyCooldown()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("AutoCannon20", "AutoCannon20"))
				.build();

		assertThat("mech has insuffient heat to fire",
				mech.isWeaponReady(1), is(true));

		mech.fire(0, target, 50);

		assertThat("ac/20 penalty group in cooldown",
				mech.getHeatPenaltyGroupCooldown(9), is(0.5f));

		assertThat("first ac/10 on cooldown",
				mech.isWeaponReady(0), is(false));

		assertThat("can't fire 2nd ac/20",
				mech.isWeaponReady(1), is(false));

		mech.regen(0.5f);

		assertThat("insufficient heat to fire",
				mech.isWeaponReady(0), is(false));

		assertThat("off heat penalty cooldown",
				mech.isWeaponReady(1), is(true));
	}

	@Test
	void testLargeLaserHeatPenaltyCooldown()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("ERLargeLaser", "ERLargeLaser", "ERLargeLaser", "ERLargeLaser"))
				.build();

		assertThat("mech has insuffient heat to fire",
				mech.isWeaponReady(1), is(true));

		// fire 3 ERLargeLaser
		mech.fire(0, target, 50);
		mech.fire(1, target, 50);
		mech.fire(2, target, 50);

		assertThat("ERLargeLaser penalty group in cooldown",
				mech.getHeatPenaltyGroupCooldown(3), is(0.5f));

		assertThat("ERLargeLaser penalty group in cooldown",
				mech.getHeatPenaltyGroupCooldownCount(3), is(3));

		assertThat("4th ERLargeLaser would incur a heat penalty",
				mech.isWeaponReady(3), is(false));
	}

	@Test
	void testUseAmmo()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withComponents(components("LRM20", "LRMAmmo", "LRMAmmo"))
				.build();

		// fire 3 ERLargeLaser
		assertThat(mech.hasAmmo("LRMAmmo"), is(480));
		mech.fire(0, target, 50);
		assertThat(mech.hasAmmo("LRMAmmo"), is(460));
	}

	@Test
	void testInsufficientAmmo()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withComponents(components("LRM20", "LRMAmmo"))
				.build();

		mech.ammo("LRMAmmo", 0);
		// fire 3 ERLargeLaser
		assertThat(mech.hasAmmo("LRMAmmo"), is(0));
		assertThat(mech.isWeaponReady(0), is(false));
	}

	@Test
	void testNotAmmo()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withComponents(components("LRM20"))
				.build();

		// mech.ammo("LRMAmmo", 0);
		// fire 3 ERLargeLaser
		assertThat(mech.hasAmmo("LRMAmmo"), is(0));
		assertThat(mech.isWeaponReady(0), is(false));
	}

	@Test
	void testAc10HeatPenaltyCooldown()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(20)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("AutoCannon10", "AutoCannon10", "AutoCannon10", "UltraAutoCannon10"))
				.build();

		mech.fire(0, target, 50);
		mech.fire(1, target, 50);

		assertThat("ac/20 penalty group in cooldown",
				mech.getHeatPenaltyGroupCooldownCount(8), is(2));

		assertThat("sufficient heat to fire",
				mech.isWeaponReady(2), is(true));

		assertThat("sufficient heat to fire",
				mech.isWeaponReady(3), is(false));
	}

	@Test
	void testNoNegativeCooldowns()
	{
		final Mech mech = Mech.builder()
				.withInternalHeatSinks(4)
				.withExternalHeatSinks(0)
				.withWeapons(weapons("ClanERLargeLaser"))
				.build();

		mech.regen(CombatSimulator.TICK_TIME);

		assertThat("should have no heat",
				mech.getHeat(), equalTo(0f));

		assertThat("weapon shouldn't be on",
				mech.getWeapons(), containsInAnyOrder(
						weaponMatching(MechWeapon.builder()
								.withCooldown(0))));

		assertThat("weapon group shouldn't be on cooldown",
				mech.getWeaponsGroupCooldown(3), equalTo(0f));
	}

	private List<MechComponent> components(final String... names)
	{
		final List<MechComponent> weapons = new ArrayList<>();

		for (final String name : names)
		{
			final Component component = component(name);

			if (component.isWeapon())
			{
				final MechWeapon weapon2 = MechWeapon.builder()
						.withDamage(component.getDamage())
						.withHeat(component.getHeat())
						.withMaxCooldown(component.getCooldown())
						.withHeatPenaltyId(component.getHeatPenaltyId())
						.withMinHeatPenaltyLevel(component.getMinHeatPenaltyLevel())
						.withOptimalRange(component.getLongRange())
						.withMaxRange(component.getMaxRange())
						.withMinRange(component.getMinRange())
						.withAmmoType(component.getAmmoType())
						.withAmmoPerShot(component.getAmmoPerShot())
						.build();
				weapons.add(weapon2);
			}
			else if (component.isAmmo())
			{
				final MechAmmo ammo = MechAmmo.builder()
						.withName(component.getName())
						.withNumShots(component.getNumShots())
						.build();
				weapons.add(ammo);
			}
		}

		return weapons;
	}

	private List<MechWeapon> weapons(final String... names)
	{
		final List<MechWeapon> weapons = new ArrayList<MechWeapon>();

		for (final String name : names)
		{
			final Component llaser = component(name);

			final MechWeapon weapon2 = MechWeapon.builder()
					.withDamage(llaser.getDamage())
					.withHeat(llaser.getHeat())
					.withMaxCooldown(llaser.getCooldown())
					.withHeatPenaltyId(llaser.getHeatPenaltyId())
					.withMinHeatPenaltyLevel(llaser.getMinHeatPenaltyLevel())
					.withOptimalRange(llaser.getLongRange())
					.withMaxRange(llaser.getMaxRange())
					.withMinRange(llaser.getMinRange())
					.build();

			weapons.add(weapon2);
		}
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
