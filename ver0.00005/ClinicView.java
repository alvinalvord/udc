import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public abstract class ClinicView extends BorderPane implements View {
	
	protected ClinicViewController cvc;
	protected GregorianCalendarModel gcm;
	
	protected HBox upperHbox;
		protected Label viewLabel;
		protected Button today;
		
		protected HBox dateHBox;
			protected Label currentDate;
			
		protected HBox calendarAndAgendaHbox;
			protected ToggleGroup toggleGroup;
				protected ToggleButton calendar;
				protected ToggleButton agenda;
				
	protected HBox lowerHbox;
	
		protected VBox leftVbox;
			protected ToggleButton create;
			protected CalendarGrid calendarBox;
			
		protected BorderPane rightPane;
			protected HBox rightPaneUpperHBox;
				protected HBox weekAndDayHbox;
					protected ComboBox<String> viewSelect;
					protected ToggleGroup weekAndDayGroup;
						protected ToggleButton week;
						protected ToggleButton day;
			protected CalendarDayScroller calendarDayScroller;
			protected CalendarWeekScroller calendarWeekScroller;
			protected AgendaDayScroller agendaDayScroller;
			protected AgendaWeekScroller agendaWeekScroller;
			protected CreateBox createBox;
	
	public ClinicView (ClinicViewController cvc) {
		super ();
		
		this.cvc = cvc;
		
		gcm = new GregorianCalendarModel ();
		gcm.attach (this);
		
		initScene ();
		initHandlers ();
	}
	
	protected void initScene () {
		setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		initUpperHBox ();
		initLowerHBox ();
		
		setTop (upperHbox);
		setCenter (lowerHbox);
	}
	
	protected void initUpperHBox () {
		upperHbox = new HBox();
		upperHbox.setId ("UpperHbox");
		
		viewLabel = new Label ();
		viewLabel.setId ("DefaultLabel");
		
		today = new Button ("Today");
		today.getStyleClass ().add ("Today");
		
		dateHBox = new HBox ();
		dateHBox.setAlignment (Pos.CENTER);
		
			currentDate = new Label ();
			currentDate.setId ("DefaultLabel");
		
		dateHBox.getChildren ().add (currentDate);
		
		calendarAndAgendaHbox = new HBox ();
		calendarAndAgendaHbox.setAlignment (Pos.CENTER_RIGHT);
		
			toggleGroup = new ToggleGroup ();
				calendar = new ToggleButton ("Calendar");
				calendar.setToggleGroup (toggleGroup);
				calendar.getStyleClass ().add ("ToggleButton");
				calendar.setPrefWidth (150);
				calendar.setSelected (true);
				
				agenda = new ToggleButton ("Agenda");
				agenda.setToggleGroup (toggleGroup);
				agenda.getStyleClass ().add ("ToggleButton");
				agenda.setPrefWidth (150);
		
		calendarAndAgendaHbox.getChildren ().addAll (calendar, agenda);
		
		upperHbox.getChildren ().addAll (viewLabel, today, dateHBox, calendarAndAgendaHbox);
		
		HBox.setHgrow (today, Priority.ALWAYS);
		HBox.setHgrow (dateHBox, Priority.ALWAYS);
		HBox.setHgrow (calendarAndAgendaHbox, Priority.ALWAYS);
		
	}
	
	protected void initLowerHBox () {
		lowerHbox = new HBox ();
		lowerHbox.setId ("lowerHbox");
		
		initLeftVBox ();
		initRightPane ();
		
		lowerHbox.getChildren ().addAll (leftVbox, rightPane);
		HBox.setHgrow (rightPane, Priority.ALWAYS);
		HBox.setHgrow (leftVbox, Priority.ALWAYS);
		HBox.setHgrow(createBox, Priority.ALWAYS);
	}
	
	protected void initLeftVBox () {
		leftVbox = new VBox ();
		leftVbox.setId ("leftVbox");
		leftVbox.setAlignment (Pos.CENTER_LEFT);
		leftVbox.setMaxWidth (Screen.getPrimary ().getBounds ().getWidth () / 7);
		
		create = new ToggleButton ("Create");
		create.setToggleGroup(toggleGroup);
		create.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		create.getStyleClass ().add ("ButtonCreate");
		
		calendarBox = new CalendarGrid (gcm);
		
		leftVbox.getChildren ().addAll (create, calendarBox);
	}
	
	protected void initRightPane () {
		rightPane = new BorderPane ();
		rightPane.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		rightPane.setId ("UpperHbox");
		rightPane.setStyle ("-fx-padding: 5px;");
		
		initRightPaneUpperHBox ();
		
		calendarDayScroller = new CalendarDayScroller (cvc, gcm);
		calendarWeekScroller = new CalendarWeekScroller (cvc, gcm);
		agendaDayScroller = new AgendaDayScroller (cvc, gcm);
		agendaWeekScroller = new AgendaWeekScroller (cvc, gcm);
		createBox = new CreateBox (cvc, gcm);
		
		rightPane.setTop (rightPaneUpperHBox);
		rightPane.setCenter (calendarDayScroller);
	}
	
	protected void initRightPaneUpperHBox () {
		rightPaneUpperHBox = new HBox ();
			
			weekAndDayHbox = new HBox ();
			weekAndDayHbox.setAlignment (Pos.CENTER_RIGHT);
			
				viewSelect = new ComboBox <String> ();
				viewSelect.setId ("ViewSelectComboBox");
				
				for (String str: cvc.getViewableSchedules ())
					viewSelect.getItems ().add (str);
				
				viewSelect.getSelectionModel ().select (0);
			
				weekAndDayGroup = new ToggleGroup ();
					day = new ToggleButton ("Day");
					day.setToggleGroup (weekAndDayGroup);
					day.getStyleClass ().add ("ToggleButton");
					day.setPrefWidth (130);
					day.setSelected (true);
					
					week = new ToggleButton ("Week");
					week.setToggleGroup (weekAndDayGroup);
					week.getStyleClass ().add ("ToggleButton");
					week.setPrefWidth (130);
		
			weekAndDayHbox.getChildren ().addAll (day, week);
		
		rightPaneUpperHBox.getChildren ().addAll (viewSelect, weekAndDayHbox);
		HBox.setHgrow (weekAndDayHbox, Priority.ALWAYS);
	}
	
	protected void initHandlers () {
		today.setOnAction (e -> {
			gcm.setDateToday ();
			update ();
		});
		
		calendar.setOnAction (e -> {
			calendar.setSelected (true);
			create.setDisable (false);
			
			if (!lowerHbox.getChildren ().contains(rightPane)) {
				lowerHbox.getChildren ().removeAll (lowerHbox.getChildren ());
				lowerHbox.getChildren ().addAll (leftVbox, rightPane);
			}
			
			if (day.isSelected ())
				rightPane.setCenter (calendarDayScroller);
			else if (week.isSelected ())
				rightPane.setCenter (calendarWeekScroller);
				
			update ();
		});
		
		agenda.setOnAction (e -> {
			agenda.setSelected (true);
			create.setDisable (false);
			
			if (!lowerHbox.getChildren ().contains(rightPane)) {
				lowerHbox.getChildren ().removeAll (lowerHbox.getChildren ());
				lowerHbox.getChildren ().addAll (leftVbox, rightPane);
			}
			
			if (day.isSelected ())
				rightPane.setCenter (agendaDayScroller);
			else if (week.isSelected ())
				rightPane.setCenter (agendaWeekScroller);
				
			update ();
		});
		
		create.setOnAction (e -> {
			create.setDisable (true);
			lowerHbox.getChildren ().removeAll (lowerHbox.getChildren ());
			lowerHbox.getChildren ().addAll (leftVbox, createBox);
			update ();
		});
		
		day.setOnAction (e -> {
			day.setSelected (true);
			
			if (!lowerHbox.getChildren ().contains(rightPane)) {
				lowerHbox.getChildren ().removeAll (lowerHbox.getChildren ());
				lowerHbox.getChildren ().addAll (leftVbox, rightPane);
			}
			
			if (calendar.isSelected ())
				rightPane.setCenter (calendarDayScroller);
			else if (agenda.isSelected ())
				rightPane.setCenter (agendaDayScroller);
			update ();
		});
		
		week.setOnAction (e -> {
			week.setSelected (true);
			
			if (!lowerHbox.getChildren ().contains(rightPane)) {
				lowerHbox.getChildren ().removeAll (lowerHbox.getChildren ());
				lowerHbox.getChildren ().addAll (leftVbox, rightPane);
			}
			
			if (calendar.isSelected ())
				rightPane.setCenter (calendarWeekScroller);
			else if (agenda.isSelected ())
				rightPane.setCenter (agendaWeekScroller);
			update ();
		});
		
		viewSelect.setOnAction (e -> {
			update ();
		});
		
		cvc.getMainStage ().setFullScreenExitHint ("M E M E S");
		cvc.getMainStage ().maximizedProperty ().addListener ((a,b,c) -> {
			if (c) {
				cvc.getMainStage ().setMaximized (false);
				cvc.getMainStage ().setFullScreen (true);
			}
		});
	}
	
	public void setViewLabel (String str) {
		viewLabel.setText (str);
	}
	
	public void update () {
		agendaDayScroller.update ();
		currentDate.setText ("" + gcm.getCurrentMonth ().toString ().substring (0,3) + " " 
			+ gcm.getSelectedDate () + ", " + gcm.getCurrentYear ());
	}
	
}