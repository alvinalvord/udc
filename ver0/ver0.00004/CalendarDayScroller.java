import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.stage.*;
import java.util.*;
import java.sql.*;

public class CalendarDayScroller extends ScrollPane implements View {
	
	private DatabaseControl dbc;
	private CalendarModel cm;
	
	private VBox dayVBox;
		private HBox[] hboxArr;
			private Label[] hourLabels;
			private VBox[] vboxArr;
				private Label[] timeLabels;
	
	private int[] ids;
	
	public CalendarDayScroller (DatabaseControl dbc, CalendarModel cm) {
		super ();
		
		this.dbc = dbc;
		this. cm = cm;
		
		cm.attach (this);
		
		initCDS ();
	}
	
	private void initCDS () {
		getStyleClass ().add ("CalendarDayScroller");
		setPrefHeight (551);
		setHbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy (ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		dayVBox = new VBox ();
		dayVBox.setId ("dayScroller");
		
		hboxArr = new HBox[24];
		for (int i = 0; i < hboxArr.length; i++)
			hboxArr[i] = new HBox ();
		
		hourLabels = new Label[24];
		for (int i = 0; i < hourLabels.length; i++) {
			if (i < 10)
				hourLabels[i] = new Label ("0" + i + ":00");
			else
				hourLabels[i] = new Label ("" + i + ":00");
			
			hourLabels[i].setMinWidth (125);
			hourLabels[i].setMaxWidth (Double.MAX_VALUE);
			hourLabels[i].setMaxHeight (Double.MAX_VALUE);
			hourLabels[i].setId ("hourLabels");
		}
		
		vboxArr = new VBox[24];
		for (int i = 0; i < vboxArr.length; i++) {
			vboxArr[i] = new VBox ();
			HBox.setHgrow (vboxArr[i], Priority.ALWAYS);
		}
		
		timeLabels = new Label[48];
		for (int i = 0; i < timeLabels.length; i++) {
			timeLabels[i] = new Label ();
			timeLabels[i].setPrefHeight (80);
			timeLabels[i].setMinWidth (750);
			timeLabels[i].setMaxWidth (Double.MAX_VALUE);
			timeLabels[i].setMaxHeight (Double.MAX_VALUE);
			timeLabels[i].setAlignment (Pos.TOP_LEFT);
		}
		
		int ctr = 0;
		for (int i = 0; i < hboxArr.length; i++) {
			hboxArr[i].getChildren ().addAll (hourLabels[i], vboxArr[i]);
			
			for (int j = 0; j < 2; j++) {
				vboxArr[i].getChildren ().add (timeLabels[ctr]);
				ctr ++;
			}
			
			dayVBox.getChildren ().add (hboxArr[i]);
		}
		
		setContent (dayVBox);
		
		ids = new int[48];
	}
	
	public void update () {
		
	}
	
	
	
	
	
	
	
}