package com.example.client;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

public class GameView extends View {

	private Bitmap player0Card1;
	private Bitmap player0Card2;
	private Bitmap player1Card1;
	private Bitmap player1Card2;
	private Bitmap player2Card1;
	private Bitmap player2Card2;
	private Bitmap player3Card1;
	private Bitmap player3Card2;
	private Bitmap player4Card1;
	private Bitmap player4Card2;
	private Bitmap player5Card1;
	private Bitmap player5Card2;
	private Bitmap flop1;
	private Bitmap flop2;
	private Bitmap flop3;
	private Bitmap turn;
	private Bitmap river;
	private Bitmap deck;
	private Bitmap call;
	private Bitmap raise;
	private Bitmap fold;
	private Bitmap callD;
	private Bitmap raiseD;
	private Bitmap foldD;

	private int screenW;
	private int screenH;
	private float scale;
	private int scaledCardW;
	private int scaledCardH;
	private int drawCase;
	
	private Paint text;
	private Paint namesPaint;
	private int cardNum;
	private String messageString;
	private int bet=0;
	private int pot=0;
	
	private String player0;
	private String player1;
	private String player2;
	private String player3;
	private String player4;
	private String player5;
	private int chipCount=0;

	private Context myContext;
	private Boolean pressed1 = false;
	private Boolean pressed2 = false;
	private Boolean pressed3 = false;
	
	public GameView(Context context) {
		super(context);
		//saving a reference to context
		myContext = context;
		
		//load Bitmap into memory 
		player0Card1 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player0Card2 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player1Card1 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player1Card2 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player2Card1 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player2Card2 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player3Card1 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player3Card2 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player4Card1 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player4Card2 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player5Card1 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		player5Card2 = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		deck = BitmapFactory.decodeResource(getResources(),  R.drawable.card_back);
		
		call = BitmapFactory.decodeResource(getResources(),  R.drawable.call);
		raise = BitmapFactory.decodeResource(getResources(),  R.drawable.raise);
		fold = BitmapFactory.decodeResource(getResources(),  R.drawable.fold);  
		callD = BitmapFactory.decodeResource(getResources(),  R.drawable.call_d);
		raiseD = BitmapFactory.decodeResource(getResources(),  R.drawable.raised);
		foldD = BitmapFactory.decodeResource(getResources(),  R.drawable.fold_d);
		
		scale = myContext.getResources().getDisplayMetrics().density;
		
		//creates new Paint object
		text = new Paint();
		//anti-aliase make the text look smoother 
		text.setAntiAlias(true);
		//font color set to white and alignment left
		text.setColor(Color.WHITE);
		text.setStyle(Paint.Style.STROKE);
		text.setTextAlign(Paint.Align.RIGHT);
		text.setTextSize(scale*15);
		
		//creates new Paint object
		namesPaint = new Paint();
		//anti-aliase make the text look smoother 
		namesPaint.setAntiAlias(true);
		//font color set to white and alignment left
		namesPaint.setColor(Color.WHITE);
		namesPaint.setStyle(Paint.Style.STROKE);
		namesPaint.setTextAlign(Paint.Align.LEFT);
		namesPaint.setTextSize(scale*15);

	}
	
	
	//getting the values of width and height of the screen
	//loading scaled bitmaps
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
		
		//scale Bitmaps
		player0Card1 = initCards(player0Card1);
		player0Card2 = initCards(player0Card2);
		player1Card1 = initCards(player1Card1);
		player1Card2 = initCards(player1Card2);
		player2Card1 = initCards(player2Card1);
		player2Card2 = initCards(player2Card2);
		player3Card1 = initCards(player3Card1);
		player3Card2 = initCards(player3Card2);
		player4Card1 = initCards(player4Card1);
		player4Card2 = initCards(player4Card2);
		player5Card1 = initCards(player5Card1);
		player5Card2 = initCards(player5Card2);
		deck = initCards(deck);
		
