import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LogInView extends BorderPane {
	private LogInController lic;
	private VBox loginBox;
		private HBox usernameBox;
			private Label username;
			private TextField usernameField;
		private HBox passwordBox;
			private Label password;
			private TextField passwordField;
		private Button login;
	
	public LogInView (LogInController lic) {
		super ();
		
		this.lic = lic;
		
		setMaxSize (Double.MAX_VALUE, Double.MAX_VALUE);
		
		initLogInView ();
		initHandlers ();
		
		setCenter (loginBox);
	}
	
	private void initLogInView () {		
		loginBox = new VBox (20);
		loginBox.setAlignment (Pos.CENTER);
			usernameBox = new HBox (10);
			usernameBox.setAlignment (Pos.CENTER);
				username = new Label ();
				username.setText ("Username:");
				username.setId ("DefaultLabel");
				usernameField = new TextField ();
				usernameField.setText("");
				usernameField.setId("TextField");
			usernameBox.getChildren ().addAll (username, usernameField);
			passwordBox = new HBox (10);
			passwordBox.setAlignment (Pos.CENTER);
				password = new Label ();
				password.setText ("Password:");
				password.setId ("DefaultLabel");
				passwordField = new TextField ();
				passwordField.setText ("");
				passwordField.setId("TextField");
			passwordBox.getChildren ().addAll (password, passwordField);
			login = new Button ();
			login.setText("Login");
			login.getStyleClass ().add ("ButtonSave");
		loginBox.getChildren ().addAll (usernameBox, passwordBox, login);
	}
	
	private void initHandlers () {
		login.setOnAction(e -> {
			lic.changeControl(1);
		});
	}
}
