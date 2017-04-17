import java.sql.*;

public class ClientModel extends DatabaseModel{

	private Statement stmt;
	private ResultSet rs;
	
	public ClientModel () {
		super ();
	}
	
	public int insertAppointment(String name, String date, String start_time, String end_time, String appointer_id, String appointee_id) {
		return 0;
	}

}
