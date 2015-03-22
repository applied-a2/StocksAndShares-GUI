package sns.model;
import java.util.*;

import sns.utility.RandomGenerator;

public class Shares {

	private ArrayList<Commodity> shares;
	private Random randomGenerator = new Random();
	
	public Shares(ArrayList<String> shareTypes, int amount)
	{
		shares = new ArrayList<Commodity>();
		for(String type: shareTypes)
		{
			for(int i = 0; i < amount; i++)
			{
				shares.add(new Commodity(RandomGenerator.randomId(), type, 10));
			}	
		}
	}
	
	public String shareIndicator(ArrayList<String> mainTypes)
	{
		String indicator = "";
		for(String type: mainTypes)
		{
			indicator += "| " + type + "\t" + getShareValueOnType(type) + " pound \n";
		}
		return indicator;
	}
	
	public String getShareType(Long shareId)
	{
		String type = "";
		for(Commodity commodity: shares)
		{
			if(commodity.id().equals(shareId))
			{
				type = commodity.getCommodityType();
			}
		}
		return type;
	}
	
	public ArrayList<Commodity> getShares()
	{
		return shares;
	}
	
	public void giveShareToPlayer(Long shareId, Long ownerId)
	{
		for(Commodity share: shares)
		{
			if(share.id().equals(shareId))
			{
				share.setOwnerId(ownerId);
			}
		}
	}
	
	public void takeShareFromPlayer(Long shareId)
	{
		for(Commodity share: shares)
		{
			if(share.id().equals(shareId))
			{
				share.setOwnerId(null);
			}
		}
	}
	
	public ArrayList<Long> getSoldShareIds(Long playerId, String type)
	{
		ArrayList<Long> soldShareIds = new ArrayList<Long>();
		for(Commodity share: shares)
		{
			if((share.getOwnerId() == playerId)&&share.getCommodityType().equals(type))
			{
				soldShareIds.add(share.id());
			}
		}
		return soldShareIds;
	}
	
	public ArrayList<Long> getAvailableShareIds(String type)
	{
		ArrayList<Long> availShareIds = new ArrayList<Long>();
		for(Commodity share: shares)
		{
			if((share.getOwnerId() == null)&&share.getCommodityType().equals(type))
			{
				availShareIds.add(share.id());
			}
		}
		return availShareIds;
	}
	
	public int getShareValueOnId(Long id)
	{
		int value = 0;
		for(Commodity share: shares)
		{
			if(share.id().equals(id))
			{
				value = share.getValue();
			}
		}
		return value;
	}
	
	public int getShareValueOnType(String type)
	{
		int value = 0;
		for(Commodity share: shares)
		{
			if(share.getCommodityType().equals(type))
			{
				value = share.getValue();
			}
		}
		return value;
	}
	
	public void updateOneTypeOfShare(String type, int value)
	{
		for(Commodity share: shares)
		{
			if(share.getCommodityType().equals(type))
			{
				share.updateValue(value);
			}
		}
	}
	
	public int getAmountOfSharesSoldToPlayer(String type, Long playerId)
	{
		int amount = 0;
		for(Commodity share: shares)
		{
			if(share.getOwnerId() == null) {
				
			}
			else if((share.getCommodityType().equals(type))&&(share.getOwnerId().equals(playerId)))
			{
				amount++;
			}
		}
		return amount;
	}
	
	public int getAvailableSharesAmount(String type)
	{
		int amount = 0;
		for(Commodity share: shares)
		{
			if(share.getCommodityType().equals(type) && (share.getOwnerId() == null))
			{
				amount++;
			}
		}
		return amount;
	}
}
