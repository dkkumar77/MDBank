package Controllers.ModelControllers;

import Model.Constants.TransactionType;
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


    private GeneralDatabase gd;

    private String username;

    @FXML
    void submitToChecking(ActionEvent event) {

    }

    public void init(GeneralDatabase generalDatabase, String username)
    {
        this.gd = generalDatabase;
        this.username = username;
    }

    public void init(GeneralDatabase gd,String username, TransactionType type){
        this.gd = gd;
        this.username = username;

        if(type.equals(TransactionType.DEPOSIT)){



        }

    }
        /*
    public void initDeposit(GeneralDatabase gd, String username, String TransactionType){

        this.gd = gd;
        this.username = username;
       // sendStackPanesToBack(transferOut,transferWithin,withdraw);
        deposit.toFront();

    }
    public void initWithdrawl(GeneralDatabase gd, String username){
        this.gd = gd;
        this.username = username;

        sendStackPanesToBack(deposit,transferOut,transferWithin);
        withdraw.toFront();


    }

    public void initTransferIn(GeneralDatabase gd, String username) {
        this.gd = gd;
        this.username = username;
        sendStackPanesToBack(deposit,transferOut,withdraw);
        transferWithin.toFront();


    }

    public void initTransferOut(GeneralDatabase gd, String username){
        this.gd = gd;
        this.username = username;
        sendStackPanesToBack(deposit,transferWithin,withdraw);

    }


    private void sendStackPanesToBack(StackPane ... stackPanes){
        for(StackPane stackpane: stackPanes){
            stackpane.toBack();

        }
    }

         */
}



