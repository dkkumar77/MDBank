package Controllers.ModelControllers;


import Model.Constants.TransactionType;
import Model.Databases.GeneralDatabase;
import Model.Objects.SceneInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import static Model.Constants.FilePaths.*;
import static Model.Objects.Date.getGreeting;


// Every controller class will implement SceneInterface
// This was the database variable will only have one connection through out the program and it will prevent
// each new controller from making a new connection with the database.
public class HomeController implements SceneInterface {


	/*

	 */
	@FXML
	public StackPane stackPane;

	//lines 17 - 26 should be isoundsn every controller file except the sign up controller.
	private GeneralDatabase generalDatabase;
	private String username;

	public String fullname;

	@FXML
	private JFXTextField welcomeMessage, currentBal;


	@FXML
	private JFXButton setting, logoffButton;


	@FXML
	private JFXTextArea quotearea;

	@FXML
	private Label check, save;


	@FXML
	private JFXButton transferWithinAccount;

	@FXML
	private JFXButton transferToAnother;

	@FXML
	private JFXButton withdraw;

	@FXML
	private JFXButton deposit;

	@FXML
	private JFXButton recentTransaction;


	@FXML
	void handleRecentTransaction(ActionEvent event) {
	}

	@FXML
	void handleTransferToAnother(ActionEvent event) {

	}

	@FXML
	void handleTransferWithinAccounts(ActionEvent event) {

	}

	@FXML
	void handleWithdraw(ActionEvent event) {

	}


	public void init(GeneralDatabase generalDatabase, String username) {

		this.generalDatabase = generalDatabase;
		this.username = username;

		setMessage(username);
		setQuote();
		setUserCurrentBalance();


	}


	public void setUserCurrentBalance() {

		currentBal.setText(Double.toString((generalDatabase.getCurrentBalance(username))));


	}


	@FXML
	public void handleLogoff(ActionEvent event) {
		if (event.getSource().equals(logoffButton)) {
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

	}

	@FXML
	public void handleSettingButton(ActionEvent press) {
		if (press.getSource().equals(setting)) {


			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource(SETTINGS_PAGE));
			Parent loginParent = null;
			try {
				loginParent = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			assert loginParent != null;
			Scene currScene = new Scene(loginParent);
			SettingController controller = loader.getController();
			controller.init(generalDatabase, username);


			Stage homeWindow;
			homeWindow = (Stage) ((Node) press.getSource()).getScene().getWindow();
			homeWindow.setScene(currScene);
			homeWindow.show();

		}


	}

	public void setQuote() {

		int randomNum = ThreadLocalRandom.current().nextInt(1, 40 + 1);
		try {
			String quoteofD = Files.readAllLines(Paths.get("src/Controllers/TextFiles/Word.txt")).get(randomNum);

			quotearea.setText(quoteofD);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void setMessage(String username) {

		String name = generalDatabase.grabFullName(username);
		String firstName = generalDatabase.grabFirstName(username);

		welcomeMessage.setText("Welcome, " + name + " and " + getGreeting());
		check.setText(firstName + "'s Checking");
		save.setText(firstName + "'s Savings");


	}



	@FXML
	public void handleDeposit(ActionEvent actionEvent) throws IOException {

		if(actionEvent.getSource().equals(deposit)){

			handleSceneChange(actionEvent, TransactionType.DEPOSIT);


		}

	}

	@FXML
	public void handleWithdrawl(ActionEvent event) throws IOException {

		if (event.getSource().equals(withdraw)) {

			handleSceneChange(event, TransactionType.WITHDRAWAL);


		}
	}

	@FXML
	public void handleTransferIn(ActionEvent event) throws IOException{

		if (event.getSource().equals(transferWithinAccount)){

			handleSceneChange(event, TransactionType.TRANSFERIN);

		}
	}


	@FXML
	public void handleTransferOut(ActionEvent actionEvent) throws IOException{

		if(actionEvent.getSource().equals(transferToAnother)){
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TRANSACTION_PAGE));
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setMaxHeight(400);
			stage.setMinHeight(400);
			stage.setMaxWidth(600);
			stage.setMinWidth(600);
			TransferController controller = fxmlLoader.getController();
			controller.init(generalDatabase,username);

			AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
			AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);
			root.setOnMousePressed(event -> {
				xOffset.set(event.getSceneX());
				yOffset.set(event.getSceneY());
			});
			//move around here
			root.setOnMouseDragged(event -> {
				stage.setX(event.getScreenX() - xOffset.get());
				stage.setY(event.getScreenY() - yOffset.get());
			});
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();

		}
	}
	//actionEvent and resource get apssed
	public void handleSceneChange(ActionEvent actionEvent, TransactionType resource) throws IOException {

//		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TRANSACTION_PAGE));
//		Parent root = (Parent) fxmlLoader.load();
//
//		Stage stage = new Stage();
//		stage.setScene(new Scene(root));
//		stage.initStyle(StageStyle.UNDECORATED);
//		stage.setMaxHeight(200);
//		stage.setMinHeight(200);
//		stage.setMaxWidth(400);
//		stage.setMinWidth(400);
//		handleTransactionController controller = new handleTransactionController();
//		controller.init(generalDatabase,username,resource);
//
//
//		// controller stuff;
//		AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
//		AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);
//		root.setOnMousePressed(event -> {
//			xOffset.set(event.getSceneX());
//			yOffset.set(event.getSceneY());
//		});
//		root.setOnMouseDragged(event -> {
//			stage.setX(event.getScreenX() - xOffset.get());
//			stage.setY(event.getScreenY() - yOffset.get());
//		});
//		stage.show();


	}
}




