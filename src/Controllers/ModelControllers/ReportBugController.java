package Controllers.ModelControllers;

import Controllers.Util.EmailSender;
import Model.Databases.AdminDatabase;
import Model.Date;
import Model.Email;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportBugController implements Initializable
{
	@FXML
	private StackPane stackPane;
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
	private JFXToggleButton annonToggle;

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
		nameField.setDisable(false);
	}

	@FXML
	public void handleAnonToggleSelection(ActionEvent actionEvent)
	{
		if(annonToggle.isSelected()) {
			nameField.setDisable(true);
			nameField.setText("Anonymous");
			nameField.setVisible(false);
		}else{
			nameField.setVisible(true);
			nameField.setDisable(false);
			nameField.setText("");
		}
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
			JFXDialogLayout content = new JFXDialogLayout();
			content.setBody(new Text("Please enter information"));
			JFXDialog dialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
			JFXButton okButton = new JFXButton("Ok");
			okButton.setButtonType(JFXButton.ButtonType.RAISED);
			okButton.setOnAction(event -> dialog.close());
			content.setActions(okButton);
			dialog.setOnDialogClosed((JFXDialogEvent event)-> nameField.requestFocus());
			dialog.show();
		}
	}

	private boolean allFieldsCompleted()
	{
		return !nameField.getText().isEmpty() && !titleField.getText().isEmpty()
				&& !descriptionField.getText().isEmpty();
	}

	private void showEmailSentAlert()
	{
		JFXDialogLayout content = new JFXDialogLayout();
		content.setBody(new Text("Email Sent"));
		JFXDialog dialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
		JFXButton okButton = new JFXButton("Ok");
		okButton.setButtonType(JFXButton.ButtonType.RAISED);
		okButton.setOnAction(event -> closeWindow());
		content.setActions(okButton);
		dialog.setOnDialogClosed((JFXDialogEvent event)-> nameField.requestFocus());
		dialog.show();
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
