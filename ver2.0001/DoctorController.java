import java.sql.*;
import java.util.*;
import javafx.scene.control.*;

public class DoctorController extends DatabaseControl {

	public DoctorController (MainController mc) {
		this.mc = mc;
		cv = new DoctorView (this);
	}
	
	public int createUpdate(int id, String... param) {
		if (param.length == 1) {
			return DatabaseModel.getInstance ().updateAppointment 
				("" + id, 
				"`name` = '" + param[0] + "'",
				"`appointer_id` = '" + 
					DatabaseModel.getInstance ().getUserID () + "'");
		}
		else if (param.length == 3) {
			ResultSet rs = DatabaseModel.getInstance ().getData 
				("appointments",  "`id` = '" + id + "'");
			try {
				if (rs.next ()) {
					String appointee_id = rs.getString ("appointee_id");
					
					rs = DatabaseModel.getInstance ().getData 
						("appointments",
						"`date` = '" + param[0] + "'",
						"`start_time` < '" + param[2] + "'",
						"`end_time` > '" + param[1] + "'",
						"`appointee_id` = '" + appointee_id + "'",
						"`id` != '" + id + "'");
					
					if (rs.next ()) {
						invalidAlert ("time conflict");
						return -1;
					}
				}
			} catch (Exception e) { e.printStackTrace (); }
			
			return DatabaseModel.getInstance ().updateAppointment 
				("" + id, 
				"`date` = '" + param[0] + "'", 
				"`start_time` = '" + param[1] + ":00'", 
				"`end_time` = '" + param[2] + ":00'");
		}
		return -1;
	}

	public void appointmentHandler (int id, String name, String date) {	
		ResultSet rs = DatabaseModel.getInstance ().getData 
			("appointments", 
			"`id` = '" + id + "'");
		
		try {
			if (rs.next ()) {
				if (rs.getInt ("appointer_id") > 0)
					new PopupEventWindow (getMainStage (),
						id, name, date,
						this,
						PopupEventWindow.OptionType.MOVE_APPOINTMENT,
						PopupEventWindow.OptionType.DELETE_APPOINTMENT);
			
				else
					new PopupEventWindow (getMainStage (),
						id, name, date,
						this,
						PopupEventWindow.OptionType.MOVE_APPOINTMENT,
						PopupEventWindow.OptionType.DELETE_APPOINTMENT,
						PopupEventWindow.OptionType.SELF_APPOINTMENT);
			}
		} catch (Exception e) { e.printStackTrace (); }
		
	}
	
	public void notifyDoctor () {
		ResultSet rs = DatabaseModel.getInstance ().getData 
			("appointments", 
			"`notify` = '1'", 
			"`appointee_id` = '" + DatabaseModel.getInstance ().getUserID () + "'");
		
		ArrayList <String> ids = new ArrayList <String> ();
		try {
			while (rs.next ()) {
				ids.add (rs.getString ("id"));
				Alert alert = new Alert (Alert.AlertType.INFORMATION, 
					"reminder: " + rs.getString ("name"), ButtonType.OK);
				
				alert.show ();
			}
			for (String id: ids) {
				DatabaseModel.getInstance ().updateAppointment 
					(id + "' and `appointee_id` = '" + 
						DatabaseModel.getInstance ().getUserID (), 
					"`notify` = '0'");
			}
		} catch (Exception e) { e.printStackTrace (); }
	}
	
}
