package us.davidandersen.mecharoni.entity.predicate;

public class PpcPenaltyGroupPredicate extends MultiWeaponPredicate
{
	static final String[] PPC = new String[] {
			"ER PPC",
			"PPC",
			"GAUSS RIFLE",
			"HEAVY GAUSS RIFLE",
			"LIGHT PPC",
			"HEAVY PPC",
			"SNUB-NOSE PPC",
			"C-GAUSS RIFLE",
			"C-ER PPC",
	};

	public PpcPenaltyGroupPredicate()
	{
		super(PPC);
	}
}
