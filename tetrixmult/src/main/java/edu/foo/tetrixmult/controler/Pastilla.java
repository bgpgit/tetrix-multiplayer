package edu.foo.tetrixmult.controler;

public class Pastilla {

	private static Short ultimoIdGenerado = new Short((short) 0);
	private Short id;
	private Short posicionX;
	private Short posicionY;
	private Boolean estaMoviendose;

	public Pastilla(Short posicionX, Short posicionY) {
		if (posicionX < 0 || posicionY < 0) {
			throw new IllegalArgumentException(
					"Posicion invalida, [posicionX=" + posicionX + "] [posicionY=" + posicionY + "]");
		}
		id = obtenerIdPastilla();
		estaMoviendose = Boolean.TRUE;
		this.posicionX = posicionX;
		this.posicionY = posicionY;
	}

	private static synchronized Short obtenerIdPastilla() {
		ultimoIdGenerado++;
		return ultimoIdGenerado;
	}

	public Short getId() {
		return id;
	}

	public Boolean getEstaMoviendose() {
		return estaMoviendose;
	}

	public Short getPosicionX() {
		return posicionX;
	}

	public Short getPosicionY() {
		return posicionY;
	}

	public void detenerPastilla() {
		estaMoviendose = Boolean.FALSE;
	}

	public void moverAbajo() {
		if (estaMoviendose && posicionY != 0) {
			posicionY--;
		}
	}

	public void moverDerecha() {
		if (estaMoviendose) {
			posicionX++;
		}
	}

	public void moverIzquierda() {
		if (estaMoviendose && posicionX != 0) {
			posicionX--;
		}
	}

	@Override
	public String toString() {
		return "Pastilla : [id=" + id + "] [posicionX=" + posicionX + "] [posicionY=" + posicionY + "]";
	}
}
