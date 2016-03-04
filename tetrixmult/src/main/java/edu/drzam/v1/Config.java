package edu.drzam.v1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	/**
	 * Number of player in game
	 */
	private int playerNumber;

	/**
	 * Port for communication
	 */
	private int serverPort;

	/**
	 * IP from first player
	 */
	private String playerOneIp;

	/**
	 * IP from second player
	 */
	private String playerTwoIp;

	/**
	 * Defines game configuration from conf.properties
	 */
	public Config() {
		try {
			Properties prop = new Properties();
			InputStream is = getClass().getClassLoader().getResourceAsStream("conf.properties");

			if (is != null) {
				try {
					prop.load(is);
				} finally {
					is.close();
				}
				this.playerNumber = Integer.parseInt(prop.getProperty("playerNumber"));
				this.serverPort = Integer.parseInt(prop.getProperty("serverPort"));
				this.playerOneIp = prop.getProperty("playerOneIp");
				this.playerTwoIp = prop.getProperty("playerTwoIp");
			} else {
				throw new FileNotFoundException("Properties file not found.");
			}
		} catch (IOException e) {
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