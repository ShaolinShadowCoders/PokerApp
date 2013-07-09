import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class Dealer extends Person {
	
	Card cardOne = null, cardTwo = null, cardThree = null, cardFour = null, cardFive = null;
	List<Card> cards;
//	ArrayList <Integer> ranks = new ArrayList<Integer>(13);
	Card currCard = null;
	int i = 0;
	
	public Dealer() {
	}

	@Override
	void setCard(Card card, int cardNum) {

		switch (cardNum)
		{
			case 1: cardOne = card;
				break;
			case 2: cardTwo = card;
				break;
			case 3: cardThree = card;
				break;
			case 4: cardFour = card;
				break;
			case 5: cardFive = card;
				break;
		}
	}

	@Override
	Card getCard(int numCard) {
		
		Card result = null;
		switch (numCard){
			case 1: result = cardOne;
			case 2: result = cardTwo;
			case 3: result = cardThree;
			case 4: result = cardFour;
			case 5: result = cardFive;
		}
		return result;
	}

	int determineHand(Player player){
		cards = new ArrayList<Card>(7);

		if (player.getCard(1).rank >= player.getCard(2).rank){
			player.highCard = player.getCard(1);
		} else {
			player.highCard = player.getCard(2);
		}
		
		//Load cards
		cards.add(player.getCard(1));
		cards.add(player.getCard(2));
		cards.add(cardOne);
		cards.add(cardTwo);
		cards.add(cardThree);
		cards.add(cardFour);
		cards.add(cardFive); 
		
/*		cards.add(new Card(5, 0));
		cards.add(new Card(11, 2));
		cards.add(new Card(4, 3));
		cards.add(new Card(2, 1));
		cards.add(new Card(3, 2));
		cards.add(new Card(6, 1));
		cards.add(new Card(10, 0));*/

		//sort cards
		Collections.sort(cards);
		//list off all cards
		System.out.println("CARDS:");
		for(Card card: cards)
		{
			System.out.println(card.printRank()+" "+card.printSuit());
		}
		
		//run methods
		checkCards(cards, player);
		
		return 0;
	}
	
	void checkStraightFlush(List<Card> cards, Player player) {
		int i=-1, flag = 0, targetSuit = -1;
		Card[] ranks = new Card[13];
		
		for (Card card: ranks){
			card = new Card(-1, -1);
		}
		
		//Put card value in each spot of ranks where card is in cards
		for (Card card : cards){
			//If it's a card
			if (card.rank != -1){
				ranks[card.rank] = card;
			}
		}
			for (Card card: ranks){
				if (card != null){
					targetSuit = card.rank;
					break;
				}
			}
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
				player.straightFlush = true;
				for (int j = i; j >= i-4;j--)
				{
					System.out.println("Adding "+ranks[j].printRank());
					player.hand.add(ranks[j]);
					cards.set(cards.indexOf(ranks[j]), new Card(-1, -1));		
				}
			}
		}
		
	}
	
	//Check for straight
	List<Card> checkStraight(List<Card> cards, Player player){
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
			if (card.rank != -1){
				ranks[card.rank] = card;
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
				player.hasStraight(i);


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
						cards.set(cards.indexOf(ranks[j]), new Card(-1, -1));
					
				}
				return temp;
			}
		}
		return null;
		
	}
	
/*	void*/ int checkFlush(List<Card> cards, Player player){
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
			if (card.rank != -1){
			//Increment the suit location in array by 1
				suits[card.suit]++;
			}
		}
		
		//Go through suits
		for(i=0;i<suits.length;i++)
		{
			//If any are greater or less than 5
			if (suits[i] >= 5)
			{
				//Player has flush
				player.hasFlush(i);
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
	
	void checkCards(List<Card> cards, Player player){
		
		int i=0, two=0, three=0;
		int[] ranksHisto = {0,0,0,0,0,0,0,0,0,0,0,0,0};
		int[] rankPair = {-1,-1};
	
		//For each card
		for (Card card: cards)
		{
		//increase the number of cards in rank by 1
		//	ranksHisto[currCard.rank]++;
			ranksHisto[card.rank] += 1;
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
				player.setScore(8);
				//For each card in cards
				for(Card card:cards){
					//If the card is the right rank
					if (card.getRank() == i){
						//Add to player's hand
						player.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1, -1));
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
		System.out.println("Straight: "+player.straight+"  Flush: "+player.flush);
		

		//Call check for straight flush
		checkStraightFlush(cards, player);

		//Check for a flush
		checkFlush(cards, player);

		//Check for a straight
		List<Card> tempHand = checkStraight(cards, player);
	

		
		//If player has straight and flush
		if(player.straightFlush == true){
				//Straight flush
				player.setScore(9);
		}
		//If player has a two and a three or two threes
		else if(two == 1 && three == 1 || three == 2){
			//Clear straight player hand (if applicable)
			player.hand.clear();
			//Full house
			player.setScore(7);
			
			//For each card
			for(Card card: cards)
			{
				//If there's still room in the player's hand
				if (player.hand.size() < 5){
					//If the card is of the paired rank
					if (card.getRank() == rankPair[0] || card.getRank() == rankPair[1] )
					{
						//Add card to hand
						player.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1, -1));
					}
				}		
			}
		}
		//If the player has a flush
		else if(player.flush != -1){
			//Flush
			player.setScore(6);
			
			//For each cards
			for (Card card: cards)
			{
				//If the card has the right suit
				if (card.suit == player.flush)
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
		}
		//If the player has a straight
		else if(player.straight != -1){
			//Straight
			player.setScore(5);
			
			for(Card card: tempHand){
				player.hand.add(card);
			}
			
		}
		//If the player has three of one card
		else if(three == 1){
			//Three-of-a-kind
			player.setScore(4);

			//For each card
			for(Card card: cards)
			{
				//If there's room in the player's hand
				if (player.hand.size() < 5){
					//If the card is the right rank
					if (card.getRank() == rankPair[0])
					{
						//Add card to hand
						player.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1, -1));
					}
				}	
			}
		}
		//If the player has two or more two pairs
		else if(two >= 2){
			//Two pair
			player.setScore(3);
			
			//For each card
			for(Card card: cards)
			{
				//If there's still room in player's hand
				if (player.hand.size() < 5){
					//If card is of the right rank
					if (card.getRank() == rankPair[0] || card.getRank() == rankPair[1] )
					{
						//Add card to player's hand
						player.hand.add(card);
						//remove from cards
						cards.set(cards.indexOf(card), new Card(-1, -1));
					}
				}
			}
		}
		//If there's one pair of twos
		else if(two == 1){
			//One pair
			player.setScore(2);
			
			//For each card in cards
			for(Card card: cards)
			{
				//If there's room in the player's hand
				if (player.hand.size() < 5){
					//If the card is of the right rank
					if (card.getRank() == rankPair[0])
					{
						//Add card to player's hand
						player.hand.add(card);
						//Remove from cards
						cards.set(cards.indexOf(card), new Card(-1, -1));
					}
				}
			}
		}
		//Only have one of each card
		else{
			//High card
			player.setScore(1);
		}

		//Sort left over cards
		Collections.sort(cards);
		
		//for each card in cards
		for(Card card: cards){
			//If there's room in the player's hand
			if(player.hand.size() < 5){
				//Add card to player's hand
				player.hand.add(card);
				//Remove from cards
				cards.set(cards.indexOf(card), new Card(-1, -1));
			}
		}
		
		//Print out the player's hand
		System.out.println("WINNING HAND");
		for(Card card: player.hand){
			System.out.println(card.printRank());
		}
	}
	
}

