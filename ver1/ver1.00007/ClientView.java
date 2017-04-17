import java.sql.*;

public class ClientView extends ClinicView {
	
	public ClientView (DatabaseControl dc) {
		super (dc);
		
		leftVbox.getChildren().remove(create);
	}
	
	protected void setViewableSchedules () {
		ResultSet rs = dm.getData ("users", "`user_level` = '3'");
		
		try {
			while (rs.next ()) {
				viewSelect.getItems ().add (rs.getString ("name"));
			}
		} catch (Exception e) { e.printStackTrace (); }
		
		rs = dm.getData ("users", "`id` = '" + dm.getUserID () + "'");
		
		try {
			if (rs.next ()) {
				viewSelect.getItems ().add (rs.getString ("name"));
				dm.setViewingID (rs.getInt ("id"));
				viewSelect.getSelectionModel ().selectLast ();
			}
		} catch (Exception e) { e.printStackTrace (); }
	}
	
	protected void setViewLabel () {
		viewLabel.setText ("Client's View");
	}
	
}