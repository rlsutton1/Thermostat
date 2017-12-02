package au.com.rsutton.vaadin;

public enum Days
{
	EVERY_DAY(new int[] {
			1, 2, 3, 4, 5, 6, 7 }),

	WEEK_DAYS(new int[] {
			2, 3, 4, 5, 6 }),

	WEEK_ENDS(new int[] {
			1, 7 }),

	MON(new int[] {
			2 }),

	TUE(new int[] {
			3 }),

	WED(new int[] {
			4 }),

	THUR(new int[] {
			5 }),

	FRI(new int[] {
			6 }),

	SAT(new int[] {
			7 }),

	SUN(new int[] {
			1 });

	private int[] dayNumbers;

	Days(int[] dayNumbers)
	{
		this.dayNumbers = dayNumbers;
	}

	boolean matchesDay(int dayNumber)
	{
		boolean matches = false;
		for (int day : this.dayNumbers)
		{
			if (dayNumber == day)
			{
				matches = true;
				break;
			}
		}
		return matches;
	}

}
