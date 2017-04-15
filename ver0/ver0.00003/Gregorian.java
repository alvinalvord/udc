public class Gregorian implements SuperCalendar {
	
	public Days[] getWeekdays () {
		return GregorianDays.values ();
	}
	
	public Days getFirstDay () {
		return getWeekdays ()[0];
	}
	
	public Days getLastDay () {
		return getWeekdays ()[getWeekdays ().length - 1];
	}
	
	public int getWeekCount () {
		return GregorianDays.values ().length;
	}
	
	public Months[] getMonths () {
		return GregorianMonths.values ();
	}
	
	public Months getFirstMonth () {
		return getMonths ()[0];
	}
	
	public Months getLastMonth () {
		return getMonths ()[getMonths ().length - 1];
	}
	
	public int getMonthCount () {
		return GregorianMonths.values ().length;
	}
	
}