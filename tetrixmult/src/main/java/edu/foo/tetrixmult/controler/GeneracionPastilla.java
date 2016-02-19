package edu.foo.tetrixmult.controler;

public class GeneracionPastilla implements Regla {

	@Override
	public void aplicar(Partida partida) {
		for (Jugador jugador : partida.getJugadores()) {
			Frasco frasco = jugador.getFrasco();
			if (frasco.getPastillaEnMovimiento() == null) {
				frasco.agregarPastilla(new Pastilla((short) (frasco.getTamanoX() / 2), frasco.getTamanoY()));
			}
		}

	}

}
