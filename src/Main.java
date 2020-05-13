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

        primaryStage.setScene(new Scene(root, 1000, 812));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(730);
        primaryStage.setMaxWidth(1000);
        primaryStage.setMaxHeight(730);
        primaryStage.show();
    }

    /*
    HELLO

     */


    public static void main(String[] args) {
        launch(args);
    }
}
