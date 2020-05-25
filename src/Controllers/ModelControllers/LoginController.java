package Controllers.ModelControllers;

import Controllers.Util.DialogAlert;
import Model.Databases.GeneralDatabase;
import Model.Definitions.PatchUpdate;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Controllers.Util.Encrypter;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static Model.Constants.FilePaths.*;

public class LoginController implements Initializable
{
	@FXML
	private StackPane stackPane;

	@FXML
	private JFXTextField usernameField;

	@FXML
	private JFXPasswordField passwordField;

	@FXML
	private JFXButton loginButton;

	@FXML
	private JFXButton contactButton;

	@FXML
	private JFXTextArea patchupdate;

	@FXML
	private Label dateLabel;

	@FXML
	private JFXButton reportButton;

	@FXML
	private JFXButton signupButton;

	@FXML
	private JFXTextField ast1,ast2;


	private GeneralDatabase generalDatabase;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		//check for internet
		generalDatabase = new GeneralDatabase();
		initClock();
		try {
			initPatchUpdate();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private void initClock() {

		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy \n HH:mm:ss");
			dateLabel.setText(LocalDateTime.now().format(formatter));
		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}

	private void initPatchUpdate() throws IOException {


		patchupdate.setText(PatchUpdate.readFile());



	}


	// GeneralDatabase was already initialized above ^^ so i deleted the one you wrote below
	// Changed the getEncryptedPassword to static cause we dont need to create an object just for
	// one method.
	@FXML
	public void handleLogin(ActionEvent actionEvent) throws IOException
	{
		ast1.setText("");
		ast2.setText("");

		if(actionEvent.getSource().equals(loginButton)) {
			loginProcess(actionEvent,null);
		}
	}


	private void loginProcess(ActionEvent actionEvent, KeyEvent keyEvent)
	{
		if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
			String password = Encrypter.getEncryptedPassword(passwordField.getText());
			if(!generalDatabase.avoidDuplicate(usernameField.getText())) {
				if (generalDatabase.verifyCredentials(usernameField.getText(), password)) {
					loadHome(actionEvent,keyEvent);
				}
				else {
					setPasswordIncorrect();
				}
			}
			else{
				setUsernameIncorrect();
			}
		}
		else{
			isFieldEmpty();
		}
	}

	@FXML
	public void handleEnterKey(KeyEvent keyEvent)
	{
		if(keyEvent.getCode().equals(KeyCode.ENTER)){
			loginProcess(null,keyEvent);
		}
	}

	private void loadHome(ActionEvent actionEvent, KeyEvent keyEvent)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(HOME_FXML));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert loginParent != null;
		Scene currScene = new Scene(loginParent);
		HomeController controller = loader.getController();
		controller.init(generalDatabase, usernameField.getText());
		Stage homeWindow = null;
		if(actionEvent != null) {
			homeWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		}else if(keyEvent != null){
			homeWindow = (Stage) ((Node) keyEvent.getSource()).getScene().getWindow();
		}
		assert homeWindow != null;
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	private void showDialog(String message)
	{
		DialogAlert.showOKDialog(stackPane,message).setOnDialogClosed((JFXDialogEvent event) ->{
			usernameField.setText("");
			passwordField.setText("");
			usernameField.requestFocus();
		});
	}


	public void isFieldEmpty(){
		if(usernameField.getText().isEmpty()){
			ast2.setText("*");
		}
		else if(passwordField.getText().isEmpty()){

			ast1.setText("*");

		}
		else{
			ast1.setText("*");
			ast2.setText("*");

		}
		showDialog("Please Enter a username or password");
	}

	public void setUsernameIncorrect() {
		ast1.setText("*");
		ast1.setText("*");
		showDialog("Invalid Credentials");

	}

	public void setPasswordIncorrect(){
		ast2.setText("*");
		showDialog("Username or Password is Incorrect");
	}

	@FXML
	public void handleSignup(ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(signupButton)) {
			loadSceneWithoutController(SIGN_UP_SHEET_FXML,actionEvent);
		}
	}

	private void loadSceneWithoutController(String filePath, ActionEvent actionEvent)
	{
		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(getClass().getResource(filePath));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert loginParent != null;
		Scene currScene = new Scene(loginParent);
		Stage homeWindow;
		homeWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();

	}



	@FXML
	public void handleContactUs(ActionEvent actionevent) {

		if (actionevent.getSource().equals(contactButton)) {
			loadSceneWithoutController(CONTACT_US_PAGE,actionevent);
		}
	}

	@FXML
	public void handleReportBug(ActionEvent actionEvent) throws IOException
	{
		if(actionEvent.getSource().equals(reportButton)) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(REPORT_FXML));
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
	}



}
