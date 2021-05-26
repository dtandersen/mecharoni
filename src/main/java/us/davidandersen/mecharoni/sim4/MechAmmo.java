package us.davidandersen.mecharoni.sim4;

import javax.annotation.processing.Generated;

public class MechAmmo extends MechComponent
{
	private final String name;

	private final int numShots;

	@Generated("SparkTools")
	private MechAmmo(final MechAmmoBuilder mechAmmoBuilder)
	{
		this.name = mechAmmoBuilder.name;
		this.numShots = mechAmmoBuilder.numShots;
	}

	public int getNumShots()
	{
		return numShots;
	}

	/**
	 * Creates builder to build {@link MechAmmo}.
	 *
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static MechAmmoBuilder builder()
	{
		return new MechAmmoBuilder();
	}

	public String getName()
	{
		return name;
	}

	/**
	 * Builder to build {@link MechAmmo}.
	 */
	@Generated("SparkTools")
	public static final class MechAmmoBuilder
	{
		private String name;

		private int numShots;

		private MechAmmoBuilder()
		{
		}

		public MechAmmoBuilder withName(final String name)
		{
			this.name = name;
			return this;
		}

		public MechAmmoBuilder withNumShots(final int numShots)
		{
			this.numShots = numShots;
			return this;
		}

		public MechAmmo build()
		{
			return new MechAmmo(this);
		}
	}
}
