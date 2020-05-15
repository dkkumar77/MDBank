package Controllers;


import Model.Databases.GeneralDatabase;
import Model.SceneInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

// Every controller class will implement SceneInterface
// This was the database variable will only have one connection through out the program and it will prevent
// each new controller from making a new connection with the database.
public class HomeController implements SceneInterface
{
	@FXML
	public StackPane stackPane;

	//lines 17 - 26 should be in every controller file except the sign up controller.
	private GeneralDatabase generalDatabase;
	private String username;

	@Override
	public void init(GeneralDatabase generalDatabase, String username)
	{
		this.generalDatabase = generalDatabase;
		this.username = username;
	}
}
