import javafx.application.*;
import javafx.stage.*;

public class Driver extends Application {
	
	public void start (Stage mainStage) {
		new ClinicMainController (mainStage);
	}
	
	public static void main (String[] args) {
		launch (args);
	}
	
}

