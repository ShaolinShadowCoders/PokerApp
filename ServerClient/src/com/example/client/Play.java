package com.example.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
		                case -2: 
		                	bundle.putInt("b", message.getb());
		                	msg.setData(bundle);
				            GameScreen.handler.sendMessage(msg);
				            break;
		                case 2:
		                	bundle.putInt("int", message.getb());
			              //bundle.putString("string", message.getUsername());
			                bundle.putInt("valid", message.getValid());
			                msg.setData(bundle);
			                MainActivity.handler.sendMessage(msg);
		                	break;
		                case 3:
		                	bundle.putInt("int", message.getb());			               
				            bundle.putInt("ready", message.getReady());
				            msg.setData(bundle);
			                ReadyScreen.handler.sendMessage(msg);
		                	break;
		                case 4:
		                	bundle.putInt("b", message.getb());
		                	bundle.putInt("cardOne", message.getCardOne());
		                	bundle.putInt("cardTwo", message.getCardTwo());
		                	  msg.setData(bundle);
				              GameScreen.handler.sendMessage(msg);
		                	break;
		                case 5:
		                	bundle.putInt("b", message.getb());
		                	bundle.putInt("turn", message.getTurn());
		                	msg.setData(bundle);
				            GameScreen.handler.sendMessage(msg);
				            break;
		                case 6:
		                	bundle.putInt("b", message.getb());
		                	bundle.putInt("bet", message.getBet());
		                	bundle.putInt("pot", message.getPot());
		                	bundle.putInt("minBet", message.getMinBet());
		                	bundle.putInt("turn", message.getTurn());
		                }
					}
				}
			}
			readyKeySet.clear();
	}
	
}
	


