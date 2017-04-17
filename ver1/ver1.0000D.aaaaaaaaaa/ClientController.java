import java.sql.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class ClientController extends DatabaseControl{

	private MainController mc;
	private ClinicView cv;
	
	public ClientController (MainController mc) {
		this.mc = mc;
		cv = new ClientView (this);
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}

	public void deleteRow(int id) {
		
	}

	public int createUpdate(int id, String... param) {
		return DatabaseModel.getInstance ().updateAppointment 
			("" + id, "`appointer_id` = NULL");
	}

	public int createInsert(String... param) {
		return 0;
	}

	public void setAgendaLabels (ArrayList<Label> labels, ArrayList <Integer> appointers) {
		for (Label lbl: labels)
			lbl.setId ("DefaultLabel");
	}
	
	public void setCalendarLabels (Label[] labels, int[] appointers) {
		for (Label lbl: labels)
			lbl.setId ("TimeLabelsNone");
	}
	
	public ClinicView getView() {
		return cv;
	}
	
	public void appointmentHandler (int id, String name, String date) {
		
	}

}
