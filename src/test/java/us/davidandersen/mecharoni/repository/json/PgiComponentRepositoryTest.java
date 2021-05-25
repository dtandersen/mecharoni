package us.davidandersen.mecharoni.repository.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.HardpointType;
import us.davidandersen.mecharoni.sim4.MechStatus;

public class PgiComponentRepositoryTest
{
	@Test
	void testAutoCannon20() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "AutoCannon20")).findFirst().get();

		assertThat(comp.getName(), is("AutoCannon20"));
		assertThat(comp.getFriendlyName(), is("AutoCannon20"));
		assertThat(comp.getSlots(), is(10));
		assertThat(comp.getType(), is(HardpointType.BALLISTIC.toString()));
		assertThat(comp.getDamage(), is(20f));
		assertThat(comp.getHeatPenalty(), is(24f));
		assertThat(comp.getMinHeatPenaltyLevel(), is(2));
		assertThat(comp.getHeatPenaltyId(), is(9));
		assertThat(comp.getHeat(), is(5f));
		assertThat(comp.getCooldown(), is(4f));
		assertThat(comp.getAmmoType(), is("AC20Ammo"));
		assertThat(comp.getDamageMultiplier(), is(1));
		assertThat(comp.getTons(), is(14f));
		assertThat(comp.getDuration(), is(0f));
		assertThat(comp.getMinRange(), is(0));
		assertThat(comp.getLongRange(), is(270));
		assertThat(comp.getMaxRange(), is(540));

		assertThat(comp.isInnerSphere(), is(true));
		assertThat(comp.isClan(), is(false));
		assertThat(comp.isWeapon(), is(true));
		assertThat(comp.isHeatSink(), is(false));
	}

	@Test
	void testClanSrm4() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "ClanSRM4")).findFirst().get();

		assertThat(comp.getName(), is("ClanSRM4"));
		assertThat(comp.getFriendlyName(), is("ClanSRM4"));
		assertThat(comp.getSlots(), is(1));
		assertThat(comp.getType(), is("MISSILE"));
		assertThat(comp.getDamage(), is(2f));
		assertThat(comp.getHeatPenalty(), is(1.75f));
		assertThat(comp.getMinHeatPenaltyLevel(), is(5));
		assertThat(comp.getHeatPenaltyId(), is(7));
		assertThat(comp.getHeat(), is(3f));
		assertThat(comp.getCooldown(), is(3f));
		assertThat(comp.getAmmoType(), is("ClanSRMAmmo"));
		assertThat(comp.getDamageMultiplier(), is(4));
		assertThat(comp.getTons(), is(1f));
		assertThat(comp.getDuration(), is(0f));
		assertThat(comp.getMinRange(), is(0));
		assertThat(comp.getLongRange(), is(270));
		assertThat(comp.getMaxRange(), is(270));

		assertThat(comp.isInnerSphere(), is(false));
		assertThat(comp.isClan(), is(true));
		assertThat(comp.isWeapon(), is(true));
		assertThat(comp.isMissile(), is(true));
		assertThat(comp.isHeatSink(), is(false));
	}

	@Test
	void testClanMrm40() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "MRM40")).findFirst().get();

		assertThat(comp.getType(), is(HardpointType.MISSILE.toString()));
	}

	@Test
	void testLrm5() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "LRM5")).findFirst().get();

		assertThat(comp.getName(), is("LRM5"));
		assertThat(comp.getFriendlyName(), is("LRM5"));
		assertThat(comp.getSlots(), is(1));
		assertThat(comp.getType(), is(HardpointType.MISSILE.toString()));
		assertThat(comp.getDamage(), is(1f));
		assertThat(comp.getHeatPenalty(), is(3.4f));
		assertThat(comp.getMinHeatPenaltyLevel(), is(5));
		assertThat(comp.getHeatPenaltyId(), is(2));
		assertThat(comp.getHeat(), is(2.2f));
		assertThat(comp.getCooldown(), is(3.25f));
		assertThat(comp.getAmmoType(), is("LRMAmmo"));
		assertThat(comp.getDamageMultiplier(), is(5));
		assertThat(comp.getTons(), is(2f));
		assertThat(comp.getDuration(), is(0f));
		assertThat(comp.getMinRange(), is(180));
		assertThat(comp.getLongRange(), is(900));
		assertThat(comp.getMaxRange(), is(900));

		assertThat(comp.isInnerSphere(), is(true));
		assertThat(comp.isClan(), is(false));
		assertThat(comp.isWeapon(), is(true));
		assertThat(comp.isHeatSink(), is(false));
	}

	@Test
	void testSmallLaser() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "SmallLaser")).findFirst().get();

		assertThat(comp.getName(), is("SmallLaser"));
		// assertThat(comp.getFriendlyName(), is("LRM5"));
		// assertThat(comp.getSlots(), is(1));
		assertThat(comp.getType(), is("BEAM"));
		// assertThat(comp.getDamage(), is(1f));
		// assertThat(comp.getHeatPenalty(), is(3.4f));
		// assertThat(comp.getMinHeatPenaltyLevel(), is(5));
		// assertThat(comp.getHeatPenaltyId(), is(2));
		// assertThat(comp.getHeat(), is(2.2f));
		// assertThat(comp.getCooldown(), is(3.25f));
		// assertThat(comp.getAmmoType(), is("LRMAmmo"));
		assertThat(comp.getDamageMultiplier(), is(1));
		// assertThat(comp.getTons(), is(2f));
		// assertThat(comp.getDuration(), is(0f));
		// assertThat(comp.getMinRange(), is(180));
		// assertThat(comp.getLongRange(), is(900));
		// assertThat(comp.getMaxRange(), is(900));
		//
		// assertThat(comp.isInnerSphere(), is(true));
		// assertThat(comp.isClan(), is(false));
		// assertThat(comp.isWeapon(), is(true));
		// assertThat(comp.isHeatSink(), is(false));
	}

	@Test
	void testClanSmallPulseLaser() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "ClanSmallPulseLaser")).findFirst().get();

		// assertThat(comp.getName(), is("SmallLaser"));
		// assertThat(comp.getFriendlyName(), is("LRM5"));
		// assertThat(comp.getSlots(), is(1));
		// assertThat(comp.getType(), is("BEAM"));
		// assertThat(comp.getDamage(), is(1f));
		// assertThat(comp.getHeatPenalty(), is(3.4f));
		// assertThat(comp.getMinHeatPenaltyLevel(), is(5));
		// assertThat(comp.getHeatPenaltyId(), is(2));
		// assertThat(comp.getHeat(), is(2.2f));
		// assertThat(comp.getCooldown(), is(3.25f));
		// assertThat(comp.getAmmoType(), is("LRMAmmo"));
		// assertThat(comp.getDamageMultiplier(), is(5));
		// assertThat(comp.getTons(), is(2f));
		// assertThat(comp.getDuration(), is(0f));

		assertThat(comp.getMinRange(), is(0));
		assertThat(comp.getLongRange(), is(160));
		assertThat(comp.getMaxRange(), is(320));
		//
		// assertThat(comp.isInnerSphere(), is(true));
		// assertThat(comp.isClan(), is(false));
		// assertThat(comp.isWeapon(), is(true));
		// assertThat(comp.isHeatSink(), is(false));
	}

	@Test
	void testLrmAmmo() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "LRMAmmo")).findFirst().get();

		assertThat(comp.getName(), is("LRMAmmo"));
		assertThat(comp.getFriendlyName(), is("LRMAmmo"));
		assertThat(comp.getSlots(), is(1));
		assertThat(comp.getType(), nullValue());
		assertThat(comp.getDamage(), is(0f));
		assertThat(comp.getHeatPenalty(), is(0f));
		assertThat(comp.getMinHeatPenaltyLevel(), is(0));
		assertThat(comp.getHeatPenaltyId(), is(0));
		assertThat(comp.getHeat(), is(0f));
		assertThat(comp.getCooldown(), is(0f));
		assertThat(comp.getAmmoType(), nullValue());
		assertThat(comp.getDamageMultiplier(), is(0));
		assertThat(comp.getTons(), is(1f));
		assertThat(comp.getDuration(), is(0f));
		assertThat(comp.getMinRange(), is(0));
		assertThat(comp.getLongRange(), is(0));
		assertThat(comp.getMaxRange(), is(0));

		// assertThat(comp.getNumShots(), is(240));

		assertThat(comp.isInnerSphere(), is(true));
		assertThat(comp.isClan(), is(false));
		assertThat(comp.isWeapon(), is(false));
		assertThat(comp.isHeatSink(), is(false));
	}

	@Test
	void testNoEngines() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Optional<Component> comp = all.stream().filter(c -> Objects.equals(c.getName(), "Engine_Std_60")).findFirst();

		assertThat(comp.isPresent(), is(false));
	}

	@Test
	void testNoUpgrades() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final PgiComponentRepository repo = new PgiComponentRepository();
		final List<Component> all = repo.all();
		final Optional<Component> comp = all.stream().filter(c -> Objects.equals(c.getName(), "ClanDoubleHeatSinkType")).findFirst();

		assertThat(comp.isPresent(), is(false));
	}

	@Test
	void testDamage()
	{
		assertThat(MechStatus.calcDamage(10, 0, 100, 200, 0), is(10f));
		assertThat(MechStatus.calcDamage(10, 150, 100, 200, 0), is(5f));

		assertThat(MechStatus.calcDamage(5, 300, 160, 320, 0), is(.625f));
	}
}
