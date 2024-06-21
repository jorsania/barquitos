<?php
include('lib/util.php');

$respuesta = new Respuesta();

$idJugador = iniciaSesion();
if (!$idJugador) {
    $respuesta->exito = false;
    $respuesta->mensaje = "Esta acción require autenticación previa";
    die(json_encode($respuesta));
}

# Partidas en curso de 1 o 2 jugadores en las que participo
$sql =
    "SELECT 
        idPartida,
        nJugadores, 
        jugador1.alias jugador1, 
        jugador2.alias jugador2,
        (partida.turno = $idJugador) turno,
        fechaCrea,
        fechaMod
     FROM
        partida 
        INNER JOIN jugador as jugador1 ON partida.idJugador1 = jugador1.idJugador
        LEFT  JOIN jugador as jugador2 ON partida.idJugador2 = jugador2.idJugador
     WHERE 
        partida.idJugador2 = $idJugador OR
        (partida.idJugador1 = $idJugador AND partida.nJugadores = 1) OR
        (partida.idJugador1 = $idJugador AND partida.idJugador2 IS NOT NULL)
    ";


$bd = BaseDeDatos::conecta();
$partidas = $bd->ejecutaConsulta($sql)->fetchAll(PDO::FETCH_ASSOC);
exit(json_encode($partidas));
