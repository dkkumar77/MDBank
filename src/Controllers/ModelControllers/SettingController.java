package Controllers.ModelControllers;

import Controllers.Util.EmailSender;
import Model.Databases.AdminDatabase;
import Model.Databases.GeneralDatabase;
import Model.Databases.UserDatabase;
import Model.Objects.Email;
import Model.Objects.SceneInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import Controllers.Util.DialogAlert;


import Controllers.Util.Encrypter;
import javafx.stage.Stage;

import java.io.IOException;

import static Model.Constants.FilePaths.HOME_FXML;
import static Model.Constants.FilePaths.LOGIN_FXML;

public class SettingController implements SceneInterface {

    @FXML
    public JFXButton submitCloseAccountButton;
    @FXML
    public JFXPasswordField confirmPasswordFieldForDelete;
    @FXML
    public JFXPasswordField passwordFieldDelete;
    @FXML
    private StackPane stackpanePass;

    @FXML
    private JFXButton submitButtonForPasswordChange;

    @FXML
    private JFXPasswordField oldPassword;

    @FXML
    private JFXPasswordField newPassword;

    @FXML
    private JFXPasswordField confirmNewPassword;

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
    private StackPane stackpaneInquiry, stackpane1;

    @FXML
    private StackPane stackpaneCloseAccount;

    @FXML
    private JFXButton changePassword;

    @FXML
    private JFXButton accountInformation;

    @FXML
    private JFXButton updateEmail, back;


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
    private JFXPasswordField passwordForEmailChange;

    @FXML
    private JFXButton submitForEmailChange;

    @FXML
    private JFXTextArea textBoxForReportInquiry;

    @FXML
    private JFXTextField subjectForReportInquiry;

    @FXML
    private JFXButton  submitForReportInquiry, clearReportInquiry;



    private GeneralDatabase generalDatabase;
    private String username;

    private DialogAlert alert;

    private Encrypter encrypt;


    public void init(GeneralDatabase generalDatabase, String username) {

        this.generalDatabase = generalDatabase;
        this.username = username;
        displayDocumentedInformation();


    }


    void displayDocumentedInformation(){
        String [] array =  generalDatabase.grabBulk(username);
        nameForAccountInfo.setText(array[0]);
        accountNumberForAccountInfo.setText(array[1]);
        emailForAccountInfo.setText(array[2]);
        dobForAccountInfo.setText(array[3]);
        joinDateAccountInfo.setText(array[4]);


    }

    @FXML
    void handleSendingInquiry(ActionEvent event){

        if(event.getSource().equals(submitForReportInquiry)){
            if(textBoxForReportInquiry.getText().length() > 0 && subjectForReportInquiry.getText().length() > 0){
                Email inquiryEmail = new Email(AdminDatabase.returnAdminInfo("admin1").get(0),
                        textBoxForReportInquiry.getText(),subjectForReportInquiry.getText(),null);
                EmailSender emailSender = new EmailSender(inquiryEmail);
                if(emailSender.send()){
                    DialogAlert.showOKDialog(stackpaneInquiry,"Email Sent");
                    textBoxForReportInquiry.clear();
                    subjectForReportInquiry.clear();
                }
            }else{
                DialogAlert.showOKDialog(stackpaneInquiry,"Please enter a message");
            }

        }





    }
    @FXML
    public void clearReportInquiry(ActionEvent event){
        if(event.getSource().equals(clearReportInquiry)){
            textBoxForReportInquiry.setText("");
            subjectForReportInquiry.setText("");
        }
    }



    @FXML
    void handleSubmitForEmailChange(ActionEvent event) {

        if (event.getSource().equals(submitForEmailChange)) {
            if (!newEmailForEmailChange.getText().isEmpty() && !confirmNewEmailForEmailChange.getText().isEmpty() && !passwordForEmailChange.getText().isEmpty()) {
                if (newEmailForEmailChange.getText().contains("@")) {
                    if (newEmailForEmailChange.getText().equals(confirmNewEmailForEmailChange.getText())) {
                        if (generalDatabase.returnHashedPass(username).equals(encrypt.getEncryptedPassword(passwordForEmailChange.getText()))) {
                            generalDatabase.updateEmailQuery(username, newEmailForEmailChange.getText());
                            passwordForEmailChange.setText("");
                            newEmailForEmailChange.setText("");
                            confirmNewEmailForEmailChange.setText("");
                            stackpaneEmailUpdate.toBack();


                        }
                    }
                }
            }


        }


    }


    @FXML
    void handleSubmitStatementQuery(ActionEvent event) {

    }


    @FXML
    void handleAccountInformation(ActionEvent event) {

        if (event.getSource().equals(accountInformation)) {

            sendStackPanesToBack(stackpaneCloseAccount, stackpaneEmailUpdate, stackpaneInquiry, stackpaneStatement,
                    stackpanePass);
            stackpaneAccountInfo.toFront();

        }

    }

