package mytcp;

import java.util.ArrayList;
import java.util.Random;

public class Dealer {

ArrayList<Integer> deck;
	
	public Dealer() {
		
	}
	
	public void createDeck(){
		ArrayList<Integer> deckArrayList = new ArrayList<Integer>();
		for(int i=0;i<52;i++)
			deckArrayList.add(i);
		this.deck = deckArrayList;
	}
	
	
	public int dealCard(ArrayList<Integer> deck){
		int card;
		Random random = new Random();
		int index = random.nextInt(deck.size());
		System.out.println("index is " + index + " and deckSize is " + deck.size());
		card = deck.get(index);
		deck.remove(index);
		return card; 
	}
	
	
	/*public void blinds(ArrayList<ServerHello> gameplay,int smallBlind,int bigBlind) throws SQLException{
		DB smallblindDb = new DB(gameplay.get(smallBlind));
		smallblindDb.blindUpdate(gameplay.get(smallBlind).username, 0);
		gameplay.get(smallBlind++).sendString("small");
		
		DB bigblindDb = new DB(gameplay.get(bigBlind));
		bigblindDb.blindUpdate(gameplay.get(bigBlind).username, 0);
		gameplay.get(bigBlind++).sendString("big");
		
	}*/
}
