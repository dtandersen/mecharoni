package us.davidandersen.mecharoni.sim4;

public class Heat
{
	public static float dissipation(final int internalHeatSinks, final int externalHeatSinks)
	{
		return (float)(internalHeatSinks * .2 + externalHeatSinks * .15);
	}

	public static float getHeatCapacity(final int internalHeatSinks, final int externalHeatSinks)
	{
		return (float)(30 + internalHeatSinks * 2 + externalHeatSinks * 1.5);
	}

}
