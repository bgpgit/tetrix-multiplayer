package edu.drzam.crush;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private int playerNumber;
	private int serverPort;
	private String serverIP;
	
	public Config() {
		try{
			Properties prop = new Properties();
			InputStream is = getClass().getClassLoader().getResourceAsStream("conf.properties");
			if (is!=null) {
				prop.load(is);
				this.playerNumber = Integer.parseInt(prop.getProperty("playerNumber"));
				this.serverPort = Integer.parseInt(prop.getProperty("serverPort"));
				this.serverIP = prop.getProperty("serverIP");
			} else {
				throw new FileNotFoundException("Properties file not found.");
			}
			is.close();
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

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

}
