import java.sql.*;
import java.util.*;
import javafx.scene.control.*;

public class ClientController extends DatabaseControl{

	public ClientController (MainController mc) {
		this.mc = mc;
		cv = new ClientView (this);
	}
	
	public int createUpdate(int id, String... param) {
		if (param[0].equals ("cancel"))
			return DatabaseModel.getInstance ().updateAppointment 
				("" + id, 
				"`name` = 'Free Slot'", 
				"`appointer_id` = NULL");
		else {
			return DatabaseModel.getInstance ().updateAppointment 
				("" + id, 
				"`name` = '" + param[0] + "'",
				"`appointer_id` = '" + 
					DatabaseModel.getInstance().getUserID () + "'");
		}
	}

	public void appointmentHandler (int id, String name, String date) {
		ResultSet rs = DatabaseModel.getInstance ().getData 
			("appointments", 
			"`id` = '" + id + "'");
			
		try {
			if (rs.next ()) {
				if (rs.getInt ("appointer_id") == 
					DatabaseModel.getInstance ().getUserID ())
					new PopupEventWindow (getMainStage (),
						id, name, date,
						this,
						PopupEventWindow.OptionType.CANCEL_APPOINTMENT);
				else if (rs.getInt ("appointer_id") <= 0)
					new PopupEventWindow (getMainStage (),
						id, name, date,
						this,
						PopupEventWindow.OptionType.SELF_APPOINTMENT);
			}
		} catch (Exception e) { e.printStackTrace (); }
			
	}

}
