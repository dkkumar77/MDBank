package Controllers;

import Controllers.Util.EmailSender;
import Model.Databases.AdminDatabase;
import Model.Date;
import Model.Email;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportBugController implements Initializable
{
	@FXML
	private JFXTextField titleField;

	@FXML
	private JFXTextField nameField;

	@FXML
	private Label dateLabel;

	@FXML
	private JFXButton submitButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private JFXRadioButton highOpt;

	@FXML
	private JFXRadioButton mediumOpt;

	@FXML
	private JFXRadioButton lowOption;

	@FXML
	private JFXTextArea descriptionField;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		dateLabel.setText(Date.getDate());
		ToggleGroup toggleGroup = new ToggleGroup();
		highOpt.setToggleGroup(toggleGroup);
		mediumOpt.setToggleGroup(toggleGroup);
		lowOption.setToggleGroup(toggleGroup);
	}

	@FXML
	public void handleSubmit(ActionEvent actionEvent)
	{
		if(allFieldsCompleted()) {
			String subject = "Bug Report SEVERITY: " + getRadioButtonSelection();
			Email bugEmail = new Email(AdminDatabase.returnAdminInfo("admin1").get(0), subject,
					dateLabel.getText() + "\nCustomerName: " + nameField.getText()
							+ "\nTitle: " + titleField.getText()
							+ "\nDescription: " + descriptionField.getText(), null);
			EmailSender emailSender = new EmailSender(bugEmail);
			if (emailSender.send()) {
				showEmailSentAlert();
			}
		}else{
			Alert enterInfoAlert = new Alert(Alert.AlertType.NONE,"Empty Fields", ButtonType.OK);
			enterInfoAlert.showAndWait();
		}
	}

	private boolean allFieldsCompleted()
	{
		return !nameField.getText().isEmpty() && !titleField.getText().isEmpty()
				&& !descriptionField.getText().isEmpty();
	}

	private void showEmailSentAlert()
	{
		Alert emailAlert = new Alert(Alert.AlertType.NONE,"Email Sent", ButtonType.CLOSE);
		emailAlert.showAndWait();
		if(emailAlert.getResult() == ButtonType.CLOSE){
			closeWindow();
		}
	}

	private void closeWindow()
	{
		Stage currStage = (Stage) cancelButton.getScene().getWindow();
		currStage.close();
	}

	private String getRadioButtonSelection()
	{
		if(highOpt.isSelected())
			return "HIGH ";
		if(mediumOpt.isSelected())
			return "MEDIUM ";
		if(lowOption.isSelected())
			return  "LOW ";
		return "NONE";
	}
	@FXML
	public void handleCancel(ActionEvent actionEvent)
	{
		if(actionEvent.getSource().equals(cancelButton)) {
			closeWindow();
		}
	}

	@FXML
	public void handleRadioButtons(ActionEvent actionEvent)
	{

	}
}
