package com.example.basictextchat;

import java.io.PrintWriter;
import java.net.Socket;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.d("Lavish","check");
		
		setContentView(R.layout.activity_main);
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(this);
	}

	@SuppressLint("ShowToast")
	public void showToastMessage(String message)
	{
		Log.d("Lavish","method called");
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onClick(View v) {
		showToastMessage("Sending message to Server");
		final EditText userName=(EditText)findViewById(R.id.editText1);
		final EditText message=(EditText)findViewById(R.id.editText2);
		
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Socket server=new Socket("10.0.2.2",4444);
					PrintWriter dos= new PrintWriter(server.getOutputStream(),true);
					
					String receivedString=userName.getText().toString()+message.getText().toString();
					
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