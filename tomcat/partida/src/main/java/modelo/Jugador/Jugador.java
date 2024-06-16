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
package modelo.Jugador;

import java.util.List;
import modelo.Tablero.TableroOponente;
import modelo.Tablero.TableroPropio;
import util.Barco;
import util.Flota;
import util.Posicion;

/**
 *
 * @author Javier Ors Ania
 */
public interface Jugador {
	
    public enum EfectoDisparo {
        AGUA, TOCADO, HUNDIDO, AGUA_TOCADA, BARCO_TOCADO, FLOTA_HUNDIDA
    }

    public String getNombre();
    
    public Flota getFlota();

    public TableroOponente getTableroOponente();

    public TableroPropio getTableroPropio();
    
    public boolean esHumano();
    
    public EfectoDisparo recibeDisparo(Posicion pos);

    public void informeDisparo(Posicion disparo, EfectoDisparo efecto);

    public void colocaBarcos(List<Barco> barcos);

    public boolean despliegueFlotaAleatorio();
}
