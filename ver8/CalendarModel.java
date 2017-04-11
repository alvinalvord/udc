public abstract class CalendarModel extends Model implements CalendarManipulation, CalendarDisplayManipulation {
	
	protected SuperCalendar calendar;
	protected Months currentMonth;
	protected int currentYear;
	protected int currentDate;
	
	public CalendarModel (SuperCalendar calendar, Months currentMonth, int currentDate, int currentYear) {
		this.calendar = calendar;
		this.currentMonth = currentMonth;
		this.currentDate = currentDate;
		this.currentYear = currentYear;
	}
	
	public SuperCalendar getCalendar () {
		return calendar;
	}
	
	public Months getCurrentMonth () {
		return currentMonth;
	}
	
	public int getCurrentYear () {
		return currentYear;
	}
	
	public int getSelectedDate () {
		return currentDate;
	}
	
	public void setCurrentMonth (String m) {
		for (Months cm: calendar.getMonths ()) {
			if (cm.toString ().equals (m)) {
				this.currentMonth = cm;
				break;
			}
		}
		notifyViews ();
	}
	
	public void setCurrentYear (int y) {
		this.currentYear = y;
		notifyViews ();
	}
	
	public void setSelectedDate (int d) {
		this.currentDate = d;
		notifyViews ();
	}
	
	public Months getNextMonth () {
		int index = getCurrentMonth ().getMonth () + 1;
		if (index > calendar.getLastMonth ().getMonth ()) {
			index = calendar.getFirstMonth ().getMonth ();
			currentYear ++;
		}
		
		return calendar.getMonths ()[index];
	}
	
	public Months getPrevMonth () {
		int index = getCurrentMonth ().getMonth () - 1;
		if (index < calendar.getFirstMonth ().getMonth ()) {
			index = calendar.getLastMonth ().getMonth ();
			currentYear --;
		}
		return calendar.getMonths ()[index];
	}
	
}