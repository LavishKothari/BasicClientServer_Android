/*************************************************/
/*************************************************/
/*************************************************/
//server=new Socket(serverIP,4444);
					
package com.example.basictextchat;

import java.util.Hashtable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	StringBuffer strb=new StringBuffer();
	View promptView;
	Socket server;
	String serverIP;
	volatile boolean wantToCloseDialog=true;
	EditText message;
	TextView chatArea;
	EditText uName,uPass;
	volatile int flag=0;
	Hashtable<String,String> userPassword=new Hashtable<String,String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		userPassword.put("Lavish", "Lavi");
		userPassword.put("xyz", "xyz");
		userPassword.put("abc", "abc");
		
		Log.d("Lavish","check");
		
		setContentView(R.layout.activity_main);

		/***********************************************************/
		createLoginDialog();
		
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(this);
	}

	public void createLoginDialog()
	{
		LayoutInflater layoutInflater=LayoutInflater.from(this);
		promptView=layoutInflater.inflate(R.layout.prompt_dialog,null);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Login");
		alertDialogBuilder.setView(promptView);
		uName = (EditText) promptView.findViewById(R.id.editText1);
		uPass = (EditText) promptView.findViewById(R.id.editText2);
		serverIP=((EditText)promptView.findViewById(R.id.editText3)).getText().toString();
		
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

						serverIP=((EditText)promptView.findViewById(R.id.editText3)).getText().toString();
						/////////////////////////////////////
						new Thread(new Runnable()
						{

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									server=new Socket(serverIP,4444);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}).start();
						
						///////////////////////////////////
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
		message=(EditText)findViewById(R.id.editText2);
		chatArea=(TextView)findViewById(R.id.textView1);
		Thread t=new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					String receivedString="[user-name] : "+uName.getText().toString()+"\t\t[message] : "+message.getText().toString();
					
					DataOutputStream dos=new DataOutputStream(server.getOutputStream());
					DataInputStream dis=new DataInputStream(server.getInputStream());
					
					dos.writeUTF(receivedString);
					//MainActivity.this.showToastMessage("Message Successfully Sent");
					//Log.d("Lavish",dis.readUTF());
					//chatArea.append("\n"+dis.readUTF());
					Log.d("Lavish","x");
					strb.append("\n"+dis.readUTF());
					Log.d("Lavish","y");
					//MainActivity.this.chatArea.setText(strb);
					Log.d("Lavish","z");
				}
				catch(Exception exp)
				{
					Log.d("Lavish",exp.toString());
					Log.d("Lavish","aaaaaaa : "+exp.getMessage());
				}
			}
		});
		t.start();
		try {
			t.join(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainActivity.this.chatArea.setText(strb);
		
	}
}