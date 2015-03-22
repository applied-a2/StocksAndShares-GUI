package sns.model;

public class Commodity {

	private Long id;
	private Long ownerId;
	private String commodityType;
	private int value;
	
	public Commodity(Long id, String commodityType, int value)
	{
		this.id = id;
		this.commodityType = commodityType;
		this.value = value;
		ownerId = null;
	}
	
	public void setOwnerId(Long ownerId)
	{
		this.ownerId = ownerId;
	}
	
	public Long getOwnerId()
	{
		return ownerId;
	}
	
	public Long id()
	{
		return id;
	}
	
	public String getCommodityType()
	{
		return commodityType;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void updateValue(int nvalue)
	{
		if(((value+nvalue) >=0)&&(((value+nvalue) <= 20)))
		{
			value += nvalue;
		}
	}
	
	public void updateCommodityType(String commodityType)
	{
		this.commodityType = commodityType;
	}
	
	public String toString()
	{
		return (commodityType + "\t" + value + " pound");
	}
}