		call = initButton(call);
		raise = initButton(raise);
		fold = initButton(fold);
		callD = initButton(callD);
		raiseD = initButton(raiseD);
		foldD = initButton(foldD);
		
		
	}
	
	//canvas placements
	@Override
	protected void onDraw(Canvas canvas){
		//draw Bitmaps to screen
		//text
		canvas.drawText( messageString, screenW*19/20, screenH*1/18, text);
		canvas.drawText( "Bet: " + Integer.toString(bet), screenW*19/20, screenH*3/18, text);
		canvas.drawText( "Pot: " + Integer.toString(pot), screenW*7/20, screenH*7/18, namesPaint);
		
		//player name
		canvas.drawText( player0 + ": "+ chipCount, (screenW*5/13)-player0Card1.getWidth(), (screenH*11/14), namesPaint);
		canvas.drawText( player1, (screenW/9)-player1Card1.getWidth(), (screenH*11/14), namesPaint);
		canvas.drawText( player2, (screenW/9)-player2Card1.getWidth(), (screenH*2/16), namesPaint);
		canvas.drawText( player3, (screenW*5/13)-player3Card1.getWidth(), (screenH*2/16), namesPaint);
		canvas.drawText( player4, (screenW*7/11)-player4Card1.getWidth(), (screenH*2/16), namesPaint);
		canvas.drawText( player5, (screenW*7/11)-player5Card1.getWidth(), (screenH*11/14), namesPaint);
		
		//cards
		canvas.drawBitmap(player0Card1, (float)((float)(screenW*5/13)-player0Card1.getWidth()), (float)(screenH*4/5), null);	
		canvas.drawBitmap(player0Card2, (float)((float)((screenW*5/13)+(player0Card2.getWidth())/20)), (float)(screenH*4/5), null);	

		canvas.drawBitmap(player1Card1, (float)((float)(screenW/9)-player1Card1.getWidth()), (float)(screenH*4/5), null);	
		canvas.drawBitmap(player1Card2, (float)((float)((screenW/9)+(player1Card2.getWidth())/20)), (float)(screenH*4/5), null);			

		canvas.drawBitmap(player2Card1, (float)((float)(screenW/9)-player2Card1.getWidth()), (float)(screenH*2/15), null);	
		canvas.drawBitmap(player2Card2, (float)((float)((screenW/9)+(player2Card2.getWidth())/20)), (float)(screenH*2/15), null);			

		canvas.drawBitmap(player3Card1, (float)((float)(screenW*5/13)-player3Card1.getWidth()), (float)(screenH*2/15), null);	
		canvas.drawBitmap(player3Card2, (float)((float)((screenW*5/13)+(player3Card2.getWidth())/20)), (float)(screenH*2/15), null);	
		
		canvas.drawBitmap(player4Card1, (float)((float)(screenW*7/11)-player4Card1.getWidth()), (float)(screenH*2/15), null);	
		canvas.drawBitmap(player4Card2, (float)((float)((screenW*7/11)+(player4Card2.getWidth())/20)), (float)(screenH*2/15), null);
		
		canvas.drawBitmap(player5Card1, (float)((float)(screenW*7/11)-player5Card1.getWidth()), (float)(screenH*4/5), null);	
		canvas.drawBitmap(player5Card2, (float)((float)((screenW*7/11)+(player5Card2.getWidth())/20)), (float)(screenH*4/5), null);
		
		canvas.drawBitmap(deck, (float)((float)(screenW*1/8)-deck.getWidth()), (float)(screenH*4/9), null);
		
		//determining cards to draw
		switch(drawCase){
		case 3:
			canvas.drawBitmap(flop1, (float)((float)((screenW*1/8)+(flop2.getWidth())/5)), (float)(screenH*4/9), null);	
			break;
		case 4:
			canvas.drawBitmap(flop2, (float)((float)((screenW*2/8)+(flop2.getWidth())/10)), (float)(screenH*4/9), null);
			break;
		case 5:
			canvas.drawBitmap(flop3, (float)((float)((screenW*3/8)+(flop3.getWidth())/15)), (float)(screenH*4/9), null);
			break;
		case 6:
			canvas.drawBitmap(turn, (float)((float)((screenW*4/8)+(turn.getWidth())/15)), (float)(screenH*4/9), null);
			break;
		case 7:
			canvas.drawBitmap(river, (float)((float)((screenW*5/8)+(river.getWidth())/15)), (float)(screenH*4/9), null);
			break;
		}
		
		//determining button states
		if (pressed1) {
			canvas.drawBitmap(callD, (float)((float)(screenW*19/20)-call.getWidth()), (float)(screenH*5/15), null);
		} else {
			canvas.drawBitmap(call, (float)((float)(screenW*19/20)-call.getWidth()), (float)(screenH*5/15), null);	
		}
		if (pressed2) {
			canvas.drawBitmap(raiseD, (float)((float)(screenW*19/20)-raise.getWidth()), (float)(screenH*9/16), null);
		} else {
			canvas.drawBitmap(raise, (float)((float)(screenW*19/20)-raise.getWidth()), (float)(screenH*9/16), null);	
		}
		if (pressed3) {
			canvas.drawBitmap(foldD, (float)((float)(screenW*19/20)-fold.getWidth()), (float)(screenH*4/5), null);
		} else {
			canvas.drawBitmap(fold, (float)((float)(screenW*19/20)-fold.getWidth()), (float)(screenH*4/5), null);
		}
		

	}
	
	//THE BUTTONS!
	public boolean onTouchEvent(MotionEvent event){
		int eventaction = event.getAction();
		int X = (int)event.getX();
		int Y = (int)event.getY();
		
		switch (eventaction){
		case MotionEvent.ACTION_DOWN:
			//check horizontal and vertical bounds
			if ((X > ((screenW*19/20)-call.getWidth())) && 
				(X < (((screenW*19/20)-call.getWidth()) + call.getWidth())) && 
				(Y > (int)(screenH*5/15)) && 
				(Y < (int)((screenH*5/15) + call.getHeight()))) {
	        	pressed1 = true;
	        }else if((X > ((screenW*19/20)-raise.getWidth())) && 
					(X < (((screenW*19/20)-raise.getWidth()) + raise.getWidth())) && 
					(Y > (int)(screenH*9/16)) && 
					(Y < (int)((screenH*9/16) + raise.getHeight()))){
	        	pressed2 = true;
	        }else if((X > ((screenW*19/20)-fold.getWidth())) && 
					(X < (((screenW*19/20)-fold.getWidth()) + fold.getWidth())) && 
					(Y > (int)(screenH*4/5)) && 
					(Y < (int)((screenH*4/5) + fold.getHeight()))){
	        	pressed3 = true;
	        }
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (pressed1) {
				GameScreen.btn = 1;
        	}else if(pressed2){
				GameScreen.btn = 2;
        	}else if(pressed3){
				GameScreen.btn = 3;
        	}
			pressed1 = false;
			pressed2 = false;
			pressed3 = false;
			break;
		}
		invalidate();
	//		return true;
		return false;
	};
	
	
	//scale bitmaps relative to screen size
	private Bitmap initCards(Bitmap card) {		
		//set scaled values for the bitmap of the card
		scaledCardW = (int) (screenW/13); //width is 1/13 the screen width
		scaledCardH = (int) (scaledCardW*1.35714285714); //height is 1.35714285714 times the width
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(card, scaledCardW, scaledCardH, false); 
				
		return scaledBitmap; 
	}
	
	//scale buttons relative to screen size
	private Bitmap initButton(Bitmap button) {		
		//set scaled values for the bitmap of the button
		scaledCardW = (int) (screenW/5); //width is 1/13 the screen width
		scaledCardH = (int) (scaledCardW*0.3409); //height is 2.9333333 times the width
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(button, scaledCardW, scaledCardH, false); 
				
		return scaledBitmap; 
	}
	
	public void addCards(int num, int caseNum){
		//determining suit and card number
		if (num >= 0 && num <= 12){
			this.cardNum = 100+(num+2);
		} else if (num >= 13 && num <= 25){
			this.cardNum = 200+(num-11);
		} else if (num >= 26 && num <= 38){
			this.cardNum = 300+(num-24);
		} else if (num >= 39 && num <= 51){
			this.cardNum = 400+(num-37);
		} 
		
		//get filePath id based on filename and load the bitmap
		int filePath = getResources().getIdentifier("card" + cardNum, "drawable", myContext.getPackageName());
		
		//determining which card to update
		drawCase = caseNum;
		switch(caseNum){
			case 1: 
				player0Card1 = BitmapFactory.decodeResource(myContext.getResources(),  filePath);
				player0Card1 = initCards(player0Card1);
				break;
			case 2:
				player0Card2 = BitmapFactory.decodeResource(myContext.getResources(),  filePath);
				player0Card2 = initCards(player0Card2);
				break;
			case 3:
				flop1 = BitmapFactory.decodeResource(myContext.getResources(),  filePath);
				flop1 = initCards(flop1);
				break;
			case 4:
				flop2 = BitmapFactory.decodeResource(myContext.getResources(),  filePath);
				flop2 = initCards(flop2);
				break;
			case 5:
				flop3 = BitmapFactory.decodeResource(myContext.getResources(),  filePath);
				flop3 = initCards(flop3);
				break;
			case 6:
				turn = BitmapFactory.decodeResource(myContext.getResources(),  filePath);
				turn = initCards(turn);
				break;
			case 7:
				river = BitmapFactory.decodeResource(myContext.getResources(),  filePath);
				river = initCards(river);
				break;
		}
		
	}
	
	//all the text displays
	public void displayText(String text, int bet, int pot){
		this.messageString = text;
		this.bet = bet;
		this.pot = pot;
	}
	
	public void displayName0(String name){
		this.player0 = name;
	}
	
	public void displayName1(String name){
		this.player1 = name;
	}
	
	public void displayName2(String name){
		this.player2 = name;
	}
	
	public void displayName3(String name){
		this.player3 = name;
	}
	
	public void displayName4(String name){
		this.player4 = name;
	}
	
	public void displayName5(String name){
		this.player5 = name;
	}

	public void displayChipCount(int chip){
		this.chipCount = chip;
	}
}