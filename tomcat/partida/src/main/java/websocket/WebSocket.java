package websocket;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import modelo.Jugador.Jugador.EfectoDisparo;
import modelo.Partida.Partida;
import util.Posicion;


@ServerEndpoint("/ws")
public class WebSocket {

    private Partida partida = new Partida();
    
    private void informaTurno(Basic remote) throws IOException {
        remote.sendText(Json.createObjectBuilder().add("turno", partida.isTurnoJugador()).build().toString());
    }

    private void informaTableros(Basic remote) throws IOException {
        remote.sendText(partida.getTableroIA());
        remote.sendText(partida.getTableroHumano());
    }

    private void informaEfectoDisparo(Basic remote, EfectoDisparo efectoDisparo, String agente) throws IOException {
        String efecto = null;
        switch (efectoDisparo) {
            case AGUA:
                efecto = "Agua";
                break;
            case TOCADO:
                efecto = "Tocado";
                break;
            case HUNDIDO:
                efecto = "Barco hundido";
                break;
            case FLOTA_HUNDIDA:
                efecto = "Flota hundida";
                break;
            case AGUA_TOCADA:
            case BARCO_TOCADO:
                agente = "error";
                efecto = "Ya has disparado a esa casilla";
                break;
        }
        remote.sendText(Json.createObjectBuilder().add(agente, efecto).build().toString());
        
        if (agente != "error" ) informaTableros(remote);
    }

    private void dispara(Session session, JsonObject disparo) throws IOException, InterruptedException {

        Basic remote = session.getBasicRemote();
        Posicion posicion = Posicion.getPosicion(disparo.getInt("x"), disparo.getInt("y"));

        if (posicion == null) {
            remote.sendText(Json.createObjectBuilder().add("error", "Posición inválida").build().toString());
            return;
        }

        EfectoDisparo efectoDisparo = partida.turnoJugador(posicion);
        informaEfectoDisparo(remote, efectoDisparo, "jugador");
        if (efectoDisparo == EfectoDisparo.FLOTA_HUNDIDA) {
            remote.sendText(
                Json.createObjectBuilder().add("ganador", true).build().toString());
            session.close();
            return;
        }

        if ( !partida.isTurnoJugador() ) {
            informaTurno(remote);
            do {
                Thread.sleep(1000);
                efectoDisparo = partida.turnoIA();
                informaEfectoDisparo(remote, efectoDisparo, "IA");
                if (efectoDisparo == EfectoDisparo.FLOTA_HUNDIDA) {
                    remote.sendText(
                        Json.createObjectBuilder().add("ganador", false).build().toString());
                    session.close();
                    return;
                }
            } while ( !partida.isTurnoJugador() );
        };
       informaTurno(remote);        
    }

    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException {

        Basic remote = session.getBasicRemote();

        informaTurno(remote);
        informaTableros(remote);

        if ( !partida.isTurnoJugador() ) {
            informaTurno(remote);
            do {
                Thread.sleep(1000);
                EfectoDisparo efectoDisparo = partida.turnoIA();
                informaEfectoDisparo(remote, efectoDisparo, "IA");
            } while ( !partida.isTurnoJugador());
        };
        informaTurno(remote);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException, InterruptedException {

        JsonObject jsonMsg = Json.createReader(new StringReader(message)).readObject();

        if (jsonMsg.containsKey("disparo")) dispara(session, jsonMsg.getJsonObject("disparo"));
        if (jsonMsg.containsKey("situacion")) {
            informaTurno(session.getBasicRemote());
            informaTableros(session.getBasicRemote());
        };

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}
