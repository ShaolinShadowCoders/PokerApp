package com.manish.dialogbox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;


public class ServerIO {
	Socket echoSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	Handler handler = null;
	
	public ServerIO(){
		
	}
	
	
	public void con(){
		Test test = new Test();
		test.doInBackground();
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



