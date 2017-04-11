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
	
	private int viewingId;
	private boolean secretaryMode;
	
	public AgendaWeekScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		
		this.dbc = dbc;
		this.cm = cm;
		
		viewingId = 0;
		secretaryMode = false;
		
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

	
	public void setViewingId (int id) {
		viewingId = id;
		update ();
	}
	
	public void setSecretaryMode (boolean b) {
		secretaryMode = b;
	}
	
	public void update() {
		if(!agendaVBox.getChildren ().isEmpty ())
			agendaVBox.getChildren ().removeAll (agendaVBox.getChildren ());
		
		dayLabel = new Label[7];
		
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
			
			agendaVBox.getChildren().add(dayLabel[i]);
			
			ResultSet rs = null;
			try {
				rs = dbc.getUpdates ("" + viewingId, cm.getCurrentYear (),
				cm.getCurrentMonth ().getMonth () + 1, table[weekIndex][i]);
			} catch (Exception e) { e.printStackTrace (); }
			
			if (rs == null)
				continue;
			
			try {
				agendaScrollerLabel = new ArrayList<Label> ();
				ArrayList <Integer> accessLevels = new ArrayList <Integer> ();
				
				while (rs.next ()) {
					Label lbl = new Label ();
					String str = "";
					str += rs.getString ("start_time").substring (0, 5);
					str += " - ";
					str += rs.getString ("end_time").substring (0, 5);
					str += "     ";
					str += rs.getString ("name");
					
					lbl.setText (str);
					lbl.setId ("DefaultLabel");
					
					if (secretaryMode) {
						if (rs.getInt ("status_done") != 1)
							accessLevels.add (rs.getInt ("appointer_id"));
						else {
							lbl.setId ("AgendaDoneLabel");
							accessLevels.add (0);
						}
					}
					agendaScrollerLabel.add (lbl);
				}
				if (secretaryMode) {
					for (int x = 0; x < agendaScrollerLabel.size (); x++) {
						switch (dbc.getAccessLevel (accessLevels.get(x))) {
							case 3: 
								agendaScrollerLabel.get(x).setId ("DoctorLabel");
								break;
								
							case 2:
								agendaScrollerLabel.get(x).setId ("SecretaryLabel");
								break;
								
							case 1:
								agendaScrollerLabel.get(x).setId ("ClientLabel");
								break;
						}
					}
				}
				for (Label l: agendaScrollerLabel)
					agendaVBox.getChildren ().add (l);
			} catch (Exception e) { e.printStackTrace (); }
		}
	}

}
