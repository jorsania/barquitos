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

import java.util.List;


public class BarcoBuilder {

    private int tHoriz = 0;
    private int tVert = 0;
    private Posicion posicion = null;
    private boolean girar = false;

    public BarcoBuilder() {
    }
    
    public BarcoBuilder(int tHoriz, int tVert) {
        this.tHoriz = tHoriz;
        this.tVert = tVert;
    }
    
    public BarcoBuilder(Barco barco) {
        tHoriz = barco.getTHoriz();
        tVert = barco.getTVert();
        posicion = barco.getPosicion();
    }
    
    public BarcoBuilder(List<Integer> tamaño) {
        tHoriz = tamaño.get(0);
        tVert = tamaño.get(1);
    }

    public BarcoBuilder tHoriz(int tHoriz) {
        this.tHoriz = tHoriz;
        return this;
    }

    public BarcoBuilder tVert(int tVert) {
        this.tVert = tVert;
        return this;
    }

    public BarcoBuilder posicion(Posicion posicion) {
        this.posicion = posicion;
        return this;
    }

    public BarcoBuilder girar() {
        this.girar = true;
        return this;
    }

    public Barco build() {
        return new Barco(tHoriz, tVert, posicion, girar);
    }
    
}
