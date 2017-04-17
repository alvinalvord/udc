import java.util.*;
import javafx.scene.control.*;
import java.sql.*;

public abstract class DatabaseControl {
	public abstract void deleteRow (int id);
	public abstract int createUpdate (int id, String... param);
	public abstract int createInsert (String... param);
	public abstract ClinicView getView ();
	
	public ResultSet getDisplayData (int y, int m, int d) {
		String date = "`date` = '" + y + "-" + m + "-" + d + "'";
		int vID =DatabaseModel.getInstance ().getViewingID ();
		String user = "(`appointer_id` = '" + vID + "' or `appointee_id` = '" + vID + "')";
		
		return DatabaseModel.getInstance ().getData ("appointments", date, user);
	}
	
	public abstract void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers);
	
	public abstract void setCalendarLabels (Label[] labels, int[] appointers);
	
	public abstract void appointmentHandler (int id, String name, String date);
	
	public void invalidAlert (String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait().filter(response -> response == ButtonType.OK ).ifPresent(response -> {return;});
	}

}