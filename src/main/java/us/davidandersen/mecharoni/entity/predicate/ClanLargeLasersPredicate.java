package us.davidandersen.mecharoni.entity.predicate;

public class ClanLargeLasersPredicate extends MultiWeaponPredicate
{
	static final String[] lasers = new String[] {
			"ER LARGE LASER",
			"LARGE LASER",
			"LRG PULSE LASER",
			"C-ER LRG LASER",
			"C-LRG PULSE LASER",
			"C-HEAVY LRG LASER"
	};

	public ClanLargeLasersPredicate()
	{
		super(lasers);
	}
}
