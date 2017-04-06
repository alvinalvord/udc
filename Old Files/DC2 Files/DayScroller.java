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

public class DayScroller extends ScrollPane implements View {
	
	private GregorianCalendarController gcc;
	private GregorianCalendarModel gcm;
	private AgendasModel am;

	private VBox dayVBox;
		private HBox[] hboxArr;
			private Label[] hourLabels;
			private VBox[] vboxArr;
				private Label[] timeLabels;
				
	private int[] ids;
	private Stage popup;
		private HBox popupHbox;
			private Button done;
			private Button delete;
	
	public DayScroller (GregorianCalendarController gcc, GregorianCalendarModel gcm, AgendasModel am) {
		super ();
		
		this.gcc = gcc;
		this.gcm = gcm;
		this.am = am;
		
		gcm.attach (this);
		am.attach (this);
		
		initDS ();
	}
	
	private void initDS () {
		setId ("dayScroller");
		setPrefHeight (551);
		setHbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy (ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		java.util.Set<Node> nodes = lookupAll (".scroll-bar");
		for (final Node node: nodes)
			if (node instanceof ScrollBar) {
				ScrollBar sb = (ScrollBar) node;
				sb.getStyleClass ().add ("scroll-bar");
			}
			
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
				
				vboxArr[i].getChildren ().add (timeLabels[ctr]);
				ctr ++;
				vboxArr[i].getChildren ().add (timeLabels[ctr]);
				ctr ++;
			}
			
		setContent (dayVBox);
		
		ids = new int[48];
	}
	
	public VBox getDayVbox () {
		return dayVBox;
	}
	
	private void timeLabelHandler (MouseEvent e, int i, String name, String status) {
		System.out.println (ids[i]);
		
		if (popup != null)
			popup.close ();
		
		popup = new Stage ();
		popup.setTitle (name);
		
		popupHbox = new HBox (5);
		popupHbox.setAlignment (Pos.CENTER);
		
		done = new Button ("done");
		done.setMaxWidth (Double.MAX_VALUE);
		done.setId ("DefaultLabel");
		if (status.equals ("I"))
			done.setDisable (true);
		
		delete = new Button ("delete");
		delete.setMaxWidth (Double.MAX_VALUE);
		delete.setId ("DefaultLabel");
		
		done.setOnAction (mouseAction -> {
			gcc.setDone (ids[i]);
			update ();
		});
		
		delete.setOnAction (mouseAction -> {
			gcc.setDelete (ids[i]);
			update ();
		});
		
		popupHbox.getChildren ().addAll (done, delete);
		popup.setScene (new Scene (popupHbox, 200, 50));
		popup.getScene ().getStylesheets ().add ("./StyleSheet.css");
		popup.show ();
	}
	
	public void update () {
		ResultSet rs = null;
		
		for (int i = 0; i < ids.length; i++)
			ids[i] = 0;
		
		if (!dayVBox.getChildren ().isEmpty ())
			dayVBox.getChildren ().removeAll (dayVBox.getChildren ());
		
		try {
			rs = gcc.getUpdates (gcm.getCurrentYear (), gcm.getCurrentMonth ().getMonth () + 1, gcm.getSelectedDate ());
		}catch (Exception e) {e.printStackTrace ();}
		
		if (rs == null) {
			return;
		}
		
		try {
			for (int i = 0; i < timeLabels.length; i++) {
				timeLabels[i].setText ("");
				timeLabels[i].setId ("TimeLabels");
				timeLabels[i].setOnMouseClicked (null);
				timeLabels[i].setGraphic (null);
			}
			
			rs.first ();
			String start = rs.getString ("start");
			rs.last ();
			String end = rs.getString ("end");
			
			start = start.substring (start.indexOf (" "));
			end = end.substring (end.indexOf (" "));
			
			start = start.trim ();
			end = end.trim ();
			
			start = start.substring (0,2);
			end = end.substring (0,2);
			
			int s = Integer.parseInt (start.trim ());
			int e = Integer.parseInt (end.trim ());	
			
			for (int i = s; i <= e; i++)
				dayVBox.getChildren ().add (hboxArr[i]);
			
			rs.first ();
			
			do {
				start = rs.getString ("start");
				end = rs.getString ("end");
				
				start = start.substring (start.indexOf (" ")).trim ();
				end = end.substring (end.indexOf (" ")).trim ();
				
				String shr = start.substring (0,2);
				String smin = start.substring (3,5);
				
				String ehr = end.substring (0,2);
				String emin = end.substring (3,5);
				
				s = Integer.parseInt (shr) * 2;
				if (Integer.parseInt (smin) != 0)
					s++;
				
				e = Integer.parseInt (ehr) * 2;
				if (Integer.parseInt (emin) != 0)
					e++;
				
				for (int i = s; i < e; i++) {
					if (i == s)
						timeLabels[i].setText (rs.getString ("name"));
					if (rs.getString ("type").equals ("T"))
						timeLabels[i].setId ("TaskLabels");
					else
						timeLabels[i].setId ("EventLabels");
					if (rs.getString ("status").equals ("I")) {
						Image f = new Image (new java.io.File ("memekek.jpg").toURI ().toString (), 75, 75, false, false);
						ImageView iv = new ImageView (f);
						
						timeLabels[i].setGraphic (iv);
						timeLabels[i].setId ("DoneLabel");
					}
					
					ids[i] = rs.getInt ("id");
					final int x = i;
					final String name = rs.getString ("name");
					final String status = rs.getString ("status");
					
					timeLabels[i].setOnMouseClicked (mouseAction -> {
						timeLabelHandler (mouseAction, x, name, status);
					});
				}
			} while (rs.next ());
		} catch (Exception e) {}
	}
	
}