import java.sql.*;

public abstract class DatabaseModel extends Model {
	
	protected static final DatabaseModel dbm = new DatabaseModel ();
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	
	public DatabaseModel () {
		this ("localhost", "3306", "clinic", "root", "root");
	}
	
	public DatabaseModel (String host, String port, String database, String user, String pass) {
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
	
	public abstract int insertAppointment (String name, String date, String start_time, String end_time, String appointer_id, String appointee_id);
	
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
			rs = stmt.executeQuery ("select * from `appointments` where `date` = '" + date + "' and (`appointee_id` = '" + id + "' or `appointer_id` = '" + id + "');");
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
	
	public int getUser (String name) {
		try {
			rs = stmt.executeQuery ("select id from `users` where `name` = '" + name + "';");
			if (rs.next ())
				return rs.getInt ("id");
		} catch (Exception e) {e.printStackTrace ();}
		return 0;
	}
	
	public ResultSet getAllUsers () {
		try {
			rs = stmt.executeQuery ("select * from `users`;");
			
			return rs;
		} catch (Exception e) { e.printStackTrace (); }
		
		return null;
	}
	
	public int getAccessLevel (String id) {
		try {
			rs = stmt.executeQuery ("select user_level from `users` where id = '" + id + "';");
			if (rs.next ())
				return rs.getInt (1);
		} catch (Exception e) { e.printStackTrace (); }
		
		return 0;
	}
	
	public synchronized void checkUpdates () {
		try {
			rs = stmt.executeQuery ("select timestampdiff(second, (select update_time from information_schema.tables where table_schema = 'clinic' and table_name = 'appointments'), now());");
			if (rs.next ()) {
				if (rs.getInt (1) <= 10 && rs.getInt (1) > 0)
					notifyViews ();
			}
			
			int n = stmt.executeUpdate ("update `appointments` set	`status_done` = '1' where `id` in (select `id` from (select * from `appointments`) as x where `date` <= date(now()) and `end_time` < time(now()) and `status_done` = '0');");
			
			if (n > 0)
				notifyViews ();
		} catch (Exception e) { e.printStackTrace (); }
	}
	
}