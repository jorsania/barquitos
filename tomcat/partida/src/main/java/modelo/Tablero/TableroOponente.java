/*
 * Copyright (C) 2021 Javier Ors <jaorani@iesmordefuentes.com>
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import util.Barco;
import util.BarcoBuilder;
import util.Posicion;

/**
 *
 * @author Javier Ors <jaorani@iesmordefuentes.com>
 */
public class TableroOponente extends Tablero {

    public TableroOponente(boolean esquinasColindantes) {
        super(construyeEstilo(), esquinasColindantes);
        inicializa();
    }

    private static HashMap<EstadoCasilla, String> construyeEstilo() {
        HashMap<EstadoCasilla, String> estilo = new HashMap<>();
        estilo.put(EstadoCasilla.INCOGNITA, "");
        estilo.put(EstadoCasilla.BARCO, "directions_boat");
        estilo.put(EstadoCasilla.AGUA,"water");
        return estilo;
    }

    public final void inicializa() {
        super.inicializa(EstadoCasilla.INCOGNITA);
    }

    public void marcarTocado(Posicion posicion) {
        setCasilla(posicion, EstadoCasilla.BARCO);
    }

    public void marcarAgua(Posicion posicion) {
        setCasilla(posicion, EstadoCasilla.AGUA);
    }

    public boolean casillaBarco(Posicion posicion) {
        return getCasilla(posicion) == EstadoCasilla.BARCO;
    }

    public boolean casillaAgua(Posicion posicion) {
        return getCasilla(posicion) == EstadoCasilla.AGUA;
    }

    public boolean casillaMarcada(Posicion posicion) {
        return getCasilla(posicion) != EstadoCasilla.INCOGNITA;
    }

    public HashSet<Posicion> posicionesIntactas(Barco barco) {
        HashSet<Posicion> posicionesIntactas = new HashSet<>();
        int xBarco = barco.getPosicion().getX();
        int yBarco = barco.getPosicion().getY();
        int horiz = barco.getTHoriz();
        int vert = barco.getTVert();

        for (int y = yBarco; y < yBarco + vert; y++) {
            for (int x = xBarco; x < xBarco + horiz; x++) {
                if (!casillaMarcada(new Posicion(x, y))) {
                    posicionesIntactas.add(new Posicion(x, y));
                }
            }
        }
        return posicionesIntactas;
    }

    public List<Barco> barcosCompatibles(List<Barco> barcosCandidatos) {
        ArrayList<Barco> barcosCompatibles = new ArrayList<>();
        for (Barco barco : barcosCandidatos) {
            for (int x = 1; x <= HORIZONTAL; x++) {
                for (int y = 1; y <= VERTICAL; y++) {
                    Barco candidato = new BarcoBuilder(barco)
                            .posicion(new Posicion(x, y))
                            .build();
                    if (compatibleCon(candidato)) {
                        barcosCompatibles.add(candidato);
                    }
                }
            }
        }
        return barcosCompatibles;
    }

    public List<Barco> barcosCompatibles(Posicion disparo,
            List<Barco> barcosCandidatos) {

        ArrayList<Barco> barcosCompatibles = new ArrayList<>();
        int xDisparo = disparo.getX();
        int yDisparo = disparo.getY();

        for (Barco barco : barcosCandidatos) {
            int horiz = barco.getTHoriz();
            int vert = barco.getTVert();

            for (int x = (xDisparo - (horiz - 1)); x <= xDisparo; x++) {
                for (int y = (yDisparo - (vert - 1)); y <= yDisparo; y++) {
                    if (Posicion.esValidaX(x) && Posicion.esValidaY(y)) {
                        Barco candidato = new BarcoBuilder(barco)
                                .posicion(new Posicion(x, y))
                                .build();
                        if (compatibleCon(candidato)) {
                            barcosCompatibles.add(candidato);
                        }
                    }
                }
            }
        }
        return barcosCompatibles;
    }

    public void marcaBordesAgua(Posicion pos) {
        int x = pos.getX();
        int y = pos.getY();

        //Buscamos borde derecho
        while (tablero[y][x] == EstadoCasilla.BARCO) {
            x++;
        }
        // Buscamos el borde superior
        while (tablero[y][x - 1] == EstadoCasilla.BARCO) {
            tablero[y][x] = EstadoCasilla.AGUA;
            y--;
        }
        if (!esquinasColindantes) {
            tablero[y][x] = EstadoCasilla.AGUA;
        }
        x--;
        // Buscamos el borde izquierdo
        while (tablero[y + 1][x] == EstadoCasilla.BARCO) {
            tablero[y][x] = EstadoCasilla.AGUA;
            x--;
        }
        if (!esquinasColindantes) {
            tablero[y][x] = EstadoCasilla.AGUA;
        }
        y++;
        // Buscamos el borde inferior
        while (tablero[y][x + 1] == EstadoCasilla.BARCO) {
            tablero[y][x] = EstadoCasilla.AGUA;
            y++;
        }
        if (!esquinasColindantes) {
            tablero[y][x] = EstadoCasilla.AGUA;
        }
        x++;
        //Buscamos borde derecho
        while (tablero[y - 1][x] == EstadoCasilla.BARCO) {
            tablero[y][x] = EstadoCasilla.AGUA;
            x++;
        }
        if (!esquinasColindantes) {
            tablero[y][x] = EstadoCasilla.AGUA;
        }
        y--;
        // Buscamos el borde superior
        while (tablero[y][x - 1] == EstadoCasilla.BARCO) {
            tablero[y][x] = EstadoCasilla.AGUA;
            y--;
        }
    }
}
