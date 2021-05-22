package us.davidandersen.mecharoni.repository.json;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import com.google.gson.reflect.TypeToken;
import us.davidandersen.mecharoni.evolve.EvolveMech;

public class SmurfyAmmoReader
{
	HashMap<String, AmmoJson> readAmmo()
	{
		final Type listType = new TypeToken<HashMap<String, SmurfyAmmoReader.AmmoJson>>() {}.getType();
		final HashMap<String, SmurfyAmmoReader.AmmoJson> weapons = GlobalGson.fromJson(new InputStreamReader(EvolveMech.class.getResourceAsStream("/ammo.json")), listType);

		return weapons;
	}

	static class AmmoJson
	{
		public String id;

		public String type;

		public float tons;

		public int slots;

		public String translated_name;

		public int num_shots;

		public String[] weapons;
	}
}
