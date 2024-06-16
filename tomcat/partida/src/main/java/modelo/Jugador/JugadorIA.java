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

import util.*;
import modelo.Tablero.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Javier Ors Ania
 */
public class JugadorIA extends JugadorHumano {

    public enum NivelIA {
        Bajo, Medio, Alto
    }

    private final NivelIA nivelIA;
    private Posicion posBarcoEncontrado;

    public JugadorIA(Flota flota, boolean notificacionHundido, boolean esquinasColindantes, NivelIA nivelIA) {
        super("Ordenador", flota, notificacionHundido, esquinasColindantes);
        this.nivelIA = nivelIA;
    }

    @Override
    public boolean esHumano() {
        return false;
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

    @Override
    public void informeDisparo(Posicion disparo, EfectoDisparo efecto) {
        super.informeDisparo(disparo, efecto);
        switch (efecto) {
            case TOCADO:
                if (nivelIA != NivelIA.Bajo) {
                    posBarcoEncontrado = disparo;
                }
            default:
                break;
        }
    }

    public Posicion calculaDisparo() {
        Posicion disparo = null;
        while (disparo == null) {
            if (flota.isEmpty()) {
                throw new RuntimeException("La IA cree haber ganado la partida.");
            }
            if (posBarcoEncontrado == null) {
                if (nivelIA == NivelIA.Alto) {
                    disparo = calculaDisparoProbabilistico();
                } else {
                    disparo = disparoAleatorio();
                }
            } else {
                disparo = calculaDisparoBarcoEncontrado();
            }
        }
        return disparo;
    }

    private Posicion disparoAleatorio() {
        Posicion disparo;
        int x = Aleatorio.genera.nextInt(Tablero.HORIZONTAL) + 1;
        int y = Aleatorio.genera.nextInt(Tablero.VERTICAL) + 1;
        disparo = new Posicion(x, y);
        if (tableroOponente.casillaMarcada(disparo)) {
            disparo = null;
        }
        return disparo;
    }

    private Posicion calculaDisparoProbabilistico() {
        List<Barco> barcosCandidatos;
        barcosCandidatos = barcosCandidatos();
        barcosCandidatos = tableroOponente.barcosCompatibles(barcosCandidatos);

        CuentaPosiciones disparosCandidatos = new CuentaPosiciones();
        barcosCandidatos.stream()
                .forEach(barco
                        -> tableroOponente.posicionesIntactas(barco).stream()
                        .forEach(posicion
                                -> disparosCandidatos.pon(posicion)));
        Posicion disparo = null;
        if (!disparosCandidatos.isEmpty()) {
            int maxProb = Collections.max(disparosCandidatos.entrySet(),
                    Map.Entry.comparingByValue()).getValue();
            List<Posicion> mejoresCandidatos
                    = disparosCandidatos.entrySet().stream()
                            .filter(p -> p.getValue() == maxProb)
                            .map(d -> d.getKey())
                            .collect(Collectors.toList());
            int index = Aleatorio.genera.nextInt(mejoresCandidatos.size());
            disparo = mejoresCandidatos.get(index);
        } else {
            throw new RuntimeException(""
                    + "A la IA no le acaban de cuadrar las cuentas. "
                    + "¿Puede que hayas estado haciendo trampas?");
        }
        return disparo;
    }

    private Posicion calculaDisparoBarcoEncontrado() {
        List<Barco> barcosCandidatos;
        barcosCandidatos = barcosCandidatos();
        barcosCandidatos = tableroOponente
                .barcosCompatibles(posBarcoEncontrado, barcosCandidatos);

        if (barcosCandidatos.isEmpty()) {
            throw new RuntimeException(""
                    + "A la IA no le acaban de cuadrar las cuentas. "
                    + "¿Puede que hayas estado haciendo trampas?");
        }

        CuentaPosiciones disparosCandidatos = new CuentaPosiciones();
        barcosCandidatos.stream()
                .forEach(barco
                        -> tableroOponente.posicionesIntactas(barco).stream()
                        .forEach(posicion
                                -> disparosCandidatos.pon(posicion)));

        Posicion nuevoDisparo = null;
        if (!disparosCandidatos.isEmpty()) {

            int maxProb = Collections.max(disparosCandidatos.entrySet(),
                    Map.Entry.comparingByValue()).getValue();

            nuevoDisparo = disparosCandidatos.entrySet().stream()
                    .filter(t -> t.getValue() == maxProb)
                    .min((t1, t2) -> {
                        return t1.getKey().distancia(posBarcoEncontrado)
                                - t2.getKey().distancia(posBarcoEncontrado);
                    })
                    .get().getKey();
        } else {
            Barco barcoHundido = barcosCandidatos.get(0);
            if (!flota.containsKey(barcoHundido)) {
                barcoHundido = new BarcoBuilder(barcoHundido).girar().build();
            }
            if (!flota.containsKey(barcoHundido)) {
                throw new RuntimeException(""
                        + "A la IA no le acaban de cuadrar las cuentas. "
                        + "¿Puede que hayas estado haciendo trampas?");
            }
            flota.quita(barcoHundido);
            tableroOponente.marcaBordesAgua(posBarcoEncontrado);
            posBarcoEncontrado = null;
        }
        return nuevoDisparo;
    }

    private List<Barco> barcosCandidatos() {
        ArrayList<Barco> barcosCandidatos = new ArrayList<>();
        for (Barco barco : flota.keySet()) {
            barcosCandidatos.add(barco);
            barcosCandidatos.add(new BarcoBuilder(barco).girar().build());
        }
        return barcosCandidatos;
    }
}
