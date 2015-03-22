package sns.controller;

import java.util.ArrayList;

import org.controlsfx.dialog.Dialogs;

import sns.driver.MainApp;
import sns.model.Commodity;
import sns.model.Player;
import sns.model.Shares;
import sns.model.ValidObject;
import sns.utility.ValidRestriction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TransactionPanelController {

	private MainApp snsApp;
	//private Player currentPlayer;
	
	private Stage transactionStage;
	private String function;
	private String[] mainTypes = { "motors", "stores", "shippings",  "steels"};
	
	@FXML
	private Label headerLabel;
	@FXML
	private Label motorsLabel;
	@FXML
	private Label storesLabel;
	@FXML
	private Label shippingsLabel;
	@FXML
	private Label steelsLabel;
	
	//private boolean buyCompleted = false;
	
	public void setApp(MainApp app)
	{
		snsApp = app;
	}
	
	@FXML
	private ChoiceBox motors;
	@FXML
	private ChoiceBox stores;
	@FXML
	private ChoiceBox shippings;
	@FXML
	private ChoiceBox steels;
	
	//private ChoiceBox[] choiceBoxes = {motors, stores, shippings, steels};
	
	@FXML
	private Button buyOrSellButton = new Button();
	
	@FXML
	private void initialize()
	{ }
	
	public void setFunction(String function)
	{
		this.function = function;
	}
	
	public void setStage(Stage stage)
	{
		transactionStage = stage;
	}
	
	@SuppressWarnings("deprecation")
	@FXML
	private void handleBuyOrSellButton()
	{
		String emptyError = checkEmpty();
		
		if(emptyError == "") {
			int motorsChoice = (int) motors.getSelectionModel().getSelectedItem();
			int storesChoice = (int) stores.getSelectionModel().getSelectedItem();
			int shippingsChoice = (int) shippings.getSelectionModel().getSelectedItem();
			int steelsChoice = (int) steels.getSelectionModel().getSelectedItem();
			
			Player currentPlayer = snsApp.getCurrentPlayer();
			int[] choices = {motorsChoice,storesChoice,shippingsChoice,steelsChoice};
			ArrayList<ValidObject> valids = new ArrayList<ValidObject>();
			for(int i = 0; i < choices.length; i++) {
				if(function.equals("buy")) {
					valids.add(ValidRestriction.checkValid(choices[i], currentPlayer.getMoney(), 
							snsApp.getShares().getShareValueOnType(mainTypes[i]), 
							snsApp.getShares().getAvailableSharesAmount(mainTypes[i]) ,function, mainTypes[i]));
				}
				else {
					valids.add(ValidRestriction.checkValid(choices[i], currentPlayer.getMoney(), 
							snsApp.getShares().getShareValueOnType(mainTypes[i]), 
							snsApp.getShares().getAmountOfSharesSoldToPlayer(mainTypes[i], currentPlayer.playerId()) ,function, mainTypes[i]));
				}
			}
			
			boolean noError = true;
			String errorMessage = ""; 
			for(ValidObject valid: valids) {
				if(!valid.hasNoError()) {
					noError = false;
				}
				errorMessage += valid.getErrorMessage();
			}
			if((errorMessage.equals(""))&&(noError)) {
				if(function.equals("buy")) {
					giveSharesToPlayer(choices, currentPlayer);
				}
				else {
					takeSharesFromPlayer(choices, currentPlayer);
				}
				transactionStage.close();
				snsApp.showPlayTable();
			}
			else {
				printError(errorMessage);
			}
		}
		else {
			System.out.println(emptyError);
			printError(emptyError);
		}
	}
	
	private void printError(String error)
	{
		Dialogs.create()
		.title("Error")
		.masthead("")
		.message(error)
		.showWarning();
	}
	
	private String checkEmpty()
	{
		String error = "";
		ChoiceBox[] choiceBoxes = {motors, stores, shippings, steels};
		for(ChoiceBox choiceBox: choiceBoxes) {
			if(choiceBox.getSelectionModel().getSelectedItem() == null) {
				error = "Please choose all boxes";
			}
		}
		return error;
	}
	
	private void giveSharesToPlayer(int[] choices, Player currentPlayer)
	{
		for(int i = 0; i < choices.length;i++) {
			if(choices[i] != 0) {
				snsApp.buyShares(currentPlayer, mainTypes[i], choices[i]);
			}
		}
	}
	
	private void takeSharesFromPlayer(int[] choices, Player currentPlayer)
	{
		for(int i = 0; i < choices.length;i++) {
			if(choices[i] != 0) {
				snsApp.sellShares(currentPlayer, mainTypes[i], choices[i]);
			}
		}
	}
	
	public void secondInitialize(String function)
	{
		int availMotors = 0;
		int availStores = 0;
		int availShippings = 0;
		int availSteels = 0;
		
		if(function.equals("buy")) {
			buyOrSellButton.setText("Buy");
			headerLabel.setText("Enter amount of shares that you want to buy");
			availMotors = snsApp.getShares().getAvailableSharesAmount("motors");
			availStores = snsApp.getShares().getAvailableSharesAmount("stores");
			availShippings = snsApp.getShares().getAvailableSharesAmount("shippings");
			availSteels = snsApp.getShares().getAvailableSharesAmount("steels");
		}
		else {
			buyOrSellButton.setText("Sell");
			headerLabel.setText("Enter amount of shares that you want to sell");
			Shares shares = snsApp.getShares();
			availMotors = shares.getAmountOfSharesSoldToPlayer("motors", snsApp.getCurrentPlayer().playerId());
			availStores = shares.getAmountOfSharesSoldToPlayer("stores", snsApp.getCurrentPlayer().playerId());;
			availShippings = shares.getAmountOfSharesSoldToPlayer("shippings", snsApp.getCurrentPlayer().playerId());;
			availSteels = shares.getAmountOfSharesSoldToPlayer("steels", snsApp.getCurrentPlayer().playerId());;
		}
		
		motorsLabel.setText("(" + availMotors + " available)");
		storesLabel.setText("(" + availStores + " available)");
		shippingsLabel.setText("(" + availShippings + " available)");
		steelsLabel.setText("(" + availSteels + " available)");
		
		//Have to place choiceBoxes here because when the controller is 
		//initialized, all choice boxes are empty(avoid null pointer exception)
		ChoiceBox[] choiceBoxes = {motors, stores, shippings, steels};
		int[] n = {0,1,2,3,4,5,10,15,20};
		for(ChoiceBox choiceBox: choiceBoxes) {
			for(int i = 0; i < n.length; i++){
				choiceBox.getItems().add(n[i]);
			}
		}
	}
}
