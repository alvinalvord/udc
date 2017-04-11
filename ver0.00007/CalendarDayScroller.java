import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.stage.*;
import java.util.*;
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
			
	private boolean secretaryMode;
	private int viewingId;
	
	public CalendarDayScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		
		this.dbc = dbc;
		this. cm = cm;
		secretaryMode = false;
		viewingId = 0;
		
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
	
	public void setViewingId (int id) {
		viewingId = id;
		update ();
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
			rs = dbc.getUpdates ("" + viewingId, cm.getCurrentYear (), cm.getCurrentMonth ().getMonth () + 1, cm.getSelectedDate ());
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