package sns.model;

public class ValidObject {
	
	private boolean noError;
	private String errorMessage;
	
	public ValidObject(boolean noError, String errorMessage)
	{
		this.noError = noError;
		this.errorMessage = errorMessage;
	}
	
	public void setNoError(boolean flag)
	{
		noError = flag;
	}
	
	public boolean hasNoError()
	{
		return noError;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public void addErrorMessage(String nMessage)
	{
		errorMessage += "\n" + nMessage;
	}
}
