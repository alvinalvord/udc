import java.sql.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class DoctorController extends DatabaseControl {

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
			
	public DoctorController (MainController mc) {
		this.mc = mc;
		cv = new DoctorView (this);
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}
	
	public void deleteRow(int id) {
		
	}

	public void createUpdate(int id, String... param) {
		
	}

	public int createInsert(String... param) {
		
		boolean b = DatabaseModel.getInstance ().timeConflict (param[4], param[1], param[2], param[3]);
		
		if (b)
			return -1;
		
		if (param.length == 5) {
			String[] arr = {"name", "date", "start_time", "end_time", "appointee_id"};
			return DatabaseModel.getInstance ().insertAppointment (arr, param[0], param[1], param[2], param[3], param[4]);
		}
			
		else if (param.length == 6) {
			String[] arr = {"name", "date", "start_time", "end_time", "appointer_id", "appointee_id"};
			return DatabaseModel.getInstance ().insertAppointment (arr, param[0], param[1], param[2], param[3], Integer.toString(DatabaseModel.getInstance().getUserID()), param[4]);
		}
		
		return 0;
	}

	public void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers) {
		for (Label lbl: labels)
			lbl.setId ("DefaultLabel");
	}
	
	public void setCalendarLabels (Label[] labels, int[] appointers) {
		for (Label lbl: labels)
			lbl.setId ("TimeLabelsNone");
	}
	
	public ClinicView getView() {
		return cv;
	}
	
	public void appointmentHandler (int id, String name) {
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
			//createUpdate(ids[i], popupDateLabel.getText(), popupFrom.getValue(), popupTo.getValue());
		});
		
		discard.setOnAction (mouseAction -> {
			popup.close();
		});
		
		popup.setScene (new Scene (popupVbox, 200, 50));
		popup.getScene ().getStylesheets ().add ("./StyleSheet.css");
		popup.showAndWait();
	}

}
