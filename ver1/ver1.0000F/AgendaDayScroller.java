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
	private DatabaseModel dm;
	
	private VBox agendaVBox;
		private ArrayList <Label> agendaScrollerLabel;
		
	public AgendaDayScroller (DatabaseControl dbc, CalendarModel cm, DatabaseModel dm) {
		super ();
		
		this.dbc = dbc;
		this.cm = cm;
		this.dm = dm;
		
		cm.attach (this);
		dm.attach (this);
		
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
		update ();
	}
	
	public void update () {
		if (!agendaVBox.getChildren ().isEmpty ())
			agendaVBox.getChildren ().removeAll (agendaVBox.getChildren ());
		
		ResultSet rs = null;
		
		rs = dbc.getDisplayData (cm.getCurrentYear (),
			cm.getCurrentMonth ().getMonth () + 1, 
			cm.getSelectedDate ());
		
		if (rs == null)
			return;
		
		try {
			agendaScrollerLabel = new ArrayList <Label> ();
			
			ArrayList <Integer> appointers = new ArrayList <Integer> ();
			
			while (rs.next ()) {
				Label lbl = new Label ();
				String str = "";
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