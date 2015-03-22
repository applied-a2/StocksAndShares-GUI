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
import sns.utility.HandleXML;
import sns.utility.ModMaker;
import sns.utility.RandomGenerator;
import sns.utility.ValidRestriction;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Stocks And Shares game");
		
		initRootLayout();
		showStartUp();
		System.out.println("done");
	}

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
	public ArrayList<Card> getCards() {
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
	
	public void gameSetup(int numPlayer)
	{
		history = "";
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
	
	public void setupWithMod(GameMod mod, int numPlayer)
	{
		while(players.size() < numPlayer) {
			players.add(new Player(players.size()+1, 80));
		}
		ArrayList<String> shareTypes = mod.getShareTypes();
		shares = new Shares(shareTypes, mod.getShareAmountEachType());
		mainTypes = shareTypes;
		for(String type: shareTypes) {
			for (int upValue = 2; upValue <= 4; upValue++)	{
				cards.add(new Card(type, "up", upValue));
			}
			for (int downValue = 2; downValue <= 4; downValue++) {
				cards.add(new Card(type, "down", downValue));
			}
		}
		cards.addAll(mod.getBonusCards());
	}
	
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
	
	public void load() throws Exception
	{
		gameMod = HandleXML.read();
		System.out.println("Mod loaded");
	}
	
	public void save() throws Exception
	{
		HandleXML.write(gameMod);
		System.out.println("Mod saved");
	}	
	
//<================================JAVA FX PART==================================>	
	
	public void setScenePrimaryStage(Scene newScene)
	{
		primaryStage.setScene(newScene);
	}
	
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/sns/controller/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void showStartUp()
	{
		try
		{	
			FXMLLoader loader = new FXMLLoader(); //create a FXMLLoader
			loader.setLocation(MainApp.class.getResource("/sns/controller/Startup.fxml"));
			//location error: open up the app source folder, try all path, with and without initial slash 
			AnchorPane startup = (AnchorPane) loader.load(); //get the anchor pane from the loader
			
			rootLayout.setCenter(startup);
			
			StartUpController controller = loader.getController(); //get the controller from the loader
			controller.setApp(this); 
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
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
	
	public boolean showBuyOrSellPanel(String function)
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
		
		return false;
	}
	
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
	
	public void writeHistory()
	{
		history += "Round " + fxRound 
				+ "\n" + shares.shareIndicator(mainTypes) 
				+ "----------------------\n";
	}
	
//<================================CONSOLE PART===================================>
	
	/*
	 * Main driver of classic version
	 */
	public void display() {
		System.out.println("How many players?");
		System.out.print("==>");
		int numPlayers = input.nextInt();
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
	
	//case 2 does not need break since mod needs to be loaded anyway
	public void classicChoices(int numPlayers)
	{
		System.out.println("Options:");
		System.out.println("1) Play default");
		System.out.println("2) Modify the game");
		System.out.println("3) Load previous modification");
		System.out.print("===>");
		int choice = input.nextInt();
		switch (choice) {
			case 1: gameSetup(numPlayers);break;
			case 2: makeMod(); 
					setupWithMod(gameMod, numPlayers);
					break;
			case 3: try {
						load();
						setupWithMod(gameMod, numPlayers);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					;break;
			default: ;break; 
		}
		
	}
	
	/*
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
	
	public void setupRoundConsole(int round) {
		if(round != 1) {
			Collections.rotate(players,players.size() -1);
			dealtCardIndexInRound.clear();
		}
		Collections.shuffle(cards);	
		buyCompleted = false;
	}
	
	/*
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
	
	
	/*
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
	
	/*
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
	
	
	/*
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
	
	public boolean consoleTransaction(Player player, String choice) {
		printMainTypes();
		String shareType = getChosenCommodityTypeFromPlayer();
		System.out.println("How many " + shareType + "?");
		int shareNum = ValidRestriction.restrictNumber(input.nextInt());
		int sharesLimit = 0;
		if(choice.equals("buy")){
			sharesLimit = shares.getAvailableSharesAmount(shareType);
		}
		else {
			sharesLimit = shares.getAmountOfSharesSoldToPlayer(shareType, player.playerId());
		}
		if (ValidRestriction.checkValid(shareNum, player.getMoney(),
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
	
	/*
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
	
//<=================METHODS FOR BOTH JAVAFX AND CONSOLE VERSION=====================>	

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
	
	/*
	 * Update the shares values using all the dealt card
	 * in round
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
				if(shareType.equals(cardType)
						||cardType.equals("bear")||cardType.equals("bull")) {
					shares.updateOneTypeOfShare(shareType,cardValue);
				}
			}
		}
	}
	
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
