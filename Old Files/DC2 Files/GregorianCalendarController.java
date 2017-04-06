import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.*;

public class GregorianCalendarController {
	
	private GregorianCalendarView gcv;
	private MainController mc;
	private Timer timer;
	
	public GregorianCalendarController (MainController mc) {
		this.mc = mc;
		gcv = new GregorianCalendarView (this);
		gcv.update ();
		
		timer = new Timer ();
		
		TimerTask task = new TimerTask () {
			public void run () {
				Platform.runLater (new Runnable () {
					public void run () {
						System.out.println ("updating");
						new AgendasModel ().updateData ();
						gcv.update ();
					}
				});
			}
		};
		
		timer.schedule (task, 0, 1000);
		
		getMainStage ().setOnCloseRequest (e -> { 
			timer.cancel ();
			timer.purge ();
			Platform.exit (); 
		});
	}
	
	public GregorianCalendarView getView () {
		return gcv;
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}
	
	public void changeControl (int n) {
		mc.setScene (n);
	}

	public ResultSet getUpdates (int y, int m, int d) {
		String str = "" + y + "-" + m + "-" + d;
		boolean e = gcv.showEvents ();
		boolean t = gcv.showTasks ();
		
		if (!e && !t) {
			e = true;
			t = true;
		}
		
		return new AgendasModel ().getData (str, t, e);
	}
	
	public int createInsert (String eventName, boolean isEvent, String start, String end, int y, int m, int d) {
		String date = "" + y + "-" + m + "-" + d;
		try {
			ResultSet rs = new AgendasModel ().getData (date, true, true);
			while (rs.next ()) {
				String s = rs.getString ("start");
				String e = rs.getString ("end");
				
				s = s.substring (s.indexOf (" "));
				e = e.substring (e.indexOf (" "));
				s = s.trim ();
				e = e.trim ();
				
				if (start.equals (s))
					return -1;
				if (end.equals (e))
					return -1;
				
				int shr = Integer.parseInt (start.substring (0,2));
				int smin = Integer.parseInt (start.substring (3,5));
				int ehr = Integer.parseInt (end.substring (0,2));
				int emin = Integer.parseInt (end.substring (3,5));
				
				int sshr = Integer.parseInt (s.substring (0,2));
				int ssmin = Integer.parseInt (s.substring (3,5));
				int eehr = Integer.parseInt (e.substring (0,2));
				int eemin = Integer.parseInt (e.substring (3,5));
				
				
				if (shr == sshr)
					if (ehr > sshr)
						return -1;
				
				if (shr < sshr)
					if (ehr > sshr)
						return -1;
					else if (ehr == sshr)
						if (emin > ssmin)
							return -1;
				
				if (shr > sshr)
					if (eehr > shr)
						if (eemin > smin)
							return -1;
				
				if (shr > ehr)
					return -1;
			}
		} catch (Exception e) {e.printStackTrace ();}
		
		String eventType = (isEvent) ? "E": "T";
		return new AgendasModel ().insertData (eventName, eventType, date + " " + start + ":00", date + " " + end + ":00");
	}
	
	public void setDone (int id) {
		new AgendasModel ().markDone (id);
	}
	
	public void setDelete (int id) {
		new AgendasModel ().deleteRow (id);
	}
	
	public void setRightPane (Node child, Pane parent) {
		if (parent.getChildren ().contains (child))
			return;
		if (!parent.getChildren ().isEmpty ())
			parent.getChildren ().removeAll (parent.getChildren ());
		parent.getChildren ().add (child);
	}
}