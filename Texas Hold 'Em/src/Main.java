import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;


public class Main {

	public static void main(String[] args){
		ArrayList<Card> deck = new ArrayList<Card>();
		Dealer dealer = new Dealer();
		Calendar time = Calendar.getInstance();
		int cardNum = 0, i, rank, suit;
		ArrayList<Player> players = new ArrayList<Player>();
		Iterator<Player> playerIter;
		Player currPlayer;
		Random rand = new Random(time.getTimeInMillis());
		int randomCard = 0;
		int pot = 0, bet = 0, newBet = 0, flag, numPlayers;
		int bigBlind = 10;
		int flopNum = 0;
		Card currCard = null;
		
		for(suit=0;suit<4;suit++){
			for(rank=0;rank<13;rank++){
				currCard = new Card(rank, suit);
				deck.add(currCard);
				cardNum++;
			}
		}
		
		System.out.println("How many players? ");
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		for (int z=1;z<=input;z++){
			players.add(new Player(z));
		}

		for (Player player: players){
			System.out.println("Player "+player.getNum()+ " has "+player.getTotal()+" dollars");
		}
		
		//STEP 1: THE BLIND BETS
		System.out.println("The Blind Bets: ");
		currPlayer = players.get(0);
		currPlayer.setTotal((int) (currPlayer.getTotal()-(.5*bigBlind)));
		pot += (.5*bigBlind);
		System.out.println("Player "+currPlayer.getNum()+" now has "+currPlayer.getTotal()+ " dollars");
		
		currPlayer = players.get(1);
		currPlayer.setTotal((currPlayer.getTotal()-bigBlind));
		pot += bigBlind;
		System.out.println("Player "+currPlayer.getNum()+" now has "+currPlayer.getTotal()+ " dollars");
		System.out.println();
		
		//STEP 2: THE POCKET CARDS
		for(i=1;i<3;i++)
		{
			
			for (int j=0;j<players.size();j++)
			{
//				int card = rand.nextInt(deck.size()) +1;
//				System.out.println(card);
				flopNum = (j+2)%players.size();
//				players.get(j).setCard(deck.remove(card), i);
				players.get(flopNum).setCard(deck.remove(rand.nextInt(deck.size())), i);

			}
		}
	
		//Print out cards
		for (Player player: players){
			System.out.println("Player "+player.getNum()+" has "+player.getCard(1).printRank()+ " of "+player.getCard(1).getSuit()+" and ");
			System.out.println(player.getCard(2).printRank()+ " of "+player.getCard(2).getSuit());
		}
		System.out.println();
		
		bet = -1;
		
		//STEP 3: THE FIRST BETTING ROUND
		System.out.println("First Betting Round: ");
		flag = 0;//new
		numPlayers = players.size();//new
		
		while (flag != numPlayers){//new
			for (i=0;i<players.size();i++)
			{
				flopNum = (i+2)%players.size();
				if (players.get(flopNum).fold == false){//new
					if (bet == -1){
//						System.out.println("How much would you like to bet? Number must be an increment of 5");
//						bet = scanner.nextInt();
//						if (bet%5 != 0 && bet != 0){
//							do {
//							System.out.println("Number is not an increment of 5. Please enter a new number with an increment of 5");
//							bet = scanner.nextInt();
//							} while (bet%5 != 0);
//						}
						bet = players.get(i).turn(1);
						System.out.println("The bet is now "+bet);
						pot += bet;
						flag++;
						System.out.println("Player "+players.get(i).getNum()+" now has "+players.get(i).getTotal()+ " dollars");
						System.out.println();
					} else {
						newBet = players.get(flopNum).turn(bet);
						if (newBet == bet){
							System.out.println("Player "+players.get(flopNum).getNum()+" has called");
							pot += bet;
							flag++;//new
						}
						else if (newBet > bet){
							System.out.println("Player "+players.get(flopNum).getNum()+" has raised. The new bet is now "+newBet);
							pot = pot + newBet - bet;
							bet = newBet;
							flag = 1;//new
						} else if (newBet == 0){
							System.out.println("Player "+players.get(flopNum).getNum()+" has folded");
							numPlayers--;//new
						}
						System.out.println("Player "+players.get(flopNum).getNum()+" now has "+players.get(flopNum).getTotal()+ " dollars");
						System.out.println();
						}                                                   
					}
					if (flag == numPlayers || numPlayers == 1){
						break;
					}
				} 
				if (numPlayers == 1){
					flag = 1;
				}
		}

		//STEP 4: THE FLOP
		randomCard = rand.nextInt(deck.size());
		//Burn Card
		deck.remove(randomCard);
		
		for (i=1;i<4;i++){
			randomCard = rand.nextInt(deck.size());
			dealer.setCard(deck.remove(randomCard), i);
		}
		
		currCard = dealer.cardOne;
		System.out.println("Dealer has played "+currCard.printRank()+ " of "+currCard.getSuit()+" and ");
		currCard = dealer.cardTwo;
		System.out.println(currCard.printRank()+ " of "+currCard.getSuit()+" and ");
		currCard = dealer.cardThree;
		System.out.println(currCard.printRank()+ " of "+currCard.getSuit());
		System.out.println();
		
		//STEP 5: THE SECOND BETTING ROUND
		System.out.println("Second Betting Round: ");
		flag = 0;//new
		bet = -1;
		for (Player player: players){
			player.currentBet = 0;
		}
		
		while (flag != numPlayers){//new
			for (i=0;i<players.size();i++)
			{
				if (players.get(i).fold == false){//new
					if (bet == -1){
//						System.out.println("How much would you like to bet? Number must be an increment of 5");
//						bet = scanner.nextInt();
//						if (bet%5 != 0 && bet != 0){
//							do {
//							System.out.println("Number is not an increment of 5. Please enter a new number with an increment of 5");
//							bet = scanner.nextInt();
//							} while (bet%5 != 0);
//						}
						bet = players.get(i).turn(1);
						System.out.println("The bet is now "+bet);
						pot += bet;
						flag++;
						System.out.println("Player "+players.get(i).getNum()+" now has "+players.get(i).getTotal()+ " dollars");
						System.out.println();
					} else {
						newBet = players.get(i).turn(bet);
						if (newBet == bet){
							System.out.println("Player "+players.get(i).getNum()+" has called");
							pot += bet;
							flag++;//new
						}
						else if (newBet > bet){
							System.out.println("Player "+players.get(i).getNum()+" has raised. The new bet is now "+newBet);
							pot = pot + newBet - bet;
							bet = newBet;
							flag = 1;//new
						} else if (newBet == 0){
							System.out.println("Player "+players.get(i).getNum()+" has folded");
							numPlayers--;//new
						}
						System.out.println("Player "+players.get(i).getNum()+" now has "+players.get(i).getTotal()+ " dollars");
						System.out.println();
						}                                                   
					}
					if (flag == numPlayers || numPlayers == 1){
						break;
					}
				} 
				if (numPlayers == 1){
					flag = 1;
				}
		}
		
		//STEP 6: THE TURN
		//Burn Card
		deck.remove(rand.nextInt(deck.size()));
		
		dealer.setCard(deck.remove(rand.nextInt(deck.size())), 4);
		currCard = dealer.cardFour;
		System.out.println("Dealer has now played "+currCard.printRank()+ " of "+currCard.getSuit());
		System.out.println();

		//STEP 7: THE THIRD BETTING ROUND
		System.out.println("Third Betting Round:¡¡");
		
		flag = 0;//new
		bet = -1;
		for (Player player: players){
			player.currentBet = 0;
		}
		
		while (flag != numPlayers){//new
			for (i=0;i<players.size();i++)
			{
				if (players.get(i).fold == false){//new
					if (bet == -1){
//						System.out.println("How much would you like to bet? Number must be an increment of 5");
	//					bet = scanner.nextInt();
		//				if (bet%5 != 0 && bet != 0){
			//				do {
//							System.out.println("Number is not an increment of 5. Please enter a new number with an increment of 5");
//							bet = scanner.nextInt();
//							} while (bet%5 != 0);
//						}
						bet = players.get(i).turn(1);
						System.out.println("The bet is now "+bet);
						pot += bet;
						flag++;
						System.out.println("Player "+players.get(i).getNum()+" now has "+players.get(i).getTotal()+ " dollars");
						System.out.println();
					} else {
						newBet = players.get(i).turn(bet);
						if (newBet == bet){
							System.out.println("Player "+players.get(i).getNum()+" has called");
							pot += bet;
							flag++;//new
						}
						else if (newBet > bet){
							System.out.println("Player "+players.get(i).getNum()+" has raised. The new bet is now "+newBet);
							pot = pot + newBet - bet;
							bet = newBet;
							flag = 1;//new
						} else if (newBet == 0){
							System.out.println("Player "+players.get(i).getNum()+" has folded");
							numPlayers--;//new
						}
						System.out.println("Player "+players.get(i).getNum()+" now has "+players.get(i).getTotal()+ " dollars");
						System.out.println();
						}                                                   
					}
					if (flag == numPlayers || numPlayers == 1){
						break;
					}
				} 
				if (numPlayers == 1){
					flag = 1;
				}
		}
		
		//STEP 8: THE RIVER
		//Burn card
		deck.remove(rand.nextInt(deck.size()));
		
		dealer.setCard(deck.remove(rand.nextInt(deck.size())), 5);
		currCard = dealer.cardFive;
		System.out.println("Dealer had now played "+currCard.printRank()+ " of "+currCard.getSuit());
		System.out.println();

		//STEP 9: THE FOURTH/FINAL BETTING ROUND
		//SET BIG BLIND TO HIGHER LIMIT STAKE
		System.out.println("Fourth Betting Round: ");
		
		flag = 0;//new
		bet = -1;
		for (Player player: players){
			player.currentBet = 0;
		}
		
		while (flag != numPlayers){//new
			for (i=0;i<players.size();i++)
			{
				if (players.get(i).fold == false){//new
					if (bet == -1){
//						System.out.println("How much would you like to bet? Number must be an increment of 5");
//						bet = scanner.nextInt();
//						if (bet%5 != 0 && bet != 0){
//							do {
//							System.out.println("Number is not an increment of 5. Please enter a new number with an increment of 5");
//							bet = scanner.nextInt();
//							} while (bet%5 != 0);
//						}
						bet = players.get(i).turn(1);
						System.out.println("The bet is now "+bet);
						pot += bet;
						flag++;
						System.out.println("Player "+players.get(i).getNum()+" now has "+players.get(i).getTotal()+ " dollars");
						System.out.println();
					} else {
						newBet = players.get(i).turn(bet);
						if (newBet == bet){
							System.out.println("Player "+players.get(i).getNum()+" has called");
							pot += bet;
							flag++;//new
						}
						else if (newBet > bet){
							System.out.println("Player "+players.get(i).getNum()+" has raised. The new bet is now "+newBet);
							pot = pot + newBet - bet;
							bet = newBet;
							flag = 1;//new
						} else if (newBet == 0){
							System.out.println("Player "+players.get(i).getNum()+" has folded");
							numPlayers--;//new
						}
						System.out.println("Player "+players.get(i).getNum()+" now has "+players.get(i).getTotal()+ " dollars");
						System.out.println();
						}                                                   
					}
					if (flag == numPlayers || numPlayers == 1){
						break;
					}
				} 
				if (numPlayers == 1){
					flag = 1;
				}
		}
				
		//STEP 10: THE SHOWDOWN
/*		for (Player player: players){
			dealer.determineHand(player);
		} CONDENSED VERSION OF CODE. WILL NEED TO UPDATE*/
			
			//TESTING CODE
/*			playerIter = players.iterator();
			while(playerIter.hasNext())
			{
				Collections.sort(cards, Collections.reverseOrder());
			}           */
		
	/*	i=1;
		playerIter = players.iterator();
		while(playerIter.hasNext())
		{
			currPlayer = playerIter.next();
			dealer.determineHand(currPlayer);
			System.out.println("Player " +i+" has a " + currPlayer.getScore());
		}*/

		for (Player player: players){
			if (player.fold == false){
				dealer.determineHand(player);
				System.out.println("Player "+player.getNum()+" has a "+ player.getScore());
			}
		}
		
		if(players.get(0).getScore() > players.get(1).getScore()){
			System.out.println("Player 1 won!");
			players.get(0).setTotal(players.get(0).getTotal() +  pot);
		} else if (players.get(0).getScore() < players.get(1).getScore()){
			System.out.println("Player 2 won!");
			players.get(1).setTotal(players.get(1).getTotal() +  pot);
		} else {
			if (players.get(0).hand.get(0).rank > players.get(1).hand.get(0).rank){
				System.out.println("Player 1 won!");
				players.get(0).setTotal(players.get(0).getTotal() +  pot);
			} else if (players.get(0).hand.get(0).rank < players.get(1).hand.get(0).rank){
				System.out.println("Player 2 won!");
				players.get(1).setTotal(players.get(1).getTotal() +  pot);
			} else {
				if (players.get(0).highCard.rank > players.get(1).highCard.rank){
					System.out.println("Player 1 won!");
					players.get(0).setTotal(players.get(0).getTotal() +  pot);
				} else {
					System.out.println("Player 2 won!");
					players.get(1).setTotal(players.get(1).getTotal() +  pot);
				}
			}
		}
		
		
		System.out.println("player "+ players.get(0).getNum()+": " + players.get(0).getScore()+" and Player"+ players.get(1).getNum()+": "+ players.get(1).getScore());

		for (Player player: players){
			System.out.println("Player "+player.getNum()+ " now has "+player.getTotal()+" dollars");
		}
	} 	

}
