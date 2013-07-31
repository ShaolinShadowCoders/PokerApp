package com.example.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class NewGameScreen extends Activity {

	int amount = 0;
	boolean alert = false;
	 AlertDialog.Builder builder;
       /** Called when the activity is first created. */
       @Override
       public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
              Button call = (Button) findViewById(R.id.Button03);
              Button raise = (Button) findViewById(R.id.Button02);
              Button fold = (Button) findViewById(R.id.Button01);
              Button quit = (Button) findViewById(R.id.button1);
              final TextView text=(TextView) findViewById(R.id.text);
              call.setOnClickListener(new OnClickListener() {

                     @Override
                     public void onClick(View v) {
                           // TODO Auto-generated method stub
                           text.setText("You have pressed the call button");
                     }
              });
              quit.setOnClickListener(new OnClickListener() {

                  @Override
                  public void onClick(View v) {
                        // TODO Auto-generated method stub
                      text.setText("You have pressed the raise button");

                  }
           });
              fold.setOnClickListener(new OnClickListener() {

                  @Override
                  public void onClick(View v) {
                        // TODO Auto-generated method stub
                      text.setText("You have pressed the fold button");

                  }
           });
              	quit.setOnClickListener(new OnClickListener() {

                  @Override
                  public void onClick(View v) {
                        // TODO Auto-generated method stub
                      text.setText("You have pressed the quit button");

                  }
           }); 
       }
}