import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.*;
import java.sql.*;

public class AgendaDayScroller extends ScrollPane implements View {
	
	private DatabaseControl dbc;
	private CalendarModel cm;
	
	private VBox agendaVBox;
		private ArrayList <Label> agendaScrollerLabel;
		
	private boolean secretaryMode;
		
	public AgendaDayScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		
		this.dbc = dbc;
		this.cm = cm;
		
		secretaryMode = false;
		
		cm.attach (this);
		
		initADS ();
	}
	
	private void initADS () {
		getStyleClass ().add ("AgendaDayScroller");
		setPrefHeight (551);
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		agendaVBox = new VBox ();
		agendaVBox.setId ("agendaScroller");
		
		setContent (agendaVBox);
	}
	
	public void setSecretaryMode (boolean b) {
		secretaryMode = b;
	}
	
	public void update () {
		ResultSet rs = null;
		
		if (!agendaVBox.getChildren ().isEmpty ())
			agendaVBox.getChildren ().removeAll (agendaVBox.getChildren ());
		
		try {
			rs = dbc.getDisplayData (cm.getCurrentYear (),
				cm.getCurrentMonth ().getMonth () + 1, cm.getSelectedDate ());
		} catch (Exception e) { e.printStackTrace (); }
		
		if (rs == null) {
			return;
		}
		
		try {
			agendaScrollerLabel = new ArrayList <Label> ();
			
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
				for (int i = 0; i < agendaScrollerLabel.size (); i++) {
					switch (dbc.getAccessLevel (accessLevels.get(i))) {
						case 3: 
							agendaScrollerLabel.get(i).setId ("DoctorLabel");
							break;
							
						case 2:
							agendaScrollerLabel.get(i).setId ("SecretaryLabel");
							break;
							
						case 1:
							agendaScrollerLabel.get(i).setId ("ClientLabel");
							break;
					}
				}
			}
			
			for (Label l: agendaScrollerLabel)
				agendaVBox.getChildren ().add (l);
		} catch (Exception e) { e.printStackTrace (); }
		
	}
	
}