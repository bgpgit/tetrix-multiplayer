package edu.foo.tetrixmult.controler;

public class ColisionPastilla implements Regla {

	@Override
	public void aplicar(Partida partida) {
		for (Jugador jugador : partida.getJugadores()) {
			Frasco frasco = jugador.getFrasco();
			Pastilla pastillaMovimiento = frasco.getPastillaEnMovimiento();
			for (Pastilla pastilla : frasco.getPastillas()) {
				if (pastillaMovimiento.getPosicion().equals(pastilla.getPosicion())) {
					// Hubo colision
					if (pastillaMovimiento.getPosicionAnterior().getPosicionX() - 1 == pastillaMovimiento.getPosicion()
							.getPosicionX()) {
						// Movio a izquierda
						pastillaMovimiento.regresarPosicion();
					} else if (pastillaMovimiento.getPosicionAnterior().getPosicionX() + 1 == pastillaMovimiento
							.getPosicion().getPosicionX()) {
						// Movio a derecha
						pastillaMovimiento.regresarPosicion();
					} else if (pastillaMovimiento.getPosicionAnterior().getPosicionY() - 1 == pastillaMovimiento
							.getPosicion().getPosicionY()) {
						// Movio abajo
						pastillaMovimiento.regresarPosicion();
						pastillaMovimiento.detenerPastilla();
					}
				}
			}
		}

	}

}
