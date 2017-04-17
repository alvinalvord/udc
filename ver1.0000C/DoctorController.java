import java.sql.*;
import java.text.*;
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
		private HBox popupHBox;
			private VBox leftVBox;
				private ToggleGroup toggleGroup;
					private ToggleButton move;
					private ToggleButton delete;
			private BorderPane rightPane;
				private VBox moveVBox;
					private Label appointment;
					private TextField date;
					private HBox popupTimeHBox;
						private ComboBox<String> popupFrom;
						private Label to;
						private ComboBox<String> popupTo;
					private HBox popupButtonHBox;
						private Button save;
						private Button discard;
				private VBox deleteVBox;
					private Label deleteAppointment;
					private HBox deleteHBox;
						private Button deleteConfirm;
						private Button deleteDiscard;

	public DoctorController (MainController mc) {
		this.mc = mc;
		cv = new DoctorView (this);
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}
	
	public void deleteRow(int id) {
		DatabaseModel.getInstance ().deleteRow (id);
	}

	public void createUpdate(int id, String... param) {
		DatabaseModel.getInstance ().timeConflict (Integer.toString(id), param[0], param[1] + ":00", param[2] + ":00");
		DatabaseModel.getInstance ().updateAppointment (Integer.toString(id), "`date` = '" + param[0] + "'", "`start_time` = '" + param[1] + ":00'", "`end_time` = '" + param[2] + ":00'");
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
	
	public void appointmentHandler (int id, String name, String date) {		
		if (popup != null)
			popup.close ();
		
		popup = new Stage ();
		popup.setMinWidth(640);
		popup.setMinHeight(480);
		popup.setTitle (name);
		
		popupHBox = new HBox ();
		popupHBox.setSpacing (20);
		popupHBox.setAlignment (Pos.CENTER);
		
			leftVBox = new VBox ();
			leftVBox.setSpacing(20);
			leftVBox.setAlignment (Pos.CENTER);
			
				toggleGroup = new ToggleGroup ();
				
					move = new ToggleButton ("Move");
					move.getStyleClass ().add ("ButtonCreate");
					move.setToggleGroup (toggleGroup);
					move.setSelected (true);
					
					delete = new ToggleButton ("Delete");
					delete.getStyleClass ().add ("ButtonCreate");
					delete.setToggleGroup (toggleGroup);
					
			leftVBox.getChildren ().addAll (move, delete);
			
			rightPane = new BorderPane ();
			rightPane.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
			rightPane.setId ("UpperHbox");
			rightPane.setStyle ("-fx-padding: 5px;");
			
				moveVBox = new VBox ();
				moveVBox.setSpacing (20);
				moveVBox.setAlignment (Pos.CENTER);
				
					appointment = new Label ();
					appointment.setText (name);
					appointment.setId ("DefaultLabel");
					appointment.setAlignment (Pos.CENTER_LEFT);
					
					this.date = new TextField ();
					this.date.setText (date);
					this.date.setId ("TextField");
					this.date.setAlignment (Pos.CENTER_LEFT);
					
					popupTimeHBox = new HBox ();
					popupTimeHBox.setSpacing (20);
					popupTimeHBox.setAlignment (Pos.CENTER_LEFT);
					
						popupFrom = new ComboBox<String> ();
						popupFrom.setMinWidth(Screen.getPrimary ().getBounds ().getWidth () / 11.78);
						popupFrom.getStyleClass ().add ("ComboBox");
						
						to = new Label ();
						to.setText ("To");
						to.setId("DefaultLabel");
						
						popupTo = new ComboBox<String> ();
						popupTo.setMinWidth (Screen.getPrimary ().getBounds ().getWidth () / 11.78);
						popupTo.getStyleClass ().add ("ComboBox");
						
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
					
				moveVBox.getChildren ().addAll (appointment, this.date, popupTimeHBox, popupButtonHBox);
				
				deleteVBox = new VBox ();
				deleteVBox.setSpacing (20);
				deleteVBox.setAlignment (Pos.CENTER);
				
					deleteAppointment = new Label ();
					deleteAppointment.setText("Delete " + name + "?");
					deleteAppointment.setId("DefaultLabel");
					
					deleteHBox = new HBox ();
					deleteHBox.setSpacing (20);
					deleteHBox.setAlignment (Pos.CENTER_RIGHT);
					
						deleteConfirm = new Button ("Confirm");
						deleteConfirm.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
						deleteConfirm.getStyleClass ().add ("ButtonSave");
						
						deleteDiscard = new Button ("Discard");
						deleteDiscard.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
						deleteDiscard.getStyleClass ().add ("ButtonDiscard");
					
					deleteHBox.getChildren ().addAll (deleteConfirm, deleteDiscard);
					
				deleteVBox.getChildren ().addAll (deleteAppointment, deleteHBox);
			
			rightPane.setCenter (moveVBox);
			
		popupHBox.getChildren ().addAll (leftVBox, rightPane);
		
		move.setOnAction (mouseAction -> {
			move.setSelected (true);
			rightPane.setCenter (moveVBox);
		});
		
		delete.setOnAction (mouseAction -> {
			delete.setSelected (true);
			rightPane.setCenter (deleteVBox);
		});
		
		save.setOnAction (mouseAction -> {
			if (popupFrom.getSelectionModel().isEmpty() || popupTo.getSelectionModel().isEmpty() || this.date.getText().isEmpty())
				invalidAlert ("Input is invalid/incomplete");
			else {
				try {
					SimpleDateFormat time = new SimpleDateFormat("HH:mm");
					
					if (time.parse(popupFrom.getValue()).compareTo(time.parse(popupTo.getValue())) >= 0)
						invalidAlert ("The start time " + popupFrom.getValue() + " is after the end time " + popupTo.getValue());
					else {
						createUpdate(id, this.date.getText(), popupFrom.getValue(), popupTo.getValue());
						popup.close();
					}
				}
				catch (Exception e) {
					invalidAlert ("Invalid date format");
				}		
			}
		});
		
		discard.setOnAction (mouseAction -> {
			popupFrom.getSelectionModel().select(0);
			popupTo.getSelectionModel().select(0);
		});
		
		deleteConfirm.setOnAction (mouseAction -> {
			deleteRow (id);
			popup.close();
		});
		
		deleteDiscard.setOnAction (mouseAction -> {
			rightPane.setCenter (moveVBox);
		});
		
		popup.setScene (new Scene (popupHBox, 200, 50));
		popup.getScene ().getStylesheets ().add ("./StyleSheet.css");
		popup.showAndWait();
	}
	
	public void invalidAlert (String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait().filter(response -> response == ButtonType.OK ).ifPresent(response -> {return;});
	}

}
