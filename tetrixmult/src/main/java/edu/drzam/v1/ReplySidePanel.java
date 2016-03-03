package edu.drzam.v1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.google.gson.Gson;

public class ReplySidePanel extends JPanel {
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 2181495598854992747L;

	/**
	 * The width of the border around the game board.
	 */
	private static final int BORDER_WIDTH = 5;
	
	/**
	 * The number of columns on the board.
	 */
	public static final int COL_COUNT = 10;
		
	/**
	 * The number of visible rows on the board.
	 */
	private static final int VISIBLE_ROW_COUNT = 20;
	
	/**
	 * The number of rows that are hidden from view.
	 */
	private static final int HIDDEN_ROW_COUNT = 2;
	
	/**
	 * The total number of rows that the board contains.
	 */
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
	
	/**
	 * The number of pixels that a tile takes up.
	 */
	public static final int TILE_SIZE = 30;
	
	/**
	 * The width of the shading on the tiles.
	 */
	public static final int SHADE_WIDTH = 5;
	
	/**
	 * The central x coordinate on the game board.
	 */
	private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
	
	/**
	 * The central y coordinate on the game board.
	 */
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
		
	/**
	 * The total width of the panel.
	 */
	public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * The total height of the panel.
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * The larger font to display.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

	/**
	 * The smaller font to display.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);
	
	/**
	 * The DrZam instance.
	 */
	private DrZam drzam;
	
	/**
	 * The tiles that make up the board.
	 */
	private PillColor[][] tiles;
	
	private PlayerInfo infoPlayer;
	
	/**
	 * Creates a new SidePanel and sets it's display properties.
	 * @param drzam The DrZam instance to use.
	 */
	public ReplySidePanel(DrZam drzam) {
		this.drzam = drzam;
		this.tiles = new PillColor[ROW_COUNT][COL_COUNT];
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	public void sinchronizeReplyBoard(String jsonInfoLive) {
		if (jsonInfoLive!=null) {

			System.out.println("incoming info:"+jsonInfoLive);
			
			Gson gson = new Gson();
			setInfoPlayer(gson.fromJson(jsonInfoLive, PlayerInfo.class));

			for(int x = 0; x < COL_COUNT; x++) {
				//System.out.print("col["+x+"][");
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					if (getInfoPlayer()!=null) {
						setTile(x, y, getInfoPlayer().getTiles()[y][x]);
					}
				}
			}
		}
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//This helps simplify the positioning of things.
		g.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		/*
		 * Draw the board differently depending on the current game state.
		 */
		if(drzam.isPaused()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			String msg = "PAUSED";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
		} else if(drzam.isNewGame() || drzam.isGameOver()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			
			/*
			 * Because both the game over and new game screens are nearly identical,
			 * we can handle them together and just use a ternary operator to change
			 * the messages that are displayed.
			 */
			String msg = drzam.isNewGame() ? "DR. ZAM" : "GAME OVER";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = "Press Enter to Play" + (drzam.isNewGame() ? "" : " Again");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
		
		} else {
			
			if (getInfoPlayer()!=null) {
				PillColor type = getInfoPlayer().getType();
				int pieceCol = getInfoPlayer().getPieceCol();
				int pieceRow = getInfoPlayer().getPieceRow();
				int rotation = getInfoPlayer().getRotation();
				
				//Draw the piece onto the board.
				for(int col = 0; col < type.getDimension(); col++) {
					for(int row = 0; row < type.getDimension(); row++) {
						if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
							drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}
				
				/*
				 * Draw the tiles onto the board.
				 */
				for(int x = 0; x < COL_COUNT; x++) {
					for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
						PillColor tile = getTile(x, y);
						if(tile != null) {
							drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}
				
				/*
				 * Draw the ghost (semi-transparent piece that shows where the current piece will land). I couldn't think of
				 * a better way to implement this so it'll have to do for now. We simply take the current position and move
				 * down until we hit a row that would cause a collision.
				 */
//				Color base = type.getBaseColor();
//				base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
//				for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
//					//If no collision is detected, try the next row.
////					if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {					
////						continue;
////					}
//					
//					//Draw the ghost one row higher than the one the collision took place at.
//					lowest--;
//					
//					//Draw the ghost piece.
//					for(int col = 0; col < type.getDimension(); col++) {
//						for(int row = 0; row < type.getDimension(); row++) {
//							if(lowest + row >= 2 && type.isTile(col, row, rotation)) {
//								drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
//							}
//						}
//					}
//					
//					break;
//				}
			
			}
			
			/*
			 * Draw the background grid above the pieces (serves as a useful visual
			 * for players, and makes the pieces look nicer by breaking them up.
			 */
			g.setColor(Color.DARK_GRAY);
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}
		}
		
		/*
		 * Draw the outline.
		 */
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
	}
	
	/**
	 * Draws a tile onto the board.
	 * @param type The type of tile to draw.
	 * @param x The column.
	 * @param y The row.
	 * @param g The graphics object.
	 */
	private void drawTile(PillColor type, int x, int y, Graphics g) {
		drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
	}
	
	/**
	 * Draws a tile onto the board.
	 * @param base The base color of tile.
	 * @param light The light color of the tile.
	 * @param dark The dark color of the tile.
	 * @param x The column.
	 * @param y The row.
	 * @param g The graphics object.
	 */
	private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {
		
		/*
		 * Fill the entire tile with the base color.
		 */
		g.setColor(base);
		g.fillOval(x, y, TILE_SIZE, TILE_SIZE);
		
		/*
		 * Fill the bottom and right edges of the tile with the dark shading color.
		 */
		g.setColor(dark);
		
		/*
		 * Fill the top and left edges with the light shading. We draw a single line
		 * for each row or column rather than a rectangle so that we can draw a nice
		 * looking diagonal where the light and dark shading meet.
		 */
		g.setColor(light);
	}
	
	/**
	 * Sets a tile located at the desired column and row.
	 * @param x The column.
	 * @param y The row.
	 * @param type The value to set to the tile to.
	 */
	private void setTile(int  x, int y, PillColor type) {
		tiles[y][x] = type;
	}
		
	/**
	 * Gets a tile by it's column and row.
	 * @param x The column.
	 * @param y The row.
	 * @return The tile.
	 */
	private PillColor getTile(int x, int y) {
		return tiles[y][x];
	}

	public PlayerInfo getInfoPlayer() {
		return infoPlayer;
	}

	public void setInfoPlayer(PlayerInfo infoPlayer) {
		this.infoPlayer = infoPlayer;
	}
	
}
