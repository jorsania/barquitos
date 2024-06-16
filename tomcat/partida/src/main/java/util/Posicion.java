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
import modelo.Tablero.Tablero;
import static modelo.Tablero.Tablero.HORIZONTAL;
import static modelo.Tablero.Tablero.VERTICAL;

/**
 *
 * @author Javier Ors Ania
 */
public class Posicion implements Serializable {

    private int x;
    private int y;

    public Posicion(int x, int y) {
        if (!(esValidaX(x) && esValidaY(y))) {
            throw new RuntimeException("Posición inválida.");
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distancia(Posicion pos) {
        return Math.abs(this.x - pos.getX()) + Math.abs(this.y - pos.getY());
    }

    static public boolean esValidaX(int x) {
        return (x > 0 && x <= HORIZONTAL);
    }

    static public boolean esValidaY(int y) {
        return (y > 0 && y <= VERTICAL);
    }

    static public Posicion getPosicion(int x, int y) {
        Posicion posicion = null;
        if (esValidaX(x) && esValidaY(y)) posicion = new Posicion (x, y);
        return posicion;
    }

    static public Posicion getPosicion(String linea) {
        Posicion posicion = null;

        if (linea.matches("^[a-jA-J]([0-9]|10)$")) {
            int x, y;
            x = linea.toUpperCase().toCharArray()[0] - 64;
            y = Integer.parseInt(linea.substring(1));
            posicion = new Posicion(x, y);
        } else if (linea.matches("^([0-9]|10)[a-jA-J]$")) {
            int x, y;
            x = linea.toUpperCase().toCharArray()[linea.length() - 1] - 64;
            y = Integer.parseInt(linea.substring(0, linea.length() - 1));
            posicion = new Posicion(x, y);
        }
        
        return posicion;
    }

    @Override
    public boolean equals(Object pos) {
        boolean equal;
        if (pos instanceof Posicion) {
            equal = this.x == ((Posicion) pos).getX()
                    && this.y == ((Posicion) pos).getY();
        } else {
            equal = false;
        }
        return equal;
    }

    @Override
    public String toString() {
        return "" + (char) (x + 64) + y;
    }

    @Override
    public int hashCode() {
        return (this.x - 1) + Tablero.HORIZONTAL * (this.y - 1);
    }
}
