package edu.foo.tetrixmult.controler;

public class Eliminacion implements Regla {

	private static final int CANTIDAD_CONSECUTIVOS = 4;

	@Override
	public void aplicar(Partida partida) {
		for (ColorPastilla color : ColorPastilla.values()) {
			for (Jugador jugador : partida.getJugadores()) {

				Frasco frasco = jugador.getFrasco();
				// Lleando matriz de posiciones de pastillas
				Pastilla[][] matrizPastillas = new Pastilla[frasco.getTamanoX()][frasco.getTamanoY()];
				for (Pastilla pastilla : frasco.getPastillas()) {
					matrizPastillas[pastilla.getPosicion().getPosicionX()][pastilla.getPosicion()
							.getPosicionY()] = pastilla;
				}

				// Eliminando en fila
				for (int f = 0; f < frasco.getTamanoY(); f++) {
					int consecutivos = 0;
					for (int c = 0; c < frasco.getTamanoX(); c++) {
						Pastilla pastilla = matrizPastillas[f][c];
						boolean colorBuscado = pastilla.getColor().equals(color);
						if (colorBuscado) {
							consecutivos++;
						} else {
							if (consecutivos >= CANTIDAD_CONSECUTIVOS) {
								for (; consecutivos > 0; consecutivos--) {
									frasco.eliminarPastilla(matrizPastillas[f][c - (consecutivos - 1)]);
								}
							}
						}
					}
				}

				// Eliminando en columna
				for (int c = 0; c < frasco.getTamanoY(); c++) {
					int consecutivos = 0;
					for (int f = 0; f < frasco.getTamanoX(); f++) {
						Pastilla pastilla = matrizPastillas[f][c];
						boolean colorBuscado = pastilla.getColor().equals(color);
						if (colorBuscado) {
							consecutivos++;
						} else {
							if (consecutivos >= CANTIDAD_CONSECUTIVOS) {
								for (; consecutivos > 0; consecutivos--) {
									frasco.eliminarPastilla(matrizPastillas[c][f - (consecutivos - 1)]);
								}
							}
						}
					}
				}
			}
		}
	}

}
