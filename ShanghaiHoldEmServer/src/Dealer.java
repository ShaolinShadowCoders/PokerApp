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
	
	
	public int card(ArrayList<Integer> deck){
		int card;
		Random random = new Random();
		int index = random.nextInt() % deck.size();
		card = deck.get(index);
		deck.remove(index);
		return card; 
	}
	
	
	
	
}

	
