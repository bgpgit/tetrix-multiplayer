package edu.drzam.v1;

/**
 * Contains the Player Information DTO 
 * @author usuario
 *
 */
public class PlayerInfo {
	
	/**
	 * Player ID
	 */
	private long id;
	
	/**
	 * Player number
	 */
	private int player;
	
	/**
	 * Player score 
	 */
	private long score;
	
	/**
	 * Pill Color type
	 */
	private PillColor type;
	
	/**
	 * Column of piece
	 */
	private int pieceCol;
	
	/**
	 * Row of piece
	 */
	private int pieceRow;
	
	/**
	 * Rotation of piece
	 */
	private int rotation;
	
	/**
	 * Pill Color instance
	 */
	private PillColor[][] tiles;
	
	/**
	 * Indicates when game ends
	 */
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
