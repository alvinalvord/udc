import java.sql.*;
import java.util.*;
import javafx.scene.control.*;

public class SecretaryController extends DatabaseControl {
	
	public SecretaryController (MainController mc) {
		this.mc = mc;
		cv = new SecretaryView (this);
	}
	
	public int createUpdate(int id, String... param) {
		if (param.length == 2)
			return DatabaseModel.getInstance ().updateAppointment
				("" + id,
				"`name` = '" + param[0] + "'",
				"`appointer_id` = '" + param[1] + "'");
		else if (param[0].equals ("notify"))
			return DatabaseModel.getInstance ().updateAppointment
				("" + id,
				"`notify` = '1'");
		return -1;
	}

	public void appointmentHandler (int id, String name, String date) {
		ResultSet rs = DatabaseModel.getInstance ().getData 
			("appointments", 
			"`id` = '" + id + "'");
		
		try {
			if (rs.next ()) {
				if (rs.getInt ("appointer_id") > 0)
					new PopupEventWindow (getMainStage (),
						id, name, date,
						this,
						PopupEventWindow.OptionType.NOTIFY_APPOINTMENT);
				else 
					new PopupEventWindow (getMainStage (),
						id, name, date,
						this,
						PopupEventWindow.OptionType.BOOK_APPOINTMENT);
			}
		} catch (Exception e) { e.printStackTrace (); }
		
		
	}
}
