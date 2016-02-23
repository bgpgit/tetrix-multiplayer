package edu.foo.tetrixmult.controler;

public class LimiteFrascos implements Regla {

	@Override
	public void aplicar(Partida partida) {
		for (Jugador jugador : partida.getJugadores()) {
			Frasco frasco = jugador.getFrasco();
			Pastilla pastilla = frasco.getPastillaEnMovimiento();
			while (pastilla.getPosicionX() > frasco.getTamanoX()) {
				pastilla.moverIzquierda();
			}
			while (pastilla.getPosicionY() > frasco.getTamanoY()) {
				pastilla.moverAbajo();
			}
		}

	}

}
