import javafx.stage.*;

public class ClinicMainController extends MainController {
	
	private ClinicViewController cvc;
	private LogInController lic;
	
	public ClinicMainController (Stage stage) {
		super (stage);
		mainStage.setTitle ("Ultimate Design Challenge");
		scene.getStylesheets ().add ("./StyleSheet.css");
	}
	
	protected void initControllers () {
		lic = new LogInController (this);
		cvc = new ClinicViewController (this);
	}
	
	public void setScene (int n) {
		switch (n) {
			case 0:
			case 1:
				currentView = n;
				break;
				
			default:
				currentView = 0;
		}
		
		changeView ();
	}
	
	protected void changeView () {
		switch (currentView) {
			case 0:
				scene.setRoot (lic.getView ());
				break;
				
			case 1:
				scene.setRoot (cvc.getView ());
				break;
				
			default:
				scene.setRoot (lic.getView ());
		}
	}
	
}