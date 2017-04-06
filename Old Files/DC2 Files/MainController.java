import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

public abstract class MainController {
	
	protected Stage mainStage;
	protected Scene scene;
	protected int currentView;
	
	public MainController (Stage stage) {
		this.mainStage = stage;
		mainStage.setTitle ("Design Challenge 2");
		mainStage.setMinWidth (1280);
		mainStage.setMinHeight (720);
		
		scene = new Scene (new Group (), mainStage.getWidth (), mainStage.getHeight ());
		scene.getStylesheets ().add ("./StyleSheet.css");
		initControllers ();
		
		setScene (0);
		
		stage.setScene (scene);
		stage.show ();
	}
	
	protected abstract void initControllers ();
	
	protected abstract void changeView ();
	
	public abstract void setScene (int n);
	
	public Stage getStage () {
		return mainStage;
	}
	
}