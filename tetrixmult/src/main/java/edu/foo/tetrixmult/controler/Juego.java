package edu.foo.tetrixmult.controler;

import java.util.ArrayList;
import java.util.List;

public class Juego {

	private List<Partida> listaPartidasAnteriores;
	private Partida partidaActual;

	public Juego(List<Jugador> jugadores) {
		this.listaPartidasAnteriores = new ArrayList<Partida>();
		this.partidaActual = new Partida(jugadores);
	}

	public void mainLoop() {

	}
}
