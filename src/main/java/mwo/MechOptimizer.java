package mwo;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Item;
import us.davidandersen.mecharoni.evolve.EvolveMech;
import us.davidandersen.mecharoni.repository.json.JsonWeaponReader;

public class MechOptimizer
{
	public static void main(final String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final List<Item> items = JsonWeaponReader.clanItems().stream()
				.filter(item -> !item.getName().startsWith("ClanFlamer"))
				.filter(item -> !item.getName().startsWith("ClanLRM"))
				.filter(item -> !item.getName().startsWith("ClanMicro"))
				.filter(item -> !item.getName().startsWith("ClanERMicroLaser"))
				.collect(Collectors.toList());

		final EvolveMech.FitnessCheckerConfig config = new EvolveMech.FitnessCheckerConfig();
		config.slots = 33;
		config.tons = 17;
		config.items = items;
		config.amsSlots = 0;
		config.ballisticSlots = 0;
		config.energySlots = 5;
		config.missileSlots = 2;
		config.ecmSlots = 0;
		config.engineSinks = 10;
		config.heatSinks = 4;

		final EvolveMech hw = new EvolveMech();
		hw.run(config);
	}
}
