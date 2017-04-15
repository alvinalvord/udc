import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;

public class CalendarWeekScroller extends BorderPane implements View {
	
	private DatabaseControl dbc;
	private CalendarModel cm;
	
	private ScrollPane weekScrollPane;
		private HBox dayHbox;
			private Label[] dayLabels;
	private ScrollPane hourScrollPane;
		private VBox hourVbox;
			private Label[] hourLabels;
	private ScrollPane timeScrollPane;
		private HBox timeHbox;
			private VBox[] timeVbox;
				private Label[][] timeLabels;
				
	private int[][] ids;
		
	private boolean secretaryMode;
	
	public CalendarWeekScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		setId ("CalendarWeekScrollerBorderPane");
		
		this.dbc = dbc;
		this.cm = cm;
		
		secretaryMode = false;
		
		cm.attach (this);
		
		initCWS ();
	}
	
	private void initCWS () {
		initWeek ();
		initHours ();
		initTime ();
		
		weekScrollPane.hvalueProperty ().bindBidirectional (timeScrollPane.hvalueProperty ());
		weekScrollPane.maxWidthProperty ().bind (timeScrollPane.maxWidthProperty ());
		weekScrollPane.minWidthProperty ().bind (timeScrollPane.minWidthProperty ());
		
		hourScrollPane.vvalueProperty ().bindBidirectional (timeScrollPane.vvalueProperty ());
		hourScrollPane.maxHeightProperty ().bind (timeScrollPane.maxHeightProperty ());
		hourScrollPane.minHeightProperty ().bind (timeScrollPane.minHeightProperty ());
		
		setTop (weekScrollPane);
		setLeft (hourScrollPane);
		setCenter (timeScrollPane);
	}
	
	public void setSecretaryMode (boolean b) {
		secretaryMode = b;
	}
	
	private void initWeek () {
		weekScrollPane = new ScrollPane ();
		weekScrollPane.getStyleClass ().add ("CalendarWeekScroller");
		weekScrollPane.getStyleClass ().add ("CalendarWeekDays");
		
		dayHbox = new HBox ();
		
		dayLabels = new Label[cm.getCalendar ().getWeekdays ().length+1];
		dayLabels[0] = new Label ();
		dayLabels[0].setId ("CalendarWeekLabel");
		dayHbox.getChildren ().add (dayLabels[0]);
		
		for (int i = 1; i < dayLabels.length; i++) {
			dayLabels[i] = new Label (cm.getCalendar ().getWeekdays ()[i-1].getHeader ());
			dayLabels[i].setId ("CalendarWeekLabel");
			dayHbox.getChildren ().add (dayLabels[i]);
		}
		
		weekScrollPane.setContent (dayHbox);
	}
	
	private void initHours () {
		hourScrollPane = new ScrollPane ();
		hourScrollPane.getStyleClass ().add ("CalendarWeekScroller");
		hourScrollPane.getStyleClass ().add ("CalendarWeekHours");
		
		hourVbox = new VBox ();
		
		hourLabels = new Label[24];
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				hourLabels[i] = new Label ("0" + i + ":00");
			else
				hourLabels[i] = new Label ("" + i + ":00");
			
			hourLabels[i].setId ("CalendarWeekLabel");
			hourVbox.getChildren ().add (hourLabels[i]);
		}
		
		hourScrollPane.setContent (hourVbox);
	}
	
	private void initTime () {
		timeScrollPane = new ScrollPane ();
		timeScrollPane.getStyleClass ().add ("CalendarWeekScroller");
		timeScrollPane.getStyleClass ().add ("CalendarWeekTime");
		
		timeHbox = new HBox ();
			timeVbox = new VBox[7];
			timeLabels = new Label[7][48];
			
			for (int i = 0; i < timeLabels.length; i++) {
				timeVbox[i] = new VBox ();
				for (int j = 0; j < timeLabels[i].length; j++) {
					timeLabels[i][j] = new Label ();
					timeLabels[i][j].setId ("CalendarWeekTimeLabel");
					timeVbox[i].getChildren ().add (timeLabels[i][j]);
				}
				timeHbox.getChildren ().add (timeVbox[i]);
			}
			
		timeScrollPane.setContent (timeHbox);
	}
	
	public void update () {
		int[][] table = cm.getTable ();
		
		boolean actualStart = false;
		int weekIndex = -1;
		
		for (int x = 0; x < timeLabels.length; x++) {
			for (int y = 0; y < timeLabels[x].length; y++) {
				timeLabels[x][y].setText ("");
				timeLabels[x][y].setId ("CalendarWeekTimeLabel");
				timeLabels[x][y].setOnMouseClicked (null);
				timeLabels[x][y].setGraphic (null);
			}
		}
		
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == 1)
					actualStart = true;
				
				if (actualStart) {
					if (cm.getSelectedDate () == table[i][j]) {
						weekIndex = i;
						break;
					}
				}
			}
			if (weekIndex != -1)
				break;
		}
		
		
		ids = new int[7][48];
		for (int i = 0; i < table[weekIndex].length; i++) {
			ResultSet rs = null;
			
			try {
				rs = dbc.getDisplayData (cm.getCurrentYear (), cm.getCurrentMonth ().getMonth () + 1, table[weekIndex][i]);
			} catch (Exception e) { e.printStackTrace (); }
			
			if (rs == null)
				continue;
			
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
					
					for (int x = s; x < e; x++) {
						if (x == s)
							timeLabels[i][x].setText (rs.getString ("name"));
						
						accessLevels[x] = rs.getInt ("appointer_id");
						
						if (rs.getInt ("status_done") == 1) {
							timeLabels[i][x].setId ("CalendarWeekTimeLabelDone");
							accessLevels[x] = 0;
						}
						else
							timeLabels[i][x].setId ("CalendarWeekTimeLabelActive");
					}
				}
				
				if (secretaryMode) {
					for (int x = 0; x < accessLevels.length; x++) {
						switch (dbc.getAccessLevel (accessLevels[x])) {
							case 3:
								timeLabels[i][x].setId ("CalendarWeekTimeLabelDoctor");
								break;
							case 2:
								timeLabels[i][x].setId ("CalendarWeekTimeLabelSecretary");
								break;
							case 1:
								timeLabels[i][x].setId ("CalendarWeekTimeLabelClient");
								break;
						}
					}
				}
			} catch (Exception e) { e.printStackTrace (); }
			
		}
		
	}
	
	
}