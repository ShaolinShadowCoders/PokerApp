
package com.manish.dialogbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlertDialogActivity extends Activity {

       /** Called when the activity is first created. */
       @Override
       public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
              Button btn = (Button) findViewById(R.id.button1);
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
                        	   String username =  user.getText().toString();
                        	   String password = pass.getText().toString();
                                  Toast.makeText(AlertDialogActivity.this, "Login Completed!",
                                                Toast.LENGTH_LONG).show();
                                  break;

                           case DialogInterface.BUTTON_NEGATIVE:
                                  // No button clicked
                                  // do nothing
                                  Toast.makeText(AlertDialogActivity.this, "Login Cancelled",
                                                Toast.LENGTH_LONG).show();
                                  break;
                           }
                     }
              };
              builder.setPositiveButton("Login", dialogClickListener);
              builder.setNegativeButton("Cancel", dialogClickListener).show();
       }
}