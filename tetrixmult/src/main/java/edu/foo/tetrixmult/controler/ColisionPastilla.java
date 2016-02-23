package edu.foo.tetrixmult.controler;

public class ColisionPastilla implements Regla {

	@Override
	public void aplicar(Partida partida) {
		for (Jugador jugador : partida.getJugadores()) {
			Frasco frasco = jugador.getFrasco();
			for (Pastilla pastilla : frasco.getPastillas()) {
				// TODO: Continue
			}
		}

	}

}
