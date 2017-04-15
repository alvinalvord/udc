import javafx.stage.*;

public class LogInController {

	private LogInView liv;
	private MainController mc;
	
	public LogInController(MainController mc) {
		this.mc = mc;
		liv = new LogInView (this);
	}
	
	public LogInView getView () {
		return liv;
	}
	
	public Stage getMainStage () {
		return mc.getStage ();
	}
	
	public void changeControl (int n) {
		mc.setScene (n);
	}

	public boolean validateUser (String user, String pass) {
		int id = DatabaseModel.getInstance ().getUser (user, pass);
		if (id != 0) {
			mc.setUserID (id);
			return true;
		}
		return false;
	}
	
}
