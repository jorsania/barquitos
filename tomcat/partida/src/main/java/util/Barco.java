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
package util;

import java.io.Serializable;

/**
 *
 * @author Javier Ors Ania
 */
public class Barco implements Comparable<Barco>, Serializable {

    private final int tHoriz;
    private final int tVert;
    private final Posicion posicion;

    Barco(int tHoriz, int tVert, Posicion posicion, boolean girar) {
        this.tHoriz = girar ? tVert : tHoriz;
        this.tVert = girar ? tHoriz : tVert;
        this.posicion = posicion;
    }

    public int getTHoriz() {
        return tHoriz;
    }

    public int getTVert() {
        return tVert;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public boolean posicionado() {
        return posicion != null;
    }

    @Override
    public String toString() {
        String string = "(" + tHoriz + "x" + tVert + ")";
        return string;
    }

    @Override
    public int compareTo(Barco o) {
        int comp;
        if (this.tHoriz != o.tHoriz) {
            comp = this.tHoriz - o.tHoriz;
        } else {
            comp = this.tVert - o.tVert;
        }
        return comp;
    }
}
