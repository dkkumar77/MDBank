package Controllers.ModelControllers;

import Controllers.Util.DialogAlert;
import Model.Constants.TransactionType;
import Model.Databases.GeneralDatabase;
import Model.Databases.UserDatabase;
import Model.Objects.SceneInterface;
import Model.Objects.Transaction;
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
	public synchronized void handleTransfer(ActionEvent event)
	{
		if(event.getSource().equals(transferButton)){
			double balance;
			try{
				balance = Double.parseDouble(amountField.getText());
			}catch (NumberFormatException e){
				DialogAlert.showOKDialog(stackPane,"Invalid Amount: " + amountField.getText());
				amountField.clear();
				return;
			}
			if(generalDatabase.avoidDuplicate(usernameField.getText())){
				DialogAlert.showOKDialog(stackPane,"Recipient Does not exist");
				clearFields();
				return;
			}
			if(generalDatabase.getCurrentBalance(username) <= balance){
				DialogAlert.showOKDialog(stackPane,"Amount must be less than the balance");
			}else{
				TransferTransaction transferTransaction = new TransferTransaction(username, usernameField.getText(), amountField.getText());
				generalDatabase.transferMoney(transferTransaction);
				DialogAlert.showOKDialog(stackPane, "Transferred $" + transferTransaction.getAmountToTransfer()
						+ "\nTo: " + transferTransaction.getRecipientUsername());
				clearFields();

				UserDatabase userDatabase = new UserDatabase(username, generalDatabase);
				double newbalance = generalDatabase.getCurrentBalance(username) - Double.parseDouble(transferTransaction.getAmountToTransfer());
				Transaction transaction = new Transaction(Double.parseDouble(transferTransaction.getAmountToTransfer()), TransactionType.TRANSFEROUT);
				userDatabase.logTransaction(transaction, newbalance);
			}
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
