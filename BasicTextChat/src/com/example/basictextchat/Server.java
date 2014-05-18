package com.example.basictextchat;
/*

	this is a simple java file without any android content
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String message;
	 

	public static void main(String[] args) throws IOException {
		 
		serverSocket = new ServerSocket(4444);
		
		System.out.println("Server started. Waiting for some client to connect ....");
		 
		while (true) 
		{
			try {
				clientSocket = serverSocket.accept(); // accept the client connection
				inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader); // get the client message
				message = bufferedReader.readLine();
				 
				System.out.println(message);
				inputStreamReader.close();
				clientSocket.close();
			 
			} catch (IOException e) {
				System.out.println("There is a problem in message reading"+e);
			}
		}
	}

}
