import java.sql.*;

public class DoctorView extends ClinicView {
	
	public DoctorView (DatabaseControl dc) {
		super (dc);
	}
	
	protected void setViewableSchedules () {
		ResultSet rs = dm.getData ("users", "`id` = '" + dm.getUserID () + "'");
		
		try {
			if (rs.next ()) {
				viewSelect.getItems ().add (rs.getString ("name"));
				dm.setViewingID (rs.getInt ("id"));
				
				viewSelect.getSelectionModel ().selectFirst ();
			}
		} catch (Exception e) { e.printStackTrace (); }
	}
	
	protected void setViewLabel () {
		viewLabel.setText ("Doctor's View");
	}
	
}