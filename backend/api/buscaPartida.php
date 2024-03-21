<?php
include('lib/util.php');

$respuesta = new Respuesta();

$idJugador = iniciaSesion();
if (!$idJugador) {
    $respuesta->exito = false;
    $respuesta->mensaje = "Esta acción require autenticación previa";
    die(json_encode($respuesta));
}

# Partidas de dos jugadores en espera de segundo jugador
$keepalive = KEEPALIVE_ESPERA_JUGADOR;
$sql =
    "SELECT 
        idPartida, 
        jugador.alias jugador,  
        fechaCrea,
        fechaMod
    FROM
        partida JOIN jugador
    ON 
        partida.idJugador1 = jugador.idJugador
    WHERE 
        partida.nJugadores = 2
        AND partida.idJugador2 IS NULL
        AND idJugador <> $idJugador
        #AND
        #CURRENT_TIMESTAMP < DATE_ADD(fechaMod ,INTERVAL $keepalive)
    ORDER BY 
        fechaCrea;";


$bd = BaseDeDatos::conecta();
$partidas = $bd->ejecutaConsulta($sql)->fetchAll(PDO::FETCH_ASSOC);
exit(json_encode($partidas));
