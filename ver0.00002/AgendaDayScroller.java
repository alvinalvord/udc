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
		
	public AgendaDayScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		
		this.dbc = dbc;
		this.cm = cm;
		
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
	
	public void update () {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}