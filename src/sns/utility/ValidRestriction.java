package sns.utility;

import java.util.Scanner;

import sns.model.ValidObject;

public class ValidRestriction {

	public static int restrictNumber(int num) {
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
	
	public static ValidObject checkValid(int num, 
			int currentMoney, int value, int sharesLimit, String function, String shareType) {
		ValidObject valid = new ValidObject(true,"");
		if(num < 0) {
			System.out.println("Error negative amount");
			valid.setNoError(false);
			valid.addErrorMessage("Error negative amount");
		}
		if (num > sharesLimit) {
			System.out.println("There is not enough " + shareType + " to " + function);
			valid.setNoError(false);
			valid.addErrorMessage("There is not enough " + shareType + " to " + function);
		}
		if((function.equals("buy"))&&(currentMoney < num*value)) {
			System.out.println("You don't have enough money");
			valid.setNoError(false);
			valid.addErrorMessage("You don't have enough money");
		}
		return valid;
	}
	

	public static int restrictInteger(int limit1, int limit2,String errorMessage)
	{
		Scanner input = new Scanner(System.in);
		int validNum = 999;
		boolean asking = true;
		while(asking) {
			int num = input.nextInt();
			if((num > limit1)&&(num < limit2)) {
				validNum = num;
				asking = false;
			}
			else {
				System.out.print(errorMessage + "... try again \n===>");
			}
		}
		return validNum;
	}
	
	//scanner is kept as local varible because its bug is 
	//so hard to control in this case 
	public static String restrictString(int limit1, int limit2)
	{
		Scanner input = new Scanner(System.in);
		String line = input.nextLine();
		if (line.length() < limit1) {
			line += "----";
		}
		else if(line.length() > limit2) {
			line = line.substring(0, limit2);
		}
		return line;
	}
}
