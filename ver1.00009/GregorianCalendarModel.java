import java.util.*;

public class GregorianCalendarModel extends CalendarModel {
	
	public GregorianCalendarModel () {
		this (GregorianMonths.values () 
			[new GregorianCalendar ().get (GregorianCalendar.MONTH)], 
			new GregorianCalendar ().get (GregorianCalendar.DATE),
			new GregorianCalendar ().get (GregorianCalendar.YEAR)
			);
	}
	
	public GregorianCalendarModel (Months m, int d, int y) {
		super (new Gregorian (), m, d, y);
	}
	
	public int[][] getTable () {
		GregorianCalendar cal = new GregorianCalendar 
			(currentYear, currentMonth.getMonth (), 1);
		int[][] n = new int[getRowCount ()][getColumnCount ()];
		int max = cal.getActualMaximum 
			(GregorianCalendar.DAY_OF_MONTH);
		int start = cal.get (GregorianCalendar.DAY_OF_WEEK);
		
		GregorianCalendar cprev = new GregorianCalendar (
			((getCurrentMonth ().getMonth () - 1 < 0) ? 
				currentYear - 1: currentYear),
			((getCurrentMonth ().getMonth () - 1 < 0) ? 
				calendar.getLastMonth ().getMonth ():
				getCurrentMonth ().getMonth () - 1), 
			1);
		int extraStart = cprev.getActualMaximum (GregorianCalendar.DAY_OF_MONTH);

		int x = start - 2;
		for (int j = extraStart; x >= 0; x--) {
			n[0][x] = j - start + 2 + x;
		}
		
		int ctr = 1;
		for (int j = start - 1; j < getColumnCount (); j++) {
			n[0][j] = ctr;
			ctr++;
		}
	
		for (int i = 1 ; i < getRowCount () && ctr <= max; i++)
			for (int j = 0; j < getColumnCount () && ctr <= max; j++) {
				n[i][j] = ctr;
				ctr++;
			}
			
		x = 1; 
		for (int i = 0; i < n[0].length; i++) {
			if (n[n.length - 1][i] == 0) {
				n[n.length - 1][i] = x;
				x++;
			}
		}
				
		return n;
	}
	
	public int getColumnCount () {
		return calendar.getWeekdays ().length;
	}
	
	public int getRowCount () {
		GregorianCalendar cal = new GregorianCalendar 
			(currentYear, currentMonth.getMonth (), 1);
		int max = cal.getActualMaximum 
			(GregorianCalendar.DAY_OF_MONTH);
		int start = cal.get (GregorianCalendar.DAY_OF_WEEK);
		
		int nrows = (start + max - 1) / calendar.getWeekCount ();
		
		if ((start + max - 1) % calendar.getWeekCount () != 0)
			nrows++;
		
		return nrows;
	}
	
	public int getDateCount (Months m, int y) {
		GregorianCalendar cal = new GregorianCalendar (y, m.getMonth (), 1);
		int max = cal.getActualMaximum 
			(GregorianCalendar.DAY_OF_MONTH);
		return max;
	}
	
	public void setDateToday () {
		this.currentDate = new GregorianCalendar ().get (GregorianCalendar.DATE);
		this.currentYear = new GregorianCalendar ().get (GregorianCalendar.YEAR);
		this.currentMonth = GregorianMonths.values () 
			[new GregorianCalendar ().get (GregorianCalendar.MONTH)];
		notifyViews ();
	}
	
	public Days getDayOfWeek (Months m, int d, int y) {
		GregorianCalendar cal = new GregorianCalendar (y, m.getMonth (), d);
		
		return calendar.getWeekdays ()[cal.get (GregorianCalendar.DAY_OF_WEEK) - 1];
	}
	
}