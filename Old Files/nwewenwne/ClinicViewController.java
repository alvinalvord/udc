import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.*;

public class ClinicViewController implements DatabaseControl {
	
	private MainController mc;
	private DoctorView dv;
	private Timer timer;
	private TimerTask task;
	
	public ClinicViewController (MainController mc) {
		this.mc = mc;
		dv = new DoctorView (this);
		dv.update ();
		
		timer = new Timer ();
		
		task = new TimerTask () {
			public void run () {
				Platform.runLater (new Runnable () {
					public void run () {
						System.out.println ("updating ");
						dv.update ();
						DatabaseModel.getInstance ().checkUpdates();
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
	
	public ClinicView getView () {
		return dv;
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}
	
	public void changeControl (int n) {
		mc.setScene (n);
	}
	
	public void deleteRow (int id) {
		
	}
	
	public void markDone (int id) {
		
	}
	
	public void createUpdate (int id, String... param) {
		
	}
	
	public int createInsert (String... param) {
		
		return 0;
	}
	
	public ResultSet getUpdates (String id, int y, int m, int d) {
		String str = "" + y + "-" + m + "-" + d;
		
		ResultSet rs = DatabaseModel.getInstance ().getData (id, str);
		
		return rs;
	}
	
}