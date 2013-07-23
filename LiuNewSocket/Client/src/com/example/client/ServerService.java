package com.example.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
	private static final String TAG = ServerService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.d(TAG, "Started running the service");
		
		(new Thread(new ReceivingThread())).start();
		(new Thread(new SendingThread())).start();
		
		return START_STICKY;
	}
	
	
	@Override 
	public void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();	
	}

	class Connect {
		SocketChannel socketChannel=null;
		public ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
		ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
		Selector selector;
		public Connect() {
			
			try {
				socketChannel=SocketChannel.open();
				socketChannel.configureBlocking(false);
				SocketAddress remoteAddress=new InetSocketAddress("192.168.2.11", 20001);
				socketChannel.connect(remoteAddress);
			    selector=Selector.open();
			    SelectionKey socketChannelKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);
			    
			    while (selector.select(500)>0){
			    	Set keysSet =selector.selectedKeys();
			    	Iterator i = keysSet.iterator();
			    	
			    	//for each key
			    	while(i.hasNext()){
			    		SelectionKey key = (SelectionKey) i.next();
			    		i.remove();
			    		SocketChannel channel = (SocketChannel) key.channel();
			    		
			    		//Attempt connection
			    		if(key.isConnectable()){
			    			System.out.println("Server Found");
			    			
			    			//Close pendent connections 
			    			if(channel.isConnectionPending())
			    				channel.finishConnect();
			    			
			    			///Do something else here in the client
			    		}
			    	}
			    }
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public class ReceivingThread implements Runnable{

		@Override
		public void run() {
			Log.d(TAG, "Started running the receive thread");
			connect=new Connect();
			try {
				while(true){
					if(connect.selector.select()==0)
						continue;
					Play receivedMessage = new Play();
					receivedMessage.play();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public class SendingThread implements Runnable{

		@Override
		public void run() {
			Log.d(TAG, "Started running the send thread");
			Looper.prepare();
			threadHandler=new MyHandle(Looper.myLooper());
			Looper.loop();
		}
		
	}
	
	class MyHandle extends Handler{  
		public MyHandle(){
		}
		public MyHandle(Looper looper){ 
					super(looper);
		}
		public void handleMessage(Message msg){
			String str=msg.getData().getString("name");
			String strps=msg.getData().getString("password");
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
				
				e.printStackTrace();
			}
		}
	}
	
}



