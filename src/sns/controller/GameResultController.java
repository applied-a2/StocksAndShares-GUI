package sns.controller;

import java.util.ArrayList;

import sns.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller of the GameResult.fxml
 * @author Thai Kha Le
 */
public class GameResultController {
	
	private ArrayList<Player> players;
	
	@FXML
	private Label winnerLabel;
	
	@FXML
	private Label resultLabel;
	
	public void setPlayers(ArrayList<Player> players)
	{
		this.players = players; 
	}
	
	public GameResultController()
	{ }
	
	@FXML
	private void initialize()
	{
		players = new ArrayList<Player>();
	}
	
	public void secondInitialize()
	{		
		String winner = "";
		String result = "";
		
		int money = 0;
		for(Player player: players) {
			if(player.getMoney() > money) {
				money = player.getMoney();
				winner = player.toString();
			}
			result += player.toString();
		}
		
		winnerLabel.setText(winner);
		resultLabel.setText(result);
	}
}
