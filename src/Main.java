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

        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }

    /*

    HELLO

     */


    public static void main(String[] args) {
        launch(args);
    }
}
