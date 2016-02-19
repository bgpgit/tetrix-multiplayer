package edu.foo.tetrixmult.controler;

public class Jugador {

	private Frasco frasco;

	public Jugador() {
		this.frasco = new Frasco();
	}

	public void moverPastillaDerecha() {
		frasco.getPastillaEnMovimiento().moverDerecha();
	}

	public void moverPastillaIzquierda() {
		frasco.getPastillaEnMovimiento().moverIzquierda();
	}

	public Frasco getFrasco() {
		return frasco;
	}

}
