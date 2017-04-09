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
		
	public CalendarWeekScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		setId ("CalendarWeekScrollerBorderPane");
		
		this.dbc = dbc;
		this.cm = cm;
		
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
		
	}
	
	
}