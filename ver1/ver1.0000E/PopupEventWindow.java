import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;

public class PopupEventWindow extends Stage {
	
	private DatabaseControl dbc;
	private final int id;
	private String eventName;
	private String eventDate;
	
	private Scene scene;
	private BorderPane borderPane;
		private HBox topHbox;
			private Label eventNameLabel;
		private VBox leftVbox;
			private ToggleGroup tg;
				private ArrayList <ToggleButton> buttonGroup;
	
	public PopupEventWindow (Stage parentStage, 
		int id, String name, String date, 
		DatabaseControl dbc, 
		OptionType... options) 
	{
		initOwner (parentStage);
		initModality (Modality.WINDOW_MODAL);
		
		setTitle (name);
		setMinWidth (640);
		setMinHeight (480);
		
		this.dbc = dbc;
		this.id = id;
		eventName = name;
		eventDate = date;
		
		borderPane = new BorderPane ();
		initTopHbox ();
		initLeftVbox ();
		
		for (OptionType op: options)
			initOption (op);
		
		for (ToggleButton tb: buttonGroup)
			leftVbox.getChildren ().add (tb);
		
		buttonGroup.get (0).fire ();
		
		scene = new Scene (borderPane);
		scene.getStylesheets ().add ("./StyleSheet.css");
		
		setScene (scene);
		showAndWait ();
	}
	
	private void initTopHbox () {
		topHbox = new HBox ();
		topHbox.setAlignment (Pos.CENTER);
		
		eventNameLabel = new Label (eventName);
		eventNameLabel.setId ("DefaultLabel");
		
		topHbox.getChildren ().add (eventNameLabel);
		
		BorderPane.setMargin (topHbox, new Insets (30,30,30,30));
		borderPane.setTop (topHbox);
	}
	
	private void initLeftVbox () {
		leftVbox = new VBox (10);
		leftVbox.setStyle ("-fx-padding: 5px; -fx-alignment: top-center;");
		
		tg = new ToggleGroup ();
		buttonGroup = new ArrayList <ToggleButton> ();
		
		borderPane.setLeft (leftVbox);
	}
	
	private void initOption (OptionType option) {
		switch (option) {
			case MOVE_APPOINTMENT:
				addButton ("Move", new MoveAppointment ());
				break;
			
			case DELETE_APPOINTMENT:
				addButton ("Delete", new DeleteAppointment ());
				break;
			
			case SELF_APPOINTMENT:
				addButton ("Book", new SelfAppointment ());
				break;
			
			case BOOK_APPOINTMENT:
				addButton ("Book", new BookAppointment ());
				break;
			
			case CANCEL_APPOINTMENT:
				addButton ("Cancel", new CancelAppointment ());
				break;
				
			case NOTIFY_APPOINTMENT:
				addButton ("Notify", new NotifyAppointment ());
				break;
		}
	}
	
	private void addButton (String str, Pane pane) {
		ToggleButton button = new ToggleButton (str);
		button.getStyleClass ().add ("ButtonCreate");
		button.setToggleGroup (tg);
		button.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		
		button.setOnAction (e -> {
			button.setSelected (true);
			borderPane.setCenter (pane);
		});
		
		buttonGroup.add (button);
	}
	
	private class MoveAppointment extends VBox {
		
		private HBox dateHBox;
			private Label dateLabel;
			private TextField dateField;
		private HBox timeHBox;
			private ComboBox <String> fromTime;
			private Label toLabel;
			private ComboBox <String> toTime;
		private HBox optionsHBox;
			private Button save;
			private Button discard;
			
		public MoveAppointment () {
			super (20);
		
			initDateBox ();
			initTimeBox ();
			initOptionsBox ();
		}
		
		private void initDateBox () {
			dateHBox = new HBox (20);
			dateHBox.setAlignment (Pos.CENTER);
			
			dateLabel = new Label ("Date: ");
			dateLabel.setId ("DefaultLabel");
			
			dateField = new TextField ();
			dateField.setText (eventDate);
			dateField.setId ("TextField");
			dateField.setAlignment (Pos.CENTER_LEFT);
			
			dateHBox.getChildren ().addAll (dateLabel, dateField);
			
			getChildren ().add (dateHBox);
		}
		
