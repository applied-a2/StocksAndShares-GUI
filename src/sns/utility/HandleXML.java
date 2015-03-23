package sns.utility;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.ArrayList;

import sns.model.Card;
import sns.model.GameMod;

/**
 * This class handle XML encoding/decoding, typically for modifying game data.
 *  XML file cannot save a complete object so it needs to be made to save 
 *  every single variable in the object
 * @author Thai Kha Le
 */
public class HandleXML {
	
	public static void write(GameMod gameMod) throws Exception
	{
		ArrayList<String> shareTypes = gameMod.getShareTypes();
		int sharePerType = gameMod.getShareAmountEachType();
		ArrayList<Card> bonusCards = gameMod.getBonusCards();
		
		XMLEncoder bonusCardsAmountEncoder = 
				new XMLEncoder(
						new BufferedOutputStream(
								new FileOutputStream("bonusCardsAmount.xml")));
		bonusCardsAmountEncoder.writeObject(bonusCards.size());
		bonusCardsAmountEncoder.close();
		
		
		for(int i = 0; i < bonusCards.size(); i++) {
			String cardType = bonusCards.get(i).getCardType();
			String cardFunction = bonusCards.get(i).getCardFunction();
			int cardValue = bonusCards.get(i).getCardValue();
			
			XMLEncoder cardTypeEncoder = 
					new XMLEncoder(
							new BufferedOutputStream(
									new FileOutputStream(i + "tp.xml")));
			cardTypeEncoder.writeObject(cardType);
			cardTypeEncoder.close();
			
			XMLEncoder cardFunctionEncoder = 
					new XMLEncoder(
							new BufferedOutputStream(
									new FileOutputStream(i + "ft.xml")));
			cardFunctionEncoder.writeObject(cardFunction);
			cardFunctionEncoder.close();
			
			XMLEncoder cardValueEncoder = 
					new XMLEncoder(
							new BufferedOutputStream(
									new FileOutputStream(i + "vl.xml")));
			cardValueEncoder.writeObject(cardValue);
			cardValueEncoder.close();
		}
		
		
		XMLEncoder shareTypesEncoder = 
				new XMLEncoder(
						new BufferedOutputStream(
								new FileOutputStream("shareTypes.xml")));
		shareTypesEncoder.writeObject(shareTypes);
		shareTypesEncoder.close();
		
		XMLEncoder sharePerTypeEncoder = 
				new XMLEncoder(
						new BufferedOutputStream(
								new FileOutputStream("sharePerType.xml")));
		sharePerTypeEncoder.writeObject(sharePerType);
		sharePerTypeEncoder.close();
			
	}
	
	public static GameMod read() throws Exception
	{
		XMLDecoder shareTypesDecoder = 
				new XMLDecoder(
						new BufferedInputStream(
								new FileInputStream("shareTypes.xml")));
		ArrayList<String> shareTypes = (ArrayList<String>) shareTypesDecoder.readObject();
		shareTypesDecoder.close();
		
		XMLDecoder sharePerTypeDecoder = 
				new XMLDecoder(
						new BufferedInputStream(
								new FileInputStream("sharePerType.xml")));
		int sharePerType = (int) sharePerTypeDecoder.readObject();
		shareTypesDecoder.close();
		
		XMLDecoder bonusCardsAmountDecoder = 
				new XMLDecoder(
						new BufferedInputStream(
								new FileInputStream("bonusCardsAmount.xml")));
		int bonusCardsAmount = (int) bonusCardsAmountDecoder.readObject();
		bonusCardsAmountDecoder.close();
		
		ArrayList<Card> bonusCards = new ArrayList<Card>();
		
		for(int i = 0; i < bonusCardsAmount; i++) {
			XMLDecoder cardTypeDecoder = 
					new XMLDecoder(
							new BufferedInputStream(
									new FileInputStream(i + "tp.xml")));
			String cardType = (String) cardTypeDecoder.readObject();
			cardTypeDecoder.close();
			
			XMLDecoder cardFunctionDecoder = 
					new XMLDecoder(
							new BufferedInputStream(
									new FileInputStream(i + "ft.xml")));
			String cardFunction = (String) cardFunctionDecoder.readObject();
			cardFunctionDecoder.close();
			
			XMLDecoder cardValueDecoder = 
					new XMLDecoder(
							new BufferedInputStream(
									new FileInputStream(i + "vl.xml")));
			int cardValue = (int) cardValueDecoder.readObject();
			cardValueDecoder.close();
			bonusCards.add(new Card(cardType, cardFunction, cardValue));
		}
		
		return new GameMod(shareTypes, sharePerType, bonusCards);
	}
}
