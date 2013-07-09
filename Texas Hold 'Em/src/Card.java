
public class Card extends Object implements Comparable {
		int rank;
		int suit;
	
	public Card(int rank, int suit){
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
		String result = null;
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
		String result = null;
		switch (suit)
		{
			case 0: result = "Spades";
				break;
			case 1: result = "Hearts";
				break;
			case 2: result = "Clubs";
				break;
			case 3: result = "Diamonds";
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
