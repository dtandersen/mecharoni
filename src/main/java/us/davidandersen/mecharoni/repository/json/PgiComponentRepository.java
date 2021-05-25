package us.davidandersen.mecharoni.repository.json;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.BasicComponent;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.repository.ComponentRepository;
import us.davidandersen.mecharoni.repository.json.PgiItemReader.PgiItemJson;
import us.davidandersen.mecharoni.repository.json.SmurfyAmmoReader.AmmoJson;

public class PgiComponentRepository implements ComponentRepository
{
	private final PgiItemReader itemReader;

	private final SmurfyAmmoReader jsonAmmoReader;

	public PgiComponentRepository()
	{
		itemReader = new PgiItemReader();
		jsonAmmoReader = new SmurfyAmmoReader();
	}

	@Override
	public List<Component> all() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<BasicComponent> components = new ArrayList<>();

		readItems(components);
		// readAmmo(components);
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

	private void readItems(final List<BasicComponent> components)
	{
		final Map<String, PgiItemReader.PgiItemJson> weapons = itemReader.readWeapons();

		weapons.values().stream()
				.forEach(item -> {
					final BasicComponent x = mapItem(item);
					if (x != null)
					{
						components.add(x);
					}
				});
	}

	private BasicComponent mapItem(final PgiItemJson item)
	{
		int minRange = 0, longRange = 0, maxRange = 0;

		if (item.stats == null)
		{
			return null;
		}

		if (Objects.equals(item.ctype, "CHeatSinkStats"))
		{
			return null;
		}

		if (Objects.equals(item.category, "upgrade"))
		{
			return null;
		}

		if (item.ranges == null)
		{
		}
		else if (item.ranges.length == 3)
		{
			if (item.ranges[0].damageModifier > 0)
			{
				minRange = item.ranges[0].start;
				longRange = item.ranges[1].start;
				maxRange = item.ranges[2].start;
			}
			else
			{
				minRange = item.ranges[1].start;
				longRange = item.ranges[2].start;
				maxRange = item.ranges[2].start;
			}
		}
		else if (item.ranges.length == 2)
		{
			minRange = item.ranges[0].start;
			longRange = item.ranges[1].start;
			maxRange = item.ranges[1].start;
		}

		String type;
		if (item.stats.type == null)
		{
			type = null;
		}
		else if (Objects.equals(item.stats.type, "Energy"))
		{
			type = "BEAM";
		}
		else
		{
			type = item.stats.type.toUpperCase();
		}

		return BasicComponent.builder()
				.withId(item.id)
				.withTons(item.stats.tons)
				.withSlots(item.stats.slots)
				.withName(item.name)
				.withFriendlyName(item.name)
				.withDamage(item.stats.damage)
				.withCooldown(item.stats.cooldown)
				.withAmmoType(item.stats.ammoType)
				.withDuration(item.stats.duration)
				.withType(type)
				.withHeat(item.stats.heat)
				.withMinRange(minRange)
				.withLongRange(longRange)
				.withMaxRange(maxRange)
				.withDamageMultiplier(item.stats.numFiring)
				.withMinHeatPenaltyLevel(item.stats.minheatpenaltylevel)
				.withHeatPenalty(item.stats.heatpenalty)
				.withHeatPenaltyId(item.stats.heatPenaltyID)
				.build();
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
