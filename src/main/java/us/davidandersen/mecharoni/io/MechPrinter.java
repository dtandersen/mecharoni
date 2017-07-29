package us.davidandersen.mecharoni.io;

import java.io.PrintStream;
import us.davidandersen.mecharoni.entity.Mech;
import us.davidandersen.mecharoni.evolve.EvolveMech.FitnessCheckerConfig;

public class MechPrinter
{
	private final PrintStream out;

	public MechPrinter(final PrintStream out)
	{
		this.out = out;
	}

	public void printMech(final Mech mech, final FitnessCheckerConfig config)
	{
		out.println("Tons: " + mech.getTons() + "/" + config.tons);
		out.println("Slots: " + mech.getSlots() + "/" + config.slots);
		out.println("Energy: " + mech.getEnergySlots() + "/" + config.energySlots);
		out.println("Ballistic: " + mech.getBallisticSlots() + "/" + config.ballisticSlots);
		out.println("Missile: " + mech.getMissileSlots() + "/" + config.missileSlots);
		out.println("ECM: " + mech.getEcmSlots() + "/" + config.ecmSlots);
		out.println("AMS: " + mech.getAmsSlots() + "/" + config.amsSlots);
		out.println("Damage: " + mech.damageOverTime(30));
		out.println("Heat: " + mech.heatExpended(30) + "/" + mech.heatRegained(30));
		out.println("Heat Sinks: " + mech.getInternalHeatSinks() + "/" + mech.getExternalHeatSinks());
		mech.forEach(item -> out.println(item.getName() + ", dps=" + item.getDps() + ", hps=" + item.getHps()));
	}
}
