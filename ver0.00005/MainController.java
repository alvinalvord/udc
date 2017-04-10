import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

public abstract class MainController {
	
	protected Stage mainStage;
	protected Scene scene;
	protected int currentView;
	protected int userID;
	
	public MainController (Stage stage) {
		this.mainStage = stage;
		mainStage.setMinWidth (1280);
		mainStage.setMinHeight (720);
		
		scene = new Scene (new Group (), mainStage.getWidth (), mainStage.getHeight ());
		initControllers ();
		
		setScene (0);
		
		stage.setScene (scene);
		stage.show ();
	}
	
	protected abstract void initControllers ();
	
	protected abstract void changeView ();
	
	public abstract void setScene (int n);
	
	public int getUserID () {
		return userID;
	}
	
	public void setUserID (int uid) {
		this.userID = uid;
	}
	
	public Stage getStage () {
		return mainStage;
	}
	
}