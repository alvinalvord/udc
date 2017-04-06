import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class GregorianCalendarView extends BorderPane implements View {
	
	private GregorianCalendarController gcc;
	private GregorianCalendarModel gcm;
	private AgendasModel agendasModel;
	private boolean fullScreenFlag;
	
	private HBox upperHbox;
		private Label myProductivityTool;
		private Button today;
		private HBox dateHBox;
			private Label currentDate;
		private HBox toggleHBox;
			private ToggleGroup toggleGroup;
				private ToggleButton day;
				private ToggleButton agenda;
	private HBox lowerHbox;
		private VBox leftVbox;
			private ToggleButton create;
			private CalendarGrid calendarBox;
			private Label view;
			private HBox eventHBox;
				private CheckBox event;
				private Label eventLabel;
			private HBox taskHBox;
				private CheckBox task;
				private Label taskLabel;
		private Pane rightPane;
			private DayScroller dayScroller;
			private AgendaScroller agendaScroller;
			private CreateBox createBox;
	
	public GregorianCalendarView (GregorianCalendarController gcc) {
		super ();
		
		this.gcc = gcc;
		
		gcm = new GregorianCalendarModel ();
		gcm.attach (this);
		
		agendasModel = new AgendasModel ();
		agendasModel.attach (this);
		
		fullScreenFlag = false;
		
		initScene ();
		initHandlers();
	}
	
	private void initScene () {
		setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		initUpperHbox ();
		initLowerHBox ();
		
		setTop (upperHbox);
		setCenter (lowerHbox);
	}
	
	private void initUpperHbox () {
		upperHbox = new HBox ();
		upperHbox.setId ("UpperHbox");
		
		myProductivityTool = new Label ("My Productivity Tool");
		myProductivityTool.setId ("DefaultLabel");
		
		today = new Button ("Today");
		today.getStyleClass ().add ("Today");
		
		dateHBox = new HBox ();
		dateHBox.setAlignment (Pos.CENTER);
		
			currentDate = new Label ("currentDate");
			currentDate.setId ("DefaultLabel");
			
		dateHBox.getChildren ().add (currentDate);
		
		toggleHBox = new HBox ();
		toggleHBox.setAlignment (Pos.CENTER_RIGHT);
		
			toggleGroup = new ToggleGroup ();
				day = new ToggleButton ("Day");
				day.setToggleGroup (toggleGroup);
				day.getStyleClass ().add ("ToggleButton");
				day.setPrefWidth (130);
				day.setSelected (true);
				
				agenda = new ToggleButton ("Agenda");
				agenda.setToggleGroup (toggleGroup);
				agenda.getStyleClass ().add ("ToggleButton");
				agenda.setPrefWidth (130);
		
		toggleHBox.getChildren ().addAll (day, agenda);
		
		upperHbox.getChildren ().addAll (myProductivityTool, today, dateHBox, toggleHBox);
		HBox.setHgrow (today, Priority.ALWAYS);
		HBox.setHgrow (toggleHBox, Priority.ALWAYS);
		HBox.setHgrow (dateHBox, Priority.ALWAYS);
	}
	
	private void initLowerHBox () {
		lowerHbox = new HBox ();
		lowerHbox.setId ("lowerHbox");
		
		initLeftVBox ();
		initRightPane ();
		
		lowerHbox.getChildren ().addAll (leftVbox, rightPane);
		HBox.setHgrow (rightPane, Priority.ALWAYS);
		HBox.setHgrow (leftVbox, Priority.ALWAYS);
	}
	
	private void initLeftVBox () {
		leftVbox = new VBox ();
		leftVbox.setId ("leftVbox");
		leftVbox.setMaxWidth (Screen.getPrimary ().getBounds ().getWidth () / 7);
		
		create = new ToggleButton ("Create");
		create.setToggleGroup(toggleGroup);
		create.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		create.getStyleClass ().add ("ButtonCreate");
		
		calendarBox = new CalendarGrid (gcm, gcc);
		
		view = new Label ("View");
		view.setId ("DefaultLabel");
		
		DropShadow dropshadow = new DropShadow();
		dropshadow.setOffsetX (4.0);
		dropshadow.setOffsetY (4.0);
		dropshadow.setRadius (0.1);
		
		eventHBox = new HBox();
		eventHBox.setId ("checkboxHbox");
		
		eventLabel = new Label ("Event");
		eventLabel.setId ("CheckBoxLabel");
		
		event = new CheckBox ();
		event.getStyleClass ().add ("CheckBox");
		event.setEffect (dropshadow);
		
		taskHBox = new HBox ();
		taskHBox.setId ("checkboxHbox");
		
		taskLabel = new Label ("Task");
		taskLabel.setId ("CheckBoxLabel");
		
		task = new CheckBox ();
		task.getStyleClass ().add ("CheckBox");
		task.setEffect (dropshadow);
		
		eventHBox.getChildren ().addAll (event, eventLabel);
		taskHBox.getChildren ().addAll (task, taskLabel);
		leftVbox.getChildren ().addAll (create, calendarBox, view, eventHBox, taskHBox);
	}
	
	private void initRightPane () {
		rightPane = new Pane ();
		rightPane.setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		
		dayScroller = new DayScroller (gcc, gcm, agendasModel);
		agendaScroller = new AgendaScroller (gcc, gcm, agendasModel);
		createBox = new CreateBox (gcc, gcm, agendasModel);
		
		rightPane.getChildren ().add (dayScroller);
	}
	
	public boolean showEvents () {
		return event.isSelected ();
	}
	
	public boolean showTasks () {
		return task.isSelected ();
	}
	
	public void update () {
		calendarBox.update ();
		dayScroller.update ();
		agendaScroller.update ();
		createBox.update ();
		currentDate.setText ("" + gcm.getCurrentMonth ().toString ().substring (0,3) + " " 
			+ gcm.getSelectedDate () + ", " + gcm.getCurrentYear ());
	}
	
	private void initHandlers () {
		today.setOnAction(e -> {
			gcm.setDateToday ();
			update ();
		});
		day.setOnAction(e -> {
			day.setSelected(true);
			create.setDisable(false);
			gcc.setRightPane (dayScroller, rightPane);
			update ();
		});
		agenda.setOnAction(e -> {
			agenda.setSelected(true);
			create.setDisable(false);
			gcc.setRightPane (agendaScroller, rightPane);
			update ();
		});
		create.setOnAction(e -> {
			create.setDisable(true);
			gcc.setRightPane (createBox, rightPane);
			update ();
		});
		event.setOnAction(e -> {
			update ();
		});
		task.setOnAction(e -> {
			update ();
		});
		
		gcc.getMainStage ().setFullScreenExitHint ("M E M E S");
		gcc.getMainStage ().maximizedProperty ().addListener ((a,b,c) -> {
			if (c) {
				fullScreenFlag = true;
				gcc.getMainStage ().setMaximized (false);
				gcc.getMainStage ().setFullScreen (true);
			}
		});
		
		gcc.getMainStage ().widthProperty ().addListener ((obs, oldVal, newVal) -> {
			dayScroller.setMinWidth((double)newVal - 375);
			dayScroller.setPrefWidth ((double)newVal - 375);
			dayScroller.getDayVbox ().setMinWidth ((double)newVal - 400);
			dayScroller.getDayVbox ().setPrefWidth((double)newVal - 400);
			agendaScroller.setMinWidth((double)newVal - 375);
			agendaScroller.setPrefWidth((double)newVal - 375);
			agendaScroller.getAgendaVBox () .setMinWidth ((double)newVal - 400);
			agendaScroller.getAgendaVBox () .setPrefWidth((double)newVal - 400);
		});
		
		gcc.getMainStage ().heightProperty ().addListener ((obs, oldVal, newVal) -> {
			if (fullScreenFlag) {
				fullScreenFlag = false;
				dayScroller.setMinHeight((double)newVal - 129);
				dayScroller.setPrefHeight ((double)newVal - 129);
				agendaScroller.setMinHeight((double)newVal - 129);
				agendaScroller.setPrefHeight((double)newVal - 129);
			}
			else {
				dayScroller.setMinHeight((double)newVal - 169);
				dayScroller.setPrefHeight ((double)newVal - 169);
				agendaScroller.setMinHeight((double)newVal - 169);
				agendaScroller.setPrefHeight((double)newVal - 169);
			}
		});
	}
}