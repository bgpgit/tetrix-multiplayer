package edu.foo.tetrixmult.controler;

import java.util.List;

public class Partida {

	private List<Jugador> jugadores;

	public Partida(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

}
