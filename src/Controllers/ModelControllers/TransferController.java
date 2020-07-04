package Controllers.ModelControllers;

import Controllers.Util.DialogAlert;
import Model.Databases.GeneralDatabase;
import Model.Objects.SceneInterface;
import Model.Objects.TransferTransaction;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TransferController implements SceneInterface
{

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private JFXButton transferButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private JFXButton resetButton;

	@FXML
	private Label currentBalanceField;

	@FXML
	private JFXTextField usernameField;

	@FXML
	private JFXTextField amountField;
	private GeneralDatabase generalDatabase;
	private String username;

	@Override
	public void init(GeneralDatabase generalDatabase, String username)
	{
		this.generalDatabase = generalDatabase;
		this.username = username;
		currentBalanceField.setText("$"+generalDatabase.getCurrentBalance(username));
	}

	@FXML
	public void handleCancel(ActionEvent event)
	{
		if(event.getSource().equals(cancelButton)) {
			Stage currStage = (Stage) cancelButton.getScene().getWindow();
			currStage.close();
		}
	}

	@FXML
	public void handleTransfer(ActionEvent event)
	{
		if(event.getSource().equals(transferButton)){
			TransferTransaction transferTransaction = new TransferTransaction(username,usernameField.getText(),amountField.getText());
			generalDatabase.transferMoney(transferTransaction);
			DialogAlert.showOKDialog(stackPane,"Transferred $"+transferTransaction.getAmountToTransfer()
					+"\nTo: "+transferTransaction.getRecipientUsername());
			clearFields();
		}
	}

	private void clearFields()
	{
		usernameField.clear();
		amountField.clear();
	}
	@FXML
	public void handleReset(ActionEvent actionEvent)
	{
		if(actionEvent.getSource().equals(resetButton)){
			clearFields();
		}
	}
}
