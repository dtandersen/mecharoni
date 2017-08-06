package us.davidandersen.mecharoni.entity;

public interface FiringStrategy
{
	boolean fire(double heat, double availableHeat, double totalHeat);
}
