package Controllers;

import Controllers.Util.Encrypter;
import Model.Customer;
import Model.Databases.GeneralDatabase;
import Model.Date;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static Model.Constants.FilePaths.*;

public class SignUpController
{

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXDatePicker dob;

    @FXML
    private JFXTextField userName;

    @FXML
    private JFXPasswordField password, confPassField;

    @FXML
    private JFXButton submit;


    private String username;

    @FXML
    private JFXButton back;



    @FXML
    public void handleBack(ActionEvent event){

        if(event.getSource().equals(back)){
           loadLogin(event);
        }
    }
    @FXML
    public void handleSubmit(ActionEvent event) throws NullPointerException {

        if(event.getSource().equals(submit)) {
            GeneralDatabase generalDatabase = new GeneralDatabase();
            if (allFieldsFilled()) {
                if (generalDatabase.avoidDuplicate(userName.getText())) {
                    if (passwordMatches()) {
                        String fullName = firstName.getText() + " " + lastName.getText();
                        Customer c = new Customer(userName.getText(), fullName, email.getText()
                                , dob.getValue().toString(), generalDatabase.createUniqueAccountNumber());
                        generalDatabase.addUser(c, Encrypter.getEncryptedPassword(password.getText()), Date.getDate());
                        loadLogin(event);
                    }
                    showDialog("Password does not match");
                }
            } else {
                showDialog("Please enter all information");
            }
        }
    }

    private void loadLogin(ActionEvent event)
    {
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
        LoginController controller = loader.getController();

        Stage homeWindow;
        homeWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        homeWindow.setScene(currScene);
        homeWindow.show();
    }

    private boolean passwordMatches()
    {
        if(password.getText().equals(confPassField.getText())) {
            password.getText().matches(".*[a-zA-Z]+.*");
            return true;
        }
        return false;
    }

    private boolean allFieldsFilled()
    {
        return !userName.getText().isEmpty() && !password.getText().isEmpty() && !firstName.getText().isEmpty()
                && !lastName.getText().isEmpty() && !dob.getValue().toString().equals("") && !email.getText().isEmpty();
    }

    private void showDialog(String message)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setBody(new Text(message));
        JFXDialog dialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
        JFXButton okButton = new JFXButton("Ok");
        okButton.setButtonType(JFXButton.ButtonType.RAISED);
        okButton.setOnAction(event -> dialog.close());
        content.setActions(okButton);
        dialog.setOnDialogClosed((JFXDialogEvent event)-> firstName.requestFocus());
        dialog.show();
    }
}

