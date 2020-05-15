package Controllers;

import Model.Databases.GeneralDatabase;
import Model.Date;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Controllers.Util.Encrypter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class LoginController implements Initializable
{
	@FXML
	private JFXTextField usernameField;

	@FXML
	private JFXPasswordField passwordField;

	@FXML
	private JFXButton loginButton;

	@FXML
	private JFXButton contactButton;

	@FXML
	private Label dateLabel;

	@FXML
	private JFXButton reportButton;

	@FXML
	private JFXButton signupButton;

	private GeneralDatabase generalDatabase;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		//check for internet
		generalDatabase = new GeneralDatabase();
		dateLabel.setText(Date.getDate());
	}


	// GeneralDatabase was already initialized above ^^ so i deleted the one you wrote below
	// Changed the getEncryptedPassword to static cause we dont need to create an object just for
	// one method.
	@FXML
	public void handleLogin(ActionEvent actionEvent) throws IOException
	{
		if(actionEvent.getSource().equals(loginButton)) {
			if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
				String password = Encrypter.getEncryptedPassword(passwordField.getText());
				if(generalDatabase.verifyCredentials(usernameField.getText(), password)){
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/View/HomeScene.fxml"));
					Parent loginParent = null;
					try {
						loginParent = loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
					assert loginParent != null;
					Scene currScene = new Scene(loginParent);
					HomeController controller = loader.getController();
					controller.init(generalDatabase,usernameField.getText());


					Stage homeWindow;
					homeWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
					homeWindow.setScene(currScene);
					homeWindow.show();
				}
				else{
					setDefaultSupressors();

				}
			}
		}


	}


	public void setDefaultSupressors(){
		passwordField.setText("false");

	}
	@FXML
	public void handleSignup(ActionEvent actionEvent)
	{

	}

	@FXML
	public void handleReportBug(ActionEvent actionEvent) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ReportBug.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setMaxHeight(400);
		stage.setMinHeight(400);
		stage.setMaxWidth(600);
		stage.setMinWidth(600);

		AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
		AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);
		root.setOnMousePressed(event -> {
			xOffset.set(event.getSceneX());
			yOffset.set(event.getSceneY());
		});
		//move around here
		root.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset.get());
			stage.setY(event.getScreenY() - yOffset.get());
		});
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}

	@FXML
	public void handleContactUs(ActionEvent actionEvent)
	{

	}


}
