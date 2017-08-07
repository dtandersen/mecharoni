package us.davidandersen.mecharoni.entity;

public class Quirk
{
	private final QuirkType quirkType;

	private final float value;

	public Quirk(final QuirkBuilder quirkBuilder)
	{
		quirkType = quirkBuilder.quirkType;
		value = quirkBuilder.value;
	}

	public QuirkType getQuirkType()
	{
		return quirkType;
	}

	public float getValue()
	{
		return value;
	}

	static class QuirkBuilder
	{
		private QuirkType quirkType;

		private float value;

		public static QuirkBuilder quirk()
		{
			return new QuirkBuilder();
		}

		public QuirkBuilder withQuirkType(final QuirkType quirkType)
		{
			this.quirkType = quirkType;
			return this;
		}

		public QuirkBuilder withValue(final float value)
		{
			this.value = value;
			return this;
		}

		public Quirk build()
		{
			return new Quirk(this);
		}
	}
}