import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;

public class CalendarDayScroller extends ScrollPane implements View {
	
	private DatabaseControl dbc;
	private CalendarModel cm;
	
	private VBox dayVBox;
		private HBox[] hboxArr;
			private Label[] hourLabels;
			private VBox[] vboxArr;
				private Label[] timeLabels;
	
	private int[] ids;
			
	private boolean secretaryMode;
	
	public CalendarDayScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		
		this.dbc = dbc;
		this.cm = cm;
		
		secretaryMode = false;
		
		cm.attach (this);
		
		initCDS ();
	}
	
	private void initCDS () {
		getStyleClass ().add ("CalendarDayScroller");
		setPrefHeight (551);
		setHbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy (ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		dayVBox = new VBox ();
		dayVBox.setId ("dayScroller");
		
		hboxArr = new HBox[24];
		for (int i = 0; i < hboxArr.length; i++)
			hboxArr[i] = new HBox ();
		
		hourLabels = new Label[24];
		for (int i = 0; i < hourLabels.length; i++) {
			if (i < 10)
				hourLabels[i] = new Label ("0" + i + ":00");
			else
				hourLabels[i] = new Label ("" + i + ":00");
			
			hourLabels[i].setMinWidth (125);
			hourLabels[i].setMaxWidth (Double.MAX_VALUE);
			hourLabels[i].setMaxHeight (Double.MAX_VALUE);
			hourLabels[i].setId ("hourLabels");
		}
		
		vboxArr = new VBox[24];
		for (int i = 0; i < vboxArr.length; i++) {
			vboxArr[i] = new VBox ();
			HBox.setHgrow (vboxArr[i], Priority.ALWAYS);
		}
		
		timeLabels = new Label[48];
		for (int i = 0; i < timeLabels.length; i++) {
			timeLabels[i] = new Label ();
			timeLabels[i].setPrefHeight (80);
			timeLabels[i].setMinWidth (750);
			timeLabels[i].setMaxWidth (Double.MAX_VALUE);
			timeLabels[i].setMaxHeight (Double.MAX_VALUE);
			timeLabels[i].setAlignment (Pos.TOP_LEFT);
		}
		
		int ctr = 0;
		for (int i = 0; i < hboxArr.length; i++) {
			hboxArr[i].getChildren ().addAll (hourLabels[i], vboxArr[i]);
			
			for (int j = 0; j < 2; j++) {
				vboxArr[i].getChildren ().add (timeLabels[ctr]);
				ctr ++;
			}
			
			dayVBox.getChildren ().add (hboxArr[i]);
		}
		
		setContent (dayVBox);
		
		ids = new int[48];
	}
	
	public void setSecretaryMode (boolean b) {
		secretaryMode = b;
	}
	
	public void update () {
		ResultSet rs = null;
		
		for (int i = 0; i < ids.length; i++)
			ids[i] = 0;
		
		for (int i = 0; i < timeLabels.length; i++) {
			timeLabels[i].setText ("");
			timeLabels[i].setId ("TimeLabelsNone");
			timeLabels[i].setOnMouseClicked (null);
			timeLabels[i].setGraphic (null);
		}
		
		try {
			rs = dbc.getDisplayData (cm.getCurrentYear (), cm.getCurrentMonth ().getMonth () + 1, cm.getSelectedDate ());
		} catch (Exception e) { e.printStackTrace (); }
		
		if (rs == null)
			return;
		
		try {
			int[] accessLevels = new int[48];
			
			while (rs.next ()) {
				int s, e;
				String start = rs.getString ("start_time");
				String end = rs.getString ("end_time");
				
				s = Integer.parseInt (start.substring (0,2)) * 2;
				if (Integer.parseInt (start.substring (3,5)) != 0)
					s ++;
				
				e = Integer.parseInt (end.substring (0,2)) * 2;
				if (Integer.parseInt (end.substring (3,5)) != 0)
					e ++;
				
				for (int i = s; i < e; i++) {
					if (i == s)
						timeLabels[i].setText (rs.getString ("name"));
					
					accessLevels[i] = rs.getInt("appointer_id");
					
					if (rs.getInt ("status_done") == 1) {
						timeLabels[i].setId ("TimeLabelsDone");
						accessLevels[i] = 0;
					}
					else 
						timeLabels[i].setId ("TimeLabelsActive");
				}
				
			}
			
			if (secretaryMode) {
				for (int i = 0; i < accessLevels.length; i++) {
					switch (dbc.getAccessLevel (accessLevels[i])) {
						case 3:
							timeLabels[i].setId ("TimeLabelsDoctor");
							break;
						case 2:
							timeLabels[i].setId ("TimeLabelsSecretary");
							break;
						case 1:
							timeLabels[i].setId ("TimeLabelsClient");
							break;
					}
				}
			}
			
		} catch (Exception e) { e.printStackTrace (); }
		
	}
	
}