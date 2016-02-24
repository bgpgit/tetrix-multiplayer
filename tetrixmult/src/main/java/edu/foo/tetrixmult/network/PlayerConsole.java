package edu.foo.tetrixmult.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class PlayerConsole implements Runnable {
	
	Player propPlayer;
	TCPServerLayer tcpServer;
	String jsonString;
	
	public PlayerConsole(String inJsonString) {
		this.jsonString = inJsonString;
	}
	
	@Override
	public void run() {
		
		String key = new String();
		int cont=1;
		
		while(!key.equals("E")) {
			System.out.println("Enter something:");
			try{
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    key = bufferRead.readLine();
			} catch(IOException e) {
				e.printStackTrace();
			}

			String keyJson = 
					  "{"
					+ "\"id\":\""+cont+"\","
					+ jsonString
					+ "}";
			try {
				InetAddress address = InetAddress.getByName("127.0.0.1");
				Socket connection = new Socket(address, 7000);
				
				BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
				
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
		        outputStreamWriter.write(keyJson+(char)13);
		        outputStreamWriter.flush();
		        		        
		        StringBuffer strBuffer = new StringBuffer();
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String messageIn = input.readLine();
				strBuffer = new StringBuffer(messageIn);
		    	
		    	System.out.println("Message Response: " + strBuffer.toString());
		    	
		    	outputStream.close();
		    	outputStreamWriter.close();
				
		    	input.close();
		    	
		    	connection.close();
				
			} catch (IOException e) {
				System.out.println("conexion no establecida...");
				e.printStackTrace();
			}
			cont++;
		}
		
	}
	
}