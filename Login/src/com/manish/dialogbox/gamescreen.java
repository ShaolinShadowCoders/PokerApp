package com.manish.dialogbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class gamescreen extends Activity {

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.gamescreen);
	        
	        Button mainNext = (Button) findViewById(R.id.button1);
	        mainNext.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                Intent i = new Intent();
	                i.setClassName("com.screenssample", "com.screenssample.gamescreen");
	                startActivity(i);
	            }
	        });
	    }
	 
}
