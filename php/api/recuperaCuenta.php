<?php
include('lib/util.php');
include('lib/jugador.php');

$cuenta = file_get_contents("php://input");

$respuesta = new Respuesta();

// Buscamos el jugador en la base de datos
$jugador = Jugador::busca($cuenta);

// Envía un enlace de recuperación de contraseña a una cuenta si existe
if (!empty($jugador)) {
    
    // Creamos una solicitud de recuperación para el jugador
    $token = $jugador->insertaSolcitudRecuperacion();

    if ($token) enviaMail('recuperacion', $jugador->alias, $jugador->email, $token->idToken);
}

// Pase lo que pase reportamos éxito
$respuesta->exito = true;
$respuesta->mensaje = "Si la cuenta está registrada, se ha intentado enviar el enlace de recuperación a su dirección de correo asociada";
die(json_encode($respuesta));
