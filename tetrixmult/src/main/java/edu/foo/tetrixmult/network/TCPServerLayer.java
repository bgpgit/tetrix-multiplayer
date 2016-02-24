package edu.foo.tetrixmult.network;

import java.net.*;
import java.util.Properties;
import java.io.*;

public class TCPServerLayer implements Runnable {
	
	ServerSocket socketServer;
	Socket connection;
	Player propPlayer;
	Properties prop;
	
	private String msgToVsPlayer;
	private String msgFromVsPlayer;
	
	public TCPServerLayer(Player inSocketPlayer, Properties inProperties) {
		this.propPlayer = inSocketPlayer;
		this.prop = inProperties;
	}
	
	@Override
	public void run() {
		
		try {
			socketServer = new ServerSocket(propPlayer.getMyPort());
			System.out.println("Socket Server Initialized");
			System.out.println("Listening on port "+propPlayer.getMyPort());
		} catch (IOException e) {return;}

		while (true) {
			System.out.println("Waiting for requests...");
			try {
				connection = socketServer.accept();
				
				if (propPlayer.isFirstPlayer()) {
					System.out.println("Request accepted from Second Player...");
				} else {
					System.out.println("Request accepted from First Player...");
				}
				
				/*BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String messageIn = input.readLine();
				//input.close();
				setMsgFromVsPlayer(messageIn);*/
				
				StringBuffer strBuffer = new StringBuffer();
				BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
		    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "US-ASCII");
		    	int c;
		    	while ( (c = inputStreamReader.read()) != 13) {
		    		strBuffer.append( (char) c);
		    		System.out.println("char:"+c);
		    	}
		    	
				System.out.println("Message From Vs Player: " + strBuffer.toString());
				
				if (strBuffer!=null && !strBuffer.equals((char)13)) {
					
					setMsgFromVsPlayer(strBuffer.toString());
					
					System.out.println("getMsgToVsPlayer: " + getMsgToVsPlayer());
					
//					if (getMsgToVsPlayer()==null || getMsgToVsPlayer().equals("")) {
//						setMsgToVsPlayer("Datos iniciales");
//					}
					
					System.out.println("Message To Vs Player: " + getMsgToVsPlayer());
					
					/*DataOutputStream output = new DataOutputStream(connection.getOutputStream());
					output.writeBytes(getMsgToVsPlayer());
					output.close();*/
					
					BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
			        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
			        outputStreamWriter.write(getMsgToVsPlayer()+(char)13);
			        outputStreamWriter.flush();
			        outputStreamWriter.close();
			        outputStream.close();
			        
			        //connection.close();
					
					System.out.println("Communication End...");
				}
				
				inputStreamReader.close();
				inputStream.close();
				
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public void readRocket(String message) {
		
	}

	public String getMsgToVsPlayer() {
		return msgToVsPlayer;
	}

	public void setMsgToVsPlayer(String msgToVsPlayer) {
		System.out.println("setMsgToVsPlayer:"+msgToVsPlayer);
		this.msgToVsPlayer = msgToVsPlayer;
	}

	public String getMsgFromVsPlayer() {
		return msgFromVsPlayer;
	}

	public void setMsgFromVsPlayer(String msgFromVsPlayer) {
		System.out.println("setMsgFromVsPlayer:"+msgFromVsPlayer);
		this.msgFromVsPlayer = msgFromVsPlayer;
	}
	
}