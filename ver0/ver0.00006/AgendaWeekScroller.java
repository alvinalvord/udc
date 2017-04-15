import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.*;
import java.sql.*;

public class AgendaWeekScroller extends ScrollPane implements View{

	private DatabaseControl dbc;
	private CalendarModel cm;
	
	private VBox agendaVBox;
		private Label[] dayLabel;
		private ArrayList <Label> agendaScrollerLabel;
	
	public AgendaWeekScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		
		this.dbc = dbc;
		this.cm = cm;
		
		cm.attach (this);
		
		initAWS ();
	}
	
	private void initAWS() {
		getStyleClass ().add ("AgendaDayScroller");
		
		setPrefHeight (551);
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		agendaVBox = new VBox ();
		agendaVBox.setId ("agendaScroller");
		
		setContent (agendaVBox);
		update();
	}

	public void update() {
		if(!agendaVBox.getChildren ().isEmpty ())
			agendaVBox.getChildren ().removeAll (agendaVBox.getChildren ());
		
		dayLabel = new Label[7];
		agendaScrollerLabel = new ArrayList<Label> ();
		
		for (int i = 0; i < dayLabel.length; i++) {
			dayLabel[i] = new Label ();
			dayLabel[i].setId("DefaultLabel");
			dayLabel[i].setAlignment(Pos.TOP_LEFT);
			
			if (i == 0)
				dayLabel[i].setText ("Sunday");
			if (i == 1)
				dayLabel[i].setText ("Monday");
			if (i == 2)
				dayLabel[i].setText ("Tuesday");
			if (i == 3)
				dayLabel[i].setText ("Wednesday");
			if (i == 4)
				dayLabel[i].setText ("Thursday");
			if (i == 5)
				dayLabel[i].setText ("Friday");
			if (i == 6)
				dayLabel[i].setText ("Saturday");
			
			Label lbl = new Label ("TIME APPOINTMENT");
			lbl.setId("DefaultLabel");
			agendaScrollerLabel.add(lbl);
			
			agendaVBox.getChildren().addAll(dayLabel[i], lbl);
		}
	}

}
