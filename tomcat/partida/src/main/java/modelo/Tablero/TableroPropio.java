/*
 * Copyright (C) 2021 Javier Ors Ania
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package modelo.Tablero;

import modelo.Jugador.Jugador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import util.Aleatorio;
import util.Barco;
import util.BarcoBuilder;
import util.Flota;
import util.Posicion;

/**
 *
 * @author Javier Ors Ania
 */
public class TableroPropio extends Tablero {

    public TableroPropio(boolean esquinasColindantes) {
        super(construyeEstilo(), esquinasColindantes);
        inicializa();
    }

    private static HashMap<EstadoCasilla, String> construyeEstilo() {
        HashMap<EstadoCasilla, String> estilo = new HashMap<>();
        estilo.put(EstadoCasilla.BARCO, "directions_boat");
        estilo.put(EstadoCasilla.AGUA, "");
        estilo.put(EstadoCasilla.BARCO_TOCADO,"flare");
        estilo.put(EstadoCasilla.AGUA_TOCADA,"water");
        return estilo;
    }

    public final void inicializa() {
        super.inicializa(EstadoCasilla.AGUA);
    }

    public void marcarBarcoTocado(Posicion posicion) {
        setCasilla(posicion, EstadoCasilla.BARCO_TOCADO);
    }

    public void marcarAguaTocada(Posicion posicion) {
        setCasilla(posicion, EstadoCasilla.AGUA_TOCADA);
    }

    public boolean casillaTocado(Posicion posicion) {
        return getCasilla(posicion) == EstadoCasilla.BARCO_TOCADO;
    }

    public Jugador.EfectoDisparo recibeDisparo(Posicion posicion) {
        Jugador.EfectoDisparo resultado = null;
        EstadoCasilla casilla = getCasilla(posicion);
        switch (casilla) {
            case AGUA:
                resultado = Jugador.EfectoDisparo.AGUA;
                break;
            case BARCO:
                resultado = Jugador.EfectoDisparo.TOCADO;
                break;
            case BARCO_TOCADO:
                resultado = Jugador.EfectoDisparo.BARCO_TOCADO;
                break;
            case AGUA_TOCADA:
                resultado = Jugador.EfectoDisparo.AGUA_TOCADA;
                break;
            default:
                break;
        }
        return resultado;
    }

    public void marcar(Barco barco) {
        quitar(barco);
        colocar(barco, EstadoCasilla.DESTACADO);
    }

    public void desmarcar(Barco barco) {
        quitar(barco);
        colocar(barco);
    }

    public void colocar(Barco barco) {
        if (barco.posicionado() && posicionValida(barco)) {
            colocar(barco, EstadoCasilla.BARCO);
        } else {
            throw new RuntimeException("No se puede colocar en esa posición");
        }
    }

    public void quitar(Barco barco) {
        if (barco.posicionado() && compatibleCon(barco)) {
            colocar(barco, EstadoCasilla.AGUA);
        } else {
            throw new RuntimeException("No existe un barco en esa posición");
        }
    }

    private void colocar(Barco barco, EstadoCasilla estado) {
        int x = barco.getPosicion().getX();
        int y = barco.getPosicion().getY();
        int horiz = barco.getTHoriz();
        int vert = barco.getTVert();

        for (int i = 0; i < vert; i++) {
            for (int j = 0; j < horiz; j++) {
                tablero[y + i][x + j] = estado;
            }
        }
    }

    public boolean girable(Barco barco) {
        boolean girable = false;
        if (barco.posicionado()) {
            if (compatibleCon(barco)) {
                quitar(barco);
                Barco barcoGirado = new BarcoBuilder(barco).girar().build();
                if (posicionValida(barcoGirado)) {
                    girable = true;
                }
                colocar(barco);
                marcar(barco);
            }
        } else {
            girable = true;
        }
        return girable;
    }

    public boolean desplazamientoPosible(Barco barco, int x, int y) {
        x = barco.getPosicion().getX() + x;
        y = barco.getPosicion().getY() + y;

        Posicion posicionNueva;
        if (Posicion.esValidaX(x) && Posicion.esValidaY(y)) {
            posicionNueva = new Posicion(x, y);
        } else {
            return false;
        }
        Barco barcoNuevo
                = new BarcoBuilder(barco).posicion(posicionNueva).build();

        boolean movimientoPosible = false;
        quitar(barco);
        if (posicionValida(barcoNuevo)) {
            movimientoPosible = true;
        }
        colocar(barco);
        marcar(barco);
        return movimientoPosible;
    }

    public boolean posicionValida(Barco barco) {
        boolean posicionValida = false;
        if (limitesValidos(barco)) {
            if (cascoNoContiene(barco, EstadoCasilla.BARCO)) {
                if (perimetroDespejado(barco)) {
                    posicionValida = true;
                }
            }
        }
        return posicionValida;
    }

    public boolean compruebaHundido(Barco barco) {
        boolean hundido = true;

        int xBarco = barco.getPosicion().getX();
        int yBarco = barco.getPosicion().getY();
        int horiz = barco.getTHoriz();
        int vert = barco.getTVert();

        for (int y = yBarco; y < yBarco + vert; y++) {
            for (int x = xBarco; x < xBarco + horiz; x++) {
                if (tablero[y][x] != EstadoCasilla.BARCO_TOCADO) {
                    hundido = false;
                }
            }
        }
        return hundido;
    }

    public List<Barco> despliegaFlotaAleatorio(Flota flota) {
        ArrayList<Barco> barcosColocados = null;
        for (int i = 0; i < 100; i++) {
            inicializa();
            barcosColocados = new ArrayList<>();
            for (Barco barco : flota.keySet()) {
                Barco colocado = null;
                for (int j = 0; j < flota.get(barco); j++) {
                    for (int k = 0; k < 100; k++) {
                        int x = Aleatorio.genera.nextInt(Tablero.HORIZONTAL) + 1;
                        int y = Aleatorio.genera.nextInt(Tablero.VERTICAL) + 1;
                        colocado = new BarcoBuilder(barco)
                                .posicion(new Posicion(x, y))
                                .build();
                        if (Aleatorio.genera.nextBoolean()) {
                            colocado = new BarcoBuilder(colocado).girar().build();
                        }
                        if (posicionValida(colocado)) {
                            break;
                        }
                    }
                    if (posicionValida(colocado)) {
                        colocar(colocado);
                        barcosColocados.add(colocado);
                    } else {
                        colocado = null;
                        break;
                    }
                }
                if (colocado == null) {
                    barcosColocados = null;
                    break;
                }
            }
            if (barcosColocados != null) {
                break;
            } else {
                inicializa();
            }
        }
        return barcosColocados;
    }
}
