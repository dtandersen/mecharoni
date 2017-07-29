package us.davidandersen.mecharoni.repository.json;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import us.davidandersen.mecharoni.entity.Item;
import us.davidandersen.mecharoni.entity.Item.ItemBuilder;
import us.davidandersen.mecharoni.evolve.EvolveMech;

public class JsonWeaponReader
{
	static List<Item> readItems() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final Gson gson = new Gson();
		final Type listType = new TypeToken<HashMap<String, JsonWeaponReader.ItemJson>>() {}.getType();
		final HashMap<String, JsonWeaponReader.ItemJson> i = gson.fromJson(new InputStreamReader(EvolveMech.class.getResourceAsStream("/weapons.json")), listType);

		final List<Item> collect = i.values().stream()
				.map(it -> new ItemBuilder()
						.withTons(it.tons)
						.withSlots(it.slots)
						.withName(it.name)
						.withDamage(it.calc_stats.dmg)
						.withCooldown(it.cooldown)
						.withDuration(it.duration)
						.withType(it.type)
						.withHeat(it.heat)
						.build())
				.collect(Collectors.toList());
		collect.add(new ItemBuilder().withName("Empty Slot").empty().build());
		collect.add(new ItemBuilder()
				.withName("Double Heat Sink")
				.heatSink()
				.withHeatCapacity(1.5)
				.withDisipation(0.15)
				.withSlots(2)
				.withTons(1)
				.build());
		return Collections.unmodifiableList(collect);
	}

	public static List<Item> clanItems() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Item> clanItems = readItems().stream()
				.filter(item -> item.isClan())
				.collect(Collectors.toList());

		return Collections.unmodifiableList(clanItems);
	}

	static class ItemJson
	{
		public String type;

		public float tons;

		public float slots;

		public float cooldown;

		public float duration;

		public String name;

		public float heat;

		public Stats calc_stats;

		static class Stats
		{
			float dmg;
		}
	}
}
