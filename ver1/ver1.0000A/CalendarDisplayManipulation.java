public interface CalendarDisplayManipulation {
	
	public int[][] getTable ();
	
	public int getColumnCount ();
	
	public int getRowCount ();
	
	public int getDateCount (Months m, int y);
	
	public Days getDayOfWeek (Months m, int d, int y);
	
	public Months getNextMonth ();
	
	public Months getPrevMonth ();
	
}