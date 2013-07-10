package com.manish.dialogbox;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AlertDialogActivity extends Activity {

	

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
		final android.app.AlertDialog.Builder show = new AlertDialog.Builder(
				this).setTitle("Error").setMessage("Wrong username/password")
				.setNeutralButton("close", null);
		final EditText user = new EditText(this);
		user.setHint("Username");
		final EditText pass = new EditText(this);
		pass.setHint("Password");
		pass.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setMessage("Please enter username and password!");
		LinearLayout login = new LinearLayout(this);
		login.setOrientation(LinearLayout.VERTICAL);
		login.addView(user);
		login.addView(pass);
		builder.setView(login);
		// builder.setView(user);
		// builder.setView(pass);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked

					//Test test = new Test();
					//test.doInBackground();
					ServerIO server = new ServerIO();
					server.con();
					
					boolean valid = false;
					while (valid == false) {
						
						
						System.out.printf("Sending String from Client\n");

						String username = user.getText().toString();
						server.out.println(username);
						String password = pass.getText().toString();
						server.out.println(password);

						String str = "";
						String buf = "";
							try {
								int val;
								while((val = server.in.read()) != '\n'){
									char hold = (char) val;
									buf += hold;
								}
									str = String.valueOf(buf);
									System.out.println(str);
									if (str.compareTo("Valid\r") == 0) {
										valid = true;
										// move onto the next screen
									} else 
										show.show();
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							if (valid == true) {
							Toast.makeText(AlertDialogActivity.this,
									"Login Completed!", Toast.LENGTH_LONG)
									.show();
							Intent i = new Intent();
							i.setClassName("com.manish.dialogbox",
									"com.manish.dialogbox.readyscreen");
							startActivity(i);

							break;
						}
					}

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					// do nothing
					break;
				}/* while loop for valid */
			}
		};
		builder.setPositiveButton("Login", dialogClickListener);
		builder.setNegativeButton("Cancel", dialogClickListener).show();
	}

	

}