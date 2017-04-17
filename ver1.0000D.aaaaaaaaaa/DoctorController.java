import java.sql.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class DoctorController extends DatabaseControl {

	private MainController mc;
	private ClinicView cv;

	public DoctorController (MainController mc) {
		this.mc = mc;
		cv = new DoctorView (this);
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}
	
	public void deleteRow(int id) {
		DatabaseModel.getInstance ().deleteRow (id);
	}

	public int createUpdate(int id, String... param) {
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

	public int createInsert(String... param) {
		
		boolean b = DatabaseModel.getInstance ().timeConflict (param[4], param[1], param[2], param[3]);
		
		if (b)
			return -1;
		
		if (param.length == 5) {
			String[] arr = {"name", "date", "start_time", "end_time", "appointee_id"};
			return DatabaseModel.getInstance ().insertAppointment (arr, param[0], param[1], param[2], param[3], param[4]);
		}
			
		else if (param.length == 6) {
			String[] arr = {"name", "date", "start_time", "end_time", "appointer_id", "appointee_id"};
			return DatabaseModel.getInstance ().insertAppointment (arr, param[0], param[1], param[2], param[3], Integer.toString(DatabaseModel.getInstance().getUserID()), param[4]);
		}
		
		return 0;
	}

	public void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers) {
		for (Label lbl: labels)
			lbl.setId ("DefaultLabel");
	}
	
	public void setCalendarLabels (Label[] labels, int[] appointers) {
		for (Label lbl: labels)
			lbl.setId ("TimeLabelsNone");
	}
	
	public ClinicView getView() {
		return cv;
	}
	
	public void appointmentHandler (int id, String name, String date) {		
		new PopupEventWindow (getMainStage (),
			id, name, date,
			this,
			PopupEventWindow.OptionType.MOVE_APPOINTMENT,
			PopupEventWindow.OptionType.DELETE_APPOINTMENT);
	}
	
}
