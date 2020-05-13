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
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
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
	}

	@FXML
	public void handleSubmit(ActionEvent actionEvent)
	{
		String subject = "Bug Report SEVERITY: " + getRadioButtonSelection();
		Email bugEmail = new Email("dk.kumar77@yahoo.com", subject,
				dateLabel.getText()+"\n"+nameField.getText()+"-"+titleField.getText() +"\n"+descriptionField.getText(),null);
		EmailSender emailSender = new EmailSender(bugEmail);
		emailSender.send();
		System.out.println("Sending Email");
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
			Stage currStage = (Stage) cancelButton.getScene().getWindow();
			currStage.close();
		}
	}

	@FXML
	public void handleRadioButtons(ActionEvent actionEvent)
	{

	}
}