    @FXML
    void handleChangePassword(ActionEvent event) {

        if (event.getSource().equals(changePassword)) {

            sendStackPanesToBack(stackpaneAccountInfo, stackpaneCloseAccount, stackpaneEmailUpdate, stackpaneInquiry,
                    stackpaneStatement);

            stackpanePass.toFront();


        }
    }

    @FXML
    void handleCloseAccount(ActionEvent event) {

        if (event.getSource().equals(closeAccount)) {
            sendStackPanesToBack(stackpaneAccountInfo, stackpaneEmailUpdate, stackpaneInquiry,
                    stackpaneStatement, stackpanePass);
            stackpaneCloseAccount.toFront();
        }
        if(event.getSource().equals(submitCloseAccountButton)){
            if(confirmPasswordFieldForDelete.getText().equals(passwordFieldDelete.getText())){
                if(generalDatabase.verifyCredentials(username,Encrypter.getEncryptedPassword(passwordFieldDelete.getText()))){
                    String emailAddress = generalDatabase.returnEmail(username);
                    if(generalDatabase.deleteUser(username)){
                        UserDatabase userDatabase = new UserDatabase(username,generalDatabase);
                        if(userDatabase.deleteTable()){
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource(LOGIN_FXML));
                            Parent loginParent = null;
                            try {
                                loginParent = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            assert loginParent != null;
                            Scene currScene = new Scene(loginParent);
                            Stage homeWindow;
                            homeWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            homeWindow.setScene(currScene);
                            homeWindow.show();
                            DialogAlert.showOKDialog(stackpaneCloseAccount,"Thank you for banking with us\nYour account is closed");
                        }else{
                            DialogAlert.showOKDialog(stackpaneCloseAccount,"Unable to delete Account");
                        }
                    }else{
                        DialogAlert.showOKDialog(stackpaneCloseAccount,"Unable to delete Account");
                    }
                }else{
                    DialogAlert.showOKDialog(stackpaneCloseAccount,"Incorrect Password");
                }
            }else{
                DialogAlert.showOKDialog(stackpaneCloseAccount,"Passwords do not match");
            }
        }
    }

    @FXML
    void handleRecieveStatement(ActionEvent event) {

        if (event.getSource().equals(recieveStatement)) {

            sendStackPanesToBack(stackpaneAccountInfo, stackpaneEmailUpdate, stackpaneInquiry, stackpanePass,
                    stackpaneCloseAccount);

            stackpaneStatement.toFront();


        }
    }


    @FXML
    void handleSubmitPassword(ActionEvent event) throws NullPointerException {

        if (event.getSource().equals(submitButtonForPasswordChange)) {
            if (!oldPassword.getText().isEmpty() || !newPassword.getText().isEmpty() || !confirmNewPassword.getText().isEmpty()) {
                String oldpass = oldPassword.getText();

                if (generalDatabase.returnHashedPass(username).equals(encrypt.getEncryptedPassword(oldpass))) {
                    if (newPassword.getText().matches(".*[a-zA-Z]+.*")) {
                        if (newPassword.getText().equals(confirmNewPassword.getText())) {

                            generalDatabase.updatePasswordQuery(username, encrypt.getEncryptedPassword(newPassword.getText()));
                            stackpanePass.toBack();
                            oldPassword.setText("");
                            newPassword.setText("");
                            confirmNewPassword.setText("");


                        } else {
                            oldPassword.setText("");
                            newPassword.setText("");
                            confirmNewPassword.setText("");
                        }
                    }
                } else {
                    oldPassword.setText("");

                }

            } else {
                oldPassword.setText("");
                newPassword.setText("");
                confirmNewPassword.setText("");

            }


        }
    }



    @FXML
    void handleback(ActionEvent event){
        if(event.getSource().equals(back)){

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource(HOME_FXML));
            Parent loginParent = null;
            try {
                loginParent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert loginParent != null;
            Scene currScene = new Scene(loginParent);
            HomeController controller = loader.getController();
            controller.init(generalDatabase,username);



            Stage homeWindow;
            homeWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            homeWindow.setScene(currScene);
            homeWindow.show();

        }


    }

    @FXML
    void handleUpdateEmail(ActionEvent event) {

        if (event.getSource().equals(updateEmail)) {
            sendStackPanesToBack(stackpaneAccountInfo, stackpaneInquiry, stackpanePass, stackpaneCloseAccount,
                    stackpaneStatement);
            stackpaneEmailUpdate.toFront();

        }

    }

    private void sendStackPanesToBack(StackPane... stackPanes) {
        for (StackPane stackPane : stackPanes) {
            stackPane.toBack();
        }
    }

    @FXML
    void handlereportInquiry(ActionEvent event) {

        if (event.getSource().equals(reportInquiry)) {
            sendStackPanesToBack(stackpaneAccountInfo, stackpanePass, stackpaneCloseAccount, stackpaneStatement,
                    stackpaneEmailUpdate);

            stackpaneInquiry.toFront();


        }
    }






}