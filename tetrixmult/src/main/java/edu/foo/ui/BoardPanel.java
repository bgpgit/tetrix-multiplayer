package edu.foo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import edu.foo.tetrixmult.controler.Jugador;
import edu.foo.tetrixmult.controler.Partida;
import edu.foo.tetrixmult.controler.Pastilla;

public class BoardPanel extends JPanel {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 5055679736784226108L;

	/**
	 * Minimum color component values for tiles. This is required if we want to
	 * show both light and dark shading on our tiles.
	 */
	public static final int COLOR_MIN = 35;

	/**
	 * Maximum color component values for tiles. This is required if we want to
	 * show both light and dark shading on our tiles.
	 */
	public static final int COLOR_MAX = 255 - COLOR_MIN;

	/**
	 * The width of the border around the game board.
	 */
	private static final int BORDER_WIDTH = 5;

	/**
	 * The number of pixels that a tile takes up.
	 */
	public static final int TILE_SIZE = 30;

	/**
	 * The width of the shading on the tiles.
	 */
	public static final int SHADE_WIDTH = 5;

	/**
	 * The larger font to display.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

	private Jugador jugador;
	private Partida partida;

	/**
	 * The tiles that make up the board.
	 */
	private Pastilla[][] tiles;

	/**
	 * Crates a new GameBoard instance.
	 * 
	 * @param drZam
	 *            The DrZam instance to use.
	 */
	public BoardPanel(Partida partida, Jugador jugador) {
		this.partida = partida;
		this.jugador = jugador;
		this.tiles = new Pastilla[jugador.getFrasco().getTamanoX()][jugador.getFrasco().getTamanoY()];
		setPreferredSize(new Dimension(jugador.getFrasco().getTamanoX() * TILE_SIZE + BORDER_WIDTH * 2,
				jugador.getFrasco().getTamanoY() * TILE_SIZE + BORDER_WIDTH * 2));
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
		for (int i = 0; i < jugador.getFrasco().getTamanoX(); i++) {
			for (int j = 0; j < jugador.getFrasco().getTamanoY(); j++) {
				tiles[i][j] = null;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// This helps simplify the positioning of things.
		g.translate(BORDER_WIDTH, BORDER_WIDTH);
		/*
		 * Draw the outline.
		 */
		g.setColor(Color.CYAN);
		// g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE *
		// VISIBLE_ROW_COUNT);
		g.draw3DRect(0, 0, TILE_SIZE * jugador.getFrasco().getTamanoX(), TILE_SIZE * jugador.getFrasco().getTamanoY(),
				false);
		// Escribiedo mensajes
		g.setFont(LARGE_FONT);
		g.setColor(Color.WHITE);
		String mensaje = null;
		if (partida == null) {
			mensaje = "Enter para iniciar partida";
		} else if (partida.getEstaTerminada()) {
			if (partida.getGanador() == null) {
				mensaje = "Empate";
			} else if (partida.getGanador().equals(jugador)) {
				mensaje = "Ganador";
			} else {
				mensaje = "Perdedor";
			}
		}
		if (mensaje != null) {
			escribirMensaje(g, mensaje);
		} else {
			pintarPastillas(g);
		}

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
	private void pintarPastilla(Pastilla pastilla, Graphics g) {
		Color color;
		switch (pastilla.getColor()) {
		case ROJO: {
			color = Color.RED;
			break;
		}
		case AZUL: {
			color = Color.BLUE;
			break;
		}
		case VERDE: {
			color = Color.GREEN;
			break;
		}
		default: {
			throw new IllegalArgumentException("Color no definido");
		}
		}
		/*
		 * Fill the entire tile with the base color.
		 */
		g.setColor(color);
		g.fillOval(pastilla.getPosicion().getPosicionX() * TILE_SIZE, pastilla.getPosicion().getPosicionY() * TILE_SIZE,
				TILE_SIZE, TILE_SIZE);

		/*
		 * Fill the bottom and right edges of the tile with the dark shading
		 * color.
		 */
		g.setColor(color.darker());
		g.setColor(color.brighter());
	}

	private void escribirMensaje(Graphics g, String mensaje) {
		int centroX = jugador.getFrasco().getTamanoX() * TILE_SIZE / 2;
		int centroY = jugador.getFrasco().getTamanoY() * TILE_SIZE / 2;
		g.setFont(LARGE_FONT);
		g.setColor(Color.WHITE);
		g.drawString(mensaje, centroX - g.getFontMetrics().stringWidth(mensaje) / 2, centroY);
	}

	private void pintarPastillas(Graphics g) {

		/*
		 * Draw the tiles onto the board.
		 */
		for (Pastilla pastilla : jugador.getFrasco().getPastillas()) {
			pintarPastilla(pastilla, g);
		}

		/*
		 * Draw the ghost (semi-transparent piece that shows where the current
		 * piece will land). I couldn't think of a better way to implement this
		 * so it'll have to do for now. We simply take the current position and
		 * move down until we hit a row that would cause a collision.
		 */
		// Color base = type.getBaseColor();
		// base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
		// for (int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
		// // If no collision is detected, try the next row.
		// if (isValidAndEmpty(type, pieceCol, lowest, rotation)) {
		// continue;
		// }
		//
		// // Draw the ghost one row higher than the one the collision took
		// // place at.
		// lowest--;
		//
		// // Draw the ghost piece.
		// for (int col = 0; col < type.getDimension(); col++) {
		// for (int row = 0; row < type.getDimension(); row++) {
		// if (lowest + row >= 2 && type.isTile(col, row, rotation)) {
		// pintarPastilla(base, base.brighter(), base.darker(), (pieceCol + col)
		// * TILE_SIZE,
		// (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
		// }
		// }
		// }
		//
		// break;
		// }

		/*
		 * Draw the background grid above the pieces (serves as a useful visual
		 * for players, and makes the pieces look nicer by breaking them up.
		 */
		// g.setColor(Color.DARK_GRAY);
		// for(int x = 0; x < COL_COUNT; x++) {
		// for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
		// g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y *
		// TILE_SIZE);
		// g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT *
		// TILE_SIZE);
		// }
		// }
	}

}
