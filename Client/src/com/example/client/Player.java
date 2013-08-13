package com.example.client;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.R.integer;

public class Player extends Person {
	
	private Card cardOne = null, cardTwo = null;
	private int highCard = 0, chipCount = 500, score = 0, straight = -1, flush = -1;
	private String user = null;
	ArrayList<Card> cards;
	boolean fold = false,  straightFlush = false;
	ArrayList<Card> hand = new ArrayList<Card>();
	
	public Player(){}

	int determineHand(ArrayList<Card> dealerCards){
		cards = new ArrayList<Card>(7);
		int i=0, two=0, three=0;
		int[] ranksHisto = {0,0,0,0,0,0,0,0,0,0,0,0,0};
		int[] rankPair = {-1,-1};
		
		//Determine Hand High Card
		if (cardOne.getRank() >= cardTwo.getRank())this.setHighCard(cardOne.getRank());
		else this.setHighCard(cardTwo.getRank());
		
		//Load cards
		cards.add(cardOne);
		cards.add(cardTwo);
		for(Card card : dealerCards){
		cards.add(card); 
		}

		//sort cards
		Collections.sort(cards);
	
		//For each card
		for (Card card: cards)
		{
		//increase the number of cards in rank by 1
		//	ranksHisto[currCard.rank]++;
			ranksHisto[card.getRank()] += 1;
		}
		
		//Print how many of each card is there
//		for (i=0;i<ranksHisto.length;i++)
//		System.out.println(i+" "+ranksHisto[i]);
	
		
		//For each rank
		for(i=0;i<ranksHisto.length;i++)
		{
			//If the rank has four cards
			if (ranksHisto[i] == 4){
				//Give player a four-of-a-kind score
				this.setScore(8);
				//For each card in cards
				for(Card card:cards){
					//If the card is the right rank
					if (card.getRank() == i){
						//Add to player's hand
						this.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1));
					}
				}
				break;
			}
			//If the rank has three cards
			if (ranksHisto[i] == 3){
				//increase number of three pairs
				three++;
				//If there's no pair rank in first slot, put in
				if(rankPair[0] == -1)
				{
					rankPair[0] = i;
				}
				//Otherwise, put in the second slot
				else{
					rankPair[1] = i;
				}
			}
			//If the rank has two cards
			else if(ranksHisto[i] == 2) {
				two++;
				//If there's no pair rank in first slot, put in
				if(rankPair[0] == -1)
				{
					rankPair[0] = i;
				}
				//Otherwise, put in the second slot(Will take top two, not third)
				else if (rankPair[1] == -1)
				{
					rankPair[1] = i;
				}
			}
		}

		//Print out how many of each
		System.out.println("two: "+two+"  three: "+three);
		System.out.println("Straight: "+this.straight+"  Flush: "+this.flush);
		

		//Call check for straight flush
		checkStraightFlush(cards);

		//Check for a flush
		checkFlush(cards);

		//Check for a straight
		List<Card> tempHand = checkStraight(cards);
	

		
		//If player has straight and flush
		if(this.straightFlush == true){
				//Straight flush
			this.setScore(9);
		}
		//If player has a two and a three or two threes
		else if(two == 1 && three == 1 || three == 2){
			//Clear straight player hand (if applicable)
			this.hand.clear();
			//Full house
			this.setScore(7);
			
			//For each card
			for(Card card: cards)
			{
				//If there's still room in the player's hand
				if (this.hand.size() < 5){
					//If the card is of the paired rank
					if (card.getRank() == rankPair[0] || card.getRank() == rankPair[1] )
					{
						//Add card to hand
						this.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1));
					}
				}		
			}
		}
		//If the player has a flush
		else if(this.flush != -1){
			//Flush
			this.setScore(6);
			
			//For each cards
			for (Card card: cards)
			{
				//If the card has the right suit
				if (card.getSuit() == this.flush)
				{
					//If there's still room in the player's hand
					if (this.hand.size() < 5){
						//Add to player's hand
						this.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1));
					}
				}
			} 
		}
		//If the player has a straight
		else if(this.straight != -1){
			//Straight
			this.setScore(5);
			
			for(Card card: tempHand){
				this.hand.add(card);
			}
			
		}
		//If the player has three of one card
		else if(three == 1){
			//Three-of-a-kind
			this.setScore(4);

			//For each card
			for(Card card: cards)
			{
				//If there's room in the player's hand
				if (this.hand.size() < 5){
					//If the card is the right rank
					if (card.getRank() == rankPair[0])
					{
						//Add card to hand
						this.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1));
					}
				}	
			}
		}
		//If the player has two or more two pairs
		else if(two >= 2){
			//Two pair
			this.setScore(3);
			
			//For each card
			for(Card card: cards)
			{
				//If there's still room in player's hand
				if (this.hand.size() < 5){
					//If card is of the right rank
					if (card.getRank() == rankPair[0] || card.getRank() == rankPair[1] )
					{
						//Add card to player's hand
						this.hand.add(card);
						//remove from cards
						cards.set(cards.indexOf(card), new Card(-1));
					}
				}
			}
		}
		//If there's one pair of twos
		else if(two == 1){
			//One pair
			this.setScore(2);
			
			//For each card in cards
			for(Card card: cards)
			{
				//If there's room in the player's hand
				if (this.hand.size() < 5){
					//If the card is of the right rank
					if (card.getRank() == rankPair[0])
					{
						//Add card to player's hand
						this.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1));
					}
				}
			}
		}
		//Only have one of each card
		else{
			//High card
			this.setScore(1);
		}

		//Sort left over cards
		Collections.sort(cards);
		
		//for each card in cards
		for(Card card: cards){
			//If there's room in the player's hand
			if(this.hand.size() < 5){
				//Add card to player's hand
				this.hand.add(card);
				//Remove from cards
				cards.set(cards.indexOf(card), new Card(-1));
			}
		}
		
		return 0;
	}
	
	void checkStraightFlush(List<Card> cards) {
		int i=-1, flag = 0, targetSuit;
		Card[] ranks = new Card[13];
		
		for (Card card: ranks){
			card = new Card(-1);
		}
		
		targetSuit = this.checkFlush(cards);
		
		if (targetSuit != -1){
			//Put card value in each spot of ranks where card is in cards
			for (Card card : cards){
				//If it's a card
				if (card.getRank() != -1){
					if (card.getSuit() == targetSuit)//NEW
					ranks[card.getRank()] = card;
				}
			}
	/*			for (Card card: ranks){
					if (card != null){
						targetSuit = card.getRank();
						break;
					}
				}*/
			//Go through ranks
			for (i=0;i<ranks.length;i++)
			{
				if(ranks[i] == null){
					flag = 0;
				}
				else if((ranks[i] != null)&&(ranks[i].getSuit() == targetSuit)){
					flag++;
				}
				else if(ranks[i].getSuit() != targetSuit){
					flag = 1;
					targetSuit = ranks[i].getSuit();
				}
				if (flag == 5){
					this.straightFlush = true;
					for (int j = i; j >= i-4;j--)
					{
						System.out.println("Adding "+ranks[j].printRank());
						this.hand.add(ranks[j]);
						cards.set(cards.indexOf(ranks[j]), new Card(-1));		
					}
				}
			}
		}
	}
	
	//Check for straight
	List<Card> checkStraight(List<Card> cards){
		Card[] ranks = new Card[13];
		List<Card> temp = new ArrayList<Card>(5);
//1		Iterator<Card> cardIter = cards.iterator();
		int i=0, straightFlag = 0;

//		ListIterator<Integer> rankIter = ranks.listIterator();
		//For each card in cards
//1		while(cardIter.hasNext()){
//1			currCard = cardIter.next();
//1			System.out.println(currCard.getRank()+" "+currCard.getSuit());
			//increase the number of cards in rank by 1
//1			ranks[currCard.rank] = currCard;
			//ranksHisto[currCard.rank] = currCard.rank;
		
		//Put card value in each spot of ranks where card is in cards
		for (Card card : cards){
			//If it's a card
			if (card.getRank() != -1){
				ranks[card.getRank()] = card;
			}
		}
		
		//Go through ranks
		for (i=0;i<ranks.length;i++)
		{
			//If the value isn't null, increment the flag by 1
			if(ranks[i] != null)
			{
				straightFlag++;
			}
			//If the value is null, set the flag back to zero
			else if (ranks[i] == null)
			{
				straightFlag = 0;
			}
			//If the flag reaches 5(meaning no nulls in between the last five cards), straight
			if(straightFlag == 5)
			{
				//Set the player to say has straight
				this.hasStraight(i);


				//For each card
//1				for (Card card: cards)
				for (int j=i;j>=i-4;j--)
				{
					//If one of the straight cards
//1 				if ((card.getRank() >=i-4 && card.getRank() <=i))
					
						//Add to player's hand
						//Remove from cards
//1						player.hand.add(card);
						temp.add(ranks[j]);
//1						cards.set(cards.indexOf(card), new Card(-1, -1));
						cards.set(cards.indexOf(ranks[j]), new Card(-1));
					
				}
				return temp;
			}
		}
		return null;
		
	}
	
 int checkFlush(List<Card> cards){
		int[] suits = {0,0,0,0};
		int i=0;

//1		Iterator<Card> cardIter = cards.iterator();
		
		//For each card in cards
//1		while(cardIter.hasNext()){
//1			currCard = cardIter.next();
			//Add 1 in the suit of card
//1			suits[currCard.suit]++;
//1		}
		
		//For each card
		for (Card card : cards){
			//If it's a card
			if (card.getRank() != -1){
			//Increment the suit location in array by 1
				suits[card.getSuit()]++;
			}
		}
		
		//Go through suits
		for(i=0;i<suits.length;i++)
		{
			//If any are greater or less than 5
			if (suits[i] >= 5)
			{
				//Player has flush
				this.hasFlush(i);
				//For each card
/*				for (Card card: cards)
				{
					//If the card has the right suit
					if (card.suit == i)
					{
						//If there's still room in the player's hand
						if (player.hand.size() < 5){
							//Add to player's hand
							player.hand.add(card);
							//Remove from cards
							cards.set(cards.indexOf(card), new Card(-1, -1));
						}
					}
				} 
				break;*/
				return i;
			}
				
		}
		return -1;

	}
	
	@Override
	public void setCard(Card card, int cardNum) {
		switch (cardNum)
		{
			case 1: this.cardOne = card;
				break;
			case 2: this.cardTwo = card;
				break;
		}
	}
	
	@Override
	public Card getCard(int numCard) {
		
		Card result = null;
		switch (numCard){
			case 1: result = this.cardOne;
					break;
			case 2: result = this.cardTwo;
					break;
		}
		return result;
	}
	
	public void setChipCount(int amount){
		this.chipCount = amount;
	}
	
	public int getChipCount(){
		return this.chipCount;
	}
	
	public boolean getFoldValue(){
		return this.fold;
	}

	public void fold(){
		this.fold = true;
	}
	
	public void hasStraight(int num){
		this.straight = num;
	}
	
	public void hasFlush(int num){
		this.flush = num;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getScore(){
		return this.score;
	}

	public void setUser(String name){
		this.user = name;
	}
	
	public String getUser(){
		return this.user;
	}

	public void setHighCard(int rank){
		this.highCard = rank;
	}
	
	public int getHighCard(){
		return this.highCard;
	}
	
}
