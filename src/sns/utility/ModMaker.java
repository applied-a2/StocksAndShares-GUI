package sns.utility;

import java.util.ArrayList;
import java.util.Scanner;

import sns.model.Card;

/**
 * Each method of this class makes one part of a game 
 * modification version
 * @author Thai Kha Le
 */
public class ModMaker {

	private static Scanner input = new Scanner(System.in);
	private static ArrayList<String> existedTypes = new ArrayList<String>();
	
	public static ArrayList<String> makeShareTypes()
	{
		ArrayList<String> shareTypes = new ArrayList<String>();
		System.out.println("How many type of shares?");
		System.out.print("===>");
		int numTypes = ValidRestriction.restrictInteger(3,10,"Error, number must "
				+ "greater than 3 and less than 10");
		for(int i = 0; i < numTypes; i++) {
			boolean asking = true;
			while(asking) {
				System.out.print("Type " + (i + 1) + ": ");
				String shareType = ValidRestriction.restrictString(5,8);
				if(existedTypes.contains(shareType) == false) {
					shareTypes.add(shareType);
					existedTypes.add(shareType);
					System.out.println(shareType + " added");
					asking = false;
				}
				else {
					System.out.println("Error: " + shareType + " used");
				}
			}
		}
		return shareTypes;
	}
	
	public static int shareAmountEachType()
	{
		System.out.println("How many shares per type?");
		System.out.print("===>");
		int num = ValidRestriction.restrictInteger(8,61, "Error, number must "
				+ "greater than 8 and less than 61");
		return num;
	}
	
	public static ArrayList<Card> bonusCard()
	{
		ArrayList<Card> bonusCards = new ArrayList<Card>();
		System.out.println("How many bonus cards?");
		System.out.print("===>");
		int num = input.nextInt();
		for(int i = 0; i < num; i++) {
			String name = "";
			boolean asking = true;
			while(asking) {
				System.out.print("Card " + i + " name: ");
				name = ValidRestriction.restrictString(5,8);
				if(existedTypes.contains(name) == false) {
					existedTypes.add(name);
					asking = false;
				}
				else {
					System.out.println("Error: " + name + " used");
				}
			}
			System.out.println("Card " + i + " function:");
			System.out.println("1) Up");
			System.out.println("2) Down");
			System.out.print("==>");
			int functionChoice = ValidRestriction.restrictInteger(0,3,"Don't have that choice");
			String function = "";
			if(functionChoice == 1) {
				function = "up";
			}
			else {
				function = "down";
			}
			System.out.print("Card " + i + " value: ");
			int value = ValidRestriction.restrictInteger(0,10,"Error, number "
					+ "must be greater than 0 and less than 10");
			bonusCards.add(new Card(name, function, value));
		}
		return bonusCards;
	}
}
