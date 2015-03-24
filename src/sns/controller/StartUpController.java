package sns.controller;

import java.io.IOException;

import sns.driver.MainApp;
import sns.utility.ErrorPrinter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Controller of the Startup.fxml
 * @author Thai Kha Le
 */
public class StartUpController {

	private MainApp snsApp;
	private boolean numPlayerSelected = false;
	
	@FXML
	private ChoiceBox choiceBox = new ChoiceBox();
	
	public void setApp(MainApp app)
	{
		snsApp = app;
	}
	
	public StartUpController()
	{ }
	
	@FXML
	private void initialize()
	{
		choiceBox.getItems().addAll(2,3,4);
	}
	
	@FXML
	private void startButtonClicked()
	{
		if(choiceBox.getSelectionModel().getSelectedItem() == null) {
			ErrorPrinter.printError(snsApp.getPrimaryStage(), 
					"No Selection", "No Players Amount Selected", "Please choose how many players");
		}
		else {
			int numPlayer = (int) choiceBox.getSelectionModel().getSelectedItem();
			snsApp.gameSetup(numPlayer);
			snsApp.shuffleCards();
			snsApp.dealtCard();
			snsApp.showPlayTable();
			System.out.println("Number of players: " + numPlayer);
		}
	}
}
