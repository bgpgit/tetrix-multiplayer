package edu.drzam.crush;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.google.gson.Gson;

import edu.drzam.v1.PillColor;

public class LivePanel extends JPanel {
	
	/**
	 * Number of player that joins the game
	 */
	private int playerNumber;
	
	private boolean isGameOver;
	private boolean isWinner;
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 5055679736784226108L;

	/**
	 * Minimum color component values for tiles. This is required if we
	 * want to show both light and dark shading on our tiles.
	 */
	public static final int COLOR_MIN = 35;
	
	/**
	 * Maximum color component values for tiles. This is required if we
	 * want to show both light and dark shading on our tiles.
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
	 *The quantity of virus to be shown 
	 */
	public static final int VIRUS_QUANTITY = 3;
	public int VIRUS_INACTIVE_QUANTITY = 0;
	
	/**
	 * The random number generator. This is used to
	 * spit out pieces randomly.
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
	
	private List<Virus> virus;
	
	private boolean virusSet;
	
	private List<String> erasePills = new ArrayList<String>();
	private List<String> erasePillsTypeLB = new ArrayList<String>();
	private List<String> erasePillsTypeBlue = new ArrayList<String>();
	private List<String> erasePillsTypePink = new ArrayList<String>();
	private List<String> erasePillsTypeYllw = new ArrayList<String>();
	private List<String> erasePillsTypeRed = new ArrayList<String>();
	
	private int countTypePillLB = 0;
	private int countTypePillBlue = 0;
	private int countTypePillPink = 0;
	private int countTypePillYllw = 0;
	private int countTypePillRed = 0;
	
	/**
	 * Crates a new GameBoard instance.
	 * @param drzam The DrZam instance to use.
	 */
	public LivePanel(DrZam drzam) {
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
		 * Loop through every tile index and set it's value
		 * to null to clear the board.
		 */
		for(int i = 0; i < ROW_COUNT; i++) {
			for(int j = 0; j < COL_COUNT; j++) {
				tiles[i][j] = null;
			}
		}
	}
	
	/**
	 * Determines whether or not a piece can be placed at the coordinates.
	 * @param type THe type of piece to use.
	 * @param x The x coordinate of the piece.
	 * @param y The y coordinate of the piece.
	 * @param rotation The rotation of the piece.
	 * @return Whether or not the position is valid.
	 */
	public boolean isValidAndEmpty(PillColor type, int x, int y, int rotation) {
				
		//Ensure the piece is in a valid column.
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}
		
