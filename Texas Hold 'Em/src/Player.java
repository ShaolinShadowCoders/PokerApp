import java.util.ArrayList;
import java.util.Scanner;


public class Player extends Person {
	
	private Card cardOne = null, cardTwo = null;
	Card highCard = null;
	private int num = 0, total = 500, score = 0;
	int straight = -1, flush = -1;
	int currentBet;
	boolean fold = false,  straightFlush = false;
	ArrayList<Card> hand = new ArrayList<Card>();
	
	public Player(/*int amount*/int num){
		this.setNum(num);
	/*	int amount = 0;
		int choice = -1;
		bet = 0;
		//Loop looking for when it's his turn to bet
/*		if (1 == 1Player presses button saying he'll call){
			this.setTotal(this.total - amount);
			choice = 1;
			//return choice;
		} else if(1 == 2 Player presses button saying he'll raise){
			//Pop up raise screen, passes bet, returns new bet.
//			this.setTotal(this.total - amount);
//			choice = 2;
			//return choice;
//		} else if(1 == 0Player presses button saying he'll fold){
			//Call fold function
//			this.fold();
//			choice = 0;
			//return choice;
		}*/
	}

	public int turn(int bet){
		Scanner scanner = new Scanner(System.in);

		if (bet == 1){
			System.out.println("How much would you like to bet? Number must be an increment of 5");
			bet = scanner.nextInt();
			if (bet%5 != 0 && bet != 0){
				do {
				System.out.println("Number is not an increment of 5. Please enter a new number with an increment of 5");
				bet = scanner.nextInt();
				} while (bet%5 != 0);
			}
			currentBet = bet;
			this.setTotal(this.getTotal()-bet);
		} else {
			System.out.println("Select your move! \n1 - Call. \n2 - Raise. \n3 - Fold.");

			currentBet = bet - currentBet;
			int choice = scanner.nextInt();
			switch(choice){
			case 1: 
				this.setTotal(this.getTotal()-currentBet);
				currentBet = bet;
				break;
			case 2:
				System.out.println("How much would you like to increase by? Number must be an increment of 5");
				int up = scanner.nextInt();
				if (up%5 != 0){
					do {
					System.out.println("Number is not an increment of 5. Please enter a new number with an increment of 5");
					up = scanner.nextInt();
					} while (up%5 != 0);
				}
				bet += up;
				System.out.println("The bet is now "+ bet);
				this.setTotal(this.getTotal()-bet);
				currentBet = bet;
				break;
			case 3:
				System.out.println("Player has folded");
				//Could set total to 0
				this.fold();
				bet = 0;
				break;
			}
		}
		return bet;
	}
	
	@Override
	public void setCard(Card card, int cardNum) {
		// TODO Auto-generated method stub
		switch (cardNum)
		{
			case 1: cardOne = card;
				break;
			case 2: cardTwo = card;
				break;
		}
	}
	
	@Override
	Card getCard(int numCard) {
		
		Card result = null;
		switch (numCard){
			case 1: result = cardOne;
					break;
			case 2: result = cardTwo;
					break;
		}
		return result;
	}
	
	void setTotal(int amount){
		total = amount;
	}
	
	int getTotal(){
		return total;
	}
	
	boolean getFoldValue(){
		return this.fold;
	}

	void fold(){
		fold = true;
	}
	
	void hasStraight(int num){
		straight = num;
	}
	
	void hasFlush(int num){
		flush = num;
	}
	
	void setScore(int score){
		this.score = score;
	}
	
	int getScore(){
		return this.score;
	}

	private void setNum(int num){
		this.num = num;
	}
	
	public int getNum(){
		return this.num;
	}

}
