import java.sql.*;

import javafx.stage.*;

public class SecretaryController implements DatabaseControl {
	private MainController mc;
	private ClinicView cv;
	
	public SecretaryController (MainController mc) {
		this.mc = mc;
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}

	public void deleteRow(int id) {
		
	}
	
	public void createUpdate(int id, String... param) {
		
	}

	public int createInsert(String... param) {
		return 0;
	}

	public int getAccessLevel(int id) {
		return 0;
	}

	public ResultSet getUpdates(int y, int m, int d) {
		return null;
	}
}
