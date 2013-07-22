package com.example.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class ServerService extends Service {
	
	Handler handler;
	static MyHandle threadHandler;
	Connect connect=null;
	Object myLock=new Object();
	static SocketChannel socketChannel=null;
	public ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
	static ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
	static Selector selector;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
	//	Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
Thread receiveThread=new Thread(
				
				new Runnable() {
					public void run() {
						connect=new Connect();
						try {
							while(true){
								if(connect.selector.select()==0)
									continue;
								Play receivedMessage = new Play();
								receivedMessage.play();
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
		
	
		
		class Connect {
		
			public Connect() {
				// TODO Auto-generated constructor stub
				try {
					socketChannel=SocketChannel.open();
					SocketAddress remoteAddress=new InetSocketAddress("192.168.2.11", 20001);
					socketChannel.connect(remoteAddress);
					socketChannel.configureBlocking(false);
					System.out.println("与服务器的连接建立成功");
				    selector=Selector.open();
				    socketChannel.register(selector, SelectionKey.OP_READ);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
		return START_STICKY;
	}
	
	class MyHandle extends Handler{  //Handle for sending a message on a different thread
		public MyHandle(){
			
		}
		public MyHandle(Looper looper){ 
					super(looper);
		}
		
		public void handleMessage(Message msg){
			
			String str=msg.getData().getString("name");
			String strps=msg.getData().getString("password");
	
			//send message to server
			MyMessage message=new MyMessage();
			message.setb((byte)1);
			message.setUsername(str);
			message.setPassword(strps);
			
			try {
				connect.sendBuffer.clear();
				connect.sendBuffer.put(message.Message2Byte());
				connect.sendBuffer.flip();
				connect.socketChannel.write(connect.sendBuffer);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	@Override 
	public void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();	
	}


}
