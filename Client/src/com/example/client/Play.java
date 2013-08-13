package com.example.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//this class is used to receive a message from the server.  It parses through the data to determine 
//what type of message it should be, bundles up the information, and sends it to the appropriate 
//handler to be further processed by the client
public class Play {
	
	public Play(){
	}
	
	
	Handler handler;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void play() throws IOException{
		
			Set readyKeySet=ServerService.selector.selectedKeys();
			Iterator<SelectionKey> iterator=readyKeySet.iterator();
			SelectionKey key=null;
			while(iterator.hasNext()){
				key=(SelectionKey) iterator.next();
				MyMessage message;
				if(key.isReadable()){ //receive message from server
					ServerService.receiveBuffer.clear();
					int count=ServerService.socketChannel.read(ServerService.receiveBuffer);
					if (count > 0) {  
						ServerService.receiveBuffer.flip();  
		                message = MyMessage.byte2Message(ServerService.receiveBuffer.array());  
		                Message msg=Message.obtain();
		                Bundle bundle=new Bundle();
		                switch (message.getb()){
		                case -2: /*waiting for other players to join*/
		                	bundle.putInt("b", message.getb());
		                	msg.setData(bundle);
				            GameScreen.handler.sendMessage(msg);
				            break;
		                case 2: /*returns database result from server*/
		                	bundle.putInt("int", message.getb());
			                bundle.putInt("valid", message.getValid());
			                msg.setData(bundle);
			                MainActivity.handler.sendMessage(msg);
		                	break;
		                case 3:/*gets the ready result from server */
		                	bundle.putInt("int", message.getb());			               
				            bundle.putInt("ready", message.getReady());
				            msg.setData(bundle);
			                ReadyScreen.handler.sendMessage(msg);
		                	break;
		                case 4: /*gets 1st 2 private cards */
		                	bundle.putInt("b", message.getb());
		                	bundle.putInt("numCards", message.getNumCards());
		                	switch (message.getNumCards()){//Added
		                		case 1:
		                			bundle.putInt("publicCard",message.getSinPubCard());
		                			break;
		                		case 2:
		                			bundle.putInt("cardOne", message.getCardOne());
				                	bundle.putInt("cardTwo", message.getCardTwo());
				                	break;
		                		case 3:
		                			bundle.putInt("card1", message.getPubCard1());
				                	bundle.putInt("card2", message.getPubCard2());
				                	bundle.putInt("card2", message.getPubCard2());
				                	break;
		                	}
		         /*       	bundle.putInt("cardOne", message.getCardOne());
		                	bundle.putInt("cardTwo", message.getCardTwo());*/
		                	msg.setData(bundle);
				            GameScreen.handler.sendMessage(msg);
		                	break;
		                case 5:/*bet and turn info */
		                	bundle.putInt("b", message.getb());
		                	bundle.putInt("turn", message.getTurn());
		                	bundle.putString("username", message.getUsername());
		                	bundle.putInt("move", message.getMove());
		                	bundle.putInt("minBet", message.getMinBet());
		                	bundle.putInt("pot", message.getPot());
		                	msg.setData(bundle);
				            GameScreen.handler.sendMessage(msg);
				            break;
		                case 6: //Score request 
		                	bundle.putInt("b", message.getb());
		                	msg.setData(bundle);
		                	GameScreen.handler.sendMessage(msg);
				            break;
		                case 7: //Score for who won the hand
		                	if(message.getUsername() != null)
		                		bundle.putString("username", message.getUsername());
		                	else 
		                		bundle.putString("String", message.getString());
		                	bundle.putInt("score", message.getScore());
		                	bundle.putInt("hand", message.getHand());
		                	bundle.putInt("high", message.getHigh());
		                	msg.setData(bundle);
		                	GameScreen.handler.sendMessage(msg);
		                }
					}
				}
			}
			readyKeySet.clear();
	}
	
}
	


