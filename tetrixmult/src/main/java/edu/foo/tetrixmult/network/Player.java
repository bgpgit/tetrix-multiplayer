package edu.foo.tetrixmult.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Player {
	
	String myIP;
	int myPort;
	String otherIP;
	int otherPort;
	boolean firstPlayer;
	
	String messageToVsPlayer;
	String messageFromVsPlayer;
	
	public String getMyIP() {
		return myIP;
	}
	public void setMyIP(String myIP) {
		this.myIP = myIP;
	}
	public int getMyPort() {
		return myPort;
	}
	public void setMyPort(int myPort) {
		this.myPort = myPort;
	}
	public String getOtherIP() {
		return otherIP;
	}
	public void setOtherIP(String otherIP) {
		this.otherIP = otherIP;
	}
	public int getOtherPort() {
		return otherPort;
	}
	public void setOtherPort(int otherPort) {
		this.otherPort = otherPort;
	}
	public boolean isFirstPlayer() {
		return firstPlayer;
	}
	public void setFirstPlayer(boolean firstPlayer) {
		this.firstPlayer = firstPlayer;
	}
	public String getMessageToVsPlayer() {
		return messageToVsPlayer;
	}
	public void setMessageToVsPlayer(String messageToVsPlayer) {
		this.messageToVsPlayer = messageToVsPlayer;
	}
	public String getMessageFromVsPlayer() {
		return messageFromVsPlayer;
	}
	public void setMessageFromVsPlayer(String messageFromVsPlayer) {
		this.messageFromVsPlayer = messageFromVsPlayer;
	}
//	
//	public String sendToOtherPlayer() {
//		String msgFromVsPlayer = new String();
//		try {
//			InetAddress address = InetAddress.getByName(getOtherIP());
//			Socket connection = new Socket(address, getOtherPort());
//			
//			BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
//			
//			BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
//			
//			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
//	        outputStreamWriter.write(getMessageToVsPlayer()+(char)13);
//	        outputStreamWriter.flush();
//	        
//			//System.out.println("Message send "+msgToVsPlayer+" to "+getOtherIP()+":"+getOtherPort());
//	        
//			StringBuffer strBuffer = new StringBuffer();
//	    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "US-ASCII");
//	    	int c;
//	    	while ( (c = inputStreamReader.read()) != 13) {
//	    		strBuffer.append( (char) c);
//	    	}
//			
//	    	setMessageFromVsPlayer(strBuffer.toString());
//			
//			//System.out.println("Message response "+msgFromVsPlayer+" from "+getOtherIP()+":"+getOtherPort());
//	    	
//	    	outputStream.close();
//	    	outputStreamWriter.close();
//	    	inputStream.close();
//	    	inputStreamReader.close();
//			connection.close();
//			
//		} catch (IOException e) {
//			System.out.println("conexion no establecida...");
//			e.printStackTrace();
//		}
//		return msgFromVsPlayer;
//	}
	
}
