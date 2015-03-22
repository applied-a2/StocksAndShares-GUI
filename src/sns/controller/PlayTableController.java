package sns.controller;

import java.util.ArrayList;
import java.util.Collections;

import sns.driver.MainApp;
import sns.model.Player;
import sns.model.Shares;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class PlayTableController {

	private MainApp snsApp;
	private int round;
	
	@FXML
	private Label currentPlayerLabel;
	@FXML
	private Label moneyLabel;
	@FXML
	private Label motorsLabel;
	@FXML
	private Label storesLabel;
	@FXML
	private Label shippingLabel;
	@FXML
	private Label steelsLabel;
	@FXML
	private Label firstPlayerLabel;
	@FXML
	private Rectangle firstPlayerRec;
	@FXML
	private Rectangle firstPlayerCardRec;
	@FXML
	private Label firstPlayerCardLabel;
	@FXML
	private Label secondPlayerLabel;
	@FXML
	private Rectangle secondPlayerRec;
	@FXML
	private Rectangle secondPlayerCardRec;
	@FXML
	private Label secondPlayerCardLabel;
	@FXML
	private Label thirdPlayerLabel;
	@FXML
	private Rectangle thirdPlayerRec;
	@FXML
	private Rectangle thirdPlayerCardRec;
	@FXML
	private Label thirdPlayerCardLabel;
	@FXML
	private Label cardFunctionLabel;
	@FXML
	private Label cardValueLabel;
	@FXML
	private Label motorsIndicatorLabel;
	@FXML
	private Label storesIndicatorLabel;
	@FXML
	private Label shippingsIndicatorLabel;
	@FXML
	private Label steelsIndicatorLabel;
	@FXML
	private Label roundLabel;
	
	@FXML
	private ImageView motorsCardIcon;
	@FXML
	private ImageView storesCardIcon;
	@FXML
	private ImageView shippingsCardIcon;
	@FXML
	private ImageView steelsCardIcon;
	@FXML
	private ImageView bullCardIcon;
	@FXML
	private ImageView bearCardIcon;
	
	@FXML
	private ChoiceBox playerChoiceBox;
	@FXML
	private Button nextButton;
	@FXML
	private Button historyButton;
	
	public PlayTableController()
	{}
	
	@FXML
	private void initialize()
	{
		firstPlayerLabel.setVisible(false);
		firstPlayerRec.setVisible(false);
		firstPlayerCardRec.setVisible(false);
		firstPlayerCardLabel.setVisible(false);
		
		secondPlayerLabel.setVisible(false);
		secondPlayerRec.setVisible(false);
		secondPlayerCardRec.setVisible(false);
		secondPlayerCardLabel.setVisible(false);
		
		thirdPlayerLabel.setVisible(false);
		thirdPlayerRec.setVisible(false);
		thirdPlayerCardRec.setVisible(false);
		thirdPlayerCardLabel.setVisible(false);
		
		motorsCardIcon.setVisible(false);
		storesCardIcon.setVisible(false);
		shippingsCardIcon.setVisible(false);
		steelsCardIcon.setVisible(false);
		bullCardIcon.setVisible(false);
		bearCardIcon.setVisible(false);
		
		nextButton = new Button();
		historyButton = new Button();
	}
	
	public void setApp(MainApp app)
	{
		snsApp = app;
	}
	
	public void setRound(int round)
	{
		this.round = round;
	}
	
	public void secondInitialize()
	{
		round = snsApp.getRound();
		String roundString = ("Round " + round);
		roundLabel.setText(roundString);
		if(!snsApp.isBuyCompleted()) {
			playerChoiceBox.getItems().add("Buy shares");
		}
		playerChoiceBox.getItems().addAll("Sell shares", "Pass");
		
		Shares shares = snsApp.getShares();
		ArrayList<Player> otherPlayers = snsApp.setupPlayersForTurnFx();
		Player currentPlayer = snsApp.getCurrentPlayer();
	
		currentPlayerLabel.setText("Player " + currentPlayer.getIdentity());
		moneyLabel.setText("" + currentPlayer.getMoney() + " pound");
		
		Label[] shareLabels = {motorsLabel,storesLabel,shippingLabel,steelsLabel};
		Label[] shareIndicatorLabel = {motorsIndicatorLabel, storesIndicatorLabel, shippingsIndicatorLabel, steelsIndicatorLabel};
		String[] mainTypes = {"motors","stores","shippings","steels"};
		
		for(int i = 0; i < mainTypes.length; i++) {
			shareLabels[i].setText("" 
					+ shares.getAmountOfSharesSoldToPlayer(mainTypes[i], currentPlayer.playerId()));
			shareIndicatorLabel[i].setText(shares.getShareValueOnType(mainTypes[i]) + " pound");
		}
		
		int dealtCardIndex = snsApp.getDealtCardIndex();
		
		String cardType = snsApp.getCards().get(dealtCardIndex).getCardType();
		
		if(cardType.equals("motors")) {
			motorsCardIcon.setVisible(true);
			snsApp.addCardIcon(motorsCardIcon.getImage());
		}
		else if(cardType.equals("stores")) {
			storesCardIcon.setVisible(true);
			snsApp.addCardIcon(storesCardIcon.getImage());
		}
		else if(cardType.equals("shippings")) {
			shippingsCardIcon.setVisible(true);
			snsApp.addCardIcon(shippingsCardIcon.getImage());
		}
		else if(cardType.equals("steels")) {
			steelsCardIcon.setVisible(true);
			snsApp.addCardIcon(steelsCardIcon.getImage());
		}
		else if(cardType.equals("bull")) {
			bullCardIcon.setVisible(true);
			snsApp.addCardIcon(bullCardIcon.getImage());
		}
		else {
			bearCardIcon.setVisible(true);
			snsApp.addCardIcon(bearCardIcon.getImage());
		}
		
		String cardFunction = snsApp.getCards().get(dealtCardIndex).getCardFunction();
		cardFunctionLabel.setText(cardFunction);
		
		String cardValue = "" + snsApp.getCards().get(dealtCardIndex).getCardValue() + ".00";
		cardValueLabel.setText(cardValue);
		
		Label[] playerLabels = {firstPlayerLabel,secondPlayerLabel,thirdPlayerLabel};
		Rectangle[] playerRecs = {firstPlayerRec,secondPlayerRec,thirdPlayerRec};
		Label[] playerCardLabels = {firstPlayerCardLabel,secondPlayerCardLabel,thirdPlayerCardLabel};
		Rectangle[] playerCardRecs = {firstPlayerCardRec,secondPlayerCardRec,thirdPlayerCardRec};
		
		for(int i = 0; i < otherPlayers.size(); i++)
		{
			String status = "";
			if(otherPlayers.get(i).retired()) {
				status = " (retired)";
			}
			playerLabels[i].setText("Player " + otherPlayers.get(i).getIdentity() + status);
			playerLabels[i].setVisible(true);
			playerRecs[i].setVisible(true);
			playerCardLabels[i].setVisible(true);
			playerCardRecs[i].setVisible(true);
		}
	}
	
	@FXML
	private void handleNextButton()
	{
		if(playerChoiceBox.getSelectionModel().getSelectedItem() == null) {
			nextTurnOrRound();
		}
		else if(playerChoiceBox.getSelectionModel().getSelectedItem().equals("Buy shares")) {
			snsApp.showBuyOrSellPanel("buy");
		}
		else if(playerChoiceBox.getSelectionModel().getSelectedItem().equals("Sell shares")) {
			snsApp.showBuyOrSellPanel("sell");
		}
		else {
			nextTurnOrRound();
		}
	}
	
	@FXML
	private void handleHistoryButton()
	{
		snsApp.showHistoryPanel();
	}
	
	private void nextTurnOrRound()
	{
		Player currentPlayer = snsApp.getCurrentPlayer();
		if(currentPlayer.getMoney() == 0 && currentPlayer.getShareIds().size() == 0) {
			currentPlayer.setRetired();
		}
		
		if((snsApp.getRound() == 12)&&(snsApp.getTurnCounter() == snsApp.getPlayers().size())) {
			snsApp.showExposingCards();
			snsApp.sellAll();
			snsApp.showGameResult();
		}
		else { 
			if(snsApp.getTurnCounter() == snsApp.getPlayers().size()) {
					snsApp.showExposingCards();
				}
				else {
					snsApp.dealtCard();
					snsApp.setBuyCompleted(false);
					snsApp.plusTurnCounter();
					snsApp.showPlayTable();
				}
		}
	}
}