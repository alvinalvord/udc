import java.sql.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class SecretaryController extends DatabaseControl {
	private MainController mc;
	private ClinicView cv;
	
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
	
	public SecretaryController (MainController mc) {
		this.mc = mc;
		cv = new SecretaryView (this);
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}

	public void deleteRow(int id) {
		
	}
	
	public void createUpdate(int id, String... param) {
		
	}

	public int createInsert(String... param) {
		return 0;
	}

	public void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers) {
		for (int i = 0; i < labels.size (); i++) {
			labels.get (i).setId ("DefaultLabel");
			try {
				ResultSet rs = DatabaseModel.getInstance ().getData ("users", "`id` = '" + appointers.get (i) + "'");
				
				if (rs.next ()) {
					switch (rs.getInt ("user_level")) {
						case 1:
							labels.get (i).setId ("ClientLabel");
							break;
						case 2:
							labels.get (i).setId ("SecretaryLabel");
							break;
						case 3:
							labels.get (i).setId ("DoctorLabel");
							break;
					}
				}
			} catch (Exception e) { e.printStackTrace (); }
		}
	}
	
	public void setCalendarLabels (Label[] labels, int[] appointers) {
		for (int i = 0; i < labels.length; i++) {
			labels[i].setId ("TimeLabelsNone");
			try {
				ResultSet rs = DatabaseModel.getInstance ().getData ("users", "`id` = '" + appointers[i] + "'");
				
				if (rs.next ()) {
					switch (rs.getInt ("user_level")) {
						case 1:
							labels[i].getStyleClass ().add ("TimeLabelsClient");
							break;
						case 2:
							labels[i].getStyleClass ().add ("TimeLabelsSecretary");
							break;
						case 3:
							labels[i].getStyleClass ().add ("TimeLabelsDoctor");
							break;
					}
				}
			} catch (Exception e) { e.printStackTrace (); }
		}
	}
	
	public ClinicView getView() {
		return cv;
	}
	
	public void appointmentHandler (int id, String name, String date) {
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
			popupDateLabel.setText (date);
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
			//createUpdate(ids[i][x], popupDateLabel.getText(), popupFrom.getValue(), popupTo.getValue());
		});
		
		discard.setOnAction (mouseAction -> {
			popup.close();
		});
		
		popup.setScene (new Scene (popupVbox, 200, 50));
		popup.getScene ().getStylesheets ().add ("./StyleSheet.css");
		popup.showAndWait();
	}
}
