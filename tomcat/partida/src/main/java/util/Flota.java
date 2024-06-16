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
package util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import modelo.Tablero.Tablero;

/**
 *
 * @author Javier Ors <jaorani@iesmordefuentes.com>
 */
public class Flota extends TreeMap<Barco, Integer> {

    public static final int MAX_BARCOS = Tablero.VERTICAL - 4;
    public static final int MIN_BARCOS = 1;

    public Flota() {
        super();
    }

    public Flota(Flota flota) {
        super(flota);
    }

    public int numBarcos() {
        return values().stream().reduce(0, Integer::sum);
    }

    public void pon(Barco barco) {
        pon(barco, 1);
    }

    public void pon(Barco barco, int unidades) {
        barco = normaliza(barco);
        if (containsKey(barco)) {
            put(barco, get(barco) + unidades);
        } else {
            if (unidades > 0) {
                put(barco, unidades);
            }
        }
    }

    public void quita(Barco barco) {
        barco = normaliza(barco);
        if (containsKey(barco)) {
            if (get(barco) > 1) {
                put(barco, get(barco) - 1);
            } else {
                remove(barco);
            }
        }
    }

    public void elimina(Barco barco) {
        barco = normaliza(barco);
        if (containsKey(barco)) {
            remove(barco);
        }
    }

    private Barco normaliza(Barco barco) {
        if (barco.getTVert() > barco.getTHoriz()) {
            barco = new BarcoBuilder(barco).girar().build();
        }
        return barco;
    }

    public List<Barco> asList() {
        List<Barco> barcos = new ArrayList<>();
        for (Barco barco : keySet()) {
            for (int i = 0; i < get(barco); i++) {
                barcos.add(barco);
            }
        }
        return barcos;
    }

    @Override
    public String toString() {
        String text = ""
                + "Barcos a desplegar:\n\n"
                + " Tamaño\tCantidad\n"
                + " ------\t--------\n";

        for (Barco barco : keySet()) {
            text += " " + barco + "\t   " + get(barco) + "\n";
        }
        return text;
    }
}
