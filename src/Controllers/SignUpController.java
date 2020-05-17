package Controllers;

import Controllers.Util.Encrypter;
import Model.Customer;
import Model.Databases.GeneralDatabase;
import Model.SceneInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignUpController
{

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private DatePicker dob;

    @FXML
    private JFXTextField userName;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton submit;


    private String username;

    private Encrypter e;


    @FXML
    public void handleSubmit(ActionEvent event) throws NullPointerException {

        if(event.getSource().equals(submit)) {
            GeneralDatabase generalDatabase = new GeneralDatabase();

            if (generalDatabase.avoidDuplicate(userName.getText())) {
                if (password.getText().matches(".*[a-zA-Z]+.*")){

                    String fullName = firstName.getText() + " " + lastName.getText();
                    Customer c = new Customer(userName.getText(), fullName, email.getText()
                            , dob.getValue().toString(), generalDatabase.createUniqueAccountNumber());

                    generalDatabase.addUser(c, e.getEncryptedPassword(password.getText()), getDate());

                    FXMLLoader loader = new FXMLLoader();

                    loader.setLocation(getClass().getResource("/View/ApplicationBootScene.fxml"));
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
            }

        }

    }

    public String getDate(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;

    }

}
