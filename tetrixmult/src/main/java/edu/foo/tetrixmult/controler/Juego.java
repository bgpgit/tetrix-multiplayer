package edu.foo.tetrixmult.controler;

import java.util.ArrayList;
import java.util.List;

public class Juego {

	private static final Integer CANTIDAD_MAXIMA_PARTIDAS = new Integer(3);
	private List<Partida> listaPartidasAnteriores;
	private Partida partidaActual;
	private List<Regla> reglas;

	public Juego() {
		this.listaPartidasAnteriores = new ArrayList<Partida>();
		this.partidaActual = new Partida();
		this.reglas = generarListaReglas();
	}

	public void mainLoop() {
		for (Regla regla : reglas) {
			regla.aplicar(partidaActual);
		}
		if (partidaActual.getEstaTerminada()) {
			listaPartidasAnteriores.add(partidaActual);
			if (listaPartidasAnteriores.size() == CANTIDAD_MAXIMA_PARTIDAS) {
				return;
			} else {
				partidaActual = new Partida();
			}
		}
	}

	private static List<Regla> generarListaReglas() {
		List<Regla> reglas = new ArrayList<Regla>();
		reglas.add(new Gravedad());
		reglas.add(new GeneracionPastilla());
		reglas.add(new Eliminacion());
		reglas.add(new FinJuego());
		return reglas;
	}
}
