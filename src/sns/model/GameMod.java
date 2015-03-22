package sns.model;

import java.util.ArrayList;

public class GameMod {

	//private int numRound;
	private ArrayList<String> shareTypes;
	private int sharePerType;
	private ArrayList<Card> bonusCards;
	
	public GameMod()
	{}
	
	public GameMod(ArrayList<String> shareTypes, int sharePerType, ArrayList<Card> bonusCards) 
	{
		//this.numRound = numRound;
		this.shareTypes = shareTypes;
		this.sharePerType = sharePerType;
		this.bonusCards = bonusCards;
	}
	
//	public int getNumRound()
//	{
//		return numRound;
//	}
	
	public ArrayList<String> getShareTypes()
	{
		return shareTypes;
	}
	
	public int getShareAmountEachType()
	{
		return sharePerType;
	}
	
	public ArrayList<Card> getBonusCards()
	{
		return bonusCards;
	}
}
