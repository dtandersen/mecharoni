package us.davidandersen.mecharoni.repository.json;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import us.davidandersen.mecharoni.evolve.EvolveMech;

public class PgiItemReader
{
	Map<String, PgiItemJson> readWeapons()
	{
		final Gson gson = new Gson();
		final Type listType = new TypeToken<HashMap<String, PgiItemReader.PgiItemJson>>() {
		}.getType();
		final Map<String, PgiItemReader.PgiItemJson> weapons = gson
				.fromJson(new InputStreamReader(EvolveMech.class.getResourceAsStream("/items.json")), listType);

		return weapons;
	}

	static class PgiItemJson
	{
		public String id;

		/**
		 * coded name
		 */
		public String name;

		public String category;

		public PgiStats stats;

		public PgiRange[] ranges;

		public String ctype;

		public PgiAmmoTypeStats ammotypestats;

		static class PgiStats
		{
			/**
			 * number of slots
			 */
			public int slots;

			/**
			 * energy. etc.
			 */
			public String type;

			/**
			 * damage dealt
			 */
			float damage;

			public float heatpenalty;

			/**
			 * amount to fire before penalty
			 */
			public int minheatpenaltylevel;

			/**
			 * heat category
			 */
			public int heatPenaltyID;

			/**
			 * heat per shot
			 */
			public float heat;

			/**
			 * cooldown in seconds
			 */
			public float cooldown;

			/**
			 * Type of ammo consumed
			 */
			public String ammoType;

			/**
			 * also damage multiplier
			 */
			public int ammoPerShot;

			/**
			 * weight in tons
			 */
			public float tons;

			/**
			 * burn duration
			 */
			public float duration;

			public int numFiring;
		}

		static class PgiRange
		{
			/**
			 * start of range
			 */
			public int start;

			public float damageModifier;
		}

		static class PgiAmmoTypeStats
		{
			/**
			 * amount of ammo
			 */
			public int numShots;

			public String type;
		}
	}
}
