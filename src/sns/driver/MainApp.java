package sns.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import sns.controller.ExposingCardsController;
import sns.controller.GameResultController;
import sns.controller.HistoryPanelController;
import sns.controller.PlayTableController;
import sns.controller.StartUpController;
import sns.controller.TransactionPanelController;
import sns.model.Card;
import sns.model.GameMod;
import sns.model.Player;
import sns.model.Shares;
import sns.model.ValidObject;
import sns.utility.HandleXML;
import sns.utility.ModMaker;
import sns.utility.RandomGenerator;
import sns.utility.ValidRestriction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class runs the game (all features)
 * 
 * @author Peter Hearne, Shane Halley, Ian Barnes, Abdullahi Shafii, Thai Kha Le
 * @version 1.0 
 */
public class MainApp extends Application {

	private GameMod gameMod;
	private Scanner input = new Scanner(System.in);
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ArrayList<Image> cardIcons = new ArrayList<Image>();
	private int fxRound;
	private int turnCounter;
	private Player currentPlayerFx;
	private String history;
	
	private boolean buyCompleted = false;
	private int currentDealtCardIndex;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Shares shares;
	private ArrayList<Card> cards = new ArrayList<Card>();
	private ArrayList<Integer> dealtCardIndexInRound = new ArrayList<Integer>();
	private ArrayList<String> mainTypes = new ArrayList<String>();
	
	public MainApp()
	{ }
	
	/**
	 * Set up the stage and show the game window
	 * @param stage object
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Stocks And Shares game");
		
		initRootLayout();
		showStartUp();
		System.out.println("done");
	}

	/**
	 * Main method called when the game is 
	 * run, let user choose which version to play
	 * @param args
	 */
	public static void main(String[] args) {
		int version = chooseVersion();
		if(version == 1) {
			MainApp app = new MainApp();
			app.display();
		}
		else {
			launch(args);
		}
	}
	
	/**
	 * Print out the available version for user (clasic and grahic)
	 * @return user's choice in integer
	 */
	public static int chooseVersion()
	{
		System.out.println("Stock And Shares game");
		System.out.println("Play in:");
		System.out.println("1) Classic version");
		System.out.println("2) Graphic version");
		System.out.print("===>");
		int version = ValidRestriction.restrictInteger(0, 3, "Don't have that choice");
		return version;
	}
	
	//Some essential getters and setters for JavaFx version
	public Player getCurrentPlayer()
	{
		return currentPlayerFx;
	}
	
	public void resetTurnCounter()
	{
		turnCounter = 1;
	}
	
	public void plusTurnCounter()
	{
		turnCounter++;
	}
	
	public boolean isBuyCompleted()
	{
		return buyCompleted;
	}
	
	public void setBuyCompleted(boolean flag)
	{
		buyCompleted = flag;
	}
	
	public void addCardIcon(Image cardIcon)
	{
		cardIcons.add(cardIcon);
	}
	
	public ArrayList<Image> getCardIcons()
	{
		return cardIcons;
	}
	
	public int getRound() {
		return fxRound;
	}
	
	public int getTurnCounter() 
	{
		return turnCounter;
	}
	
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public Shares getShares() 
	{
		return shares;
	}
	
	public ArrayList<Card> getCards() 
	{
		return cards;
	}
	
	public String getHistory()
	{
		return history;
	}
	
	public void shuffleCards()
	{
		Collections.shuffle(cards);
	}
	
	public int getDealtCardIndex()
	{
		return currentDealtCardIndex;
	}
	
	public void dealtCard()
	{
		currentDealtCardIndex = RandomGenerator.randomInt(cards.size()-1);
		dealtCardIndexInRound.add(currentDealtCardIndex);
	}
	
//<================================MAIN SETUPS=====================================>	
	
