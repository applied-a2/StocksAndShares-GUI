package sns.model;

public class Card {

	private String type;
	private String function;
	private int value;
	
	public Card()
	{ }
	
	public Card(String type,String function, int value)
	{
		this.type = type;
		this.function = function;
		this.value = value;
	}
	
	public String getCardType()
	{
		return type;
	}
	
	public String getCardFunction()
	{
		return function;
	}
	
	public int getCardValue()
	{
		return value;
	}
	
	public void updateCardType(String type)
	{
		this.type = type;
	}
	
	public void updateCardValue(int value)
	{
		this.value = value;
	}
	
	public String toString()
	{
		return (type + " card" + ", " + function + " " + value + " pound");
	}
}
