/*************************************************/
/*************************************************/
/*************************************************/
package com.example.basictextchat;

import java.util.Hashtable;
import java.io.PrintWriter;
import java.net.Socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	volatile boolean wantToCloseDialog=true;
	EditText userName;
	EditText message;
	volatile int flag=0;
	Hashtable<String,String> userPassword=new Hashtable<String,String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		userPassword.put("Lavish", "Lavi");
		userPassword.put("xyz", "xyz");
		userPassword.put("abc", "abc");
		
		Log.d("Lavish","check");
		
		setContentView(R.layout.activity_main);

		/***********************************************************/
		/*
		 * creating a Login Dialog Box
		 */
		//while(wantToCloseDialog)
		createLoginDialog();
		
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(this);
	}

	public void createLoginDialog()
	{
		LayoutInflater layoutInflater=LayoutInflater.from(this);
		View promptView=layoutInflater.inflate(R.layout.prompt_dialog,null);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Login");
		alertDialogBuilder.setView(promptView);
		final EditText uName = (EditText) promptView.findViewById(R.id.editText1);
		final EditText uPass = (EditText) promptView.findViewById(R.id.editText2);
		
		
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1) 
			{
				
			}
		});
		
		// create Login dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		// show Login Dialog Box
		alertDialog.show();
		
		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
	      {            
	          @Override
	          public void onClick(View v)
	          {
	        	  MainActivity.this.wantToCloseDialog=false;
					if(userPassword.containsKey(uName.getText().toString()) && userPassword.get(uName.getText().toString()).equals(uPass.getText().toString()))
					{
						Log.d("Lavish","Login Successful");
						wantToCloseDialog=true;

						MainActivity.this.showToastMessage("Client Connected");
					}
					else
					{
						Log.d("Lavish","Login faliure");
						uName.setText("");
						uPass.setText("");
					}
					if(wantToCloseDialog)
						alertDialog.dismiss();
					//else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
	          }
	      });
	}
	
	@SuppressLint("ShowToast")
	public void showToastMessage(String message)
	{
		Log.d("Lavish","method called");
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) 
	{
		showToastMessage("Message Sent to Server");
		userName=(EditText)findViewById(R.id.editText1);
		message=(EditText)findViewById(R.id.editText2);
		
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Socket server=new Socket("10.0.2.2",4444);
					PrintWriter dos= new PrintWriter(server.getOutputStream(),true);
					//userName.setText(actualUserName);
					//userName.setEnabled(false);
					String receivedString="[user-name] : "+userName.getText().toString()+"\t\t[message] : "+message.getText().toString();
					
					dos.write(receivedString);
					dos.flush();
					dos.close();
					server.close();
					MainActivity.this.showToastMessage("Message Successfully Sent");
				}
				catch(Exception exp)
				{
					Log.d("Lavish",exp.getMessage());
				}
				
			}
		}).start();
	}
}