	/**
	 * Default setup for the game
	 * @param number of players
	 */
	public void gameSetup(int numPlayer)
	{
		//history = "";
		String[] defaultTypes = { "motors", "stores", "steels", "shippings" };
		for(String defaultType: defaultTypes) {
			mainTypes.add(defaultType);
		}
		fxRound = 1;
		turnCounter = 1;
		shares = new Shares(mainTypes, 28);
		while(players.size() < numPlayer) {
			players.add(new Player(players.size()+1, 80));
		}
		for (String mainType : mainTypes)	{
			for (int upValue = 2; upValue <= 4; upValue++)	{
				cards.add(new Card(mainType, "up", upValue));
			}
			for (int downValue = 2; downValue <= 4; downValue++) {
				cards.add(new Card(mainType, "down", downValue));
			}
		}
		cards.add(new Card("bull", "up", 4));
		cards.add(new Card("bear", "down", 4));
		System.out.println("Setup completed");
	}
	
	/**
	 * (For classic version) setup the game with a specific game modification
	 * @param game modification object
	 * @param number of players
	 */
	public void setupWithMod(int numPlayer)
	{
		while(players.size() < numPlayer) {
			players.add(new Player(players.size()+1, 80));
		}
		ArrayList<String> shareTypes = gameMod.getShareTypes();
		shares = new Shares(shareTypes, gameMod.getShareAmountEachType());
		mainTypes = shareTypes;
		for(String type: shareTypes) {
			for (int upValue = 2; upValue <= 4; upValue++)	{
				cards.add(new Card(type, "up", upValue));
			}
			for (int downValue = 2; downValue <= 4; downValue++) {
				cards.add(new Card(type, "down", downValue));
			}
		}
		cards.addAll(gameMod.getBonusCards());
	}
	
	/**
	 * (JavaFx version) 
	 * Pick out the current (not retired) player 
	 * for an individual turn basing on the turn counter
	 * @return list of other players (including the retired if available) 
	 */
	public ArrayList<Player> setupPlayersForTurnFx()
	{
		ArrayList<Player> otherPlayers = new ArrayList<Player>();
		for(int i = 0; i < players.size(); i++) {
			if(!players.get(i).retired() && (i == (turnCounter - 1))) {
					currentPlayerFx = players.get(i);
			}
			else {
				otherPlayers.add(players.get(i));
			}
		}
		return otherPlayers;
	}
	
	/**
	 * (JavaFx version) Setup a new round 
	 */
	public void setupForRoundFx()
	{
		Collections.rotate(players,players.size() -1);
		buyCompleted = false;
		updateShares();
		turnCounter = 1;
		dealtCardIndexInRound.clear();
		cardIcons.clear();
		fxRound++;
		shuffleCards();
		dealtCard();
	}
	
//<====================================XML PART====================================>	
	
