package com.example.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.widget.TextView;



	@SuppressLint("HandlerLeak")
	public class GameScreen extends Activity {
		
		static TextView cardOne;
		static TextView cardTwo;
		static TextView turn;
		static Handler handler;
		MyHandle threadHandler;
		Object myLock=new Object();
		int bet,minBet,pot;
		
		@SuppressLint("HandlerLeak")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.gamescreen);      
			cardOne = (TextView) findViewById(R.id.card_one);
			cardTwo = (TextView) findViewById(R.id.card_two);
			turn = (TextView) findViewById(R.id.turn);
			//send server a message 
							
			handler=new Handler(){

				@Override
				public void handleMessage(Message msg) { //Handle code for receiving messages 
					super.handleMessage(msg);
					Bundle bundle=msg.getData();
					if(bundle.getInt("b") == 4){
						//Assign the card values
						System.out.println("Should be getting some cards");
						cardOne.setText(String.valueOf(bundle.getInt("cardOne")));
						cardTwo.setText(String.valueOf(bundle.getInt("cardTwo")));
						
					}else if(bundle.getInt("b") == -2){
						cardOne.setText("Waiting for other players");
						checkServerStatus();
					}else if(bundle.getInt("b") == 5){
						//Get the turn value
						if (bundle.getInt("turn") == 1){
							turn.setText("Yes");
							//enable buttons
						} else {
							turn.setText("No");
							//cancel buttons
						}
					}else if(bundle.getInt("b") == 6){
						//display message saying "player ___ bet ___
						//update the minimum bet (if necessary)
						//update the pot
						//indicate whose turn it is 
					}
				}
				
			};			
		}
		
		protected void onResume(){
			super.onResume();
			checkServerStatus();
		}
		
		//on click for send button needed 
		//method should send bet info to the server
		//will send just an integer indicating the bet
			/*button.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View arg0) {
					Message msg=Message.obtain();
					Bundle bundle=new Bundle();
					bundle.putInt("type", 6);
					bundle.putInt("bet", bet);
					msg.setData(bundle);
					ServerService.threadHandler.sendMessage(msg);
				}
				});*/

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		
		public class MyHandle extends Handler{  //Handle for sending a message on a different thread
			public MyHandle(){
				
			}
			public MyHandle(Looper looper){ /*constructor*/
				super(looper);
			}
		}

		 public static void setCard(int card, int num){
			 if (card == 1){
				 cardOne.setText(num);
			 } else {
				 cardTwo.setText(num);
			 }
		 }
		
		 
		 public void checkServerStatus(){
			 Message msg=Message.obtain();
				Bundle bundle=new Bundle();
				bundle.putInt("type", 4);
				msg.setData(bundle);
				ServerService.threadHandler.sendMessage(msg);
		 }
}
