package us.davidandersen.mecharoni.entity.predicate;

public class SrmPenaltyGroupPredicate extends MultiWeaponPredicate
{
	static final String[] SRM = new String[] {
			"SRM 4",
			"SRM 6",
			"SRM 4 + ARTEMIS",
			"SRM 6 + ARTEMIS",
			"MRM 10",
			"MRM 20",
			"MRM 30",
			"MRM 40"
	};

	public SrmPenaltyGroupPredicate()
	{
		super(SRM);
	}
}
