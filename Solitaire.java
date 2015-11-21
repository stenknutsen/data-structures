package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * Final Version
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		CardNode ptr = null;
		CardNode after = null;
		ptr = deckRear;
		
		
		if(ptr.cardValue == 27){
			after = deckRear.next;
			int temp;
			temp = deckRear.cardValue;
			deckRear.cardValue = after.cardValue;
			after.cardValue = temp;
		}else{
			ptr = ptr.next;
			
			while(ptr.cardValue!=27){
				ptr = ptr.next;
			}
			after = ptr.next;//card after 27. . . . 
			int temp;
			temp = ptr.cardValue;//27.  . .. 
			ptr.cardValue = after.cardValue;
			after.cardValue = temp;	
			
		}
		
		
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD
		CardNode ptr = null;
		CardNode after = null;
		
		ptr = deckRear;
		
		while(ptr.cardValue!=28){
			ptr=ptr.next;
		}
		
		after=ptr.next;
		
		for(int i=0;i<=1;i++){
		int temp;
		temp = ptr.cardValue;
		ptr.cardValue = after.cardValue;
		after.cardValue = temp;	
		ptr=ptr.next;
		after=after.next;
		}
		
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		CardNode ptrA, ptrB, prevA, postB, ptrFront, ptrRear;
		
		ptrA=deckRear.next;
		prevA = deckRear;
		if(ptrA.cardValue==28 && prevA.cardValue==27 || ptrA.cardValue==27 && prevA.cardValue==28){//if there are jokers at both ends, do nothing
			//System.out.println("jokers at both ends");
			;
		
		}else if(ptrA.cardValue==27||ptrA.cardValue==28){//if first card is a joker, then find second joker and place at end
			int target;
			if(ptrA.cardValue==27){
				target=28;
			}else{
				target=27;
			}
			
			while(ptrA.cardValue!=target){//searching for target joker. . . . 
				prevA=ptrA;
				ptrA=ptrA.next;
			}
			
			//move the rear
			deckRear = ptrA;
			
			
			//prevA.next=ptrA.next;//remove node
			
			//CardNode temp = new CardNode();//creating new CardNode and placing at end of deck
			
	    	//temp.cardValue = target;
	    	//temp.next = deckRear.next;
	    	//deckRear.next = temp;
	    	//deckRear = temp;
			
		}else if(prevA.cardValue==27||prevA.cardValue==28){//if last card is a joker
			int target;
			if(prevA.cardValue==27){
				target=28;
			}else{
				target=27;
			}
			while(ptrA.cardValue!=target){//searching for target joker. . . . 
				prevA=ptrA;
				ptrA=ptrA.next;
			}
			
			
			//move the rear
			deckRear=prevA;
			
			//prevA.next=ptrA.next;//remove node
			
			//CardNode temp = new CardNode();//creating new CardNode and placing at beginning
			
	    	//temp.cardValue = target;
	    	//temp.next=deckRear.next;
	    	//deckRear.next=temp;
	    	//rear stays same
			
			
		}else{
		
		
		
		
		
		ptrFront = deckRear.next;
		ptrRear = deckRear;
		//locate cut points
		for(int i=0;i<=28;i++){
			
			if(ptrA.cardValue==27||ptrA.cardValue==28){
				break;
			}
			
			ptrA=ptrA.next;
			prevA=prevA.next;
		}
		ptrB=deckRear.next;
		postB = ptrB.next;
		
		if(ptrA.cardValue==27){
			for(int i=0;i<=28;i++){
				if(ptrB.cardValue==28){
					break;
				}
				
				ptrB=ptrB.next;
				postB=ptrB.next;}
			}else{
				for(int i=0;i<=28;i++){
					if(ptrB.cardValue==27){
						break;
					}
					
					ptrB=ptrB.next;
					postB=postB.next;
					
				}
			}
		//System.out.println("From TripleCut:");
		//System.out.println("Pointer A is pointing at "+ ptrA.cardValue + " and pointer B is pointin at " + ptrB.cardValue);
		
		deckRear = prevA;
		ptrRear.next = ptrA;
		ptrB.next = ptrFront;
		prevA.next = postB;
		
		}
		
		
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		// COMPLETE THIS METHOD*****************************************
		if(deckRear.cardValue==27||deckRear.cardValue==28){
			//System.out.println("last card is: "+ deckRear.cardValue);
			;
		}else{
		
		CardNode prevRear, ptr, ptrFront, oldFront;
		prevRear=deckRear;
		ptr=deckRear;
		oldFront = deckRear.next;
		int target = deckRear.cardValue;
		for(int i=0;i<27;i++){//setting prevRear pointer
			prevRear=prevRear.next;
		}
		//System.out.println("Next to last card is: "+ prevRear.cardValue);
		
		for(int i =0;i<target;i++){
			ptr=ptr.next;
		
		}
		ptrFront=ptr.next;
		//System.out.println("Cut point card is: "+ ptr.cardValue);
		//System.out.println("PtrFront card is: "+ ptrFront.cardValue);
		
		deckRear.next=ptrFront;
		prevRear.next= oldFront;
		ptr.next=deckRear;
		}
		
		
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		//System.out.println("Printing out deck from getKey:");
		//printList(deckRear);
		int key;
	do{
	
		//System.out.println("Calling jokerA to swap. . . . ");
		jokerA();
		//printList(deckRear);
		
		
		//System.out.println("Calling jokerB to swap 28 twice . ");
		jokerB();
		//printList(deckRear);
		
		//System.out.println("Calling triple cut. . . . ");
		tripleCut();
		//printList(deckRear);
		
		//System.out.println("Calling CountCut. . . . ");
		countCut();
		//printList(deckRear);
		
		int target = deckRear.next.cardValue;
		if(target==28){
			target= 27;
		}
		//System.out.println("First card value: "+target);
		
		CardNode ptr;
		ptr=deckRear;
		
		for(int i=0;i<=target;i++){
			ptr=ptr.next;
		}
		key = ptr.cardValue;
		//System.out.println("The key this round is "+ key);
		//System.out.print("**************");
	}while(key==27||key==28);
	
	//System.out.println("The final key is "+ key);
		
		
		
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	    return key;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		//System.out.println("Your message is: "+ message);
		
		//System.out.println("message length is "+ message.length());
		int pos;
		int key;
		int enc;
		String str="";
		
		for(int i=0;i<message.length();i++){
			if(Character.isLetter(message.charAt(i))){
				//System.out.print(message.charAt(i));
				//System.out.println(message.charAt(i)-'A'+1);
				pos = message.charAt(i)-'A'+1;
				key = getKey();
				//System.out.println("Here's the key: "+key);
				enc = key + pos;
				if(enc>26){
					enc = enc-26;
				}
				//System.out.println("This is enc: "+enc);
				char c = (char)(enc-1+'A');
				//System.out.println("Encrypted letter: "+ c);
				str = str + Character.toString(c);
				
			}else{
				continue;
				}
		}
		//System.out.println();
		//System.out.println(str);
		
		//System.out.println("Encrypt Method. . . . ");
		//System.out.println();
		//System.out.println(getKey());
		//System.out.println(getKey());
		//System.out.println(getKey());
		//System.out.println(getKey());
		//System.out.println(getKey());
		//System.out.println(getKey());
		
		
		return str;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
//System.out.println("Your message is: "+ message);
		
		//System.out.println("message length is "+ message.length());
		int pos;
		int key;
		int enc;
		String str="";
		
		for(int i=0;i<message.length();i++){
			if(Character.isLetter(message.charAt(i))){
				//System.out.print(message.charAt(i));
				//System.out.println(message.charAt(i)-'A'+1);
				pos = message.charAt(i)-'A'+1;
				key = getKey();
				//System.out.println("Here's the key: "+key);
				enc = pos - key;
				if(enc<=0){
					enc = enc+26;
				}
				//System.out.println("This is enc: "+enc);
				char c = (char)(enc-1+'A');
				//System.out.println("Encrypted letter: "+ c);
				str = str + Character.toString(c);
				
			}else{
				continue;
				}
		}
		//System.out.println();
		//System.out.println(str);
		
		
		
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	    return str;
	}
}
