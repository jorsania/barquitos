<?php
include('lib/util.php');
include('lib/partida.php');

$respuesta = new Respuesta();

$idJugador = iniciaSesion();
if (!$idJugador) {
    $respuesta->exito = false;
    $respuesta->mensaje = "Esta acción require autenticación previa";
    die(json_encode($respuesta));
}

// Obtenemos el número de jugadores
$nJugadores = json_decode(file_get_contents("php://input"), true);

if (empty($nJugadores)) {
    $respuesta->exito = false;
    $respuesta->mensaje = "Hay que proporcionar el número de Jugadores";
    die(json_encode($respuesta));
}

$partida = Partida::construye($idJugador, $nJugadores, $error);
if (!$partida) {
    $respuesta = new Respuesta;
    $respuesta->exito = false;
    $respuesta->mensaje = $error;
    die(json_encode($respuesta));
}

$respuesta->exito = true;
$respuesta->mensaje = "Se ha creado una nueva partida";
$respuesta->contenido = $partida;
exit(json_encode($respuesta));
