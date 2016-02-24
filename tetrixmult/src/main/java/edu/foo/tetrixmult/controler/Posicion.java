package edu.foo.tetrixmult.controler;

public class Posicion implements Cloneable {

	private short posicionX;
	private short posicionY;

	public Posicion(short posicionX, short posicionY) {
		if (posicionX < 0 || posicionY < 0) {
			throw new IllegalArgumentException(
					"Posicion invalida, [posicionX=" + posicionX + "] [posicionY=" + posicionY + "]");
		}
		this.posicionX = posicionX;
		this.posicionY = posicionY;
	}

	public Short getPosicionX() {
		return posicionX;
	}

	public void setPosicionX(Short posicionX) {
		this.posicionX = posicionX;
	}

	public Short getPosicionY() {
		return posicionY;
	}

	public void setPosicionY(Short posicionY) {
		this.posicionY = posicionY;
	}

	public void reducirPosicionY() {
		if (posicionY != 0) {
			posicionY--;
		}
	}

	public void aumentarPosicionX() {
		if (posicionX != 0) {
			posicionX--;
		}
	}

	public void reducirPosicionX() {
		posicionX++;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Posicion)) {
			return false;
		}
		Posicion otraPosicion = (Posicion) obj;
		return this.posicionX == otraPosicion.posicionX && this.posicionY == otraPosicion.posicionY;
	}

	@Override
	public int hashCode() {
		return this.posicionX + this.posicionY;
	}

	@Override
	public String toString() {
		return "[posicionX=" + posicionX + "] [posicionY=" + posicionY + "]";
	}

	@Override
	public Posicion clone() {
		try {
			return (Posicion) super.clone();
		} catch (CloneNotSupportedException e) {
			// Nada que hacer
			return null;
		}
	}

}
