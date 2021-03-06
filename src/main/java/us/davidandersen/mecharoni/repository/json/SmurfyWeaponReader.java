package us.davidandersen.mecharoni.repository.json;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import us.davidandersen.mecharoni.evolve.EvolveMech;

public class SmurfyWeaponReader
{
	HashMap<String, WeaponJson> readWeapons()
	{
		final Gson gson = new Gson();
		final Type listType = new TypeToken<HashMap<String, SmurfyWeaponReader.WeaponJson>>() {
		}.getType();
		final HashMap<String, SmurfyWeaponReader.WeaponJson> weapons = gson
				.fromJson(new InputStreamReader(EvolveMech.class.getResourceAsStream("/weapons.json")), listType);

		return weapons;
	}

	static class WeaponJson
	{
		public String id;

		public String type;

		public float tons;

		public int slots;

		public float cooldown;

		public float duration;

		public String translated_name;

		public String name;

		public float heat;

		public Stats calc_stats;

		public int min_range;

		public int long_range;

		public int max_range;

		static class Stats
		{
			float dmg;

			int damageMultiplier;
		}

		public int min_heat_penalty_level;

		public float heat_penalty;

		public int heat_penalty_id;
	}
}
