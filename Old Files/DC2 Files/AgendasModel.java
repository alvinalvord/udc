import java.sql.*;

public class AgendasModel extends Model {
	
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	
	private String database;
	private String table;
	private String username;
	private String password;
	
	public AgendasModel () {
		this ("agendas", "table", "root", "root");
	}
	
	public AgendasModel (String db, String tb, String user, String pass) {
		this.database = db;
		this.table = tb;
		this.username = user;
		this.password = pass;
		
		try {
			Class.forName ("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/" + 
				database + "?useSSL=false", username, password);
			stmt = con.createStatement ();
		} catch (Exception e) { e.printStackTrace (); }
	}

	public ResultSet getState () {
		return rs;
	}
	
	public int insertData (String eventName, String eventType, String eventStart, String eventEnd) {
		try {
			int n = stmt.executeUpdate ("insert into `table` (`name`,`start`,`end`,`type`,`status`) values ('" + eventName + "', " +
			"'" + eventStart + "', " +
			"'" + eventEnd + "', " +
			"'" + eventType + "', " +
			"'" + Status.active.toString () + "');");
			notifyViews ();
			return n;
		} catch (Exception e) {e.printStackTrace ();}
		return 0;
	}
	
	public void updateData () {
		try {
			rs = stmt.executeQuery ("select id from `table` where end < now() and type = '" + Type.event.toString () + "' and status = '"+ Status.active.toString () +"';");
			
			boolean flag = rs.next ();
			
			if (flag) {
				do {
					stmt.executeUpdate ("update `table` set status = '" + Status.inactive.toString () + "' where id = '" + rs.getString("id") +"';");
				} while (rs.next ());
				notifyViews ();
			}
		} catch (Exception e) {}
	}
	
	public void markDone (int id) {
		try {
			stmt.executeUpdate ("update `table` set status = 'I' where id = '" + id + "';");
		} catch (Exception e) {System.out.println ("done failed");}
	}
	
	public void deleteRow (int id) {
		try {
			stmt.executeUpdate ("delete from `table` where id = '" + id + "';");
		} catch (Exception e) {System.out.println ("delete failed");}
	}
	
	public ResultSet getData (String date, boolean task, boolean event) {
		String query = "";
		
		if (task) {
			query += "select * from `" + table + 
				"` where type = 'T' and date(start) = '" + date + "' ";
		}
		
		if (task && event)
			query += " union ";
		
		if (event) {
			query += "(select * from `" + table + 
				"` where type = 'E' and date(start) = '" + date + "') ";
		}
		
		if (query.length () <= 0)
			return null;
		
		query += " order by start ASC;";
		
		try {
			rs = stmt.executeQuery (query);
		} catch (Exception e) {}
		return rs;
	}
	
	private enum Status {
		active ("A"),
		inactive ("I");
		
		Status (String val) {
			this.value = val;
		}
		
		private final String value;
		
		public String toString () {
			return value;
		}
		
	}
	
	private enum Type {
		event ("E"),
		task ("T");
		
		Type (String val) {
			this.value = val;
		}
		
		private final String value;
		
		public String toString () {
			return value;
		}
	}
	
}