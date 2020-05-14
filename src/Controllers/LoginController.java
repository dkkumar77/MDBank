package Controllers;

import Model.Databases.GeneralDatabase;
import Model.Date;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Model.Databases.GeneralDatabase;
import Controllers.Util.Encrypter;
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
		if(actionEvent.getSource().equals(loginButton)) {
			GeneralDatabase gd = new GeneralDatabase();
			Encrypter encrypter = new Encrypter();

			if (usernameField.getText() != "" && passwordField.getText() != "") {
				String username;
				String password = encrypter.getEncryptedPassword(passwordField.getText());
				if(gd.verifyCredentials(usernameField.getText(), password)){


					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/src/View/Login.fxml"));
					Parent loginParent = null;
					try {
						loginParent = loader.load();
					} catch (IOException a) {
						a.printStackTrace();
					}
					assert loginParent != null;
					Scene currScene = new Scene(loginParent);


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
