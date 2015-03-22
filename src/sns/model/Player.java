package sns.model;
import java.util.*;

import sns.utility.RandomGenerator;

public class Player {

	private Long id;
	private int identity;	
	private int money;
	private ArrayList<Long> shareIds;
	private boolean retired;
	
	public Player(int identity, int money)
	{
		this.identity = identity;
		this.money = money;
		shareIds = new ArrayList<Long>();
		id = RandomGenerator.randomId();
		retired = false;
	}
	
	public Long playerId()
	{
		return id;
	}
	
	public ArrayList<Long> getShareIds()
	{
		return shareIds;
	}
	
	public void addShareId(Long id)
	{
		shareIds.add(id);
	}
	
	public void updateShareIds(ArrayList<Long> nshareIds)
	{
		shareIds = nshareIds;
	}
	
	public int getIdentity()
	{
		return identity;
	}
	
	public int getMoney()
	{
		return money;
	}
	
	public void setMoney(int nmoney)
	{
		money = nmoney;
	}
	
	public void addShareIds(ArrayList<Long> shareIds)
	{
		shareIds.addAll(shareIds);
	}
	
	public void removeShareId(Long shareId)
	{
		shareIds.remove(shareId);
	}
	
	public boolean retired()
	{
		return retired;
	}
	
	public void setRetired()
	{
		retired = true;
	}
	
	public String toString()
	{
		return ("Player " + identity + ": " + money + " pound \n");
	}
}
