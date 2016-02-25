package edu.foo.tetrixmult.controler;

import java.util.ArrayList;
import java.util.List;

public class Frasco {

	private static final Short TAMANO_X_DEFECTO = 8;
	private static final Short TAMANO_Y_DEFECTO = 12;

	private Short tamanoX;
	private Short tamanoY;
	private Boolean estaLleano;
	private List<Pastilla> pastillas;

	public Frasco() {
		tamanoX = TAMANO_X_DEFECTO;
		tamanoY = TAMANO_Y_DEFECTO;
		estaLleano = Boolean.FALSE;
		pastillas = new ArrayList<Pastilla>();
	}

	public Short getTamanoX() {
		return tamanoX;
	}

	public Short getTamanoY() {
		return tamanoY;
	}

	public Boolean getEstaLleno() {
		return estaLleano;
	}

	public void agregarPastilla(Pastilla pastilla) {
		if (pastilla.getPosicion().getPosicionX() > tamanoX || pastilla.getPosicion().getPosicionY() > tamanoY) {
			throw new IllegalArgumentException("La pastilla ingresada no esta dentro del frasco " + pastilla);
		}
		pastillas.add(pastilla);
	}

	public void eliminarPastilla(Pastilla pastilla) {
		pastillas.remove(pastilla);
	}

	public Pastilla getPastillaEnMovimiento() {
		for (Pastilla pastilla : pastillas) {
			if (pastilla.getEstaMoviendose()) {
				return pastilla;
			}
		}
		return null;
	}

	public List<Pastilla> getPastillas() {
		return pastillas;
	}

}
