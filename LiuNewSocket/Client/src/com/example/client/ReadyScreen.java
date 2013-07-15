/*package com.example.client;
//this is to save. boo yah
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReadyScreen extends Activity {

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.readyscreen);
	        
	        Button mainNext = (Button) findViewById(R.id.readyBtn);
	        mainNext.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	String ready = "True";
	                Intent j = new Intent(v.getContext(), GameScreen.class);
//	                j.setClassName("com.screenssample", "com.screenssample.gamescreen");
	                startActivity(j);
	            }
	        });
	    }
	 
}
*/

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
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReadyScreen extends Activity {
	
	Button button;
	String ready;
	TextView textView;
	Handler handler;
	MyHandle threadHandler;
	Connect connect=null;
	Object myLock=new Object();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readyscreen);
		textView=(TextView) findViewById(R.id.readyMsg);

		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) { //Handle code for receiving messages 
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle bundle=msg.getData();
				
				if(ready.equals("True")){
					//Recieve info and assign to player
				}else if(ready.equals("False")){
						//Time out
						alertMessage();	
						System.exit(0);
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
		
		
		
  	    button =  (Button) findViewById(R.id.readyBtn); 
	  	  final Timer timer = new Timer();
	  	    TimerTask task = new TimerTask(){
	  	    public void run(){
	  	    	Message msg=Message.obtain();
				Bundle bundle=new Bundle();
/*new*/			bundle.putString("ready", "False");
				msg.setData(bundle);
				threadHandler.sendMessage(msg);
	  	    }
	  	    };

	  		timer.schedule( task, 10*1000 );
		  			
	  		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg=Message.obtain();
				Bundle bundle=new Bundle();
/*new*/			bundle.putString("ready", "True");
				msg.setData(bundle);
				String ready = "True";
            	timer.cancel();
                Intent j = new Intent(arg0.getContext(), GameScreen.class);
				threadHandler.sendMessage(msg);
				startActivity(j);
			}
		});
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
			
			String ready=msg.getData().getString("ready");
	
			//send message to server
			MyMessage message=new MyMessage();
			message.setb((byte)9);
			message.setReady(ready);
			message.setType("ready");
			
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
				System.out.println("与服务器的连接建立成功");
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
			                System.out.println("Receive : "+message.getb()+","+message.getStr());
			                Message msg=Message.obtain();
			                Bundle bundle=new Bundle();
			                bundle.putInt("int", message.getb());
			                bundle.putString("string", message.getStr());
			                bundle.putString("type", message.getType());
			                bundle.putBoolean("valid", message.getValid());
			                msg.setData(bundle);
			                handler.sendMessage(msg);
						}
					}
				}
				readyKeySet.clear();
		}
	}

	public void alertMessage() {

		final android.app.AlertDialog.Builder show = new AlertDialog.Builder(this).setTitle("Error").setMessage("Timed Out").setNeutralButton("close", null);
		show.show();
	}
	
}
