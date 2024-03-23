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

// Obtenemos el id de la partida a la que queremos unirnos
$idPartida = json_decode(file_get_contents("php://input"), true);

if (empty($idPartida)) {
    $respuesta->exito = false;
    $respuesta->mensaje = "Hay que proporcionar el id de partida a la que unirse";
    die(json_encode($respuesta));
}

$sql = "SELECT * FROM partida WHERE idPartida = ?";
$bd = BaseDeDatos::conecta();
$consulta = $bd->prepara($sql);
$bd->ejecuta($consulta, [$idPartida]);
$partida = $consulta->fetchObject('Partida');

if(!$partida) {
    $respuesta->exito = false;
    $respuesta->mensaje = "No se encuentra la partida";
    die(json_encode($respuesta));
}

if ($partida->nJugadores == 1) {
    $respuesta->exito = false;
    $respuesta->mensaje = "No puedes unirte a una partida de un jugador";
    die(json_encode($respuesta));
}

if ($partida->idJugador1 == $idJugador) {
    $respuesta->exito = false;
    $respuesta->mensaje = "No puedes unirte a tu propia partida";
    die(json_encode($respuesta));
}

if (!empty($partida->idJugador2)) {
    $respuesta->exito = false;
    $respuesta->mensaje = "La partida ya está completa";
    die(json_encode($respuesta));
}

$turno = array_rand([$partida->idJugador1,$idJugador]);
$sql = 
    "UPDATE 
        partida
    SET 
        idJugador2 = $idJugador,
        turno = $turno;";
$bd->ejecutaSentencia($sql);

$respuesta->exito = true;
$respuesta->mensaje = "Te has unido correctamente a la partida";
exit(json_encode($respuesta));