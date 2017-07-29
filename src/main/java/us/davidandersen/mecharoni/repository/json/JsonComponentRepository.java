package us.davidandersen.mecharoni.repository.json;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.Component.ComponentBuilder;
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
	public List<Component> all() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Component> components = new ArrayList<>();

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

	private void readWeapons(final List<Component> components)
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
						.withDuration(it.duration)
						.withType(it.type)
						.withHeat(it.heat)
						.build()));
	}

	private void readAmmo(final List<Component> components)
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
						.build()));
	}

	private void readHeatSinks(final List<Component> components)
	{
		components.add(new ComponentBuilder()
				.withName("ClanDoubleHeatSink")
				.heatSink()
				.withHeatCapacity(1.5)
				.withDisipation(0.15)
				.withSlots(2)
				.withTons(1)
				.build());

		components.add(new ComponentBuilder()
				.withName("DoubleHeatSink")
				.heatSink()
				.withHeatCapacity(1.5)
				.withDisipation(0.15)
				.withSlots(3)
				.withTons(1)
				.build());

		components.add(new ComponentBuilder()
				.withName("EmptySlot")
				.empty()
				.build());
	}
}
