import java.sql.SQLException;
import java.util.ArrayList;


public class Hand {
	
	int players;
	ArrayList<ServerHello> gameplay;
	int smallBlind;
	int bigBlind;
	
	public Hand(int players, ArrayList<ServerHello> gameplay,int smallBlind,int bigBlind){
		this.players = players;
		this.gameplay = gameplay;
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
	}
	
	public void startHand() throws SQLException{
		Dealer dealer = new Dealer();
		dealer.createDeck();
		dealer.blinds(gameplay, smallBlind, bigBlind);
		
		for(int i=0;i<players;i++){
			int card1 = dealer.card(dealer.deck);
			int card2 = dealer.card(dealer.deck);
			gameplay.get(i).sendString(Integer.toString(card1));
			gameplay.get(i).sendString(Integer.toString(card2));
		}
		
		
	}
	
	
	
}
