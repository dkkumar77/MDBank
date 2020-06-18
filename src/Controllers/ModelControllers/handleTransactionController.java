package Controllers.ModelControllers;

import Model.Databases.GeneralDatabase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class handleTransactionController {

    @FXML
    private StackPane transferWithin;

    @FXML
    private StackPane recentTransaction;

    @FXML
    private StackPane deposit;

    @FXML
    private JFXButton sendDepositQuery;

    @FXML
    private JFXTextField amountForDeposit;

    @FXML
    private StackPane transferOut;

    @FXML
    private JFXTextField userCheckingNameForTransferOut;

    @FXML
    private JFXTextField userInputUsername;

    @FXML
    private JFXButton submitForTransferOut;

    @FXML
    private JFXTextArea messageWarningTransferOut;

    @FXML
    private StackPane withdraw;

    @FXML
    private JFXTextField withdrawAmount;

    @FXML
    void submitToChecking(ActionEvent event) {

    }

    public void initDeposit(GeneralDatabase gd, String username){


    }
    public void initWithdrawl(GeneralDatabase gd, String username){


    }


}



