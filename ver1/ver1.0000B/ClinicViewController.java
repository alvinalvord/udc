import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.*;

public class ClinicViewController {
	
	private MainController mc;
	private DatabaseControlDirector dcd;
	private DatabaseControl dc;
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
	
}