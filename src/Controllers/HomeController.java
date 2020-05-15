package Controllers;


import Model.Databases.GeneralDatabase;
import Model.SceneInterface;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.joda.time.DateTime;
import Model.Date;

import static Model.Date.getGreeting;


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

	public String fullname;

	@FXML
	private JFXTextField welcomeMessage;




	public void init(GeneralDatabase generalDatabase, String username)
	{

		this.generalDatabase = generalDatabase;
		this.username = username;

		setMessage(username);

	}


	public void setMessage(String username){

		String name = generalDatabase.grabFullName(username);


		welcomeMessage.setText("Welcome, " + name + " and " + getGreeting());



	}


	/*
	fucking useless


	public String returnAccurateGreeting(){
		DateTime date = new DateTime();
		int currentHour = date.getHourOfDay();
		if(currentHour <= 11 && currentHour >= 5){
			return "Good Morning";

		}
		else if (currentHour <= 6 && currentHour >= 22){
			return "Good Night";
		}
		else if(currentHour <= 21 && currentHour >= 18){
			return "Good Evening";
		}

		return "Good Afternoon";


	}


	 */

}



