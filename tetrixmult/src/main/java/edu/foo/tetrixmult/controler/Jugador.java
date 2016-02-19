package edu.foo.tetrixmult.controler;

/**
 * @author Neko
 *
 */
public class Jugador {

	private String nombre;
	private Frasco frasco;

	public Jugador(String nombre) {
		this.frasco = new Frasco();
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return nombre;
	}

}
