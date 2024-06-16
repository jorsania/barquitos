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

import java.io.Serializable;
import util.*;
import modelo.Tablero.*;
import java.util.List;

/**
 *
 * @author Javier Ors Ania
 */
public class JugadorHumano implements Jugador, Serializable {

    protected final Flota flota;
    protected List<Barco> barcosColocados;

    protected final TableroOponente tableroOponente;
    protected final TableroPropio tableroPropio;

    private final String nombre;
    private final boolean notificacionHundido;

    public JugadorHumano(String nombre, Flota flota,
            boolean notificacionHundido,
            boolean esquinasColindantes) {
        this.flota = new Flota(flota);
        tableroOponente = new TableroOponente(esquinasColindantes);
        tableroPropio = new TableroPropio(esquinasColindantes);
        this.nombre = nombre;
        this.notificacionHundido = notificacionHundido;
    }

    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override public Flota getFlota() {
        return new Flota(flota);
    }

    @Override
    public TableroOponente getTableroOponente() {
        return tableroOponente;
    }

    @Override
    public TableroPropio getTableroPropio() {
        return tableroPropio;
    }

    @Override
    public boolean esHumano() {
        return true;
    }

    @Override
    public void colocaBarcos(List<Barco> barcos) {
        this.barcosColocados = barcos;
        tableroPropio.inicializa();
        barcosColocados.stream().forEach(b -> tableroPropio.colocar(b));
    }

    @Override
    public void informeDisparo(Posicion disparo, EfectoDisparo efecto) {
        switch (efecto) {
            case AGUA:
                tableroOponente.marcarAgua(disparo);
                break;
            case TOCADO:
                tableroOponente.marcarTocado(disparo);
                break;
            case HUNDIDO:
                tableroOponente.marcarTocado(disparo);
                tableroOponente.marcaBordesAgua(disparo);
                break;
            case FLOTA_HUNDIDA:
                tableroOponente.marcarTocado(disparo);
                break;
            default:
                break;
        }
    }

    @Override
    public EfectoDisparo recibeDisparo(Posicion pos) {
        EfectoDisparo resultado;
        resultado = tableroPropio.recibeDisparo(pos);
        if (resultado == EfectoDisparo.TOCADO) {
            tableroPropio.marcarBarcoTocado(pos);
            if (compruebaHundido()) {
                if (notificacionHundido) {
                    resultado = EfectoDisparo.HUNDIDO;
                }
                if (barcosColocados.isEmpty()) {
                    resultado = EfectoDisparo.FLOTA_HUNDIDA;
                }
            }
        } else if (resultado == EfectoDisparo.AGUA) {
            tableroPropio.marcarAguaTocada(pos);
        }
        return resultado;
    }

    public boolean despliegueFlotaAleatorio() {
        boolean flotaDesplegada = false;
        List<Barco> barcosColocados;
        barcosColocados = tableroPropio.despliegaFlotaAleatorio(flota);
        if (barcosColocados != null) {
            this.barcosColocados = barcosColocados;
            flotaDesplegada = true;
        }
        return flotaDesplegada;
    }

    public void recibeDisparo(Posicion disparo, EfectoDisparo efecto) {
        switch (efecto) {
            case TOCADO:
            case HUNDIDO:
            case FLOTA_HUNDIDA:
                tableroPropio.marcarBarcoTocado(disparo);
                break;
            case AGUA:
                tableroPropio.marcarAguaTocada(disparo);
                break;
            default:
                break;
        }
    }

    boolean compruebaHundido() {
        boolean hundido = false;
        for (Barco barco : barcosColocados) {
            if (tableroPropio.compruebaHundido(barco)) {
                hundido = true;
                barcosColocados.remove(barco);
                break;
            }
        }
        return hundido;
    }
}
