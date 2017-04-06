import javafx.stage.*;

public class GregorianMainController extends MainController {
	
	private GregorianCalendarController gcc;
	
	public GregorianMainController (Stage stage) {
		super (stage);
	}
	
	protected void initControllers () {
		gcc = new GregorianCalendarController (this);
	}
	
	public void setScene (int n){
		switch (n) {
			case 0: 
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
				scene.setRoot (gcc.getView ());
				break;
			
			default:
				scene.setRoot (gcc.getView ());
		}
	}
	
}