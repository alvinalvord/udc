import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.*;
import java.sql.*;

public class AgendaScroller extends ScrollPane implements View {
	
	private GregorianCalendarController gcc;
	private GregorianCalendarModel gcm;
	private AgendasModel am;

	private VBox agendaVBox;
		private ArrayList<Label> agendaScrollerLabel;
		
	public AgendaScroller (GregorianCalendarController gcc, GregorianCalendarModel gcm, AgendasModel am) {
		super ();
		
		this.gcc = gcc;
		this.gcm = gcm;
		this.am = am;
		
		gcm.attach (this);
		am.attach (this);
		
		initAS ();
	}
	
	private void initAS () {
		setId ("agendaScroller");
		setPrefHeight (551);
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		java.util.Set<Node> nodes = lookupAll(".scroll-bar");
		for (final Node node:nodes)
			if (node instanceof ScrollBar) {
				ScrollBar sb = (ScrollBar) node;
				sb.getStyleClass ().add ("scroll-bar");
			}
		
			agendaVBox = new VBox ();
			agendaVBox.setId ("agendaScroller");
		
		setContent (agendaVBox);
	}
	
	public VBox getAgendaVBox () {
		return agendaVBox;
	}
	
	public void update () {
		ResultSet rs = null;
		
		if(!agendaVBox.getChildren ().isEmpty ())
			agendaVBox.getChildren ().removeAll (agendaVBox.getChildren ());
		try {
			rs = gcc.getUpdates (gcm.getCurrentYear (), gcm.getCurrentMonth ().getMonth () + 1, gcm.getSelectedDate ());
		}catch (Exception e) {}
		if (rs == null) {
			return;
		}
		try {
			agendaScrollerLabel = new ArrayList <Label> ();
			while (rs.next ()) {
				Label lbl = new Label ();
				String str = "";
				String s = rs.getString ("start");
				String e = rs.getString ("end");
				String ss = s.substring (s.indexOf (" "));
				ss = ss.trim ();
				String ee = e.substring (e.indexOf (" "));
				ee = ee.trim ();
				
				ss = ss.substring (0, 5);
				ee = ee.substring (0, 5);
				
				int shr = Integer.parseInt (ss.substring (0,2));
				int ehr = Integer.parseInt (ee.substring (0,2));
				
				int smin = Integer.parseInt (ss.substring (3,5));
				int emin = Integer.parseInt (ee.substring (3,5));
				
				if ((ehr - shr == 1 && emin - smin == -30) || (ehr - shr == 0 && emin - smin == 30))
					str = ss + "   " + rs.getString ("name");
				else 
					str = ss + " - " + ee + "     " + rs.getString ("name");
				
				
				lbl.setText (str);
				lbl.setId ("DefaultLabel");
				if (rs.getString ("type").equals ("T"))
					lbl.setStyle ("-fx-text-fill: #37761D;");
				else if (rs.getString ("type").equals ("E"))
					lbl.setStyle ("-fx-text-fill: #1055CC;");
				if (rs.getString ("status").equals ("I"))
					lbl.setStyle ("-fx-text-fill: red;");
				
				agendaScrollerLabel.add (lbl);
			}
			for (Label l: agendaScrollerLabel)
					agendaVBox.getChildren ().add (l);
		} catch (Exception e) {}
	}
	
}