import java.sql.*;

public class DatabaseModel extends Model {
	
	private static final DatabaseModel dbm = new DatabaseModel ();
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	
	private int userID;
	private int viewingID;
	private int accessLevel;
	
	private DatabaseModel () {
		this ("localhost", "3306", "clinic", "root", "root");
	}
	
	private DatabaseModel (String host, String port, String database, String user, String pass) {
		userID = 0;
		viewingID = 0;
		accessLevel = 0;
		
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
	
	public int insertAppointment (String[] columns, String... values) {
		if (columns.length != values.length)
			return -1;
		
		if (columns.length == 0)
			return -1;
		
		String query = "insert into `appointments` (";
		
		for (int i = 0; i < columns.length - 1; i++)
			query = query + "`" + columns[i] + "`, ";
		
		query = query + "`" + columns[columns.length - 1] + "`) ";
		query = query + "values (";
		
		for (int i = 0; i < values.length - 1; i++)
			query = query + "'" + values[i] + "', ";
		
		query = query + "'" + values[values.length - 1] + "');";
		
		try {
			int n = stmt.executeUpdate (query);
			
			notifyViews ();
			
			return n;
		} catch (Exception e) { e.printStackTrace (); }
		
		return 0;
	}
	
	public boolean timeConflict (String id, String date, String start, String end) {
		try {
			rs = stmt.executeQuery 
			(
				"select * " + 
				"from `appointments` " +
				"where `date` = '" + date + "' " + 
					"and `start_time` < '" + end + "' " + 
					"and `end_time` > '" + start + "' " + 
					"and `appointee_id` = '" + id + "';"
			);
			
			return rs.next ();
		} catch (Exception e) { e.printStackTrace (); }
		
		return true;
	}
	
	public int updateAppointment (String id, String... set) {
		if (set.length == 0)
			return -1;
		
		String query = "update `appointments` set ";
		
		for (int i = 0; i < set.length; i++)
			query = query + set[i] + " and ";
		query += set[set.length - 1];
		
		query = query + " where `id` = '" + id + "';";
		
		try {
			int n = stmt.executeUpdate (query);
			
			notifyViews ();
			return n;
		} catch (Exception e) { e.printStackTrace (); }
		
		return 0;
	}
	
	public void deleteRow (int id) {
		try {
			stmt.executeUpdate ("delete from `appointments` where id = '" + id + "';");
		} catch (Exception e) {System.out.println ("delete failed");}
	}
	
	public ResultSet getData (String from, String... where) {
		String query = "select * from `" + from + "` where ";
		
		for (int i = 0; i < where.length - 1; i++)
			query = query + where[i] + " and ";
		query = query + where[where.length - 1] + ";";
		
		try {
			rs = stmt.executeQuery (query);
		} catch (Exception e) { e.printStackTrace (); }
		return rs;
	}
	
	public boolean login (String username, String password) {
		try {
			rs = stmt.executeQuery ("select * from `users` where `username` = '" + username + "' and `password` = '" + password + "';");
			
			if (rs.next()) {
				userID = rs.getInt("id");
				System.out.println (userID);
				
				accessLevel = rs.getInt ("user_level");
				System.out.println (accessLevel);
				
				return true;
			}
			
		} catch (Exception e) { e.printStackTrace (); }
		
		return false;
	}
	
	public void logout () {
		userID = 0;
		viewingID = 0;
		accessLevel = 0;
	}
	
	public synchronized void checkUpdates () {
		try {
			rs = stmt.executeQuery ("select timestampdiff(second, (select update_time from information_schema.tables where table_schema = 'clinic' and table_name = 'appointments'), now());");
			if (rs.next ()) {
				if (rs.getInt (1) <= 10 && rs.getInt (1) > 0)
					notifyViews ();
			}
		} catch (Exception e) { e.printStackTrace (); }
	}
	
	public int getUserID () {
		return userID;
	}
	
	public int getViewingID () {
		return viewingID;
	}
	
	public void setViewingID (int n) {
		System.out.println (n);
		viewingID = n;
		notifyViews ();
	}
	
	public int getAccessLevel () {
		return accessLevel;
	}
	
}