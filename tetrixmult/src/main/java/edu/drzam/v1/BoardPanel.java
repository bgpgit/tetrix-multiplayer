package edu.drzam.v1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JPanel;

import com.google.gson.Gson;

public class BoardPanel extends JPanel {

	private static final int CONSECUTIVE_PILLS_TO_DELETE = 4;
	
	/**
	 * Number of player that joins the game
	 */
	private int playerNumber;

	/**
	 * Indicates if game is over
	 */
	private boolean isGameOver;

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 5055679736784226108L;

	/**
	 * Minimum color component values for tiles. 
	 */
	public static final int COLOR_MIN = 35;

	/**
	 * Maximum color component values for tiles. 
	 */
	public static final int COLOR_MAX = 255 - COLOR_MIN;

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
	public static final int PILL_VIEW_SIZE = 30;

	/**
	 * The width of the shading on the tiles.
	 */
	public static final int SHADE_WIDTH = 5;

	/**
	 * The central x coordinate on the game board.
	 */
	private static final int CENTER_X = COL_COUNT * PILL_VIEW_SIZE / 2;

	/**
	 * The central y coordinate on the game board.
	 */
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * PILL_VIEW_SIZE / 2;

	/**
	 * The total width of the panel.
	 */
	public static final int PANEL_WIDTH = COL_COUNT * PILL_VIEW_SIZE + BORDER_WIDTH * 2;

	/**
	 * The total height of the panel.
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * PILL_VIEW_SIZE + BORDER_WIDTH * 2;

	/**
	 * The larger font to display.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

	/**
	 * The smaller font to display.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);

	/**
	 * The quantity of virus to be shown
	 */
	public static final int VIRUS_QUANTITY = 3;

	/**
	 * The random number generator. This is used to spit out pieces randomly.
	 */
	private Random random;

	/**
	 * The DrZam instance.
	 */
	private DrZam drzam;

	/**
	 * The tiles that make up the board.
	 */
	private PillColor[][] tiles;
	
	/**
	 * Indicates if virus are set
	 */
	private boolean virusSet;

	/**
	 * Crates a new GameBoard instance.
	 * 
	 * @param drzam
	 *            The DrZam instance to use.
	 */
	public BoardPanel(DrZam drzam) {
		this.drzam = drzam;
		this.tiles = new PillColor[ROW_COUNT][COL_COUNT];

		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}

