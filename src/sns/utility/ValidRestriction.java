package sns.utility;

import java.util.Scanner;

import sns.model.ValidObject;

/**
 * This class provides methods making sure data is valid
 * @author Thai Kha Le
 */
public class ValidRestriction {

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
			for(int i = 0; i < 4; i++) {
				line += RandomGenerator.randomInt(10);
			}
		}
		else if(line.length() > limit2) {
			line = line.substring(0, limit2);
		}
		return line;
	}
}
