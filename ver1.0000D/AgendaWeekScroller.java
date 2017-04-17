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
	private DatabaseModel dm;
	
	private VBox agendaVBox;
		private Label[] dayLabel;
		private ArrayList <Label> agendaScrollerLabel;
		
	public AgendaWeekScroller (DatabaseControl dbc, CalendarModel cm, DatabaseModel dm) {
		super ();
		
		this.dbc = dbc;
		this.cm = cm;
		this.dm = dm;

		cm.attach (this);
		dm.attach (this);
		
		initAWS ();
	}
	
	private void initAWS() {
		getStyleClass ().add ("AgendaDayScroller");
		
		setPrefHeight (551);
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		agendaVBox = new VBox ();
		agendaVBox.setId ("agendaScroller");
		
		initDayLabels ();
		
		setContent (agendaVBox);
		update();
	}

	private void initDayLabels () {
		dayLabel = new Label[cm.getCalendar ().getWeekCount ()];
		Days[] d = cm.getCalendar ().getWeekdays ();
		
		for (int i = 0; i < dayLabel.length; i++) {
			dayLabel[i] = new Label (d[i].toString ());
			dayLabel[i].setId("DefaultLabel");
			dayLabel[i].setAlignment(Pos.TOP_LEFT);
		}
		
	}
	
	public void update() {
		if(!agendaVBox.getChildren ().isEmpty ())
			agendaVBox.getChildren ().removeAll (agendaVBox.getChildren ());
		
		int[][] table = cm.getTable ();
		
		boolean actualStart = false;
		int weekIndex = -1;
		
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == 1)
					actualStart = true;
				
				if (actualStart) {
					if (cm.getSelectedDate () == table[i][j]) {
						weekIndex = i;
						break;
					}
				}
			}
			if (weekIndex != -1)
				break;
		}
		
		for (int i = 0; i < dayLabel.length; i++) {
			agendaVBox.getChildren().add(dayLabel[i]);
			
			ResultSet rs = null;
			
			rs = dbc.getDisplayData (cm.getCurrentYear (),
				cm.getCurrentMonth ().getMonth () + 1, table[weekIndex][i]);
			
			if (rs == null)
				continue;
			
			try {
				agendaScrollerLabel = new ArrayList<Label> ();
				ArrayList <Integer> appointers = new ArrayList <Integer> ();
				
				while (rs.next ()) {
					Label lbl = new Label ();
					String str = "\t";
					str += rs.getString ("start_time").substring (0, 5);
					str += " - ";
					str += rs.getString ("end_time").substring (0, 5);
					str += "     ";
					str += rs.getString ("name");
					
					lbl.setText (str);
					
					appointers.add (rs.getInt ("appointer_id"));
					agendaScrollerLabel.add (lbl);
				}
				
				dbc.setAgendaLabels (agendaScrollerLabel, appointers);
				
				for (Label l: agendaScrollerLabel)
					agendaVBox.getChildren ().add (l);
			} catch (Exception e) { e.printStackTrace (); }
		}
	}

}
