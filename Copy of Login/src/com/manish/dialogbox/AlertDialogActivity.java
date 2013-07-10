
package com.manish.dialogbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AlertDialogActivity extends Activity {

	Socket echoSocket = null;
    PrintWriter out = null;
	BufferedReader in = null;
	Handler handler = null;
       
	
	/** Called when the activity is first created. */
       @Override
       public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
              
              
              Button btn = (Button) findViewById(R.id.startBtn);
              btn.setOnClickListener(new OnClickListener() {

                     @Override
                     public void onClick(View v) {
                           // TODO Auto-generated method stub
                           alertMessage();
                     }
              });
       }

       public void alertMessage() {
    	   

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           final android.app.AlertDialog.Builder show = new AlertDialog.Builder(this).setTitle("Error").setMessage("Wrong username/password").setNeutralButton("close",null);
           final EditText user = new EditText(this);
           user.setHint("Username");
           final EditText pass = new EditText(this);
           pass.setHint("Password");
           pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           builder.setMessage("Please enter username and password!");
           LinearLayout login =new LinearLayout(this);
           login.setOrientation(LinearLayout.VERTICAL);
           login.addView(user);
           login.addView(pass);
           builder.setView(login);
//           builder.setView(user);
//          builder.setView(pass);

              DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    
            	  public void onClick(DialogInterface dialog, int which) {
                           switch (which) {
                           case DialogInterface.BUTTON_POSITIVE:
                                  // Yes button clicked
                      	
                        	boolean valid =false; 
                        	while(valid == false){   
                        	   //Looper.prepare();
                   			Test test = new Test();
 //                  			test.doInBackground(null);
                   			System.out.printf("Sending String from Client\n");
                        	   
  //                      	   String username =  user.getText().toString();
  //                      	   out.println(username);
  //                      	   String password = pass.getText().toString();
  //                      	   out.println(password);
                        	   
 //                       	String str = "";
 //          					int k = 0;
          // 					int count;
         //  					char[] buf = new char[512];
       //    					while (str.compareTo("") == 0) {
        //   						System.out.println("Infinite loop?" + k);
        //   						try {
				//					if ((count = in.read(buf)) != -1) { 
		//								str = String.valueOf(buf);
		//								System.out.println(str);
							//			if(str.compareTo("Valid")==0){
		//									valid = true;
											//move onto the next screen
							//			}
//										else{
											//error message for alert\\
	//										show.show();
	//							}
										
										//content = str;
										//handler.post(runnableUi);
				//					}
		//						} catch (IOException e) {
									// TODO Auto-generated catch block
			//						e.printStackTrace();
	//							}/*if statement*/
        //   					}  /*while loop*/
      //     						if (valid == true){
           							Toast.makeText(AlertDialogActivity.this, "Login Completed!",
           							Toast.LENGTH_LONG).show();
           							Intent i = new Intent();
           							i.setClassName("com.manish.dialogbox", "com.manish.dialogbox.readyscreen");
           							startActivity(i);

                                  
                                  
                                  break;}

                           case DialogInterface.BUTTON_NEGATIVE:
                                  // No button clicked
                                  // do nothing
                                  break;
                           }/*while loop for valid*/
                     }
              };
              builder.setPositiveButton("Login", dialogClickListener);
              builder.setNegativeButton("Cancel", dialogClickListener).show();
       }
       
       
       private class Test extends AsyncTask<Void, Void, Void> {

   		@Override
   		protected Void doInBackground(Void... params) {

   			try {
   				System.out.printf("Made it here\n");
   				echoSocket = new Socket("192.168.2.11", 20001);
   				System.out.printf("Made it here2\n");
   				out = new PrintWriter(echoSocket.getOutputStream(), true);
   				in = new BufferedReader(new InputStreamReader(
   						echoSocket.getInputStream()));
   			} catch (UnknownHostException e) {
   				System.err.println("Don't know about host machine .");
   				System.exit(1);
   			} catch (IOException e) {
   				Log.e("tag", e.getMessage());
   				System.exit(1);
   			}
   			return null;
   		}

   		@Override
   		protected void onCancelled() {
   			super.onCancelled();
   		}

   		@Override
   		protected void onProgressUpdate(Void... values) {
   			super.onProgressUpdate(values);
   		}

   		@Override
   		protected void onPostExecute(Void result) {
   			super.onPostExecute(result);
   		}

   	}  
       
       
       
}