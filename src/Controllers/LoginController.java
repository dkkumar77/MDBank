package Controllers;

import Model.Databases.GeneralDatabase;
import Model.Date;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
	@FXML
	private JFXTextField usernameField;

	@FXML
	private JFXTextField passwordField;

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

	@FXML
	public void handleLogin(ActionEvent actionEvent)
	{

	}

	@FXML
	public void handleSignup(ActionEvent actionEvent)
	{

	}

	@FXML
	public void handleReportBug(ActionEvent actionEvent) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ReportBug.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.show();
	}

	@FXML
	public void handleContactUs(ActionEvent actionEvent)
	{

	}





}
