package edu.foo.tetrixmult.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class PlayerConsole__ implements Runnable {
	
	Player propPlayer;
	TCPServerLayer tcpServer;
	String jsonString;
	/*public PlayerConsole(Player inPropertiesPlayer, TCPServerLayer inTcpServerLayer) {
		this.propPlayer = inPropertiesPlayer;
		this.tcpServer = inTcpServerLayer;
	}*/
	
	public PlayerConsole__(String inJsonString) {
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
			//System.out.println("Message to "+propPlayer.getOtherIP()+":"+propPlayer.getOtherPort()+" -> "+ key);
			//tcpServer.setMsgToVsPlayer(key);
			//String messageFromOtherPlayer = propPlayer.sendToOtherPlayer(key);
			//System.out.println("Message from "+propPlayer.getOtherIP()+":"+propPlayer.getOtherPort()+" -> "+ messageFromOtherPlayer);
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
		        
//		        StringBuffer strBuffer = new StringBuffer();
//				BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
//		    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "US-ASCII");
//		    	int c;
//		    	while ( (c = inputStreamReader.read()) != 13) {
//		    		strBuffer.append( (char) c);
//		    		//System.out.println("char:"+c);
//		    	}
		        
		        StringBuffer strBuffer = new StringBuffer();
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String messageIn = input.readLine();
				//input.close();
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