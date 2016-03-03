package edu.drzam.v1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private int playerNumber;
	private int serverPort;
	private String playerOneIp;
	private String playerTwoIp;
	
	public Config() {
		try{
			Properties prop = new Properties();
			InputStream is = getClass().getClassLoader().getResourceAsStream("conf.properties");
			if (is!=null) {
				prop.load(is);
				this.playerNumber = Integer.parseInt(prop.getProperty("playerNumber"));
				this.serverPort = Integer.parseInt(prop.getProperty("serverPort"));
				this.playerOneIp = prop.getProperty("playerOneIp");
				this.playerTwoIp = prop.getProperty("playerTwoIp");
			} else {
				throw new FileNotFoundException("Properties file not found.");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getPlayerOneIp() {
		return playerOneIp;
	}

	public void setPlayerOneIp(String playerOneIp) {
		this.playerOneIp = playerOneIp;
	}

	public String getPlayerTwoIp() {
		return playerTwoIp;
	}

	public void setPlayerTwoIp(String playerTwoIp) {
		this.playerTwoIp = playerTwoIp;
	}
}
