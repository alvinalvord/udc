import java.util.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.sql.*;

public abstract class DatabaseControl {
	
	protected MainController mc;
	protected ClinicView cv;
	
	public void deleteRow(int id) {
		DatabaseModel.getInstance ().deleteRow (id);
	}

	public Stage getMainStage () {
		return mc.getStage ();
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

	public ClinicView getView() {
		return cv;
	}
	
	public ResultSet getDisplayData (int y, int m, int d) {
		String date = "`date` = '" + y + "-" + m + "-" + d + "'";
		int vID =DatabaseModel.getInstance ().getViewingID ();
		String user = "(`appointer_id` = '" + vID + "' or `appointee_id` = '" + vID + "')";
		
		return DatabaseModel.getInstance ().getData ("appointments", date, user);
	}
	
	public void invalidAlert (String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait().filter(response -> response == ButtonType.OK ).ifPresent(response -> {return;});
	}

	public abstract int createUpdate (int id, String... param);
	
	public abstract void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers);
	
	public abstract void setCalendarLabels (Label[] labels, int[] appointers);
	
	public abstract void appointmentHandler (int id, String name, String date);
}