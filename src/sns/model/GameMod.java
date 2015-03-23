package sns.model;

import java.util.ArrayList;

public class GameMod {

	private ArrayList<String> shareTypes;
	private int sharePerType;
	private ArrayList<Card> bonusCards;
	
	public GameMod()
	{}
	
	public GameMod(ArrayList<String> shareTypes, int sharePerType, ArrayList<Card> bonusCards) 
	{
		this.shareTypes = shareTypes;
		this.sharePerType = sharePerType;
		this.bonusCards = bonusCards;
	}
	
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
