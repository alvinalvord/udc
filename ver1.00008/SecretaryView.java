import java.sql.*;

public class SecretaryView extends ClinicView {
	
	public SecretaryView (DatabaseControl dc) {
		super (dc);
		
		leftVbox.getChildren ().remove (create);
	}
	
	protected void setViewableSchedules () {
		ResultSet rs = dm.getData ("users", "`user_level` = '3'");
		
		try {
			while (rs.next ()) {
				viewSelect.getItems ().add (rs.getString ("name"));
			}
			rs.first ();
			dm.setViewingID (rs.getInt ("id"));
			
			viewSelect.getSelectionModel ().selectFirst ();
		} catch (Exception e) { e.printStackTrace (); }
		
	}
	
	protected void setViewLabel () {
		viewLabel.setText ("Secretary's View");
	}
	
}