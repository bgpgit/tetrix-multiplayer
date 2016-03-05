package edu.drzam.crush;

public class PlayerInfo {
	
	private long id;
	private int player;
	private long score;
	private PillColor type;
	private int pieceCol;
	private int pieceRow;
	private int rotation;
	private PillColor[][] tiles;
	private boolean gameOver;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public PillColor getType() {
		return type;
	}
	public void setType(PillColor type) {
		this.type = type;
	}
	public int getPieceCol() {
		return pieceCol;
	}
	public void setPieceCol(int pieceCol) {
		this.pieceCol = pieceCol;
	}
	public int getPieceRow() {
		return pieceRow;
	}
	public void setPieceRow(int pieceRow) {
		this.pieceRow = pieceRow;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public PillColor[][] getTiles() {
		return tiles;
	}
	public void setTiles(PillColor[][] tiles) {
		this.tiles = tiles;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public boolean isGameOver() {
		return gameOver;
	}
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	
	
	
}
