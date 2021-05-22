package us.davidandersen.mecharoni.sim4;

public class Heat
{
	public static float dissipation(final int internalHeatSinks, final int externalHeatSinks)
	{
		return (float)(internalHeatSinks * .2 + externalHeatSinks * .15);
	}

	// https://mwomercs.com/forums/topic/251858-heat-capacity/#:~:text=30%20base%20heat%20capacity%20%2B%2020,heatsink%20adds%201.2%20heat%20cap.
	public static float getHeatCapacity(final int internalHeatSinks, final int externalHeatSinks)
	{
		return (float)(30 + internalHeatSinks * 2 + externalHeatSinks * 1.5);
	}
}
