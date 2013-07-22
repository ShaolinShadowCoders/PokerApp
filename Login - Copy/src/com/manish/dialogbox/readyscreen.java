	    package com.manish.dialogbox;
	  //this is to save. boo yah


	  import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

	  public class readyscreen extends Activity {
	  	    public void onCreate(Bundle savedInstanceState) {
	  	        super.onCreate(savedInstanceState);
	  	        setContentView(R.layout.readyscreen);
	  	        
	  	        
	  	        
	  	        
	  	      final String ready = "";
	  	    Button mainNext = (Button) findViewById(R.id.readyBtn);    
		  	  final Timer timer = new Timer();
	  	    TimerTask task = new TimerTask(){
	  	    public void run(){
	  	    if( ready.equals("") ){
	  	    String ready = "False";
	  	    System.exit( 0 );
	  	    } 
	  	    }
	  	    };
 
	  	 
	  	timer.schedule( task, 10*1000 );
	  	
	  	mainNext.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	String ready = "True";
	            	timer.cancel();
	                Intent j = new Intent(v.getContext(), gamescreen.class);
	                startActivity(j);
	            }
	        }); 
	    }                
	  	

	  
	 	        
	  	        
	  	       
	  	        
	  }
	  	    
	    
	    
	    
	    