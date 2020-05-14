package Controllers;

import Model.Customer;
import Model.Databases.GeneralDatabase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignUpController {

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

    @FXML
    public void handleSubmit(ActionEvent event) {

        if(event.getSource().equals(submit)) {
            GeneralDatabase generalDatabase = new GeneralDatabase();
            if (generalDatabase.avoidDuplicate(userName.getText())) {
                if (password.getText().matches(".*[a-zA-Z]+.*")){

                    String fullName = firstName.getText() + " " + lastName.getText();
                    Customer c = new Customer(userName.getText(), lastName.getText(), email.getText()
                            , dob.getValue().toString(), generalDatabase.createUniqueAccountNumber());
                    generalDatabase.addUser(c, password.getText(), getDate());



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
