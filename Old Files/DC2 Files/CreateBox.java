import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.*;

public class CreateBox extends VBox implements View {
	
	private GregorianCalendarController gcc;
	private GregorianCalendarModel gcm;
	private AgendasModel am;

	private Label createLabel;
	private TextField createName;
	private HBox createCheckBoxHBox;
		private ToggleGroup toggleRadioButton;
			private RadioButton createEvent;
			private RadioButton createTask;
	private HBox createDateHBox;
		private VBox createDateVBox;
			private Label createDate;
		private ComboBox<String> createFrom;
		private Label createToLabel;
		private ComboBox<String> createTo;
	private HBox createButtonHBox;
		private Button save;
		private Button discard;
	
	private ArrayList<String> times;
	
	public CreateBox (GregorianCalendarController gcc, GregorianCalendarModel gcm, AgendasModel am) {
		super ();
		this.gcc = gcc;
		this.gcm = gcm;
		gcm.attach (this);
		this.am = am;
		am.attach (this);
		
		initCB ();
		initHandlers ();
	}
	
	private void initCB () {
		setId ("createBox");
			
			createLabel = new Label ("Create View");
			createLabel.setId ("DefaultLabel");
			
			createName = new TextField ("");
			createName.setId("TextField");
			createName.setMinSize(Screen.getPrimary ().getBounds ().getWidth () / 3.50, 55);
			
			createCheckBoxHBox = new HBox();
			createCheckBoxHBox.setSpacing (100);
			
				toggleRadioButton = new ToggleGroup();
					
					createEvent = new RadioButton ("Event");
					createEvent.setToggleGroup(toggleRadioButton);
					createEvent.getStyleClass ().add ("CheckBox");
					
					createTask = new RadioButton ("Task");
					createTask.setToggleGroup(toggleRadioButton);
					createTask.getStyleClass ().add ("CheckBox");
			
			createCheckBoxHBox.getChildren ().addAll (createEvent, createTask);
			
			createDateHBox = new HBox();
			createDateHBox.setSpacing (13);
			createDateHBox.setAlignment(Pos.CENTER_LEFT);
			
				createDateVBox = new VBox ();
				createDateVBox.setMinWidth(Screen.getPrimary ().getBounds ().getWidth () / 11.78);
				createDateVBox.setId("DateVBox");
				
					createDate = new Label ("Date here");
					createDate.setId ("DefaultLabel");
					
				createDateVBox.getChildren ().addAll (createDate);
				
				createFrom = new ComboBox<String>();
				createFrom.setMinWidth(Screen.getPrimary ().getBounds ().getWidth () / 11.78);
				createFrom.setId("ComboBox");
				
				createToLabel = new Label ("to");
				createToLabel.setId ("DefaultLabel");
				
				createTo = new ComboBox<String>();
				createTo.setMinWidth(Screen.getPrimary ().getBounds ().getWidth () / 11.78);
				createTo.setId("ComboBox");
			
			createDateHBox.getChildren ().addAll (createDateVBox, createFrom, createToLabel, createTo);
			
			createButtonHBox = new HBox();
			createButtonHBox.setAlignment(Pos.CENTER_RIGHT);
			createButtonHBox.setSpacing(10);
				
				save = new Button("Save");
				save.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
				save.getStyleClass ().add ("ButtonSave");
				
				discard = new Button ("Discard");
				discard.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
				discard.getStyleClass ().add ("ButtonDiscard");
				
			createButtonHBox.getChildren ().addAll (save, discard);
		
		getChildren ().addAll (createName, createCheckBoxHBox, createDateHBox, createButtonHBox);
		
		times = new ArrayList <String> ();
		for (int i = 0; i < 24; i++) {
			String hr = "";
			if (i < 10)
				hr = "0" + i;
			else
				hr += i;
			
			times.add (hr + ":00");
			times.add (hr + ":30");
		}
		
		for (String str: times) {
			createFrom.getItems ().add (str);
			createTo.getItems ().add (str);
		}
		
	}
	
	private void initHandlers () {
		save.setOnAction (e -> {
			if (createName.getText ().length () <= 0 || createName.getText () == null) {
				alertInvalid ();
				return;
			}
			if (!createEvent.isSelected () && !createTask.isSelected ()) {
				alertInvalid ();
				return;
			}
			if (!times.contains (createFrom.getValue ())) {
				alertInvalid ();
				return;
			}
			if (!times.contains (createTo.getValue ())) {
				alertInvalid ();
				return;
			}
			
			if (times.indexOf (createFrom.getValue ()) >= 
				times.indexOf (createTo.getValue ())) {
				Alert a = new Alert (Alert.AlertType.ERROR, "Invalid times selected", ButtonType.OK);
				a.showAndWait().
					filter(response -> response == ButtonType.OK).
					ifPresent(response -> {return;});
			}
			
			int n = gcc.createInsert (createName.getText (), createEvent.isSelected (), createFrom.getValue (), createTo.getValue (), gcm.getCurrentYear (), gcm.getCurrentMonth ().getMonth () + 1, gcm.getSelectedDate ());
			
			if (n == -1) {
				Alert a = new Alert (Alert.AlertType.ERROR, "Time conflict", ButtonType.OK);
				a.showAndWait ().
					filter (response -> response == ButtonType.OK).
					ifPresent (response -> {return;});
			}
			
			else if (n == 0) {
				Alert a = new Alert (Alert.AlertType.ERROR, "Unable to add", ButtonType.OK);
				a.showAndWait ().
					filter (response -> response == ButtonType.OK).
					ifPresent (response -> {return;});
			}
			
			else {
				Alert a = new Alert (Alert.AlertType.INFORMATION, "Successfully added", ButtonType.OK);
				a.showAndWait ().
					filter (response -> response == ButtonType.OK).
					ifPresent (response -> {
						discard.fire ();
						return;
					});
			}
		});
		
		discard.setOnAction (e -> {
			createName.setText ("");
			createEvent.setSelected (false);
			createTask.setSelected (false);
			
			if (!createFrom.getItems ().isEmpty ())
				createFrom.getItems ().removeAll (createFrom.getItems ());
			
			if (!createTo.getItems ().isEmpty ())
				createTo.getItems ().removeAll (createTo.getItems ());
			
			for (String str: times) {
				createFrom.getItems ().add (str);
				createTo.getItems ().add (str);
			}
		});
	}
	
	private void alertInvalid () {
		Alert alert = new Alert (Alert.AlertType.ERROR, "Incomplete values", ButtonType.OK);
		alert.showAndWait().
			filter(response -> response == ButtonType.OK).
			ifPresent(response -> {return;});
	}
	
	public void update () {
		createDate.setText ("" + (gcm.getCurrentMonth ().getMonth () + 1) + "/" +
			gcm.getSelectedDate () + "/" + gcm.getCurrentYear ());
	}
	
}