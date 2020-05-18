import Controllers.LoginController;
import Model.Constants.ScreenDimensions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/ApplicationBootScene.fxml"));
        primaryStage.setTitle("lol_lol");

        primaryStage.setScene(new Scene(root, ScreenDimensions.WIDTH, ScreenDimensions.HEIGHT));
        primaryStage.setMinWidth(ScreenDimensions.WIDTH);
        primaryStage.setMinHeight(ScreenDimensions.HEIGHT);
        primaryStage.setMaxWidth(ScreenDimensions.WIDTH);
        primaryStage.setMaxHeight(ScreenDimensions.HEIGHT);


        primaryStage.show();
    }

    /*
    HELLO

     */


    public static void main(String[] args) {
        launch(args);
    }
}