		//Ensure the piece is in a valid row.
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}
		
		/*
		 * Loop through every tile in the piece and see if it conflicts with an existing tile.
		 * 
		 * Note: It's fine to do this even though it allows for wrapping because we've already
		 * checked to make sure the piece is in a valid location.
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Adds a piece to the game board. Note: Doesn't check for existing pieces,
	 * and will overwrite them if they exist.
	 * @param type The type of piece to place.
	 * @param x The x coordinate of the piece.
	 * @param y The y coordinate of the piece.
	 * @param rotation The rotation of the piece.
	 */
	public void addPiece(PillColor type, int x, int y, int rotation) {
		/*
		 * Loop through every tile within the piece and add it
		 * to the board only if the boolean that represents that
		 * tile is set to true.
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation)) {
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
		//System.out.println("sincInfo");
		PlayerInfo info = new PlayerInfo();
		info.setId(Calendar.getInstance().getTimeInMillis());
		info.setPlayer(getPlayerNumber());
		info.setScore(drzam.getScore());
		info.setGameOver(isGameOver());
		
		info.setType(drzam.getPieceType());
		info.setPieceCol(drzam.getPieceCol());
		info.setPieceRow(drzam.getPieceRow());
		info.setRotation(drzam.getPieceRotation());
		
		info.setTiles(tiles);
		Gson gson = new Gson();
		setjSonLiveInfo(gson.toJson(info));
		//System.out.println("jsonString:"+getjSonLiveInfo());
		return getjSonLiveInfo();
	}
	
	/**
	 * Checks the board to see if any lines have been cleared, and
	 * removes them from the game.
	 * @return The number of lines that were cleared.
	 */
	public int checkLines() {
		int completedLines = 0;
		checkVirus(0);
		
		/*
		 * Here we loop through every line and check it to see if
		 * it's been cleared or not. If it has, we increment the
		 * number of completed lines and check the next row.
		 * 
		 * The checkLine function handles clearing the line and
		 * shifting the rest of the board down for us.
		 */
		for(int row = 0; row < ROW_COUNT; row++) {
			if(checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}
			
	/**
	 * Checks whether or not {@code row} is full.
	 * @param line The row to check.
	 * @return Whether or not this row is full.
	 */
	private boolean checkLine(int line) {
		/*
		 * Iterate through every column in this row. If any of them are
		 * empty, then the row is not full.
		 */
		for(int col = 0; col < COL_COUNT; col++) {
			if(!isOccupied(col, line)) {
				return false;
			}
		}

		/*
		 * Since the line is filled, we need to 'remove' it from the game.
		 * To do this, we simply shift every row above it down by one.
		 */
		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < COL_COUNT; col++) {
				setTile(col, row + 1, getTile(col, row));
			}
		}
		return true;
	}
	
	private void inactivateVirus(PillColor virusColor) {
		for(int i = 0; i < VIRUS_QUANTITY; i++){
			Virus vir = virus.get(i);
			if (vir.getColor()==virusColor) {
				this.VIRUS_INACTIVE_QUANTITY++;
				vir.setActive(false);
				this.virus.remove(i);
				this.virus.add(i,vir);
				drzam.setScore(drzam.getScore()+100);
			}
		}
		if (VIRUS_QUANTITY==VIRUS_INACTIVE_QUANTITY) {
			setGameOver(true);
			setWinner(true);
		}
	}
	
	private void checkVirus(int rep) {
		//System.out.println("checkVirus");
		
		for(int i = 0; i < VIRUS_QUANTITY; i++){
			Virus vir = virus.get(i);
			if (vir.isActive()) {
				//System.out.println("    checkVirus[x,y,color]:["+vir.getCol()+","+vir.getRow()+","+vir.getColor()+"]");
				this.erasePills = new  ArrayList<String>();
				getVirusPills(vir.getCol(), vir.getRow(), vir.getColor(), rep);
			}
		}
		
		if (countTypePillBlue>3) {
			String checkColor = new String();
			for (int i=0; i<countTypePillBlue; i++) {
				String[] args = erasePillsTypeBlue.get(i).split(",");
				checkColor = args[2];
				setTile(Integer.parseInt(args[0]), Integer.parseInt(args[1]), null);
				drzam.setScore(drzam.getScore()+50);
			}
			this.countTypePillBlue = 0;
			inactivateVirus(PillColor.valueOf(checkColor));
		} 
		if (countTypePillLB>3) {
			String checkColor = new String();
			for (int i=0; i<countTypePillLB; i++) {
				String[] args = erasePillsTypeLB.get(i).split(",");
				checkColor = args[2];
				setTile(Integer.parseInt(args[0]), Integer.parseInt(args[1]), null);
				drzam.setScore(drzam.getScore()+50);
			}
			this.countTypePillLB = 0;
			inactivateVirus(PillColor.valueOf(checkColor));
		} 
		if (countTypePillPink>3) {
			String checkColor = new String();
			for (int i=0; i<countTypePillPink; i++) {
				String[] args = erasePillsTypePink.get(i).split(",");
				checkColor = args[2];
				setTile(Integer.parseInt(args[0]), Integer.parseInt(args[1]), null);
				drzam.setScore(drzam.getScore()+50);
			}
			this.countTypePillPink = 0;
			inactivateVirus(PillColor.valueOf(checkColor));
		} 
		if (countTypePillRed>3) {
			String checkColor = new String();
			for (int i=0; i<countTypePillRed; i++) {
				String[] args = erasePillsTypeRed.get(i).split(",");
				checkColor = args[2];
				setTile(Integer.parseInt(args[0]), Integer.parseInt(args[1]), null);
				drzam.setScore(drzam.getScore()+50);
			}
			this.countTypePillRed = 0;
			inactivateVirus(PillColor.valueOf(checkColor));
		}
		if (countTypePillYllw>3) {
			String checkColor = new String();
			for (int i=0; i<countTypePillYllw; i++) {
				String[] args = erasePillsTypeYllw.get(i).split(",");
				checkColor = args[2];
				setTile(Integer.parseInt(args[0]), Integer.parseInt(args[1]), null);
				drzam.setScore(drzam.getScore()+50);
			}
			this.countTypePillYllw = 0;
			inactivateVirus(PillColor.valueOf(checkColor));
		}
		
		if (countTypePillBlue<4) {
			this.countTypePillBlue = 0;
			this.erasePillsTypeBlue = new ArrayList<String>();
		} 
		if (countTypePillLB<4) {
			this.countTypePillLB = 0;
			this.erasePillsTypeLB = new ArrayList<String>();
		} 
		if (countTypePillPink<4) {
			this.countTypePillPink = 0;
			this.erasePillsTypePink = new ArrayList<String>();
		} 
		if (countTypePillRed<4) {
			this.countTypePillRed = 0;
			this.erasePillsTypeRed = new ArrayList<String>();
		}
		if (countTypePillYllw<4) {
			this.countTypePillYllw = 0;
			this.erasePillsTypeYllw = new ArrayList<String>();
		}
		
	}

	private void getVirusPills(int px, int py, PillColor virusColor, int rep) {
		String span = new String();
		for (int i=0; i<=rep; i++)
			span+="    ";
		//System.out.println(span+"getVirusPills:" + rep);
		//System.out.println(span+"Scan Virus Pill " + virusColor);
		int iniX = (px>0)?px-1:0;
		int iniY = (py>0)?py-1:0;
		int maxX = (px<8)?px+3:px+2;
		int maxY = (py<17)?py+3:py+2;
		//System.out.println("py:"+py+",iniY:"+iniY+",maxY:"+maxY);
		//System.out.println("py:"+py+",iniY:"+iniY+",maxY:"+maxY);
		if (maxY>22)
			maxY=22;
		//System.out.println("py:"+py+",iniY:"+iniY+",maxY:"+maxY);
		for (int y = iniY; y < maxY; y++) {
			for (int x = iniX; x < maxX; x++) {
				if (getTile(x, y)==null) {
//					//System.out.println(span+"[x,y,color]:["+x+","+y+",null]");
				} else if (getTile(x, y)==virusColor) {
					if ((x==px && y==py)) {
//						//System.out.println(span+"[x,y,color]:["+x+","+y+",virusXY]");
					} else {
						//System.out.println(span+"[x,y,color]:["+x+","+y+",equals]");
						String codXY = String.valueOf(x)+","+String.valueOf(y)+","+getTile(x, y);
						if (!erasePills.contains(codXY)) {
							//System.out.println("pill add to erase");
							this.erasePills.add(codXY);
							//System.out.println("call recursive getVirusPills");
							if (getTile(x, y)==PillColor.TypePillBlue) {
								this.countTypePillBlue++;
								this.erasePillsTypeBlue.add(codXY);
							} else if (getTile(x, y)==PillColor.TypePillLB) {
								this.countTypePillLB++;
								this.erasePillsTypeLB.add(codXY);
							} else if (getTile(x, y)==PillColor.TypePillPink) {
								this.countTypePillPink++;
								this.erasePillsTypePink.add(codXY);
							} else if (getTile(x, y)==PillColor.TypePillRed) {
								this.countTypePillRed++;
								this.erasePillsTypeRed.add(codXY);
							} else if (getTile(x, y)==PillColor.TypePillYllw) {
								this.countTypePillYllw++;
								this.erasePillsTypeYllw.add(codXY);
							}
							rep++;
							getVirusPills(x, y, virusColor, rep);
						} else {
							//System.out.println(span+"pill already exists to erase");
							//System.out.println(span+"dont call recursive getVirusPills");
						}
					}
				} else {
//					//System.out.println(span+"[x,y,color]:["+x+","+y+","+getTile(x, y)+"]");
				}
			}
		}
		
		for (String pos : erasePills) {
			//System.out.println(span+"erasePills: "+pos);
		}
		
		//System.out.println(span+"this.countTypePillBlue:"+countTypePillBlue);
		//System.out.println(span+"this.countTypePillLB:"+countTypePillLB);
		//System.out.println(span+"this.countTypePillPink:"+countTypePillPink);
		//System.out.println(span+"this.countTypePillRed:"+countTypePillRed);
		//System.out.println(span+"this.countTypePillYllw:"+countTypePillYllw);
	}
	
	/**
	 * Checks to see if the tile is already occupied.
	 * @param x The x coordinate to check.
	 * @param y The y coordinate to check.
	 * @return Whether or not the tile is occupied.
	 */
	private boolean isOccupied(int x, int y) {
		return tiles[y][x] != null;
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
			String msg = drzam.isNewGame() ? "DR. ZAM" : 
				((isWinner() && isGameOver())?"ERES EL GANADOR":"GAME OVER");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = "Press Enter to Play" + (drzam.isNewGame() ? "" : " Again");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
		} else {
			
			drawVirus();
			
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
			
			//Draw the piece onto the board.
			for(int col = 0; col < type.getDimension(); col++) {
				for(int row = 0; row < type.getDimension(); row++) {
					if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
						drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			
			/*
			 * Draw the ghost (semi-transparent piece that shows where the current piece will land). I couldn't think of
			 * a better way to implement this so it'll have to do for now. We simply take the current position and move
			 * down until we hit a row that would cause a collision.
			 */
			Color base = type.getBaseColor();
			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
			for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
				//If no collision is detected, try the next row.
				if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {					
					continue;
				}
				
				//Draw the ghost one row higher than the one the collision took place at.
				lowest--;
				
				//Draw the ghost piece.
				for(int col = 0; col < type.getDimension(); col++) {
					for(int row = 0; row < type.getDimension(); row++) {
						if(lowest + row >= 2 && type.isTile(col, row, rotation)) {
							drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}
				
				break;
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
	 * Draw a virus onto the board
	 * @param virus Color of virus to be draw
	 * @param tileSize
	 * @param g The graphics object.
	 */
	public void drawVirus(){
		if (!isVirusSet()) {
			setVirusSet(true);
			PillColor virusColor = null;
			int x = 0;
			int y= 0;
			random = new Random();
			this.virus = new ArrayList<Virus>();
			List<String> lstVirus = new ArrayList<String>();
			for(int i = 0; i < VIRUS_QUANTITY; i++){
				boolean repeat = true;
				String vi = new String();
				while (repeat) {
					vi = String.valueOf(random.nextInt(PillColor.values().length));
					if (!lstVirus.contains(vi)) {
						repeat = false;
						lstVirus.add(vi);
					}
				}
				virusColor = PillColor.values()[Integer.parseInt(vi)];
				
				x = (i*3) + random.nextInt(COL_COUNT/3);
				y = 10 + random.nextInt(VISIBLE_ROW_COUNT/2);
				
				//System.out.println("x,y:["+x+","+y+"]");

				setTile(x, y, virusColor);
				Virus vir = new Virus();
				vir.setCol(x);
				vir.setRow(y);
				vir.setActive(true);
				vir.setColor(virusColor);
				this.virus.add(vir);
			}
//			setVirus(iniVirus);
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

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

}
