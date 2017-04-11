import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.ArrayList;

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
				
	private int[] ids;
	private Stage popup;
	private VBox popupVbox;
		private Label appointmentPopup;
		private Label popupDateLabel;
		private HBox popupTimeHBox;
			private ComboBox<String> popupFrom;
			private Label to;
			private ComboBox<String> popupTo;
		private HBox popupButtonHBox;
			private Button save;
			private Button discard;
		
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
	
	private void appointmentHandler (MouseEvent e, int i, String name) {
		//System.out.println (ids[i]);
		
		if (popup != null)
			popup.close ();
		
		popup = new Stage ();
		popup.setTitle (name);
		
		popupVbox = new VBox ();
		popupVbox.setAlignment (Pos.CENTER);
		
			appointmentPopup = new Label ();
			appointmentPopup.setText (name);
			appointmentPopup.setId ("DefaultLabel");
			appointmentPopup.setAlignment (Pos.CENTER_LEFT);
			
			popupDateLabel = new Label ();
			popupDateLabel.setText ("");
			popupDateLabel.setId ("DefaultLabel");
			popupDateLabel.setAlignment (Pos.CENTER_LEFT);
			
			popupTimeHBox = new HBox ();
			popupTimeHBox.setSpacing (20);
			popupTimeHBox.setAlignment (Pos.CENTER_LEFT);
			
				popupFrom = new ComboBox<String> ();
				popupFrom.setMinWidth(Screen.getPrimary ().getBounds ().getWidth () / 11.78);
				popupFrom.setId ("ComboBox");
				
				to = new Label ();
				to.setText ("To");
				to.setId("DefaultLabel");
				
				popupTo = new ComboBox<String> ();
				popupTo.setMinWidth (Screen.getPrimary ().getBounds ().getWidth () / 11.78);
				popupTo.setId ("ComboBox");
				
			popupTimeHBox.getChildren ().addAll (popupFrom, to, popupTo);
			
			popupButtonHBox = new HBox ();
			popupButtonHBox.setSpacing (20);
			popupButtonHBox.setAlignment (Pos.CENTER_RIGHT);
			
				save = new Button("Save");
				save.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
				save.getStyleClass ().add ("ButtonSave");
				
				discard = new Button ("Discard");
				discard.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
				discard.getStyleClass ().add ("ButtonDiscard");
			
			popupButtonHBox.getChildren ().addAll (save, discard);
			
		popupVbox.getChildren ().addAll (appointmentPopup, popupDateLabel, popupTimeHBox, popupButtonHBox);
		
		ArrayList <String> times = new ArrayList <String> ();
		for (int j = 0; j < 24; j++) {
			String hr = "";
			if (j < 10)
				hr = "0" + j;
			else
				hr += j;
			
			times.add (hr + ":00");
			times.add (hr + ":30");
		}
		
		for (String str: times) {
			popupFrom.getItems ().add (str);
			popupTo.getItems ().add (str);
		}
		
		save.setOnAction (mouseAction -> {
			dbc.createUpdate(ids[i], popupDateLabel.getText(), popupFrom.getValue(), popupTo.getValue());
			update ();
		});
		
		discard.setOnAction (mouseAction -> {
			popup.close();
			update ();
		});
		
		popup.setScene (new Scene (popupVbox, 200, 50));
		popup.getScene ().getStylesheets ().add ("./StyleSheet.css");
		popup.showAndWait();
	}
	
	public void update () {
		
	}
	
	
}