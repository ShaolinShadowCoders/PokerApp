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


import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.client.ServerService.Connect;



public class ReadyScreen extends Activity {
	
	Button button;
	String ready;
	TextView textView;
	static Handler handler;
	Connect connect=null;
	Object myLock=new Object();
	boolean flag = false;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readyscreen);
		textView=(TextView) findViewById(R.id.readyMsg);
		button =  (Button) findViewById(R.id.readyBtn); 

		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) { //Handle code for receiving messages 
				super.handleMessage(msg);
				Bundle bundle=msg.getData();
				
				if(bundle.getInt("ready") == 1){
					//Receive info and assign to player
					Intent j = new Intent();
					j.setClassName("com.example.client",
							"com.example.client.GameScreen");
					startActivity(j);
					
				}else{
						//Time out
						alertMessage();	
						System.exit(0);
				}
			}
			
		};

  	    
  	    
	  	  final Timer timer = new Timer();
	  	    TimerTask task = new TimerTask(){
	  	    public void run(){
	  	    	if(!flag){
	  	    		Message msg=Message.obtain();
	  	    		Bundle bundle=new Bundle();
	  	    		bundle.putInt("type", 3);
					bundle.putInt("ready", 0);
					msg.setData(bundle);
					ServerService.threadHandler.sendMessage(msg);
	  	    		}
	  	    	}
	  	    };
	  	  timer.schedule( task, 10*1000 );
	  		
		  			
	  		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				flag = true;
				Message msg=Message.obtain();
				Bundle bundle=new Bundle();
				bundle.putInt("type", 3);
/*new*/			bundle.putInt("ready", 1);
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
	
	public void alertMessage() {

		final android.app.AlertDialog.Builder show = new AlertDialog.Builder(this).setTitle("Error").setMessage("Timed Out").setNeutralButton("close", null);
		show.show();
	}
	
}
