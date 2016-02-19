package edu.foo.tetrixmult.controler;

public class Gravedad implements Regla {

	@Override
	public void aplicar(Partida partida) {
		for (Jugador jugador : partida.getJugadores()) {
			jugador.getFrasco().getPastillaEnMovimiento().moverAbajo();
		}
	}

}