	/**
	 * Resets the board and clears away any tiles.
	 */
	public void clear() {
		/*
		 * Loop through every tile index and set it's value to null to clear the
		 * board.
		 */
		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = 0; j < COL_COUNT; j++) {
				tiles[i][j] = null;
			}
		}
	}

	/**
	 * Determines whether or not a piece can be placed at the coordinates.
	 * 
	 * @param type
	 *            THe type of piece to use.
	 * @param x
	 *            The x coordinate of the piece.
	 * @param y
	 *            The y coordinate of the piece.
	 * @param rotation
	 *            The rotation of the piece.
	 * @return Whether or not the position is valid.
	 */
	public boolean isValidAndEmpty(PillColor type, int x, int y, int rotation) {

		// Ensure the piece is in a valid column.
		if (x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}

		// Ensure the piece is in a valid row.
		if (y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}

		/*
		 * Loop through every tile in the piece and see if it conflicts with an
		 * existing tile.
		 * 
		 * Note: It's fine to do this even though it allows for wrapping because
		 * we've already checked to make sure the piece is in a valid location.
		 */
		for (int col = 0; col < type.getDimension(); col++) {
			for (int row = 0; row < type.getDimension(); row++) {
				if (type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Adds a piece to the game board. Note: Doesn't check for existing pieces,
	 * and will overwrite them if they exist.
	 * 
	 * @param type
	 *            The type of piece to place.
	 * @param x
	 *            The x coordinate of the piece.
	 * @param y
	 *            The y coordinate of the piece.
	 * @param rotation
	 *            The rotation of the piece.
	 */
	public void addPiece(PillColor type, int x, int y, int rotation) {
		/*
		 * Loop through every tile within the piece and add it to the board only
		 * if the boolean that represents that tile is set to true.
		 */
		for (int col = 0; col < type.getDimension(); col++) {
			for (int row = 0; row < type.getDimension(); row++) {
				if (type.isTile(col, row, rotation)) {
					setTile(col + x, row + y, type);
				}
			}
		}
	}

	public String jSonLiveInfo;

	public String getjSonLiveInfo() {
		return jSonLiveInfo;
	}

	public void setjSonLiveInfo(String jSonLiveInfo) {
		this.jSonLiveInfo = jSonLiveInfo;
	}

	public String prepareLiveBoardInfo() {
		// System.out.println("sincInfo");
		PlayerInfo info = new PlayerInfo();
		info.setId(Calendar.getInstance().getTimeInMillis());
		info.setPlayer(getPlayerNumber());
		info.setScore(0);
		info.setGameOver(isGameOver());

		info.setType(drzam.getPieceType());
		info.setPieceCol(drzam.getPieceCol());
		info.setPieceRow(drzam.getPieceRow());
		info.setRotation(drzam.getPieceRotation());

		info.setTiles(tiles);
		Gson gson = new Gson();
		setjSonLiveInfo(gson.toJson(info));
		// System.out.println("jsonString:"+getjSonLiveInfo());
		return getjSonLiveInfo();
	}

	public int deleteConsecutivePills() {
		int deletes = 0;
		for (PillColor color : PillColor.values()) {
			// Eliminando en fila
			for (int f = 0; f < tiles.length; f++) {
				int consecutives = 0;
				for (int c = 0; c < tiles[f].length; c++) {
					PillColor pill = tiles[f][c];
					boolean change = (pill == null || !pill.equals(color));
					if (!change) {
						consecutives++;
					}

					boolean boardEnd = c == tiles[f].length - 1;
					if (consecutives >= CONSECUTIVE_PILLS_TO_DELETE && (change || boardEnd)) {
						int deleteFrom = boardEnd && !change ? c + 1 : c;
						for (; consecutives > 0; consecutives--) {
							tiles[f][deleteFrom - consecutives] = null;
						}
						deletes++;
					}
					if (change) {
						consecutives = 0;
					}
				}
			}

			// Eliminando en columna
			for (int c = 0; c < tiles[0].length; c++) {
				int consecutives = 0;
				for (int f = 0; f < tiles.length; f++) {
					PillColor pill = tiles[f][c];
					boolean change = (pill == null || !pill.equals(color));
					if (!change) {
						consecutives++;
					}
					boolean boardEnd = f == (tiles.length - 1);
					if (consecutives >= CONSECUTIVE_PILLS_TO_DELETE && (change || boardEnd)) {
						int deleteFrom = boardEnd && !change ? f + 1 : f;
						for (; consecutives > 0; consecutives--) {
							tiles[deleteFrom - consecutives][c] = null;
						}
						deletes++;
					}
					if (change) {
						consecutives = 0;
					}
				}

			}

		}
		return deletes;

	}

	/**
	 * Checks to see if the tile is already occupied.
	 * 
	 * @param x
	 *            The x coordinate to check.
	 * @param y
	 *            The y coordinate to check.
	 * @return Whether or not the tile is occupied.
	 */
	private boolean isOccupied(int x, int y) {
		return tiles[y][x] != null;
	}

	/**
	 * Sets a tile located at the desired column and row.
	 * 
	 * @param x
	 *            The column.
	 * @param y
	 *            The row.
	 * @param type
	 *            The value to set to the tile to.
	 */
	private void setTile(int x, int y, PillColor type) {
		tiles[y][x] = type;
	}

	/**
	 * Gets a tile by it's column and row.
	 * 
	 * @param x
	 *            The column.
	 * @param y
	 *            The row.
	 * @return The tile.
	 */
	private PillColor getTile(int x, int y) {
		return tiles[y][x];
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// This helps simplify the positioning of things.
		g.translate(BORDER_WIDTH, BORDER_WIDTH);

		/*
		 * Draw the board differently depending on the current game state.
		 */
		if (drzam.isPaused()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			String msg = "PAUSED";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
		} else if (drzam.isNewGame() || drzam.isGameOver()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);

			/*
			 * Because both the game over and new game screens are nearly
			 * identical, we can handle them together and just use a ternary
			 * operator to change the messages that are displayed.
			 */
			String msg = drzam.isNewGame() ? "DR. ZAM" : (isGameOver() ? "GAME OVER" : "ERES EL GANADOR");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = "Press Enter to Play" + (drzam.isNewGame() ? "" : " Again");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
		} else {
			drawVirus();

			/*
			 * Draw the tiles onto the board.
			 */
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					PillColor tile = getTile(x, y);
					if (tile != null) {
						drawTile(tile, x * PILL_VIEW_SIZE, (y - HIDDEN_ROW_COUNT) * PILL_VIEW_SIZE, g);
					}
				}
			}

			/*
			 * Draw the current piece. This cannot be drawn like the rest of the
			 * pieces because it's still not part of the game board. If it were
			 * part of the board, it would need to be removed every frame which
			 * would just be slow and confusing.
			 */
			prepareLiveBoardInfo();
			PillColor type = drzam.getPieceType();
			int pieceCol = drzam.getPieceCol();
			int pieceRow = drzam.getPieceRow();
			int rotation = drzam.getPieceRotation();

			/*
			 *  Draw the piece onto the board.
			 */
			for (int col = 0; col < type.getDimension(); col++) {
				for (int row = 0; row < type.getDimension(); row++) {
					if (pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
						drawTile(type, (pieceCol + col) * PILL_VIEW_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * PILL_VIEW_SIZE,
								g);
					}
				}
			}

			/*
			 * Draw the ghost (semi-transparent piece that shows where the
			 * current piece will land). I couldn't think of a better way to
			 * implement this so it'll have to do for now. We simply take the
			 * current position and move down until we hit a row that would
			 * cause a collision.
			 */
//			Color base = type.getBaseColor();
//			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
//			for (int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
//				// If no collision is detected, try the next row.
//				if (isValidAndEmpty(type, pieceCol, lowest, rotation)) {
//					continue;
//				}
//
//				// Draw the ghost one row higher than the one the collision took
//				// place at.
//				lowest--;
//
//				// Draw the ghost piece.
//				for (int col = 0; col < type.getDimension(); col++) {
//					for (int row = 0; row < type.getDimension(); row++) {
//						if (lowest + row >= 2 && type.isTile(col, row, rotation)) {
//							drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * PILL_VIEW_SIZE,
//									(lowest + row - HIDDEN_ROW_COUNT) * PILL_VIEW_SIZE, g);
//						}
//					}
//				}
//
//				break;
//			}

			/*
			 * Draw the background grid above the pieces (serves as a useful
			 * visual for players, and makes the pieces look nicer by breaking
			 * them up.
			 */
			g.setColor(Color.DARK_GRAY);
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * PILL_VIEW_SIZE, COL_COUNT * PILL_VIEW_SIZE, y * PILL_VIEW_SIZE);
					g.drawLine(x * PILL_VIEW_SIZE, 0, x * PILL_VIEW_SIZE, VISIBLE_ROW_COUNT * PILL_VIEW_SIZE);
				}
			}
		}

		/*
		 * Draw the outline.
		 */
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, PILL_VIEW_SIZE * COL_COUNT, PILL_VIEW_SIZE * VISIBLE_ROW_COUNT);
	}

	/**
	 * Draws a tile onto the board.
	 * 
	 * @param type
	 *            The type of tile to draw.
	 * @param x
	 *            The column.
	 * @param y
	 *            The row.
	 * @param g
	 *            The graphics object.
	 */
	private void drawTile(PillColor type, int x, int y, Graphics g) {
		drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
	}

	/**
	 * Draws a tile onto the board.
	 * 
	 * @param base
	 *            The base color of tile.
	 * @param light
	 *            The light color of the tile.
	 * @param dark
	 *            The dark color of the tile.
	 * @param x
	 *            The column.
	 * @param y
	 *            The row.
	 * @param g
	 *            The graphics object.
	 */
	private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {

		/*
		 * Fill the entire tile with the base color.
		 */
		g.setColor(base);
		g.fillOval(x, y, PILL_VIEW_SIZE, PILL_VIEW_SIZE);

		/*
		 * Fill the bottom and right edges of the tile with the dark shading
		 * color.
		 */
		g.setColor(dark);

		/*
		 * Fill the top and left edges with the light shading. We draw a single
		 * line for each row or column rather than a rectangle so that we can
		 * draw a nice looking diagonal where the light and dark shading meet.
		 */
		g.setColor(light);
	}

	/**
	 * Draw a virus onto the board
	 */
	public void drawVirus() {
		if (!isVirusSet()) {
			setVirusSet(true);
			PillColor virusColor = null;
			int x = 0;
			int y = 0;
			random = new Random();

			for (int i = 0; i < VIRUS_QUANTITY; i++) {

				virusColor = PillColor.values()[random.nextInt(PillColor.values().length)];

				x = (i * 3) + random.nextInt(COL_COUNT / 3);
				y = 10 + random.nextInt(VISIBLE_ROW_COUNT / 2);

				System.out.println("x,y:[" + x + "," + y + "]");
				// drawTile(virusColor, (x) * PILL_VIEW_SIZE, (y - HIDDEN_ROW_COUNT)
				// * PILL_VIEW_SIZE, g);
				setTile(x, y, virusColor);
			}
		}
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public boolean isVirusSet() {
		return virusSet;
	}

	public void setVirusSet(boolean virusSet) {
		this.virusSet = virusSet;
	}

}
