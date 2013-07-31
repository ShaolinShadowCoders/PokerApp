package com.example.service;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
	Button start =(Button) findViewById(R.id.button1);
	start.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startService(new Intent(getBaseContext(), FirstService.class));
			
		}
	});

	Button stop =(Button) findViewById(R.id.button2);
	stop.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			stopService(new Intent(getBaseContext(), FirstService.class));
			
		}
	});
	
	}
}	
	
