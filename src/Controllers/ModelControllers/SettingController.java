package Controllers.ModelControllers;

import Controllers.Util.EmailSender;
import Model.Databases.AdminDatabase;
import Model.Databases.GeneralDatabase;
import Model.Databases.UserDatabase;
import Model.Definitions.Email;
import Model.Definitions.SceneInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import Controllers.Util.DialogAlert;

import Controllers.Util.Encrypter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SettingController implements SceneInterface {

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
        if(event.getSource().equals(submitStatementQuery)){
            File pdfFile = new File("MonthlyStatement.pdf");
            UserDatabase userDatabase = new UserDatabase("user",generalDatabase);
            List<String> attachment = new ArrayList<>(1);
            try {
                userDatabase.saveMonthlyStatementToPdf(pdfFile,5,2020);
                attachment.add(pdfFile.getPath());
                Email statementEmail = new Email(generalDatabase.returnEmail(username),"MD Bank Monthly Statement",
                        "Dear "+generalDatabase.grabFirstName(username)+",\nYour statment is attached.\n" +
                                "PLEASE DO NOT REPLY AS THIS EMAIL IS NOT MONITORED.\nThank you for being a customer.\n",
                        attachment);
                EmailSender emailSender = new EmailSender(statementEmail);
                if(emailSender.send()){
                    DialogAlert.showOKDialog(stackpaneStatement,"Email Sent");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(pdfFile.exists()){
                pdfFile.deleteOnExit();
            }
        }

    }


    @FXML
    void handleAccountInformation(ActionEvent event) {

        if (event.getSource().equals(accountInformation)) {
//            stackpaneCloseAccount.toBack();
//            stackpaneEmailUpdate.toBack();
//            stackpaneInquiry.toBack();
//            stackpaneStatement.toBack();
//            stackpanePass.toBack();

            sendStackPanesToBack(stackpaneCloseAccount, stackpaneEmailUpdate, stackpaneInquiry, stackpaneStatement,
                    stackpanePass);
            stackpaneAccountInfo.toFront();

        }

    }

    @FXML
    void handleChangePassword(ActionEvent event) {

        if (event.getSource().equals(changePassword)) {
//            stackpaneAccountInfo.toBack();
//            stackpaneCloseAccount.toBack();
//            stackpaneEmailUpdate.toBack();
//            stackpaneInquiry.toBack();
//            stackpaneStatement.toBack();

            sendStackPanesToBack(stackpaneAccountInfo, stackpaneCloseAccount, stackpaneEmailUpdate, stackpaneInquiry,
                    stackpaneStatement);

            stackpanePass.toFront();


        }
    }

    @FXML
    void handleCloseAccount(ActionEvent event) {

        if (event.getSource().equals(closeAccount)) {
//            stackpaneAccountInfo.toBack();
//            stackpaneEmailUpdate.toBack();
//            stackpaneInquiry.toBack();
//            stackpaneStatement.toBack();
//            stackpanePass.toBack();

            sendStackPanesToBack(stackpaneAccountInfo, stackpaneEmailUpdate, stackpaneInquiry,
                    stackpaneStatement, stackpanePass);

            stackpaneCloseAccount.toFront();


        }
    }

    @FXML
    void handleRecieveStatement(ActionEvent event) {

        if (event.getSource().equals(recieveStatement)) {
//            stackpaneAccountInfo.toBack();
//            stackpaneEmailUpdate.toBack();
//            stackpaneInquiry.toBack();
//            stackpanePass.toBack();
//            stackpaneCloseAccount.toBack();

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
    void handleUpdateEmail(ActionEvent event) {

        if (event.getSource().equals(updateEmail)) {
            sendStackPanesToBack(stackpaneAccountInfo, stackpaneInquiry, stackpanePass, stackpaneCloseAccount,
                    stackpaneStatement);
//            stackpaneAccountInfo.toBack();
//            stackpaneInquiry.toBack();
//            stackpanePass.toBack();
//            stackpaneCloseAccount.toBack();
//            stackpaneStatement.toBack();
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

//            stackpaneAccountInfo.toBack();
//            stackpanePass.toBack();
//            stackpaneCloseAccount.toBack();
//            stackpaneStatement.toBack();
//            stackpaneEmailUpdate.toBack();

            stackpaneInquiry.toFront();


        }
    }






}