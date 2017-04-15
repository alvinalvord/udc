import java.sql.*;

public class SecretaryModel extends DatabaseModel {
	
	private Statement stmt;
	private ResultSet rs;
	
	public SecretaryModel () {
		super ();
	}
	public int insertAppointment(String name, String date, String start_time, String end_time, String appointer_id, String appointee_id) {
		return 0;
	}

}