	/**
	 * Setup a new game modification by creating 
	 * a new GameMod object (new share types, new 
	 * amount each share and new bonus cards)
	 */
	public void makeMod()
	{
		gameMod = new GameMod(ModMaker.makeShareTypes(),
				ModMaker.shareAmountEachType(), ModMaker.bonusCard());
		try {
			save();
			System.out.println("Mod saved");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load the previous game modification, put into gameMod variable
	 * @throws Exception
	 */
	public void load() throws Exception
	{
		gameMod = HandleXML.read();
		System.out.println("Mod loaded");
	}
	
	/**
	 * Save a new game modification
	 * @throws Exception
	 */
	public void save() throws Exception
	{
		HandleXML.write(gameMod);
		System.out.println("Mod saved");
	}	
	
//<================================JAVA FX PART==================================>	
	
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}
	
	/**
	 * Setter changing the main stage
	 * @param object of type Stage
	 */
	public void setScenePrimaryStage(Scene newScene)
	{
		primaryStage.setScene(newScene);
	}
	
    /**
     * Initializes the root layout (background scene)
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/sns/controller/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Show the intro screen of the game
     */
	public void showStartUp()
	{
		try
		{	
			FXMLLoader loader = new FXMLLoader(); 
			loader.setLocation(MainApp.class.getResource("/sns/controller/Startup.fxml"));
			AnchorPane startup = (AnchorPane) loader.load(); 
			rootLayout.setCenter(startup);
			StartUpController controller = loader.getController();
			controller.setApp(this); 
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Show the playing stage 
	 */
	public void showPlayTable()
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/sns/controller/PlayTable.fxml"));
			AnchorPane playing = (AnchorPane) loader.load();
			
			Scene playingScene = new Scene(playing);
			PlayTableController controller = loader.getController();
			
			controller.setApp(this);
			controller.secondInitialize();
			primaryStage.setScene(playingScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Show the transaction panel. 
	 * Usable for either when player choose to buy share or sell share  
	 * @param function (buy or sell in String) 
	 */
	public void showBuyOrSellPanel(String function)
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/sns/controller/TransactionPanel.fxml"));
			AnchorPane transaction = (AnchorPane) loader.load();
			
			TransactionPanelController controller = loader.getController();
			controller.setApp(this);
			controller.setFunction(function);
			controller.secondInitialize(function);
			
			
			Scene transactionScene = new Scene(transaction);
			Stage transactionStage = new Stage();
			controller.setStage(transactionStage);
			
			transactionStage.initModality(Modality.APPLICATION_MODAL);
			transactionStage.initOwner(primaryStage);
			transactionStage.setScene(transactionScene);
			transactionStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Show the result panel
	 */
	public void showGameResult()
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/sns/controller/GameResult.fxml"));
			AnchorPane gameResult;
			gameResult = (AnchorPane) loader.load();
			
			Scene gameResultScene = new Scene(gameResult);
			GameResultController controller = loader.getController();
			controller.setPlayers(players);
			controller.secondInitialize();
			
			primaryStage.setScene(gameResultScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pop up a window showing all dealt cards in a round 
	 */
	public void showExposingCards()
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/sns/controller/ExposingCards.fxml"));
			AnchorPane exposing = (AnchorPane) loader.load();
			
			ExposingCardsController controller = loader.getController();
			controller.setApp(this);
			controller.setDealtCards(dealtCardIndexInRound);
			controller.secondInitialize();
			
			Scene exposingScene = new Scene(exposing);
			Stage exposingStage = new Stage();
			controller.setStage(exposingStage);
			
			exposingStage.initModality(Modality.APPLICATION_MODAL);
			exposingStage.initOwner(primaryStage);
			exposingStage.setScene(exposingScene);
			exposingStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pop up a window showing history of 
	 * share indicators through game rounds
	 */
	public void showHistoryPanel()
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/sns/controller/HistoryPanel.fxml"));
			AnchorPane history = (AnchorPane) loader.load();
			
			HistoryPanelController controller = loader.getController();
			controller.setApp(this);
			controller.secondInitialize();
			
			Scene historyScene = new Scene(history);
			Stage hitoryStage = new Stage();
			controller.setStage(hitoryStage);
			
			hitoryStage.initModality(Modality.APPLICATION_MODAL);
			hitoryStage.initOwner(primaryStage);
			hitoryStage.setScene(historyScene);
			hitoryStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write the share indicator of current round to the history string
	 */
	public void writeHistory()
	{
		history += "Round " + fxRound 
				+ "\n" + shares.shareIndicator(mainTypes) 
				+ "----------------------\n";
	}
	
//<================================CONSOLE PART===================================>
	
	/**
	 * Main driver of classic version
	 */
	public void display() {
		System.out.println("How many players?");
		System.out.print("==>");
		int numPlayers = ValidRestriction.restrictInteger(1, 7, "You can only choose between 2 and 6 players");
		classicChoices(numPlayers);
		System.out.println(players.size() + " players");
		System.out.println("Number of shares: " + shares.getShares().size());
		System.out.println("Setup complete, On screen !");
		
		for(int round = 1; round <= 12; round++)//The main loop-round controller (1 to 12)
		{
			setupRoundConsole(round);
			roundDriver(round);
			printShareIndicator();	
		}
		sellAll();
	}
	
	/**
	 * Print out 3 options for classic version
	 * @param number of players
	 */
	public void classicChoices(int numPlayers)
	{
		System.out.println("Options:");
		System.out.println("1) Play default");
		System.out.println("2) Modify the game");
		System.out.println("3) Load previous modification");
		System.out.print("===>");
		int choice = ValidRestriction.restrictInteger(0, 4, "Don't have that choice");
		switch (choice) {
			case 1: gameSetup(numPlayers);break;
			case 2: makeMod(); 
					setupWithMod(numPlayers);
					break;
			case 3: try {
						load();
						setupWithMod(numPlayers);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					;break;
			default: ;break; 
		}
		
	}
	
	/**
	 * Run an individual round of the game
	 * @param round number
	 */
	public void roundDriver(int round) {	
		System.out.println("--------------------");
		System.out.println("Round " + round);
		for(Player currentPlayer: players)	{
			if(!currentPlayer.retired()) {
				System.out.println("Player " + currentPlayer.getIdentity());
				int randomCardIndex = RandomGenerator.randomInt(cards.size() - 1); 
				dealtCardIndexInRound.add(randomCardIndex);
				System.out.println(cards.get(randomCardIndex).toString());	
				boolean playerTurn = true;	
				printPlayerShares(currentPlayer);		
				buyCompleted = false;					
				while(playerTurn)						
				{
					playerTurn = !playerChoice(currentPlayer);	
				}
				if(currentPlayer.getMoney() == 0 						
						&& currentPlayer.getShareIds().size() == 0) {	
					currentPlayer.setRetired();
				}
			}
		}
		System.out.println("Okay, round " + round 		
				+ " finished, exposing cards ..."); 																
		for(int index: dealtCardIndexInRound) {												
			System.out.println(cards.get(index).toString());
		}
		updateShares();
	}
	
	/**
	 * Setup a round
	 * @param round number
	 */
	public void setupRoundConsole(int round) {
		if(round != 1) {
			Collections.rotate(players,players.size() -1);
			dealtCardIndexInRound.clear();
		}
		Collections.shuffle(cards);	
		buyCompleted = false;
	}
	
	/**
	 * This method asks player to choose an action (buy, sell or do nothing),
	 * @return boolean value indicating whether player has finished his turn
	 */
	public boolean playerChoice(Player player)
	{
		boolean finished = true;
		System.out.println("--------------------");
		System.out.println("Choices: ");
		System.out.println("1) Buy");
		System.out.println("2) Sell");
		System.out.println("3) Pass");
		int choice = input.nextInt();
		
		switch(choice) {
			case 1: if(!buyCompleted) {
						finished = consoleTransaction(player, "buy");
					}
					else {
						System.out.println("You can buy only once");
						finished = false;
					}
					break;
			case 2: finished = consoleTransaction(player, "sell");
					break;
			case 3: finished = true;
					break;
			default: System.out.println("Don't have that choice");
					break;
		}
		System.out.println("Your current balance: " + player.getMoney());
		printPlayerShares(player);
		System.out.println("--------------------");
		return finished;
	}
	
	
	/**
	 * Print out all the share types the game has 
	 */
	public void printMainTypes()
	{
		int countPrint = 0;
		System.out.println("Choose one type:");
		for(String commodityType: mainTypes) {
			System.out.println("<" + countPrint + "> " + commodityType.toString());
			countPrint++;
		}
	}
	
	/**
	 * Print the share indicator (records of shares' values)
	 */
	public void printShareIndicator() {
		System.out.println("~~~~~~Share Indicator Records~~~~~");
		System.out.print(shares.shareIndicator(mainTypes));
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		System.out.println("--------------------");
		System.out.println("Press enter to continue ...");
		input.nextLine();
		input.nextLine();
	}
	
	
	/**
	 * This prints out shares bought by player, 
	 * along with their current value
	 */
	public void printPlayerShares(Player player)
	{
		System.out.println("~~~~~~~~~Player " 
				+ player.getIdentity() + " shares~~~~~~~~~~");
		for(String commodityType: mainTypes) {
			System.out.println("| " + commodityType + "\t" 
					+ shares.getSoldShareIds(player.playerId(), commodityType).size()
					+ " (" + shares.getShareValueOnType(commodityType) + " pound each)");
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	/**
	 * Get a share type from player, if a choice does not exists, ask again
	 * @return chosen share type in String
	 */
	public String getChosenCommodityTypeFromPlayer()
	{
		String commodityTypeToBeBoughtOrSold = "";
		boolean flag = true;
		while(flag) {		
			System.out.print("===>");
			int typeNumber = input.nextInt();
			if((typeNumber < mainTypes.size())&&(typeNumber >= 0)) {
				commodityTypeToBeBoughtOrSold = mainTypes.get(typeNumber);
				flag = false;
			}
			else {
				System.out.println("Don't have that, try again ...");
			}
		}
		return commodityTypeToBeBoughtOrSold;
	}
	
	/**
	 * Transaction stage, usable for both buying and selling
	 * @param current player
	 * @param choice ("buy" or "sell")
	 * @return boolean value indicating whether player has finished
	 */
	public boolean consoleTransaction(Player player, String choice) {
		printMainTypes();
		String shareType = getChosenCommodityTypeFromPlayer();
		System.out.println("How many " + shareType + "?");
		int shareNum = restrictNumber(input.nextInt());
		int sharesLimit = 0;
		if(choice.equals("buy")){
			sharesLimit = shares.getAvailableSharesAmount(shareType);
		}
		else {
			sharesLimit = shares.getAmountOfSharesSoldToPlayer(shareType, player.playerId());
		}
		if (checkValid(shareNum, player.getMoney(),
				shares.getShareValueOnType(shareType), sharesLimit, choice, shareType).hasNoError()) {
			if((choice.equals("buy"))&&(confirmFinished("Confirm purchasing"))) {
				buyShares(player,shareType, shareNum);
			}
			else if(choice.equals("sell")) {
				sellShares(player,shareType, shareNum);
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		return confirmFinished("Finished?");
	}
	
	/**
	 * Ask for player's confirmation
	 * @param message to print (detail of the confirmation)
	 * @return player's decision of type boolean
	 */
	public boolean confirmFinished(String message)
	{
		System.out.println(message + " (y/n)");
		System.out.print("===>");
		char answer = input.next().toLowerCase().charAt(0);
		return (answer == 'y');
	}
	
	public int restrictNumber(int num) {
		if (num <= 5)  {
			return num;
		}
		for (int i = 5; (i + 5) <= 20; i += 5) {
			if (num == (i+5)) {
				return num;
			}
			else if ((num > i)&&(num < i+5)) {
				num = i + 5;
				System.out.println("If more than 5 shares"
						+ ", \nyou can only buy 10, 15 or "
						+ "20 shares \n ---> amount changed to " + num);
			}
		}
		return num;
	}
	
//<=================METHODS FOR BOTH JAVAFX AND CONSOLE VERSION=====================>	

	/**
	 * This method creates an object containning a boolean indicating whether data is valid,
	 *  if not, the String variable will contain an error message. 
	 *  Usable for all version (ie. Classic version only needs a boolean while JavaFx needs both 
	 *  a boolean and a message) and all functions (either buying or selling)
	 * @param number (eg. amount of shares to buy/sell, ...)
	 * @param current money of player
	 * @param value of the type of share
	 * @param available share for player to buy or available shares that player has
	 * @param "buy" or "sell"
	 * @param type of the share
	 * @return an object of type ValidObject
	 */
	public ValidObject checkValid(int num, 
			int currentMoney, int value, int sharesLimit, String function, String shareType) {
		ValidObject valid = new ValidObject(true,"");
		if(num < 0) {
			System.out.println("Error negative amount");
			valid.setNoError(false);
			valid.addErrorMessage("Error negative amount");
		}
		if (num > sharesLimit) {
			System.out.println("There are not enough " + shareType + " to " + function);
			valid.setNoError(false);
			valid.addErrorMessage("There are not enough " + shareType + " to " + function);
		}
		if((function.equals("buy"))&&(currentMoney < num*value)) {
			System.out.println("You don't have enough money");
			valid.setNoError(false);
			valid.addErrorMessage("You don't have enough money");
		}
		return valid;
	}
	
	/**
	 * Purchasing mechanism, using for all playing versions
	 * @param current player
	 * @param share type
	 * @param amount
	 */
	public void buyShares(Player currentPlayer, String type, int amount)
	{
		ArrayList<Long> availableShareIds = shares.getAvailableShareIds(type);
		int moneyToPay = shares.getShareValueOnType(type) * amount;
		for(int i = 0; i < amount; i++) {
			currentPlayer.addShareId(availableShareIds.get(i));
			shares.giveShareToPlayer(availableShareIds.get(i), currentPlayer.playerId());
		}
		currentPlayer.setMoney(currentPlayer.getMoney() - moneyToPay);
		System.out.println("Purchase completed");
		buyCompleted = true;	
	}
	
	/**
	 * Selling mechanism, using for all playing versions
	 * @param current player
	 * @param share type
	 * @param amount
	 */
	public void sellShares(Player currentPlayer,String shareType, int amount)
	{
		ArrayList<Long> shareIdsBoughtByPlayer 
		= shares.getSoldShareIds(currentPlayer.playerId(), shareType);
		int moneyToReceive = 0;
		for(int i = 0; i < amount; i++) {
			currentPlayer.removeShareId(shareIdsBoughtByPlayer.get(i));
			shares.takeShareFromPlayer(shareIdsBoughtByPlayer.get(i));
			moneyToReceive += shares.getShareValueOnType(shareType);
		}
		currentPlayer.setMoney(currentPlayer.getMoney() + moneyToReceive);
		System.out.println("Sell completed");
	}
	
	/**
	 * Update the shares values using all the dealt cards in round
	 */
	public void updateShares()
	{
		for(Integer cardIndex: dealtCardIndexInRound) {
			Card card = cards.get(cardIndex);
			String cardType = card.getCardType();
			String cardFunction = card.getCardFunction();
			int cardValue = card.getCardValue();
			
			if(cardFunction.equals("down")) {
				cardValue = -cardValue;
			}
			
			for(String shareType: mainTypes) {	
				if(gameMod == null) {
					if(shareType.equals(cardType)
							||cardType.equals("bear")||cardType.equals("bull")) {
						shares.updateOneTypeOfShare(shareType,cardValue);
					}
				}
				else {
					if(shareType.equals(cardType)) {
						shares.updateOneTypeOfShare(shareType,cardValue);
					}
					for(Card bonusCard: gameMod.getBonusCards()) {
						if(bonusCard.getCardType().equals(cardType)) {
							shares.updateOneTypeOfShare(shareType,bonusCard.getCardValue());
						}
					}
				}	
			}
		}
	}
	
	/**
	 * Sell all players' shares with the value 
	 * recorded on the latest share indicator
	 */
	public void sellAll()
	{
		for(Player player: players) {
			if(!player.retired()) {
				int moneyToReceive = 0;
				ArrayList<Long> shareIds = new ArrayList<Long>();
				shareIds.addAll(player.getShareIds());
				for(Long shareId: shareIds) {
					player.removeShareId(shareId);
					shares.takeShareFromPlayer(shareId);
					moneyToReceive += shares.getShareValueOnId(shareId);
				}
				player.setMoney(player.getMoney() + moneyToReceive);
			}
		}
	}
}