package us.davidandersen.mecharoni.repository.json;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import com.google.gson.Gson;

public class GlobalGson
{
	private static final Gson gson = new Gson();

	public static <T> T fromJson(final InputStreamReader inputStreamReader, final Type listType)
	{
		return gson.fromJson(inputStreamReader, listType);
	}
}
