import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.*;

public class ClinicViewController {
	
	private MainController mc;
	private DatabaseControl dc;
	private DatabaseControlDirector dcd;
	private ClinicView cv;
	private Timer timer;
	private TimerTask task;
	
	public ClinicViewController (MainController mc) {
		this.mc = mc;
		
		dcd = new DatabaseControlDirector ();
		
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
		dcd.constructDatabaseControl (mc);
		dc = dcd.getDatabaseControl ();
		
		cv = dc.getView ();
		
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
	
	/*public void deleteRow (int id) {
		
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
		boolean b = DatabaseModel.getInstance ().timeConflict (param[4], param[1], param[2], param[3]);
		
		if (b)
			return -1;
		
		return DatabaseModel.getInstance ().insertAppointment (param[0], param[1], param[2], param[3], "" + mc.getUserID (), param[4]);
	}
	
	public int getAccessLevel (int id) {
		return DatabaseModel.getInstance ().getAccessLevel ("" + id);
	}
	
	public ResultSet getUpdates (String id, int y, int m, int d) {
		String str = "" + y + "-" + m + "-" + d;
		
		ResultSet rs = DatabaseModel.getInstance ().getAppointments (id, str);
		
		return rs;
	}*/
	
}