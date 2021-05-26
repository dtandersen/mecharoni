package us.davidandersen.mecharoni.entity;

public interface Component
{
	String getFriendlyName();

	boolean isEmpty();

	int getSlots();

	float getTons();

	HardpointType getHardpointType();

	boolean hasFriendlyName(String friendlyName);

	float getDamage();

	String getName();

	float getHps();

	float getHeat();

	float getDuration();

	float getCooldown();

	String getAmmoType();

	int getNumFiring();

	int getMinRange();

	int getLongRange();

	int getMaxRange();

	int getNumShots();

	float getDps();

	String getType();

	boolean isWeapon();

	boolean isBallistic();

	boolean isMissile();

	boolean isEnergy();

	boolean isHeatSink();

	boolean isClan();

	boolean isInnerSphere();

	int getMinHeatPenaltyLevel();

	float getHeatPenalty();

	int getHeatPenaltyId();

	int getAmmoPerShot();

	boolean isAmmo();
}
