package edu.foo.tetrixmult.network;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import com.google.gson.Gson;

public class Game__ {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils utils = new Utils();
		Properties prop = new Properties();
		try {
			prop = utils.getProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Player propPlayer = new Player();

		Gson gson = new Gson();
		String jsonString = gson.toJson(propPlayer);

		System.out.println("jsonString:" + jsonString);

		String keyJson = "\"messageType\":\"put\"," + "\"player\":\"1\"," + "\"score\":\"0\"," + "\"boardMatrix\":{"
				+ "\"A\":\"0,0,0,0,0,0,0,0\"," + "\"B\":\"0,0,0,0,0,0,0,0\"," + "\"C\":\"0,0,0,0,0,0,0,0\","
				+ "\"D\":\"0,0,0,0,0,0,0,0\"" + "}";

		Thread threadConsoleFirstPlayer = new Thread(new PlayerConsole(keyJson));
		threadConsoleFirstPlayer.start();

		Thread threadSocketFirstPlayer = new Thread(new MainSocketServer(prop));
		// thread.setDaemon(true);
		threadSocketFirstPlayer.start();

		/*
		 * Player propPlayer = new Player(); if
		 * (utils.testSocketPlayer(prop.getProperty("fPlayerIP"),
		 * Integer.parseInt(prop.getProperty("fPlayerPort")))) {
		 * 
		 * propPlayer.setFirstPlayer(true);
		 * propPlayer.setMyIP(prop.getProperty("fPlayerIP"));
		 * propPlayer.setMyPort(Integer.parseInt(prop.getProperty("fPlayerPort")
		 * )); propPlayer.setOtherIP(prop.getProperty("sPlayerIP"));
		 * propPlayer.setOtherPort(Integer.parseInt(prop.getProperty(
		 * "sPlayerPort")));
		 * 
		 * Thread threadConsoleFirstPlayer = new Thread(new
		 * PlayerConsole(propPlayer, new TCPServerLayer(propPlayer, prop)));
		 * threadConsoleFirstPlayer.start();
		 * 
		 * Thread threadSocketFirstPlayer = new Thread(new
		 * TCPServerLayer(propPlayer, prop)); //thread.setDaemon(true);
		 * threadSocketFirstPlayer.start();
		 * 
		 * } else if (utils.testSocketPlayer(prop.getProperty("sPlayerIP"),
		 * Integer.parseInt(prop.getProperty("sPlayerPort")))) {
		 * 
		 * propPlayer.setFirstPlayer(false);
		 * propPlayer.setMyIP(prop.getProperty("sPlayerIP"));
		 * propPlayer.setMyPort(Integer.parseInt(prop.getProperty("sPlayerPort")
		 * )); propPlayer.setOtherIP(prop.getProperty("fPlayerIP"));
		 * propPlayer.setOtherPort(Integer.parseInt(prop.getProperty(
		 * "fPlayerPort")));
		 * 
		 * Thread threadConsoleSecondPlayer = new Thread(new
		 * PlayerConsole(propPlayer, new TCPServerLayer(propPlayer, prop)));
		 * threadConsoleSecondPlayer.start();
		 * 
		 * Thread threadSocketFirstPlayer = new Thread(new
		 * TCPServerLayer(propPlayer, prop)); //thread.setDaemon(true);
		 * threadSocketFirstPlayer.start();
		 * 
		 * }
		 */

	}

}