		private void initTimeBox () {
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
			
			timeHBox = new HBox (10);
			timeHBox.setAlignment (Pos.CENTER);
			
			fromTime = new ComboBox <String> ();
			fromTime.getStyleClass ().add ("ComboBox");
			
			toLabel = new Label ("to");
			toLabel.setId ("DefaultLabel");
			
			toTime = new ComboBox <String> ();
			toTime.getStyleClass ().add ("ComboBox");
			
			for (String str: times) {
				fromTime.getItems ().add (str);
				toTime.getItems ().add (str);
			}
			
			timeHBox.getChildren ().addAll (fromTime, toLabel, toTime);
			getChildren ().add (timeHBox);
		}
		
		private void initOptionsBox () {
			optionsHBox = new HBox (20);
			optionsHBox.setAlignment (Pos.CENTER_RIGHT);
			
			save = new Button ("Save");
			save.getStyleClass ().add ("ButtonSave");
			save.setOnAction (action -> {
				if (fromTime.getSelectionModel().isEmpty() || toTime.getSelectionModel().isEmpty() || dateField.getText().isEmpty())
					dbc.invalidAlert ("Input is invalid/incomplete");
				else {
					try {
						SimpleDateFormat time = new SimpleDateFormat("HH:mm");
						
						if (time.parse (fromTime.getValue ()).
							compareTo (time.parse (toTime.getValue ())) 
							>= 0)
							dbc.invalidAlert ("The start time " + 
								fromTime.getValue() + 
								" is after the end time " + 
								toTime.getValue());
						else {
							if (dbc.createUpdate(id, dateField.getText(), fromTime.getValue(), toTime.getValue()) != -1)
								close();
						}
					}
					catch (Exception e) {
						dbc.invalidAlert ("Invalid date format");
					}		
				}
			});
			
			discard = new Button ("Discard");
			discard.getStyleClass ().add ("ButtonDiscard");
			discard.setOnAction (action -> { 
				dateField.setText (eventDate);
				fromTime.getSelectionModel ().selectFirst ();
				toTime.getSelectionModel ().selectFirst ();
			});
			
			optionsHBox.getChildren ().addAll (save, discard);
			getChildren ().add (optionsHBox);
		}
		
	}
	
	private class DeleteAppointment extends VBox {
		
		private Label deleteLabel;
		private HBox optionsHBox;
			private Button confirm;
			private Button cancel;
		
		public DeleteAppointment () {
			super (20);
			setAlignment (Pos.TOP_CENTER);
			
			deleteLabel = new Label ("Delete " + eventName + "?");
			deleteLabel.setId ("DefaultLabel");
			getChildren ().add (deleteLabel);
			
			initOptionsBox ();
		}
		
		private void initOptionsBox () {
			optionsHBox = new HBox (20);
			optionsHBox.setAlignment (Pos.CENTER);
			
			confirm = new Button ("Confirm");
			confirm.getStyleClass ().add ("ButtonSave");
			confirm.setOnAction (action -> {
				dbc.deleteRow (id);
				close ();
			});
			
			cancel = new Button ("Cancel");
			cancel.getStyleClass ().add ("ButtonDiscard");
			cancel.setOnAction (action -> {
				close ();
			});
			
			optionsHBox.getChildren ().addAll (confirm, cancel);
			getChildren ().add (optionsHBox);
		}
	}
	
	private class SelfAppointment extends VBox {
		
		private TextField appointment;
		private HBox optionsHBox;
			private Button confirm;
			private Button cancel;
			
		public SelfAppointment () {
			super (20);
			setAlignment (Pos.CENTER);
			
			appointment = new TextField ();
			appointment.setText ("Appointment Name");
			appointment.setId ("TextField");
			
			getChildren ().add (appointment);
			
			initOptionsBox ();
		}
		
		private void initOptionsBox () {
			optionsHBox = new HBox (20);
			optionsHBox.setAlignment (Pos.CENTER);
			
			confirm = new Button ("Confirm");
			confirm.getStyleClass ().add ("ButtonSave");
			confirm.setOnAction (action -> {
				dbc.createUpdate(id, appointment.getText());
				close ();
			});
			
			cancel = new Button ("Cancel");
			cancel.getStyleClass ().add ("ButtonDiscard");
			cancel.setOnAction (action -> {
				close ();
			});
			
			optionsHBox.getChildren ().addAll (confirm, cancel);
			getChildren ().add (optionsHBox);
		}
	}
	
