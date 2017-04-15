
public class DatabaseControlDirector {
	private DatabaseControl dc;
	
	public DatabaseControl getDatabaseControl () {
		return dc;
	}
	
	public void constructDatabaseControl (MainController mc) {
		switch (DatabaseModel.getInstance().getAccessLevel()) {
			case 1: 
				dc = new ClientController (mc);
				break;
			case 2: 
				dc = new SecretaryController (mc);
				break;
			case 3: 
				dc = new DoctorController (mc);
				break;
		}
	}
}
