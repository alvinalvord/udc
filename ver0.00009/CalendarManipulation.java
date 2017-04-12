public interface CalendarManipulation {
	
	public Months getCurrentMonth ();
	
	public int getCurrentYear ();
	
	public int getSelectedDate ();
	
	public void setCurrentMonth (String m);
	
	public void setCurrentYear (int y);
	
	public void setSelectedDate (int d);
	
	public void setDateToday ();
	
}