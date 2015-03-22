package sns.controller;

import java.util.ArrayList;
import java.util.Collections;

import sns.driver.MainApp;
import sns.model.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ExposingCardsController {
	
	private MainApp snsApp;
	private ArrayList<Integer> dealtCardIndexInRound;
	private Stage exposingCardsStage; 
	
	@FXML
	private Rectangle firstCardRec;
	@FXML
	private ImageView firstCardIcon;
	@FXML
	private Label firstCardFunction;
	@FXML
	private Label firstCardValue;
	@FXML
	private Rectangle secondCardRec;
	@FXML
	private ImageView secondCardIcon;
	@FXML
	private Label secondCardFunction;
	@FXML
	private Label secondCardValue;
	@FXML
	private Rectangle thirdCardRec;
	@FXML
	private ImageView thirdCardIcon;
	@FXML
	private Label thirdCardFunction;
	@FXML
	private Label thirdCardValue;
	@FXML
	private Rectangle fourthCardRec;
	@FXML
	private ImageView fourthCardIcon;
	@FXML
	private Label fourthCardFunction;
	@FXML
	private Label fourthCardValue;
	
	public void ExposingCardsController()
	{}
	
	@FXML
	private void initialize()
	{
		firstCardRec.setVisible(false);
		firstCardIcon.setVisible(false);
		firstCardFunction.setVisible(false);
		firstCardValue.setVisible(false);
		
		secondCardRec.setVisible(false);
		secondCardIcon.setVisible(false);
		secondCardFunction.setVisible(false);
		secondCardValue.setVisible(false);
		
		thirdCardRec.setVisible(false);
		thirdCardIcon.setVisible(false);
		thirdCardFunction.setVisible(false);
		thirdCardValue.setVisible(false);
		
		fourthCardRec.setVisible(false);
		fourthCardIcon.setVisible(false);
		fourthCardFunction.setVisible(false);
		fourthCardValue.setVisible(false);
	}
	
	public void setStage(Stage stage)
	{
		exposingCardsStage = stage;
	}
	
	public void setApp(MainApp app)
	{
		snsApp = app;
	}
	
	public void setDealtCards(ArrayList<Integer> indices)
	{
		dealtCardIndexInRound = indices;
	}
	
	public void secondInitialize()
	{
		Rectangle[] cardRecs = {firstCardRec, secondCardRec, thirdCardRec, fourthCardRec};
		ImageView[] cardIcons = {firstCardIcon, secondCardIcon, thirdCardIcon, fourthCardIcon};
		Label[] cardFunctions = {firstCardFunction, secondCardFunction, thirdCardFunction, fourthCardFunction};
		Label[] cardValues = {firstCardValue, secondCardValue, thirdCardValue, fourthCardValue};
		
		for(int i = 0; i < dealtCardIndexInRound.size(); i++)
		{
			cardRecs[i].setVisible(true);
			cardIcons[i].setImage(snsApp.getCardIcons().get(i));
			cardIcons[i].setVisible(true);
			
			cardFunctions[i].setVisible(true);
			cardFunctions[i].setText(snsApp.getCards().get(dealtCardIndexInRound.get(i)).getCardFunction());
			
			cardValues[i].setVisible(true);
			cardValues[i].setText(snsApp.getCards().get(dealtCardIndexInRound.get(i)).getCardValue() + ".00");
		}
	}
	
	@FXML
	private void handleCloseButton()
	{
		snsApp.writeHistory();
		snsApp.setupForRoundFx();
		exposingCardsStage.close();
		snsApp.showPlayTable();
	}
}