	private class BookAppointment extends VBox {
		
		private ComboBox<String> clients;
		private TextField appointment;
		
		private HBox optionsHBox;
			private Button confirm;
			private Button cancel;
			
		private ArrayList<String> appointer_ids;
		
		public BookAppointment () {
			super (20);
			setAlignment (Pos.CENTER);
			
			appointment = new TextField ();
			appointment.setText ("Appointment Name");
			appointment.setId ("TextField");
			
			getChildren ().add (appointment);
			
			initComboBox ();
			initOptionsBox ();
		}
		
		private void initComboBox () {
			clients = new ComboBox<String> ();
			appointer_ids = new ArrayList<String> ();
			ResultSet rs = DatabaseModel.getInstance().getData("users", "`user_level` = '1'");
			try {
				while (rs.next()) {
					clients.getItems().add(rs.getString("name"));
					appointer_ids.add(rs.getString("id"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			getChildren ().add (clients);
		}
		
		private void initOptionsBox () {
			optionsHBox = new HBox (20);
			optionsHBox.setAlignment (Pos.CENTER);
			
			confirm = new Button ("Confirm");
			confirm.getStyleClass ().add ("ButtonSave");
			confirm.setOnAction (action -> {
				dbc.createUpdate(id, appointment.getText (), appointer_ids.get (clients.getSelectionModel().getSelectedIndex()));
				close ();
			});
			
			cancel = new Button ("Cancel");
			cancel.getStyleClass ().add ("ButtonDiscard");
			cancel.setOnAction (action -> {
				close ();
			});
			
			optionsHBox.getChildren ().addAll (confirm, cancel);
			getChildren ().add (optionsHBox);
		}
	}
	
	private class CancelAppointment extends VBox {
	
		private Label appointment;
		
		private HBox optionsHBox;
			private Button confirm;
			private Button cancel;
		
		private CancelAppointment () {
			super (20);
			setAlignment (Pos.CENTER);
			
			appointment = new Label ();
			appointment.setText ("Cancel your appointment: " + eventName + "?");
			appointment.setId ("Default");
			
			getChildren ().add (appointment);
			
			initOptionsBox ();
		}
		
		private void initOptionsBox () {
			optionsHBox = new HBox (20);
			optionsHBox.setAlignment (Pos.CENTER);
			
			confirm = new Button ("Confirm");
			confirm.getStyleClass ().add ("ButtonSave");
			confirm.setOnAction (action -> {
				dbc.createUpdate(id, "");
				close ();
			});
			
			cancel = new Button ("Discard");
			cancel.getStyleClass ().add ("ButtonDiscard");
			cancel.setOnAction (action -> {
				close ();
			});
			
			optionsHBox.getChildren ().addAll (confirm, cancel);
			getChildren ().add (optionsHBox);
		}
	}
	
	private class NotifyAppointment extends VBox {
		
		private Label appointment;
		
		private HBox optionsHBox;
			private Button confirm;
			private Button cancel;
		
		public NotifyAppointment () {
			super (20);
			setAlignment (Pos.CENTER);
			
			appointment = new Label ();
			appointment.setText ("Notify appointment: " + eventName + "?");
			appointment.setId ("Default");
			
			initOptionsBox ();
		}
		
		private void initOptionsBox () {
			optionsHBox = new HBox (20);
			optionsHBox.setAlignment (Pos.CENTER);
			
			confirm = new Button ("Confirm");
			confirm.getStyleClass ().add ("ButtonSave");
			confirm.setOnAction (action -> {
				dbc.createUpdate(id, "");
				close ();
			});
			
			cancel = new Button ("Discard");
			cancel.getStyleClass ().add ("ButtonDiscard");
			cancel.setOnAction (action -> {
				close ();
			});
			
			optionsHBox.getChildren ().addAll (confirm, cancel);
			getChildren ().add (optionsHBox);
		}
	}
	
	public enum OptionType {
		MOVE_APPOINTMENT,
		DELETE_APPOINTMENT,
		SELF_APPOINTMENT,
		BOOK_APPOINTMENT,
		CANCEL_APPOINTMENT,
		NOTIFY_APPOINTMENT;
	}
	
}