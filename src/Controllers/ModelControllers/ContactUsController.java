package Controllers.ModelControllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ContactUsController {

    @FXML
    private JFXButton back;

    @FXML
    private JFXButton githubButtonD, githubButtonM;

    @FXML
    void handleOpeningBrowser(ActionEvent event){

        if(event.getSource().equals(githubButtonD)){
            openPage("https://github.com/dkkumar77");

        }

        if(event.getSource().equals(githubButtonM)){
            openPage("https://github.com/Mani9723");
        }


    }

    public static void openPage(String link) {
        try {
            Desktop.getDesktop().browse(new URL(link).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
