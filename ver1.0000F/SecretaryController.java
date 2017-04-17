import java.sql.*;
import java.util.*;
import javafx.scene.control.*;

public class SecretaryController extends DatabaseControl {
	
	public SecretaryController (MainController mc) {
		this.mc = mc;
		cv = new SecretaryView (this);
	}
	
	public int createUpdate(int id, String... param) {
		if (param.length == 2)
			return DatabaseModel.getInstance ().updateAppointment
				("" + id,
				"`name` = '" + param[0] + "'",
				"`appointer_id` = '" + param[1] + "'");
		
		return -1;
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
	
	public void appointmentHandler (int id, String name, String date) {
		new PopupEventWindow (getMainStage (),
			id, name, date,
			this,
			PopupEventWindow.OptionType.BOOK_APPOINTMENT,
			PopupEventWindow.OptionType.NOTIFY_APPOINTMENT);
	}
}
