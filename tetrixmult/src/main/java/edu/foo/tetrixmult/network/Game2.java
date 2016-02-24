package edu.foo.tetrixmult.network;

public class Game2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String keyJson = 
				  "\"messageType\":\"get\","
				+ "\"player\":\"2\","
				+ "\"score\":\"0\","
				+ "\"boardMatrix\":{"
				+ "\"A\":\"0,0,0,0,0,0,0,0\","
				+ "\"B\":\"0,0,0,0,0,0,0,0\","
				+ "\"C\":\"0,0,0,0,0,0,0,0\","
				+ "\"D\":\"0,0,0,0,0,0,0,0\""
				+ "}";
		
		Thread threadConsoleSecondPlayer = new Thread(new PlayerConsole(keyJson));
		threadConsoleSecondPlayer.start();

	}

}
