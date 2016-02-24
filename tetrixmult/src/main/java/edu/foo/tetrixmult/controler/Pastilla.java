package edu.foo.tetrixmult.controler;

public class Pastilla {

	private static Short ultimoIdGenerado = new Short((short) 0);
	private short id;
	private Posicion posicion;
	private Posicion posicionAnterior;
	private ColorPastilla color;
	private Boolean estaMoviendose;

	public Pastilla(ColorPastilla color, Posicion posicion) {
		id = obtenerIdPastilla();
		estaMoviendose = Boolean.TRUE;
		this.posicion = posicion;
		this.color = color;
	}

	private static synchronized short obtenerIdPastilla() {
		ultimoIdGenerado++;
		return ultimoIdGenerado;
	}

	public short getId() {
		return id;
	}

	public ColorPastilla getColor() {
		return color;
	}

	public Boolean getEstaMoviendose() {
		return estaMoviendose;
	}

	public Posicion getPosicion() {
		return posicion;
	}

	public void detenerPastilla() {
		estaMoviendose = Boolean.FALSE;
	}

	public Posicion getPosicionAnterior() {
		return posicionAnterior;
	}

	public void moverAbajo() {
		if (estaMoviendose) {
			posicionAnterior = posicion.clone();
			posicion.reducirPosicionY();
		}
	}

	public void moverDerecha() {
		if (estaMoviendose) {
			posicionAnterior = posicion.clone();
			posicion.aumentarPosicionX();
		}
	}

	public void moverIzquierda() {
		if (estaMoviendose) {
			posicionAnterior = posicion.clone();
			posicion.reducirPosicionX();
		}
	}

	public void regresarPosicion() {
		this.posicion = this.posicionAnterior.clone();
	}

	@Override
	public String toString() {
		return "Pastilla : [id=" + id + "] " + posicion;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pastilla)) {
			return false;
		}
		Pastilla otraPastilla = (Pastilla) obj;
		return this.id == otraPastilla.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

}
