package us.davidandersen.mecharoni.repository.json;

public class MwoEscaper
{
	static String unescape(final String value)
	{
		if (value == null) { return null; }

		return value.replaceAll("\\\\/", "/");
	}
}
