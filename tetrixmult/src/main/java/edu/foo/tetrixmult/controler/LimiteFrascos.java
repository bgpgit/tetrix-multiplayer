package edu.foo.tetrixmult.controler;

public class LimiteFrascos implements Regla {

	@Override
	public void aplicar(Partida partida) {
		for (Jugador jugador : partida.getJugadores()) {
			Frasco frasco = jugador.getFrasco();
			Pastilla pastilla = frasco.getPastillaEnMovimiento();
			if (pastilla.getPosicion().getPosicionX() > frasco.getTamanoX()) {
				pastilla.regresarPosicion();
			}
			if (pastilla.getPosicion().getPosicionY() > frasco.getTamanoY()) {
				pastilla.regresarPosicion();
			}
			if (pastilla.getPosicion().getPosicionY() == 0) {
				pastilla.detenerPastilla();
			}
		}

	}

}
