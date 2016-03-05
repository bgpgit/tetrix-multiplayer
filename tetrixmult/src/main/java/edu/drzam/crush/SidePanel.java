package edu.drzam.crush;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SidePanel extends JPanel {
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 2181495598854992747L;

	/**
	 * The dimensions of each tile on the next piece preview.
	 */
	private static final int PILL_VIEW_SIZE = LivePanel.TILE_SIZE >> 1;
	
//	/**
//	 * The width of the shading on each tile on the next piece preview.
//	 */
//	private static final int SHADE_WIDTH = BoardPanel.SHADE_WIDTH >> 1;
	
	/**
	 * The number of rows and columns in the preview window. Set to
	 * 5 because we can show any piece with some sort of padding.
	 */
	private static final int TILE_COUNT = 5;
	
	/**
	 * The center x of the next piece preview box.
	 */
	private static final int SQUARE_CENTER_X = 130;
	
	/**
	 * The center y of the next piece preview box.
	 */
	private static final int SQUARE_CENTER_Y = 65;
	
	/**
	 * The size of the next piece preview box.
	 */
	private static final int SQUARE_SIZE = (PILL_VIEW_SIZE * TILE_COUNT >> 1);
	
	/**
	 * The number of pixels used on a small insets (generally used for categories).
	 */
	private static final int SMALL_INSET = 20;
	
	/**
	 * The number of pixels used on a large insets.
	 */
	private static final int LARGE_INSET = 40;
	
	/**
	 * The y coordinate of the stats category.
	 */
	private static final int STATS_INSET = 175;
	
	/**
	 * The y coordinate of the controls category.
	 */
	private static final int CONTROLS_INSET = 300;
	
	/**
	 * The number of pixels to offset between each string.
	 */
	private static final int TEXT_STRIDE = 25;
	
	/**
	 * The small font.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 11);
	
	/**
	 * The large font.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);
	
	/**
	 * The color to draw the text and preview box in.
	 */
	private static final Color DRAW_COLOR = new Color(128, 192, 128);
	
	/**
	 * The DrZam instance.
	 */
	private DrZam drzam;
	
	/**
	 * Creates a new SidePanel and sets it's display properties.
	 * @param drzam The DrZam instance to use.
	 */
	public SidePanel(DrZam drzam) {
		this.drzam = drzam;
		
		setPreferredSize(new Dimension(200, LivePanel.PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Set the color for drawing.
		g.setColor(DRAW_COLOR);
		
		/*
		 * This variable stores the current y coordinate of the string.
		 * This way we can re-order, add, or remove new strings if necessary
		 * without needing to change the other strings.
		 */
		int offset;
		
		/*
		 * Draw the score box.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Punteo "+drzam.config.getPlayerNumber(), SMALL_INSET, offset = STATS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString(""+drzam.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
		g.drawRect(SMALL_INSET+10, STATS_INSET+10, SQUARE_SIZE+20, SQUARE_SIZE);
		
		/*
		 * Draw the score box.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Punteo "+((drzam.config.getPlayerNumber()==1)?"2":"1"), (SMALL_INSET+90), offset = STATS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString(""+drzam.getSecondScore(), (LARGE_INSET+90), offset += TEXT_STRIDE);
		g.drawRect(SMALL_INSET+100, STATS_INSET+10, SQUARE_SIZE+20, SQUARE_SIZE);
		
		/*
		 * Draw the "Controls" category.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Controles", SMALL_INSET, offset = CONTROLS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("Flecha Izq. - Izquierda", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Flecha Der. - Derecha", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Flecha Aba. - Bajar", LARGE_INSET, offset += TEXT_STRIDE);
		//g.drawString("P - Pausa", LARGE_INSET, offset += TEXT_STRIDE);
		
		/*
		 * Draw the next piece preview box.
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Next Piece:", SMALL_INSET, 70);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		
		/*
		 * Draw a preview of the next piece that will be spawned. The code is pretty much
		 * identical to the drawing code on the board, just smaller and centered, rather
		 * than constrained to a grid.
		 */
		PillColor type = drzam.getNextPieceType();
		if(!drzam.isGameOver() && type != null) {
			/*
			 * Get the size properties of the current piece.
			 */
			int cols = type.getCols();
			int rows = type.getRows();
			int dimension = type.getDimension();
		
			/*
			 * Calculate the top left corner (origin) of the piece.
			 */
			int startX = (SQUARE_CENTER_X - (cols * PILL_VIEW_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * PILL_VIEW_SIZE / 2));
		
			/*
			 * Get the insets for the preview. The default
			 * rotation is used for the preview, so we just use 0.
			 */
			int top = type.getTopInset(0);
			int left = type.getLeftInset(0);
		
			/*
			 * Loop through the piece and draw it's tiles onto the preview.
			 */
			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(type.isTile(col, row, 0)) {
						drawTile(type, startX + ((col - left) * PILL_VIEW_SIZE), startY + ((row - top) * PILL_VIEW_SIZE), g);
					}
				}
			}
		}
	}
	
	/**
	 * Draws a tile onto the preview window.
	 * @param type The type of tile to draw.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @param g The graphics object.
	 */
	private void drawTile(PillColor type, int x, int y, Graphics g) {
		/*
		 * Fill the entire tile with the base color.
		 */
		g.setColor(type.getBaseColor());
		g.fillOval(x, y, PILL_VIEW_SIZE, PILL_VIEW_SIZE);
	}
	
}
