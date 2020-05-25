package Controllers.ModelControllers;

import Model.Databases.GeneralDatabase;
import Model.Definitions.SceneInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SettingController implements SceneInterface {

    @FXML
    private StackPane stackpanePass;

    @FXML
    private JFXButton submitButtonForPasswordChange;

    @FXML
    private JFXTextField oldPassword;

    @FXML
    private JFXTextField newPassword;

    @FXML
    private JFXTextField confirmNewPassword;

    @FXML
    private StackPane stackpaneStatement;

    @FXML
    private JFXButton submitStatementQuery;

    @FXML
    private StackPane stackpaneAccountInfo;

    @FXML
    private JFXTextField nameForAccountInfo;

    @FXML
    private JFXTextField accountNumberForAccountInfo;

    @FXML
    private JFXTextField emailForAccountInfo;

    @FXML
    private JFXTextField dobForAccountInfo;

    @FXML
    private JFXTextField joinDateAccountInfo;

    @FXML
    private StackPane stackpaneInquiry;

    @FXML
    private StackPane stackpaneCloseAccount;

    @FXML
    private JFXButton changePassword;

    @FXML
    private JFXButton accountInformation;

    @FXML
    private JFXButton updateEmail;

    @FXML
    private JFXButton closeAccount;

    @FXML
    private JFXButton reportInquiry;

    @FXML
    private JFXButton recieveStatement;

    @FXML
    private StackPane stackpaneEmailUpdate;

    @FXML
    private JFXTextField newEmailForEmailChange;

    @FXML
    private JFXTextField confirmNewEmailForEmailChange;

    @FXML
    private JFXTextField passwordForEmailChange;

    @FXML
    private JFXButton submitForEmailChange;


    private GeneralDatabase generalDatabase;
    private String username;


    public void init(GeneralDatabase generalDatabase, String username) {

        this.generalDatabase = generalDatabase;
        this.username = username;

    }

    
    @FXML
    void handleSubmitForEmailChange(ActionEvent event) {

    }


    @FXML
    void handleSubmitStatementQuery(ActionEvent event) {

    }


    @FXML
    void handleAccountInformation(ActionEvent event) {

        if (event.getSource().equals(accountInformation)) {
            stackpaneCloseAccount.toBack();
            stackpaneEmailUpdate.toBack();
            stackpaneInquiry.toBack();
            stackpaneStatement.toBack();
            stackpanePass.toBack();
            stackpaneAccountInfo.toFront();

        }

    }

    @FXML
    void handleChangePassword(ActionEvent event) {

        if (event.getSource().equals(changePassword)) {
            stackpaneAccountInfo.toBack();
            stackpaneCloseAccount.toBack();
            stackpaneEmailUpdate.toBack();
            stackpaneInquiry.toBack();
            stackpaneStatement.toBack();
            stackpanePass.toFront();


        }
    }

    @FXML
    void handleCloseAccount(ActionEvent event) {

        if (event.getSource().equals(closeAccount)) {
            stackpaneAccountInfo.toBack();
            stackpaneEmailUpdate.toBack();
            stackpaneInquiry.toBack();
            stackpaneStatement.toBack();
            stackpanePass.toBack();
            stackpaneCloseAccount.toFront();


        }
    }

    @FXML
    void handleRecieveStatement(ActionEvent event) {

        if (event.getSource().equals(recieveStatement)) {
            stackpaneAccountInfo.toBack();
            stackpaneEmailUpdate.toBack();
            stackpaneInquiry.toBack();
            stackpanePass.toBack();
            stackpaneCloseAccount.toBack();
            stackpaneStatement.toFront();


        }
    }


    @FXML
    void handleSubmitPassword(ActionEvent event) {

        if(event.getSource().equals(submitButtonForPasswordChange)){


        }
    }



    @FXML
    void handleUpdateEmail(ActionEvent event) {

        if (event.getSource().equals(updateEmail)) {
            stackpaneAccountInfo.toBack();
            stackpaneInquiry.toBack();
            stackpanePass.toBack();
            stackpaneCloseAccount.toBack();
            stackpaneStatement.toBack();
            stackpaneEmailUpdate.toFront();

        }

    }

    @FXML
    void handlereportInquiry(ActionEvent event) {

        if (event.getSource().equals(reportInquiry)) {
            stackpaneAccountInfo.toBack();
            stackpanePass.toBack();
            stackpaneCloseAccount.toBack();
            stackpaneStatement.toBack();
            stackpaneEmailUpdate.toBack();
            stackpaneInquiry.toFront();


        }
    }

}
