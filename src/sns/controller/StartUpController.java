package sns.controller;

import java.io.IOException;

import sns.driver.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.controlsfx.dialog.Dialogs;

public class StartUpController {

	@FXML
	private ChoiceBox choiceBox = new ChoiceBox();
	
	private boolean numPlayerSelected = false;
	
	private MainApp snsApp;

	public void setApp(MainApp app)
	{
		snsApp = app;
	}
	
	public StartUpController()
	{}
	
	public boolean isNumPlayerSelected()
	{
		return numPlayerSelected;
	}
	
	@FXML
	private void initialize()
	{
		choiceBox.getItems().addAll(1,2,3,4);
	}
	
	@SuppressWarnings("deprecation")
	@FXML
	private void startButtonClicked()
	{
		if(choiceBox.getSelectionModel().getSelectedItem() == null)
		{
			Dialogs.create()
				.title("No Selection")
				.masthead("No Players Amount Selected")
				.message("Please choose how many players")
				.showWarning();
		}
		else
		{
			int numPlayer = (int) choiceBox.getSelectionModel().getSelectedItem();
			snsApp.gameSetup(numPlayer);
			snsApp.shuffleCards();
			snsApp.dealtCard();
			snsApp.showPlayTable();;
			System.out.println("Number of players: " + numPlayer);
		}
	}
}
