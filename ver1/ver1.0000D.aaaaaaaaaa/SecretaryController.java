import java.sql.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class SecretaryController extends DatabaseControl {
	
	private MainController mc;
	private ClinicView cv;
	
	public SecretaryController (MainController mc) {
		this.mc = mc;
		cv = new SecretaryView (this);
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}

	public void deleteRow(int id) {
		
	}
	
	public int createUpdate(int id, String... param) {
		
		return 0;
	}

	public int createInsert(String... param) {
		return 0;
	}

	public void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers) {
		for (int i = 0; i < labels.size (); i++) {
			labels.get (i).setId ("DefaultLabel");
			try {
				ResultSet rs = DatabaseModel.getInstance ().getData ("users", "`id` = '" + appointers.get (i) + "'");
				
				if (rs.next ()) {
					switch (rs.getInt ("user_level")) {
						case 1:
							labels.get (i).setId ("ClientLabel");
							break;
						case 2:
							labels.get (i).setId ("SecretaryLabel");
							break;
						case 3:
							labels.get (i).setId ("DoctorLabel");
							break;
					}
				}
			} catch (Exception e) { e.printStackTrace (); }
		}
	}
	
	public void setCalendarLabels (Label[] labels, int[] appointers) {
		for (int i = 0; i < labels.length; i++) {
			labels[i].setId ("TimeLabelsNone");
			try {
				ResultSet rs = DatabaseModel.getInstance ().getData ("users", "`id` = '" + appointers[i] + "'");
				
				if (rs.next ()) {
					switch (rs.getInt ("user_level")) {
						case 1:
							labels[i].getStyleClass ().add ("TimeLabelsClient");
							break;
						case 2:
							labels[i].getStyleClass ().add ("TimeLabelsSecretary");
							break;
						case 3:
							labels[i].getStyleClass ().add ("TimeLabelsDoctor");
							break;
					}
				}
			} catch (Exception e) { e.printStackTrace (); }
		}
	}
	
	public ClinicView getView() {
		return cv;
	}
	
	public void appointmentHandler (int id, String name, String date) {
	
	}
}
