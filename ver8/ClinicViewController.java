import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.*;

public class ClinicViewController implements DatabaseControl {
	
	private MainController mc;
	private ClinicView cv;
	private Timer timer;
	private TimerTask task;
	
	public ClinicViewController (MainController mc) {
		this.mc = mc;
		
		timer = new Timer ();
		
		task = new TimerTask () {
			public void run () {
				Platform.runLater (new Runnable () {
					public void run () {
						System.out.println ("updating ");
						cv.update ();
						DatabaseModel.getInstance ().checkUpdates();
					}
				});
			}
		};
		
		getMainStage ().setOnCloseRequest (e -> {
			timer.cancel ();
			timer.purge ();
			Platform.exit ();
		});
	}
	
	public ClinicView getView () {
		int n = getAccessLevel (mc.getUserID ());
		switch (n) {
			case 1: 
				cv = new ClientView (this);
				break;
			case 2: 
				cv = new SecretaryView (this);
				break;
			case 3: 
				cv = new DoctorView (this);
				break;
		}
		
		timer.schedule (task, 0, 1000);
		
		return cv;
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}
	
	public void changeControl (int n) {
		timer.cancel ();
		timer.purge ();
		mc.setScene (n);
	}
	
	public void deleteRow (int id) {
		
	}
	
	public void markDone (int id) {
		
	}
	
	public void createUpdate (int id, String... param) {
		
	}
	
	public ArrayList <String> getViewableSchedules () {
		ArrayList <String> names = new ArrayList <String> ();
		
		int n = getAccessLevel (mc.getUserID ());
		
		ResultSet rs = DatabaseModel.getInstance ().getAllUsers ();
		
		try {
			while (rs.next ()) {
				switch (n) {
					case 1:
						if (rs.getInt ("user_level") == 3 || rs.getInt ("id") == mc.getUserID ())
							names.add (rs.getString ("name"));
						break;
						
					case 2:
						if (rs.getInt ("user_level") == 3)
							names.add (rs.getString ("name"));
						break;
					
					case 3:
						if (rs.getInt ("id") == mc.getUserID ())
							names.add (rs.getString ("name"));
						break;
				}
			}
		} catch (Exception e) { e.printStackTrace (); }
		
		return names;
	}
	
	public int selectUserSchedule (String name) {
		return DatabaseModel.getInstance ().getUser (name);
	}
	
	public int createInsert (String... param) {
		
		return 0;
	}
	
	public int getAccessLevel (int id) {
		return DatabaseModel.getInstance ().getAccessLevel ("" + id);
	}
	
	public ResultSet getUpdates (String id, int y, int m, int d) {
		String str = "" + y + "-" + m + "-" + d;
		
		ResultSet rs = DatabaseModel.getInstance ().getAppointments (id, str);
		
		return rs;
	}
	
}