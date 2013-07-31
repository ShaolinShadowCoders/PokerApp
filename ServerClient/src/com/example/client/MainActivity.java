package com.example.client;

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
import android.widget.EditText;
import android.widget.TextView;

import com.example.client.ServerService.Connect;



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
		setContentView(R.layout.activity_main);
		button=(Button) findViewById(R.id.button1);
		textView=(TextView) findViewById(R.id.textView1);
		editText=(EditText) findViewById(R.id.editText1);
		editTextps=(EditText) findViewById(R.id.editText2);
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) { //Handle code for receiving messages 
				super.handleMessage(msg);//when a message is received it's input is processed here
				Bundle bundle=msg.getData();
				if(bundle.getInt("int") == 2){
					if(bundle.getInt("valid") == 1){
						Intent i = new Intent();
						i.setClassName("com.example.client",
								"com.example.client.ReadyScreen");
						startActivity(i);
					}else{
						alertMessage();	
						editText.setText("");
						editTextps.setText("");
					}
				}
			}
			
		};
		
		startService(new Intent(getBaseContext(), ServerService.class));

		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String str=editText.getText().toString();
				String strps=editTextps.getText().toString();
				Message msg=Message.obtain();
				Bundle bundle=new Bundle();
				bundle.putInt("type", 1);
				bundle.putString("name", str);
				bundle.putString("password", strps);
				msg.setData(bundle);
				ServerService.threadHandler.sendMessage(msg);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void alertMessage() {

		final android.app.AlertDialog.Builder show = new AlertDialog.Builder(this).setTitle("Error").setMessage("Wrong username/password").setNeutralButton("close", null);
		show.show();
	}
}
