package Controllers.ModelControllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactUsController {

    @FXML
    private JFXButton back;

    @FXML
    void handleback(ActionEvent event) {
        if(event.getSource().equals(back)){
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
