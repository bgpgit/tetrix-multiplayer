package edu.foo.tetrixmult.network;

import java.io.IOException;
import java.util.Properties;

public class Game {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils utils = new Utils();
		Properties prop = new Properties();
		try {
			prop = utils.getProperties();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
//		Player propPlayer = new Player();
//		Gson gson = new Gson();
//		String jsonString = gson.toJson(propPlayer);
//		System.out.println("jsonString:"+jsonString);
		
		String keyJson = 
				  "\"messageType\":\"put\","
				+ "\"player\":\"1\","
				+ "\"score\":\"0\","
				+ "\"boardMatrix\":{"
				+ "\"A\":\"0,0,0,0,0,0,0,0\","
				+ "\"B\":\"0,0,0,0,0,0,0,0\","
				+ "\"C\":\"0,0,0,0,0,0,0,0\","
				+ "\"D\":\"0,0,0,0,0,0,0,0\""
				+ "}";
		
		Thread threadConsoleFirstPlayer = new Thread(new PlayerConsole(keyJson));
		threadConsoleFirstPlayer.start();
		
		Thread threadSocketFirstPlayer = new Thread(new MainSocketServer(prop));
		//thread.setDaemon(true);
		threadSocketFirstPlayer.start();

	}

}
