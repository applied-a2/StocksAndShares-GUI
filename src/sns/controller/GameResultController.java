package sns.controller;

import java.util.ArrayList;

import sns.driver.MainApp;
import sns.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller of the GameResult.fxml
 * @author Thai Kha Le
 */
public class GameResultController {
	
	private MainApp snsApp;
	
	@FXML
	private Label winnerLabel;
	
	@FXML
	private Label resultLabel;
	
	public void setApp(MainApp app)
	{
		snsApp = app;
	}
	
	public GameResultController()
	{ }
	
	@FXML
	private void initialize()
	{ }
	
	public void secondInitialize()
	{		
		snsApp.findWinner();
		winnerLabel.setText(snsApp.getWinner());
		resultLabel.setText("Result after 12 rounds: \n\n" + snsApp.getResult());
	}
}
