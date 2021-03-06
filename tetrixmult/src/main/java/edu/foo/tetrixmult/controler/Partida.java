package edu.foo.tetrixmult.controler;

import java.util.ArrayList;
import java.util.List;

public class Partida {

	private List<Jugador> jugadores;
	private Boolean estaTerminada;
	private Jugador ganador;

	public Partida() {
		this.jugadores = new ArrayList<Jugador>();
		this.estaTerminada = Boolean.FALSE;
	}

	public void agregarJugador(Jugador jugador) {
		jugadores.add(jugador);
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public Boolean getEstaTerminada() {
		return estaTerminada;
	}

	public Jugador getGanador() {
		return ganador;
	}

	public void terminarPartidaEmpate() {
		terminarPartida(null);
	}

	public void terminarPartida(Jugador ganador) {
		if (ganador != null && !jugadores.contains(ganador)) {
			throw new IllegalArgumentException("El jugandor " + ganador + " no es un jugador de esta partida");
		}
		this.ganador = ganador;
		estaTerminada = Boolean.TRUE;
	}

}
