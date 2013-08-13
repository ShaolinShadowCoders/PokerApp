package com.example.client;
public class Card extends Object implements Comparable {
		private int rank;
		private int suit;
		private String result;
		
	public Card(int card){
		if (card >= 0 && card <= 12){
			this.setRank(card);
			this.setSuit(0);
		} else if (card >= 13 && card <= 25){
			this.setRank(card - 13);
			this.setSuit(1);
		} else if (card >= 26 && card <= 38){
			this.setRank(card-26);
			this.setSuit(2);
		} else if (card >= 39 && card <= 51){
			this.setRank(card-39);
			this.setSuit(3);
		} else {
			this.setRank(-1);
			this.setSuit(-1);
		}
		this.setRank(rank);
		this.setSuit(suit);
	}
	
	void setRank (int rank){
		this.rank = rank;
	}
	
	int getRank(){
		return this.rank;
	}
	
	void setSuit(int suit){
		this.suit = suit;
	}
	
	int getSuit(){
		return this.suit;
	}
	
	String printRank(){
		switch (rank)
		{
		case 0: result = "2";
		break;
		case 1: result = "3";
		break;
		case 2: result = "4";
		break;
		case 3: result = "5";
		break;
		case 4: result = "6";
		break;
		case 5: result = "7";
		break;
		case 6: result = "8";
		break;
		case 7: result = "9";
		break;
		case 8: result = "10";
		break;
		case 9: result = "Jack";
		break;
		case 10: result = "Queen";
		break;
		case 11: result = "King";
		break;
		case 12: result = "Ace";
		break;
		}
		return result;
	}
	
	String printSuit() {
		switch (suit)
		{
			case 0: result = "Diamonds";
				break;
			case 1: result = "Clubs";
				break;
			case 2: result = "Hearts";
				break;
			case 3: result = "Spades";
				break;
		}
		return result;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		int thisValue = this.getRank();
		int thatValue = ((Card)o).getRank();
		if(thatValue > thisValue){
			return 1;
		} else if (thatValue == thisValue){
		return 0;
		} else {
			return -1;
		}
	}
}
