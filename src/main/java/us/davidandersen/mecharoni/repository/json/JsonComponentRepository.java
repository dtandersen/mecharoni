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
import us.davidandersen.mecharoni.entity.BasicComponent.ComponentBuilder;
import us.davidandersen.mecharoni.repository.ComponentRepository;
import us.davidandersen.mecharoni.repository.json.JsonAmmoReader.AmmoJson;

public class JsonComponentRepository implements ComponentRepository
{
	private final JsonWeaponReader jsonWeaponReader;

	private final JsonAmmoReader jsonAmmoReader;

	public JsonComponentRepository()
	{
		jsonWeaponReader = new JsonWeaponReader();
		jsonAmmoReader = new JsonAmmoReader();
	}

	@Override
	public List<BasicComponent> all() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<BasicComponent> components = new ArrayList<>();

		readWeapons(components);
		readAmmo(components);
		readHeatSinks(components);

		return Collections.unmodifiableList(components);
	}

	@Override
	public List<BasicComponent> clanComponents() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<BasicComponent> clanItems = all().stream()
				.filter(item -> item.isClan())
				.collect(Collectors.toList());

		return Collections.unmodifiableList(clanItems);
	}

	@Override
	public List<BasicComponent> isComponents() throws Exception
	{
		final List<BasicComponent> clanItems = all().stream()
				.filter(item -> item.isInnerSphere())
				.collect(Collectors.toList());

		return Collections.unmodifiableList(clanItems);
	}

	private void readWeapons(final List<BasicComponent> components)
	{
		final HashMap<String, JsonWeaponReader.WeaponJson> weapons = jsonWeaponReader.readWeapons();

		weapons.values().stream()
				.forEach(it -> components.add(ComponentBuilder.component()
						.withId(it.id)
						.withTons(it.tons)
						.withSlots(it.slots)
						.withName(it.name)
						.withFriendlyName(MwoEscaper.unescape(it.translated_name))
						.withDamage(it.calc_stats.dmg)
						.withCooldown(it.cooldown)
						.withDuration(it.duration < 0 ? .1f : it.duration)
						.withType(it.type)
						.withHeat(it.heat)
						.withMinRange(it.min_range)
						.withLongRange(it.long_range)
						.withMaxRange(it.max_range)
						.withDamageMultiplier(it.calc_stats.damageMultiplier)
						.build()));
	}

	private void readAmmo(final List<BasicComponent> components)
	{
		final HashMap<String, AmmoJson> ammo = jsonAmmoReader.readAmmo();

		ammo.values().stream()
				.forEach(component -> components.add(ComponentBuilder.component()
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
		components.add(new ComponentBuilder()
				.withName("ClanDoubleHeatSink")
				.withFriendlyName("Clan Double Heat Sink")
				.heatSink()
				.withSlots(2)
				.withTons(1)
				.build());

		components.add(new ComponentBuilder()
				.withName("DoubleHeatSink")
				.withFriendlyName("Double Heat Sink")
				.heatSink()
				.withSlots(3)
				.withTons(1)
				.build());

		components.add(new ComponentBuilder()
				.withName("EmptySlot")
				.withFriendlyName("Empty Slot")
				.empty()
				.build());
	}
}
