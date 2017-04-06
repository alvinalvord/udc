import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CalendarGrid extends VBox implements View {
	
	private CalendarModel calendar;
	private GregorianCalendarController gcc;
	
	private HBox topHbox;
	private Label monthYear;
	private HBox leftRightHbox;
		private Button leftButton;
		private Button rightButton;
	private GridPane dateGrid;
		private Label[] headers;
		private ToggleGroup toggleGroup;
			private ToggleButton[][] daysbtn;
	
	public CalendarGrid (CalendarModel calendar, GregorianCalendarController gcc) {
		super (10);
		
		this.calendar = calendar;
		this.gcc = gcc;
		calendar.attach (this);
		
		topHbox = new HBox ();
		topHbox.setAlignment (Pos.BOTTOM_RIGHT);
			monthYear = new Label ("March 2016");
			monthYear.setId ("CalendarLabel");
			
			leftRightHbox = new HBox (5);
			leftRightHbox.setAlignment (Pos.BOTTOM_RIGHT);
			
				leftButton = new Button ("<");
				leftButton.getStyleClass ().add ("CalendarArrows");
				
				rightButton = new Button (">");
				rightButton.getStyleClass ().add ("CalendarArrows");
				
			leftRightHbox.getChildren ().addAll (leftButton, rightButton);
		topHbox.getChildren ().addAll (monthYear, leftRightHbox);
		HBox.setHgrow (leftRightHbox, Priority.ALWAYS);
			
		dateGrid = new GridPane ();
		toggleGroup = new ToggleGroup ();
		
		getChildren ().addAll (topHbox, dateGrid);
		update ();
		
		initHandlers ();
	}
	
	public void update () {
		monthYear.setText ("" + calendar.getCurrentMonth ().toString() + " " + calendar.getCurrentYear ());
		
		if (!dateGrid.getChildren ().isEmpty ())
			dateGrid.getChildren ().removeAll (dateGrid.getChildren ());
	
		headers = new Label[calendar.getCalendar ().getWeekCount ()];
		for (int i = 0; i < calendar.getCalendar ().getWeekCount (); i++) {
			headers[i] = new Label ("" + calendar.getCalendar ().getWeekdays ()[i].getHeader ().charAt(0));
			headers[i].setId ("CalendarLabel");
			dateGrid.add (headers[i], i, 0);
			GridPane.setHalignment (headers[i], HPos.CENTER);
		}
		
		int[][] table = calendar.getTable ();
		daysbtn = new ToggleButton[table.length][table[0].length];
		
		boolean actualDate = false;
		
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				daysbtn[i][j] = new ToggleButton ("" + table[i][j]);
				daysbtn[i][j].getStyleClass ().add ("CalendarDates");
				daysbtn[i][j].setToggleGroup (toggleGroup);
				daysbtn[i][j].setMinWidth (35);
				daysbtn[i][j].setMaxWidth (35);
				
				if (table[i][j] == 1)
					actualDate = !actualDate;
				
				if (!actualDate)
					daysbtn[i][j].setDisable (true);
				
				if (calendar.getSelectedDate () == table[i][j] && actualDate)
					daysbtn[i][j].setSelected (true);
				
				final int x = i;
				final int y = j;
				daysbtn[i][j].setOnAction (e -> {
					daysbtn[x][y].setSelected(true);
					calendar.setSelectedDate (Integer.parseInt (daysbtn[x][y].getText ()));
				});
				
				dateGrid.add (daysbtn[i][j], j, i + 1);
			}
		}
		
		boolean hasSelected = false;
		
		for (ToggleButton[] tbg: daysbtn) {
			if (hasSelected)
				break;
			for (ToggleButton tb: tbg) {
				if (tb.isSelected ()) {
					hasSelected = true;
					break;
				}
			}
		}
		
		if (!hasSelected)
			for (int i = 0; i < daysbtn[0].length; i++)
				if (table[0][i] == 1) {
					calendar.setSelectedDate (1);
					daysbtn[0][i].setSelected (true);
				}
		
	}
	
	public void initHandlers () {
		leftButton.setOnAction (e -> {
			calendar.setCurrentMonth (calendar.getPrevMonth ().toString ());
		});
		rightButton.setOnAction (e -> {
			calendar.setCurrentMonth (calendar.getNextMonth ().toString ());
		});
	}
	
}
