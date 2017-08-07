package us.davidandersen.mecharoni.entity;

import java.util.Map;

public class Quirks
{
	private final Map<QuirkType, Quirk> quirks;

	public Quirks(final Map<QuirkType, Quirk> quirks)
	{
		this.quirks = quirks;
	}

	public float get(final QuirkType quirkType)
	{
		final Quirk quirk = quirks.get(quirkType);
		if (quirk == null) { return 0; }

		return quirk.getValue();
	}
}
