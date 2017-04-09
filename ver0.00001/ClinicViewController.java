import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.*;

public class ClinicViewController {
	
	private MainController mc;
	private DoctorView dv;
	private int userID;
	private Timer timer;
	private TimerTask task;
	
	public ClinicViewController (MainController mc) {
		this.mc = mc;
		userID = 0;
		dv = new DoctorView (this);
		dv.update ();
		
		timer = new Timer ();
		
		task = new TimerTask () {
			public void run () {
				Platform.runLater (new Runnable () {
					public void run () {
						System.out.println ("updating ");
						dv.update ();
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
	
	public void setUserId (int id) {
		userID = id;
	}
	
	public int getUserId () {
		return userID;
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
	
	
	
	
	
	
}