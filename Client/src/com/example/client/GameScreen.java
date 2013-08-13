package com.example.client;

import java.util.ArrayList;

import com.example.gamescreentest.GameView;
import com.example.gamescreentest.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class GameScreen extends Activity {

	Button call, raise, fold;
	static TextView cardOneText, cardTwoText, turn, move, dealerHand, river, turnText;
	static int flopOne, flopTwo, flopThree, turnOne = -1, riverOne;
	Card cardOne, cardTwo, flopOneCard, flopTwoCard, flopThreeCard, turnOneCard, riverOneCard;
	Card[] dealerCards;
	static Handler handler;
	MyHandle threadHandler;
	Object myLock = new Object();
	int bet, minBet, pot;
	int amount = 0;
	ArrayList<Card> fullHand = new ArrayList<Card>();
	Player player = new Player();
	GameView gameView;
	static int btn;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //create a new view
        gameView = new GameView(this);
        gameView.setBackgroundResource(R.drawable.android_screen); //set view background
        gameView.setKeepScreenOn(true); //screen time out
        //full screen display
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					 WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(gameView); //launch view

		//setContentView(R.layout.gamescreen);
		
		
		cardOneText = (TextView) findViewById(R.id.card_one);
		cardTwoText = (TextView) findViewById(R.id.card_two);
		turn = (TextView) findViewById(R.id.turn);
		move = (TextView) findViewById(R.id.move);
		dealerHand = (TextView) findViewById(R.id.dealerHand);
		river = (TextView) findViewById(R.id.river);
		turnText = (TextView) findViewById(R.id.turnText);
		call = (Button) findViewById(R.id.call);
		raise = (Button) findViewById(R.id.raise);
		fold = (Button) findViewById(R.id.fold);
		player.setUser(MainActivity.str);
		final Toast toast = null;
		
		checkServerStatus();
		
		handler = new Handler() {


			public void sendMessage(int move) {
				Message msg = Message.obtain();
				Bundle bundle = new Bundle();
				bundle.putInt("type", 5);
				bundle.putInt("move", move);
				bundle.putString("username", player.getUser());
				bundle.putInt("minBet", minBet);
				msg.setData(bundle);
				ServerService.threadHandler.sendMessage(msg);
			}
			
			@Override
			public void handleMessage(Message msg) { // Handle code for
				btn = 0;									 // receiving messages from server
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				switch (bundle.getInt("b")) {
				case 4:
					switch (bundle.getInt("numCards")){
					case 1:
						if (turnOne == -1){
							turnOne = bundle.getInt("publicCard");//Assign
							turnOneCard = new Card(turnOne);
							fullHand.add(turnOneCard);//Add to cards
							turnText.setText(String.valueOf("4: " + turnOneCard.printRank() + " of " + turnOneCard.printSuit()));
							gameView.addCards(turnOne, 6);
						} else{
							riverOne = bundle.getInt("publicCard");//Assign
							riverOneCard = new Card(riverOne);
							fullHand.add(riverOneCard);//Add to cards
							river.setText(String.valueOf("5: " + riverOneCard.printRank() + " of " + riverOneCard.printSuit()));
							gameView.addCards(riverOne, 7);
						}
						gameView.invalidate();
						break;
					case 2:
						// Create the cards
						cardOne = new Card(bundle.getInt("cardOne"));
						//showCard(cardOne);
						cardTwo = new Card(bundle.getInt("cardTwo"));
						//showCard(cardTwo);
						//Set the cards to the player
						player.setCard(cardOne, 1);
						player.setCard(cardTwo, 2);
						//Prints the card values
						cardOneText.setText(String.valueOf(player.getCard(1).printRank()+" of "+player.getCard(1).printSuit()));
						cardTwoText.setText(String.valueOf(player.getCard(2).printRank()+" of "+player.getCard(2).printSuit()));
						//Adds cards to hand
						fullHand.add(cardOne);
						fullHand.add(cardTwo);
						
						gameView.addCards(bundle.getInt("cardOne"), 1);
						gameView.addCards(bundle.getInt("cardTwo"), 2);
						gameView.invalidate();
						break;
						
					case 3:
						flopOne = bundle.getInt("card1");//Assign
						flopOneCard = new Card(flopOne);
						fullHand.add(flopOneCard);//Add to cards
						flopTwo = bundle.getInt("card2");//Assign
						flopTwoCard = new Card(flopTwo);
						fullHand.add(flopTwoCard);//Add to cards
						flopThree = bundle.getInt("card3");//Assign
						flopThreeCard = new Card(flopThree);
						fullHand.add(flopThreeCard);//Add to cards
						dealerHand.setText(String.valueOf("1: " + flopOneCard.printRank() + " of " + flopOneCard.printSuit() + " 2:"  + flopTwoCard.printRank() + " of " + flopTwoCard.printSuit() + " 3:"  + flopThreeCard.printRank() + " of " + flopThreeCard.printSuit()));
						gameView.addCards(flopOne, 3);
						gameView.addCards(flopTwo, 4);
						gameView.addCards(flopThree, 5);
						gameView.invalidate();
						break;
					}
				case -2:
					checkServerStatus();
					break;
				case 5:
					minBet = bundle.getInt("minBet");
					// display message saying "player ___ bet ___//
					switch (bundle.getInt("move")) {
						case 7:// indicates no one has made a move yet
							move.setText(String.valueOf("No one has made a move yet."));
							break;
						case 1:// indicates that player called/checked
							move.setText(String.valueOf(bundle
								.getString("username")
								+ " called for "
								+ bundle.getInt("minBet")
								+ " pot is now "
								+ bundle.getInt("pot")));
							break;
						case 2:// indicates that player raised the pot
						move.setText(String.valueOf(bundle
								.getString("username")
								+ " raised for "
								+ bundle.getInt("minBet")
								+ " pot is now "
								+ bundle.getInt("pot")));
							break;
						case 3:// indicates that player folded
							move.setText(String.valueOf(bundle
								.getString("username") + " folded")
								+ " pot is still " + bundle.getInt("pot"));
							break;
						case 4:// indicates that player quit
							move.setText(String.valueOf(bundle
								.getString("username") + " quit"));
							break;
					}

					// Get the turn value
					if (bundle.getInt("turn") == 1) {
						turn.setText("Yes");
						gameView.setEnabled(true);
						while(btn == 0){
							
						}
						switch (btn){
						case 1:
							sendMessage(1);
							break;
						case 2:
							raise();
							sendMessage(2);
							break;
						case 3:
							player.fold();
							sendMessage(3);
							break;
						}
					} else {
						turn.setText("No");
						gameView.setEnabled(false);
					}
					break;

				case 6:
					//determines winning hand
					player.determineHand(fullHand);
					Message score = Message.obtain();
					Bundle scoreBundle = new Bundle();
					scoreBundle.putInt("b", 6);
					scoreBundle.putInt("score", player.getScore());
					scoreBundle.putInt("hand", player.hand.get(0).getRank());
					scoreBundle.putInt("high", player.getHighCard());
					scoreBundle.putString("username", player.getUser());
					scoreBundle.putInt("chipCount", player.getChipCount());
					score.setData(bundle);
					ServerService.threadHandler.sendMessage(score);
					break;
				case 7: // DISPLAY WINNER
					//unpack the message
					//store in local variables
					String winner = null, scoreString = null, handString = null, highString = null;
					if(bundle.containsKey("username")){
						winner =  bundle.getString("username");
					}else winner = bundle.getString("string");
					
					int winnerScore  = bundle.getInt("score");
					int hand = bundle.getInt("hand");
					//convert into what the hand is ex(queen high straight)
					int high = bundle.getInt("high");
					//display results to user 
					
					switch (winnerScore) {
					case 1:
						scoreString = "High Card";
						break;
					case 2:
						scoreString = "One Pair";
						break;
					case 3:
						scoreString = "Two Pair";
						break;
					case 4:
						scoreString = "Three of a Kind";
						break;
					case 5:
						scoreString = "Straight";
						break;
					case 6:
						scoreString = "Flush";
						break;
					case 7:
						scoreString = "Full House";
						break;
					case 8:
						scoreString = "Four of a Kind";
						break;
					case 9:
						scoreString = "Straight Flush";
						break;
					}
					
					handString = getTextOfCard(hand);
					highString = getTextOfCard(high);
					
						Toast.makeText(GameScreen.this, winner + "has won with a " + handString + " high " + scoreString + ", and a high pocket card of " + highString,
                                Toast.LENGTH_LONG).show();
					//exit the game
					
					break;
				default:
					break;
				}
			}
		};
	}

