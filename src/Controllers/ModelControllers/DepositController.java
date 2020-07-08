package Controllers.ModelControllers;

import Controllers.Util.DialogAlert;
import Model.Constants.TransactionType;
import Model.Databases.GeneralDatabase;
import Model.Databases.UserDatabase;
import Model.Objects.SceneInterface;
import Model.Objects.Transaction;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DepositController implements SceneInterface
{

	@FXML
	private StackPane stackpane;

	@FXML
	private JFXRadioButton checkingRadio;

	@FXML
	private JFXRadioButton savingRadio;

	@FXML
	private JFXButton depositButton;

	@FXML
	private TextField balanceInputField;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private Label currCheckBalLabel;

	@FXML
	private Label currSavBalLabel;

	private GeneralDatabase generalDatabase;
	private UserDatabase userDatabase;
	private String username;

	private boolean checkingsAccountSelected;
	private boolean savingsAccountSelected;


	@Override
	public void init(GeneralDatabase generalDatabase, String username)
	{
		this.generalDatabase = generalDatabase;
		this.username = username;
		this.userDatabase = new UserDatabase(username,generalDatabase);

		currCheckBalLabel.setText(Double.toString(this.generalDatabase.getCurrentBalance(username)));
		currSavBalLabel.setText(Double.toString(this.generalDatabase.getSavingBalance(username)));

		this.checkingsAccountSelected = false;
		this.savingsAccountSelected = false;
		
		ToggleGroup toggleGroup = new ToggleGroup();
		checkingRadio.setToggleGroup(toggleGroup);
		savingRadio.setToggleGroup(toggleGroup);

	}

	@FXML
	public void handleRadioButtons(ActionEvent event)
	{
		if(checkingRadio.isSelected()){
			checkingsAccountSelected = true;
			savingsAccountSelected = false;
			System.out.println("checking");
		}
		if(savingRadio.isSelected()){
			savingsAccountSelected = true;
			checkingsAccountSelected = false;
			System.out.println("savings");
		}
	}

	@FXML
	public void handleDeposit(ActionEvent event)
	{
		if(event.getSource().equals(depositButton)) {
			double amountToDeposit = 0;

				try {
					amountToDeposit = Math.floor(Double.parseDouble(balanceInputField.getText()) * 100)/100;
				} catch (NumberFormatException e) {
					DialogAlert.showOKDialog(stackpane, "Invalid Amount $" + balanceInputField.getText());
					return;
				}



				if (amountToDeposit <= 0 || balanceInputField.getText().isEmpty()) {
					DialogAlert.showOKDialog(stackpane, "Amount must be greater that $0.00");
					return;
				}


			if (checkingsAccountSelected == true) {
				double currentBalance = generalDatabase.getCurrentBalance(username);

				double newBalance = currentBalance + amountToDeposit;
				newBalance = Math.floor(newBalance*100)/100;
				Transaction transaction = new Transaction(amountToDeposit, TransactionType.DEPOSIT_CHECKING);
				userDatabase.logTransaction(transaction, newBalance);
				DialogAlert.showOKDialog(stackpane, "Amount $" + amountToDeposit + " deposited to your account.");
				currCheckBalLabel.setText(Double.toString(this.generalDatabase.getCurrentBalance(username)));
				currSavBalLabel.setText(Double.toString(this.generalDatabase.getSavingBalance(username)));
			}
			else{
				double currentSavings = generalDatabase.getSavingBalance(username);
				Transaction trans = new Transaction(amountToDeposit,TransactionType.DEPOSIT_SAVING);
				userDatabase.logTransaction(trans, Math.floor((currentSavings + amountToDeposit)*100)/100);
				DialogAlert.showOKDialog(stackpane,"Amount $ " + amountToDeposit + " was deposited into your Savings");
				currSavBalLabel.setText(Double.toString(generalDatabase.getSavingBalance(username)));


			}
		}

	}

	@FXML
	public void handleCancel(ActionEvent event)
	{
		if(event.getSource().equals(cancelButton)) {
			Stage currStage = (Stage) cancelButton.getScene().getWindow();
			currStage.close();
		}
	}

}
