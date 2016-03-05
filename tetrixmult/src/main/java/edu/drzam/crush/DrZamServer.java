package edu.drzam.crush;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import com.google.gson.Gson;

public class DrZamServer implements Runnable {
	
	
	ServerSocket socketServer;
	Socket connection;
	Properties prop;
	private int mainSocketPort;
	
	private Config config;
	private PlayerInfo player;
	
	private String infoFirstPlayer;
	private String infoSecondPlayer;
	
	public DrZamServer() {	
		this.config = new Config();
		this.mainSocketPort = config.getServerPort();
	}
	
	@Override
	public void run() {
		
		try {
			socketServer = new ServerSocket(mainSocketPort);
			System.out.println("Main Socket Server Initialized");
			System.out.println("Listening on port "+mainSocketPort);
		} catch (IOException e) {return;}

		while (true) {
			//System.out.println("Waiting for requests...");
			
			try {
				connection = socketServer.accept();
				
				StringBuffer strBuffer = new StringBuffer();
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String messageIn = input.readLine();
				//input.close();
				strBuffer = new StringBuffer(messageIn);
				
				Gson gson = new Gson();
				this.player = gson.fromJson(messageIn, PlayerInfo.class);

				String messageResponse = new String();
				if (player.getPlayer()==1) {
					//System.out.println("Request accepted from First Player...");
					setInfoFirstPlayer(strBuffer.toString());
					//System.out.println("Message from First Player: " + getInfoFirstPlayer());
					messageResponse = getInfoSecondPlayer();
					//System.out.println("Last Message from Second Player: " + messageResponse);
				} else {
					//System.out.println("Request accepted from Second Player...");
					//System.out.println("InfoSecondPlayer1:"+getInfoSecondPlayer());
					setInfoSecondPlayer(strBuffer.toString());
					//System.out.println("Message from Second Player: " + getInfoSecondPlayer());
					messageResponse = getInfoFirstPlayer();
					//System.out.println("Last Message from First Player: " + messageResponse);
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
