import javafx.stage.*;

public class GregorianMainController extends MainController {
	
	private GregorianCalendarController gcc;
	private LogInController lic;
	
	public GregorianMainController (Stage stage) {
		super (stage);
	}
	
	protected void initControllers () {
		lic = new LogInController (this);
		gcc = new GregorianCalendarController (this);
	}
	
	public void setScene (int n){
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
				scene.setRoot (gcc.getView ());
			
			default:
				scene.setRoot (gcc.getView ());
		}
	}
	
}