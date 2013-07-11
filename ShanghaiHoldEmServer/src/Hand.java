import java.util.ArrayList;


public class Hand {
	
	int players;
	ArrayList<ServerHello> gameplay;
	
	public Hand(int players, ArrayList<ServerHello> gameplay){
		this.players = players;
		this.gameplay = gameplay;
	}
	
	public void startHand(){
		Dealer dealer = new Dealer();
		dealer.createDeck();
		
		for(int i=0;i<players;i++){
			int card1 = dealer.card(dealer.deck);
			int card2 = dealer.card(dealer.deck);
			gameplay.get(i).sendString(Integer.toString(card1));
			gameplay.get(i).sendString(Integer.toString(card2));
		}
		
		
	}
	
	
	
}
