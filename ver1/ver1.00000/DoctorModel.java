import java.sql.*;

public class DoctorModel extends DatabaseModel{

	private Statement stmt;
	private ResultSet rs;
	
	public DoctorModel () {
		super ();
	}
	
	public int insertAppointment (String name, String date, String start_time, String end_time, String appointer_id, String appointee_id) {
		try {
			int n = stmt.executeUpdate 
				(
					"insert into `appointments` (" + 
						"`name`, " + 
						" `date`, " + 
						"`start_time`, " + 
						"`end_time`, " + 
						"`appointer_id`, " + 
						"`appointee_id`, " + 
						"`status_done`" + 
					")" + 
					"values (" +
						"'" + name + "', " + 
						"'" + date + "', " + 
						"'" + start_time + "', " + 
						"'" + end_time + "', " + 
						"'" + appointer_id + "', " + 
						"'" + appointee_id + "', " + 
						"'0'" +
					");"
				);
			notifyViews ();
			
			return n;
		} catch (Exception e) { e.printStackTrace (); }
		
		return 0;
	}
	
	public boolean timeConflict (String id, String date, String start_time, String end_time) {
		try {
			rs = stmt.executeQuery 
			(
				"select * " + 
				"from `appointments` " +
				"where `date` = '" + date + "' " + 
					"and `start_time` < '" + end_time + "' " + 
					"and `end_time` > '" + start_time + "' " + 
					"and `appointee_id` = '" + id + "';"
			);
			
			return rs.next ();
		} catch (Exception e) { e.printStackTrace (); }
		
		return true;
	}
	
	public void moveAppointment (String id, String date, String start_time, String end_time) {
		try {
			stmt.executeUpdate ("update `appointments` set `date` = '" + date +"' `start_time` = '" + start_time + "' `end_time` = '" + end_time + "' where `id` = '" + id + "';");
		} catch (Exception e) { e.printStackTrace (); }
	}
}
