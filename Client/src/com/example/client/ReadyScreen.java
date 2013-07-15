package com.example.client;
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
