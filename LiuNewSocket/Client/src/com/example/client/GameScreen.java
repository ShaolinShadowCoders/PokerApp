
	package com.example.client;

	import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.widget.TextView;

	public class GameScreen extends Activity {
		
		TextView cardOne;
		TextView cardTwo;
		TextView turn;
		Handler handler;
		MyHandle threadHandler;
		Connect connect=null;
		Object myLock=new Object();
		boolean gameTime = false;
		boolean flag = true;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.gamescreen);      
			cardOne = (TextView) findViewById(R.id.card_one);
			cardTwo = (TextView) findViewById(R.id.card_two);
			turn = (TextView) findViewById(R.id.turn);
			System.out.println("Start of GameScreen");
			
			handler=new Handler(){

				@Override
				public void handleMessage(Message msg) { //Handle code for receiving messages 
					super.handleMessage(msg);
					Bundle bundle=msg.getData();
					System.out.println("on the game screen receiving message");
					
					switch(bundle.getInt("b")){
					case 4: 
						//Assign the card values
						cardOne.setText(bundle.getInt("cardOne"));
						cardTwo.setText(bundle.getInt("cardTwo"));
						break;
					case 5:
						if (bundle.getInt("turn") == 1)
							turn.setText("Yes");
						 else 
							turn.setText("No");
						break;
					case -1:
						//cardOne.setText("Current Game in Progress");
						//send back to the ready screen
						break;
					case -2:
						cardOne.setText("Waiting for other players");
						break;
					}
					
				}
				
			};
			
			
			Thread receiveThread=new Thread(
					
					new Runnable() {
						public void run() {
							connect=new Connect();
							try {
								while(true){
									if(connect.selector.select()==0)
										continue;
									connect.play();
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						};
					}
				);
			receiveThread.start();
			
			Thread sendThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Looper.prepare();
					threadHandler=new MyHandle(Looper.myLooper());
					Looper.loop();

				}
			});
			sendThread.start();
			
			//send server a message
			Message msg=Message.obtain(); 
			//while(gameTime == false){
			Bundle bundle=new Bundle();
			msg.setData(bundle);
			System.out.println("Going to send the message");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			threadHandler.sendMessage(msg);
			//}
			
		}

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
			
			public void handleMessage(Message msg){
				
				MyMessage message=new MyMessage();
				message.setb((byte)4);			
				try {
					System.out.println("Does it write to the server before the crash?");
					connect.sendBuffer.clear();
					connect.sendBuffer.put(message.Message2Byte());
					connect.sendBuffer.flip();
					connect.socketChannel.write(connect.sendBuffer);
					//msg.recycle();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			
			}
		}
		
		public void onStart(){
			super.onStart();
			
		}
		
		public class Connect {
			SocketChannel socketChannel=null;
			public ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
			ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
			Selector selector;
			public Connect() {
				// TODO Auto-generated constructor stub
				try {
					socketChannel=SocketChannel.open();
					SocketAddress remoteAddress=new InetSocketAddress("192.168.2.11", 20001);
					socketChannel.connect(remoteAddress);
					socketChannel.configureBlocking(false);
					System.out.println("Connected");
				    selector=Selector.open();
				    socketChannel.register(selector, SelectionKey.OP_READ);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			public void play() throws IOException{
				
				//socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
					Set readyKeySet=selector.selectedKeys();
					Iterator<SelectionKey> iterator=readyKeySet.iterator();
					SelectionKey key=null;
					while(iterator.hasNext()){
						key=(SelectionKey) iterator.next();
						MyMessage message;
						if(key.isReadable()){ //receive message from server
							receiveBuffer.clear();
							int count=socketChannel.read(receiveBuffer);
							if (count > 0) {  
								receiveBuffer.flip();  
				                message = MyMessage.byte2Message(receiveBuffer.array());  
				                Message msg=Message.obtain();
				                Bundle bundle=new Bundle();
				                if (message.getb() == 4){ //Cards
				                	gameTime = true;
				                	bundle.putInt("b", message.getb());
				                	bundle.putInt("cardOne", message.getCardOne());
				                	bundle.putInt("cardTwo", message.getCardTwo());
				                } else if (message.getb() == 5){
				                	bundle.putInt("b", message.getb());
				                	bundle.putInt("turn", message.getTurn());
				                }else if(message.getb() == -2){
				                	bundle.putInt("b", message.getb());
				                }
				                msg.setData(bundle);
				                handler.sendMessage(msg);
							}
						}
					}
					readyKeySet.clear();
			}
		}

		
	}
