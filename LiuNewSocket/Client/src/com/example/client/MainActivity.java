package com.example.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button button;
	TextView textView;
	EditText editText;
	EditText editTextps;
	static Handler handler;
	Connect connect=null;
	Object myLock=new Object();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		button=(Button) findViewById(R.id.button1);
		textView=(TextView) findViewById(R.id.textView1);
		editText=(EditText) findViewById(R.id.editText1);
		editTextps=(EditText) findViewById(R.id.editText2);
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) { //Handle code for receiving messages 
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle bundle=msg.getData();
				
				if(bundle.getInt("int") == 2){
					//call function to check and see if they should move onto the next screen
					//otherwise, restart this activity and make them try to login again depending
					//on response from the server
					if(bundle.getInt("valid") == 1){
						//move onto the next screen
						Intent i = new Intent();
						i.setClassName("com.example.client",
								"com.example.client.ReadyScreen");
						startActivity(i);
					}else{
						//restart this activity screen with message prompting them 
						//that username or password was incorrect
						alertMessage();	
						editText.setText("");
						editTextps.setText("");
					}
				}//else if(bundle.getString("type").compareTo("ready") == 0){
					//move onto the next screen if ready
		//		}else if(bundle.getString("type").compareTo("bet") == 0){
					//handles incoming messages for betting
		//		}else{
					//exit the game
		//		}
				//textView.setText(bundle.getString("string"));
			}
			
		};
		
		/*	IN SERVER SERVICE
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
		sendThread.start();*/
		
		startService(new Intent(getBaseContext(), ServerService.class));

		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str=editText.getText().toString();
				String strps=editTextps.getText().toString();
				Message msg=Message.obtain();
				Bundle bundle=new Bundle();
				bundle.putString("name", str);
				bundle.putString("password", strps);
				msg.setData(bundle);
				ServerService.threadHandler.sendMessage(msg);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*	IN SERVER SERVICE

	public class MyHandle extends Handler{  //Handle for sending a message on a different thread
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
		*/
	/*	public void play() throws IOException{
			
			//socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
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
			                System.out.println("Receive : "+message.getb()+","+message.getUsername());
			                Message msg=Message.obtain();
			                Bundle bundle=new Bundle();
			                bundle.putInt("int", message.getb());
			                bundle.putString("string", message.getUsername());
			                bundle.putInt("valid", message.getValid());
			                msg.setData(bundle);
			                handler.sendMessage(msg);
						}
					}
				}
				readyKeySet.clear();
		}*/
	

	public void alertMessage() {

		final android.app.AlertDialog.Builder show = new AlertDialog.Builder(this).setTitle("Error").setMessage("Wrong username/password").setNeutralButton("close", null);
		show.show();
	}
	
}
