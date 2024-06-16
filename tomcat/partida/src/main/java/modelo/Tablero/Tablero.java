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

import java.io.Serializable;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import util.Posicion;
import util.Barco;

/**
 *
 * @author Javier Ors Ania
 */
public class Tablero implements Serializable {

    public static final int HORIZONTAL = 10;
    public static final int VERTICAL = 10;

    protected enum EstadoCasilla {
        AGUA, BARCO, INCOGNITA, AGUA_TOCADA, BARCO_TOCADO, DESTACADO
    }

    protected final EstadoCasilla[][] tablero;
    protected final boolean esquinasColindantes;
    private final HashMap<EstadoCasilla, String> estilo;

    protected Tablero(HashMap<EstadoCasilla, String> estilo,
            boolean esquinasColindantes) {
        this.estilo = estilo;
        this.tablero = new EstadoCasilla[VERTICAL + 2][HORIZONTAL + 2];
        this.esquinasColindantes = esquinasColindantes;
    }

    public EstadoCasilla getCasilla(Posicion posicion) {
        int x = posicion.getX();
        int y = posicion.getY();
        return tablero[y][x];
    }

    protected void setCasilla(Posicion posicion, EstadoCasilla estado) {
        int x = posicion.getX();
        int y = posicion.getY();
        tablero[y][x] = estado;

    }

    protected void inicializa(EstadoCasilla estado) {
        for (int i = 0; i < VERTICAL + 2; i++) {
            for (int j = 0; j < HORIZONTAL + 2; j++) {
                tablero[i][j] = estado;
            }
        }
    }

    protected boolean limitesValidos(Barco barco) {

        int x = barco.getPosicion().getX();
        int horiz = barco.getTHoriz();
        int y = barco.getPosicion().getY();
        int vert = barco.getTVert();

        return !(x + horiz - 1 > HORIZONTAL | y + vert - 1 > VERTICAL);
    }

    protected boolean cascoNoContiene(Barco barco, EstadoCasilla estado) {

        int xBarco = barco.getPosicion().getX();
        int yBarco = barco.getPosicion().getY();
        int horiz = barco.getTHoriz();
        int vert = barco.getTVert();

        for (int y = 0; y < vert; y++) {
            for (int x = 0; x < horiz; x++) {
                if (tablero[yBarco + y][xBarco + x] == estado) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean perimetroDespejado(Barco barco) {

        int x = barco.getPosicion().getX();
        int horiz = barco.getTHoriz();
        int y = barco.getPosicion().getY();
        int vert = barco.getTVert();

        for (int i = (esquinasColindantes ? 0 : -1);
                i < (esquinasColindantes ? vert : vert + 1);
                i++) {
            if (tablero[y + i][x - 1] == EstadoCasilla.BARCO
                    || tablero[y + i][x + horiz] == EstadoCasilla.BARCO) {
                return false;
            }
        }

        for (int j = (esquinasColindantes ? 0 : -1);
                j < (esquinasColindantes ? horiz : horiz + 1); j++) {
            if (tablero[y - 1][x + j] == EstadoCasilla.BARCO
                    || tablero[y + vert][x + j] == EstadoCasilla.BARCO) {
                return false;
            }
        }
        return true;
    }

    protected boolean compatibleCon(Barco barco) {
        boolean esCompatible = false;
        if (limitesValidos(barco)) {
            if (cascoNoContiene(barco, EstadoCasilla.AGUA)) {
                if (perimetroDespejado(barco)) {
                    esCompatible = true;
                }
            }
        }
        return esCompatible;
    }

    public JsonObject toJson() {

        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        for (int y = 1; y <= Tablero.VERTICAL; y++) {
            JsonArrayBuilder fila = Json.createArrayBuilder();
            for (int x = 1; x <= Tablero.HORIZONTAL; x++) {
                fila.add(estilo.get(tablero[y][x]));
            }
            jsonArray.add(fila);
        }

        JsonObject jsonObject = 
            Json.createObjectBuilder().add(
                this.getClass().getSimpleName(),
                jsonArray.build()
        ).build();

        return jsonObject; 
    }

    @Override
    public String toString() {
        String text = "";
        text += "   ";
        for (int j = 1; j <= Tablero.HORIZONTAL; j++) {
            text += String.format(" %s ", (char) (j + 64));
        }
        text += "\n   ";
        for (int j = 1; j <= Tablero.HORIZONTAL; j++) {
            text += "---";
        }
        text += "\n";
        for (int y = 1; y <= Tablero.VERTICAL; y++) {
            text += String.format("%2d%s", y, "|");
            for (int x = 1; x <= Tablero.HORIZONTAL; x++) {
                text += String.format(" %s ", estilo.get(tablero[y][x]));
            }
            text += "\n";
        }
        return text;
    }
}
