import java.sql.*;

public class DatabaseModel extends Model {
	
	private static final DatabaseModel dbm = new DatabaseModel ();
	
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	
	private DatabaseModel () {
		this ("localhost", "3306", "clinic", "root", "root");
	}
	
	private DatabaseModel (String host, String port, String database, String user, String pass) {
		try {
			Class.forName ("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection 
				("jdbc:mysql://" + host + ":" + port + "/" + database +
					"?useSSL=false", user, pass);
			stmt = con.createStatement ();
		} catch (Exception e) { e.printStackTrace (); }
	}
	
	public static DatabaseModel getInstance () {
		return dbm;
	}
	
	public int insertAppointment (String name, String date, String start_time, String end_time, String appointer_id, String appointee_id) {
		try {
			int n = stmt.executeUpdate 
				(
					"insert into `appointements` (" + 
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
	
	public void markDone (int id) {
		try {
			stmt.executeUpdate ("update `appointments` set `status_done` = '1' where `id` = '" + id + "';");
		} catch (Exception e) { e.printStackTrace (); }
	}
	
	public void deleteRow (int id) {
		try {
			stmt.executeUpdate ("delete from `table` where id = '" + id + "';");
		} catch (Exception e) {System.out.println ("delete failed");}
	}
	
	public ResultSet getAppointments (String id, String date) {
		try {
			rs = stmt.executeQuery ("select * from `appointments` where `date` = '" + date + "' where`id` = '" + id + "';");
		} catch (Exception e) { e.printStackTrace (); }
		return rs;
	}
	
	public int getUser (String username, String password) {
		try {
			rs = stmt.executeQuery ("select id from `users` where `username` = '" + username + "' and `password` = '" + password + "';");
			if (rs.next())
				return rs.getInt("id");
		} catch (Exception e) { e.printStackTrace (); }
		return 0;
	}
	
	public void moveAppointment (String id, String date, String start_time, String end_time) {
		try {
			stmt.executeUpdate ("update `appointments` set `date` = '" + date +"' `start_time` = '" + start_time + "' `end_time` = '" + end_time + "' where `id` = '" + id + "';");
		} catch (Exception e) { e.printStackTrace (); }
	}
	
	public void checkUpdates () {
		try {
			rs = stmt.executeQuery ("select timestampdiff(second, (select update_time from information_schema.tables where table_schema = 'clinic' and table_name = 'appointments'), now());");
			if (rs.next ()) {
				if (rs.getInt (1) <= 10 && rs.getInt (1) > 0)
					notifyViews ();
			}
		} catch (Exception e) { e.printStackTrace (); }
	}
	
}