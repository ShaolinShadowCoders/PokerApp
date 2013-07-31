package com.example.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
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
		System.out.println(TAG + "Started running the service");
		
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
		public ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
		ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
		public Connect() {
			
			try {
				socketChannel=SocketChannel.open();
				SocketAddress remoteAddress=new InetSocketAddress("192.168.2.19", 20001);
				socketChannel.connect(remoteAddress);
				socketChannel.configureBlocking(false);
			    selector=Selector.open();
			    socketChannel.register(selector, SelectionKey.OP_READ);
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
					if(selector.select()==0)
						continue;
					Play receivedMessage = new Play();
					receivedMessage.play();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
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
			int type = msg.getData().getInt("type");
			MyMessage message=new MyMessage();
			switch(type){
			
			case 1:
				String str=msg.getData().getString("name");
				String strps=msg.getData().getString("password");
				message.setb((byte)1);
				message.setUsername(str);
				message.setPassword(strps);
				break;
			case 2:
				
				break;
			case 3:
				int ready=msg.getData().getInt("ready");
				message.setb((byte)3);
				message.setReady(ready);
				break;
			case 4:
				message.setb((byte)4);
				break;	
			case 5:
				
				break;
			case 6:
				int bet = msg.getData().getInt("bet");
				message.setb((byte)6);
				message.setBet(bet);
				break;	
			}
			
			try {
				connect.sendBuffer.clear();
				connect.sendBuffer.put(message.Message2Byte());
				connect.sendBuffer.flip();
				socketChannel.write(connect.sendBuffer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}