/*	public void bet() {
		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}

		});
		raise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		fold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			
			}
		});
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class MyHandle extends Handler { // Handle for sending a message on a
											// different thread
		public MyHandle() {

		}

		public MyHandle(Looper looper) { /* constructor */
			super(looper);
		}
	}

	public static void setCard(int card, int num) {
		if (card == 1)
			cardOneText.setText(num);
		else
			cardTwoText.setText(num);
	}

	public void checkServerStatus() {
		Message msg = Message.obtain();
		Bundle bundle = new Bundle();
		bundle.putInt("type", 4);
		msg.setData(bundle);
		ServerService.threadHandler.sendMessage(msg);
	}

	public void raise() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText bet = new EditText(this);
		bet.setHint("Bet");
		bet.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setMessage("Enter Bet in Increment of 5!");
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(bet);
		builder.setView(layout);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (Integer.parseInt(bet.getText().toString()) % 5 != 0) {
						alertMessage(1);

					} else if (minBet > Integer.parseInt(bet.getText()
							.toString())) {
						alertMessage(2);
					} else {
						minBet = Integer.parseInt(bet.getText().toString());
						player.setChipCount(player.getChipCount() - minBet);
						break;
					}
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}

		};
		builder.setPositiveButton("Bet", dialogClickListener);
		builder.setNegativeButton("Cancel", dialogClickListener).show();
	}

	public void alertMessage(int num) {
		android.app.AlertDialog.Builder show = null;
		switch (num) {
		case 1:
			show = new AlertDialog.Builder(this).setTitle("Error")
					.setMessage("Bet not increment of 5")
					.setNeutralButton("close", null);
			break;
		case 2:
			show = new AlertDialog.Builder(this).setTitle("Error")
					.setMessage("Bet is smaller than original bet")
					.setNeutralButton("close", null);
			break;
		}
		show.show();
	}
	
	
	
	public String getTextOfCard(int rank){
		String result = null;
		switch (rank){
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
		
}


