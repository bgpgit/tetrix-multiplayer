package edu.foo.tetrixmult.network;

import java.net.*;
import java.util.Properties;
import java.io.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainSocketServer implements Runnable {
	
	ServerSocket socketServer;
	Socket connection;
	Properties prop;
	private int mainSocketPort;
	
	private String infoFirstPlayer;
	private String infoSecondPlayer;
	
	public MainSocketServer(Properties inProperties) {
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
				
				JsonParser parser = new JsonParser();
				JsonObject jsonInfo = parser.parse(strBuffer.toString()).getAsJsonObject();
				JsonElement elPlayer = jsonInfo.get("player");
				
				String messageResponse = new String();
				if (elPlayer.getAsString().equals("1")) {
					System.out.println("Request accepted from First Player...");
					setInfoFirstPlayer(strBuffer.toString());
					System.out.println("Message from First Player: " + getInfoFirstPlayer());
					messageResponse = getInfoSecondPlayer();
					System.out.println("Last Message from Second Player: " + messageResponse);
				} else {
					System.out.println("Request accepted from Second Player...");
					//System.out.println("InfoSecondPlayer1:"+getInfoSecondPlayer());
					setInfoSecondPlayer(strBuffer.toString());
					System.out.println("Message from Second Player: " + getInfoSecondPlayer());
					messageResponse = getInfoFirstPlayer();
					System.out.println("Last Message from First Player: " + messageResponse);
				}
				
				BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
		        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
		        outputStreamWriter.write(messageResponse+(char)13);
		        outputStreamWriter.flush();
		        outputStreamWriter.close();
		        outputStream.close();
				
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

	