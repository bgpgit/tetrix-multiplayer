package edu.foo.tetrixmult.controler;

import java.util.ArrayList;
import java.util.List;

public class FinJuego implements Regla {

	@Override
	public void aplicar(Partida partida) {
		List<Jugador> perdedores = new ArrayList<Jugador>();
		for (Jugador jugador : partida.getJugadores()) {
			if (jugador.getFrasco().getEstaLleno()) {
				perdedores.add(jugador);
			}
		}
		if (perdedores.size() == partida.getJugadores().size()) {
			partida.terminarPartidaEmpate();
		} else if (perdedores.size() == (partida.getJugadores().size() - 1)) {
			for (Jugador jugador : partida.getJugadores()) {
				if (!perdedores.contains(jugador)) {
					partida.terminarPartida(jugador);
				}
			}
		}
	}

}
