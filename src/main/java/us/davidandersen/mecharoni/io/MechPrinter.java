package us.davidandersen.mecharoni.io;

import java.io.PrintStream;
import java.util.Map;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.MechSpec;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;

public class MechPrinter
{
	private final PrintStream out;

	public MechPrinter(final PrintStream out)
	{
		this.out = out;
	}

	public void printMech(final MechSpec mech, final EvolveMechConfig config)
	{
		out.println("Tons: " + mech.getTons() + "/" + config.tons);
		out.println("Slots: " + mech.getSlots() + "/" + config.slots);
		out.print("Energy: " + mech.getEnergySlots() + "/" + config.energySlots);
		out.print("  Ballistic: " + mech.getBallisticSlots() + "/" + config.ballisticSlots);
		out.print("  Missile: " + mech.getMissileSlots() + "/" + config.missileSlots);
		out.print("  ECM: " + mech.getEcmSlots() + "/" + config.ecmSlots);
		out.println("  AMS: " + mech.getAmsSlots() + "/" + config.amsSlots);
		out.println("Damage: " + mech.damageOverTime(30));
		out.println("Heat: " + mech.disipation() + "/" + mech.hps() + " " + (mech.heatEfficiency() * 100) + "%");
		out.println("Heat (30s): " + mech.heatExpended(30) + "/" + mech.heatRegained(30) + " " + (mech.heatRegained(30) * 100) / mech.heatExpended(30) + "%");
		out.println("Heat Sinks: " + mech.getInternalHeatSinks() + "/" + mech.getExternalHeatSinks());
		final Map<String, Node> it = mech.combineItems();
		// mech.forEach(item -> {
		// if (!item.isEmpty())
		// {
		// out.println(item.getFriendlyName() + ", dps=" + item.getDps() + ", hps=" + item.getHps() + ", tons=" + item.getTons() + ", slots=" + item.getSlots());
		// }
		// });

		for (final Node n : it.values())
		{
			final Component item = n.item;
			if (!item.isEmpty())
			{
				out.println(n.quantity + " " + item.getFriendlyName());
			}
		}
	}

	public static class Node
	{
		private final Component item;

		private int quantity;

		public Node(final Component item, final int quantity)
		{
			this.item = item;
			this.quantity = quantity;
		}

		public void increment()
		{
			quantity += 1;
		}

		public Component getItem()
		{
			return item;
		}
	}
}
