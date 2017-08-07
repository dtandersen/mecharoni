package us.davidandersen.mecharoni.io;

import java.io.PrintStream;
import java.util.Map;
import us.davidandersen.mecharoni.entity.Component;
import us.davidandersen.mecharoni.entity.HardpointType;
import us.davidandersen.mecharoni.entity.Location;
import us.davidandersen.mecharoni.entity.MechSimulator;
import us.davidandersen.mecharoni.entity.MechBuild;
import us.davidandersen.mecharoni.evolve.EvolveMech.EvolveMechConfig;

public class MechPrinter
{
	private final PrintStream out;

	public MechPrinter(final PrintStream out)
	{
		this.out = out;
	}

	public void printMech(final MechBuild mech, final EvolveMechConfig config)
	{
		out.println("Firepower: " + mech.getFirepower());
		out.println("Damage@" + config.range + " (30s): " + sim(mech, config.range, 30));
		out.println("Damage@" + config.range + " (120s): " + sim(mech, config.range, 120));
		out.println("Heat: " + mech.getDisipation() + "/" + mech.hps() + " " + (mech.heatEfficiency() * 100) + "%" + "  Capacity:" + mech.getHeatCapacity());
		out.println("Heat (30s): " + mech.heatExpended(30) + "/" + mech.heatRegained(30) + " " + (mech.heatRegained(30) * 100) / mech.heatExpended(30) + "%");
		out.println("Heat Sinks: " + mech.getExternalHeatSinks() + "/(" + mech.getInternalHeatSinks() + ")");
		out.println("Tons: " + mech.occupiedTons() + "/" + config.tons);
		out.println("Slots: " + mech.getOccupiedSlots() + "/" + mech.maxFreeSlots());
		out.print("Energy: " + mech.getEnergySlots() + "/" + mech.maxHardpoints(HardpointType.ENERGY));
		out.print("  Ballistic: " + mech.getBallisticSlots() + "/" + mech.maxHardpoints(HardpointType.BALLISTIC));
		out.print("  Missile: " + mech.getMissileSlots() + "/" + mech.maxHardpoints(HardpointType.MISSILE));
		out.print("  ECM: " + mech.getEcmSlots() + "/" + mech.maxHardpoints(HardpointType.ECM));
		out.println("  AMS: " + mech.getAmsSlots() + "/" + mech.maxHardpoints(HardpointType.AMS));
		printConsolidatedItems(mech);
		// printLocationComps(mech);
	}

	private void printConsolidatedItems(final MechBuild mech)
	{
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

	private void printLocationComps(final MechBuild mech)
	{
		for (final Location location : mech.getLocations())
		{
			// out.println(location.getLocationType());
			for (final Component item : mech.componentsInLocation(location.getLocationType()))
			{
				if (!item.isEmpty())
				{
					out.println(location.getLocationType() + ": " + item.getFriendlyName());
				}
			}

		}
	}

	private float sim(final MechBuild mech, final int range, final int endTime)
	{
		final MechSimulator s = new MechSimulator();
		s.addMech(mech);
		s.go(endTime, range);
		final float damage = s.damage();
		return damage;
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
