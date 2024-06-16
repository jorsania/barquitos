package modelo.Partida;

import modelo.Jugador.Jugador.EfectoDisparo;
import modelo.Jugador.JugadorHumano;
import modelo.Jugador.JugadorIA;
import modelo.Jugador.JugadorIA.NivelIA;
import util.Aleatorio;
import util.BarcoBuilder;
import util.Flota;
import util.Posicion;

public class Partida {

    private JugadorHumano jugadorHumano;
    private JugadorIA jugadorIA;
    private boolean turnoJugador;

    public Partida() {
        Flota flota = new Flota();
        flota.pon(new BarcoBuilder(3,1).build());
        flota.pon(new BarcoBuilder(3,2).build());
        flota.pon(new BarcoBuilder(4,2).build());
        flota.pon(new BarcoBuilder(6,1).build());

        turnoJugador = Aleatorio.genera.nextBoolean(); 
        jugadorHumano = new JugadorHumano(null, flota, true, true);
        jugadorIA = new JugadorIA(flota, true, true, NivelIA.Alto);

        jugadorHumano.despliegueFlotaAleatorio();
        jugadorIA.despliegueFlotaAleatorio();
    }

    public boolean isTurnoJugador() {
        return turnoJugador;
    }

    public EfectoDisparo turnoJugador(Posicion posicion) {
        EfectoDisparo efectoDisparo = jugadorIA.recibeDisparo(posicion);        
        jugadorHumano.informeDisparo(posicion, efectoDisparo);
        
        if (efectoDisparo == EfectoDisparo.AGUA )
            turnoJugador = false;
        
        return efectoDisparo;
    }

    public EfectoDisparo turnoIA() {
        
        Posicion disparo = jugadorIA.calculaDisparo();
        EfectoDisparo efectoDisparo = jugadorHumano.recibeDisparo(disparo);
        jugadorIA.informeDisparo(disparo, efectoDisparo);
        
        if (efectoDisparo == EfectoDisparo.AGUA) turnoJugador = true;

        return efectoDisparo;
    }

    public String getTableroHumano() {
        return jugadorHumano.getTableroPropio().toJson().toString();
    }

    public String getTableroIA() {
        return jugadorHumano.getTableroOponente().toJson().toString();
    }

}
