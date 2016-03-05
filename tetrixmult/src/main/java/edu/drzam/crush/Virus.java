package edu.drzam.crush;

public class Virus {
	
	private int col;
	private int row;
	private PillColor color;
	private boolean active;
	
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public PillColor getColor() {
		return color;
	}
	public void setColor(PillColor color) {
		this.color = color;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
