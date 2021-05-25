package us.davidandersen.mecharoni.repository.json;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.BasicComponent;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.HardpointType;
import us.davidandersen.mecharoni.repository.ComponentRepository;
import us.davidandersen.mecharoni.repository.json.SmurfyAmmoReader.AmmoJson;

public class SmurfyComponentRepository implements ComponentRepository
{
	private final SmurfyWeaponReader jsonWeaponReader;

	private final SmurfyAmmoReader jsonAmmoReader;

	public SmurfyComponentRepository()
	{
		jsonWeaponReader = new SmurfyWeaponReader();
		jsonAmmoReader = new SmurfyAmmoReader();
	}

	@Override
	public List<Component> all() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<BasicComponent> components = new ArrayList<>();

		readWeapons(components);
		readAmmo(components);
		readHeatSinks(components);

		return Collections.unmodifiableList(components);
	}

	@Override
	public List<Component> clanComponents() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Component> clanItems = all().stream()
				.filter(item -> item.isClan())
				.collect(Collectors.toList());

		return Collections.unmodifiableList(clanItems);
	}

	@Override
	public List<Component> isComponents() throws Exception
	{
		final List<Component> clanItems = all().stream()
				.filter(item -> item.isInnerSphere())
				.collect(Collectors.toList());

		return Collections.unmodifiableList(clanItems);
	}

	private void readWeapons(final List<BasicComponent> components)
	{
		final HashMap<String, SmurfyWeaponReader.WeaponJson> weapons = jsonWeaponReader.readWeapons();

		weapons.values().stream()
				.forEach(it -> components.add(BasicComponent.builder()
						.withId(it.id)
						.withTons(it.tons)
						.withSlots(it.slots)
						.withName(it.name)
						.withFriendlyName(MwoEscaper.unescape(it.translated_name))
						.withDamage(it.calc_stats.dmg)
						.withCooldown(it.cooldown)
						.withDuration(it.duration < 0 ? .1f : it.duration)
						.withType(Objects.equals("MISSLE", it.type) ? HardpointType.MISSILE.toString() : it.type)
						.withHeat(it.heat)
						.withMinRange(it.min_range)
						.withLongRange(it.long_range)
						.withMaxRange(it.max_range)
						.withDamageMultiplier(it.calc_stats.damageMultiplier)
						.withMinHeatPenaltyLevel(it.min_heat_penalty_level)
						.withHeatPenalty(it.heat_penalty)
						.withHeatPenaltyId(it.heat_penalty_id)

						.build()));
	}

	private void readAmmo(final List<BasicComponent> components)
	{
		final HashMap<String, AmmoJson> ammo = jsonAmmoReader.readAmmo();

		ammo.values().stream()
				.forEach(component -> components.add(BasicComponent.builder()
						.withId(component.id)
						.withTons(component.tons)
						.withSlots(component.slots)
						.withName(component.type)
						.withFriendlyName(MwoEscaper.unescape(component.translated_name))
						.withType(null)
						.withNumShots(component.num_shots)
						.build()));

		for (final AmmoJson ammo2 : ammo.values())
		{
			for (final String id : ammo2.weapons)
			{
				final BasicComponent weapon = components.stream().filter(c -> Objects.equals(id, c.getId())).findFirst().get();
				weapon.setAmmoType(ammo2.type);
			}
		}
	}

	private void readHeatSinks(final List<BasicComponent> components)
	{
		components.add(BasicComponent.builder()
				.withName("ClanDoubleHeatSink")
				.withFriendlyName("Clan Double Heat Sink")
				.heatSink()
				.withSlots(2)
				.withTons(1)
				.build());

		components.add(BasicComponent.builder()
				.withName("DoubleHeatSink")
				.withFriendlyName("Double Heat Sink")
				.heatSink()
				.withSlots(3)
				.withTons(1)
				.build());

		components.add(BasicComponent.builder()
				.withName("EmptySlot")
				.withFriendlyName("Empty Slot")
				.empty()
				.build());
	}
}
