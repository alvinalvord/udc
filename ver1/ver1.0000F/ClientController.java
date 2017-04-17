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
		return DatabaseModel.getInstance ().updateAppointment 
			("" + id, 
			"`name` = '" + param[0] + "'",
			"`appointer_id` = '" + 
				DatabaseModel.getInstance().getUserID () + "'");
	}

	public void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers) {
		for (Label lbl: labels)
			lbl.setId ("DefaultLabel");
	}
	
	public void setCalendarLabels (Label[] labels, int[] appointers) {
		for (Label lbl: labels)
			lbl.setId ("TimeLabelsNone");
	}
	
	public void appointmentHandler (int id, String name, String date) {
		new PopupEventWindow (getMainStage (),
			id, name, date,
			this,
			PopupEventWindow.OptionType.SELF_APPOINTMENT,
			PopupEventWindow.OptionType.CANCEL_APPOINTMENT);
	}

}
