package edu.foo.tetrixmult.network;

import java.net.*;
import java.util.Properties;
import java.io.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainSocketServer__ implements Runnable {
	
	ServerSocket socketServer;
	Socket connection;
	Properties prop;
	private int mainSocketPort;
	
	private String infoFirstPlayer;
	private String infoSecondPlayer;
	
	public MainSocketServer__(Properties inProperties) {
		this.prop = inProperties;
		this.mainSocketPort = Integer.parseInt(prop.getProperty("MainSocketPort"));
	}
	
	@Override
	public void run() {
		
		try {
			socketServer = new ServerSocket(mainSocketPort);
			System.out.println("Main Socket Server Initialized");
			System.out.println("Listening on port "+mainSocketPort);
		} catch (IOException e) {return;}

		while (true) {
			System.out.println("Waiting for requests...");
			
			try {
				connection = socketServer.accept();
				
				StringBuffer strBuffer = new StringBuffer();
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String messageIn = input.readLine();
				//input.close();
				strBuffer = new StringBuffer(messageIn);
				
//				StringBuffer strBuffer = new StringBuffer();
//				BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
//		    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "US-ASCII");
//		    	int c;
//		    	while ( (c = inputStreamReader.read()) != 13) {
//		    		strBuffer.append( (char) c);
//		    		//System.out.println("char:"+c);
//		    	}
		    	
				//System.out.println("Message Player: " + strBuffer.toString());
				
				//JsonObject jsonInfo = Json.createReader(new StringReader(strBuffer.toString())).readObject();
				
				JsonParser parser = new JsonParser();
				JsonObject jsonInfo = parser.parse(strBuffer.toString()).getAsJsonObject();
				
				System.out.println("JsonObject Parser...");
				
				JsonElement element = jsonInfo.get("player");
				
				System.out.println("JsonObject:"+element.getAsString());
				
				String messageResponse = new String();
				if (element.getAsString().equals("1")) {
					System.out.println("Request accepted from First Player...");
					setInfoFirstPlayer(strBuffer.toString());
					System.out.println("Message from First Player: " + getInfoFirstPlayer());
					messageResponse = getInfoSecondPlayer();
					System.out.println("Message from Second Player: " + messageResponse);
				} else {
					System.out.println("Request accepted from Second Player...");
					//System.out.println("InfoSecondPlayer1:"+getInfoSecondPlayer());
					setInfoSecondPlayer(strBuffer.toString());
					System.out.println("Message from Second Player: " + getInfoSecondPlayer());
					messageResponse = getInfoFirstPlayer();
					System.out.println("Message from First Player: " + messageResponse);
				}
				
				/*DataOutputStream output = new DataOutputStream(connection.getOutputStream());
				output.writeBytes(getMsgToVsPlayer());
				output.close();*/
				
				BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
		        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
		        outputStreamWriter.write(messageResponse+(char)13);
		        outputStreamWriter.flush();
		        outputStreamWriter.close();
		        outputStream.close();
		        
		        //connection.close();
				
				System.out.println("Communication End...");
				
			} catch (IOException e) {e.printStackTrace();}
		}
	}

	public String getInfoFirstPlayer() {
		return infoFirstPlayer;
	}

	public void setInfoFirstPlayer(String infoFirstPlayer) {
		this.infoFirstPlayer = infoFirstPlayer;
	}

	public String getInfoSecondPlayer() {
		return infoSecondPlayer;
	}

	public void setInfoSecondPlayer(String infoSecondPlayer) {
		this.infoSecondPlayer = infoSecondPlayer;
	}
	
}

	