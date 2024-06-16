<?php
include('lib/util.php');
include('lib/jugador.php');

$respuesta = new Respuesta();

// Obtenemos los datos para el nuevo registro
$datosRegistro = json_decode(file_get_contents("php://input"), true);

// Comprobamos que los datos están debidamente formateados
if (!is_array($datosRegistro)) {
    $respuesta->exito = false;
    $respuesta->mensaje = "No hay datos o no son válidos";
    die(json_encode($respuesta));
}

// Nos aseguramos de que no se ha suministrado contraseña
if (isset($datosRegistro['pass'])) {
    $respuesta->exito = false;
    $respuesta->mensaje = "No se debe proporcionar contreña en esta interfaz";
    die(json_encode($respuesta));
}

// Intentamos construir un nuevo objeto jugador a partir de los mismos
$jugador = Jugador::construye($datosRegistro, $erroresValidacion);

// Si hay errores de validación de los imprimimos y terminamos aquí 
if (!$jugador) {
    $respuesta->exito = false;
    $respuesta->mensaje = "Se han producido errores de validación";
    $respuesta->contenido = $erroresValidacion;
    die(json_encode($respuesta));
}

// Creamos una solicitud de registro para el jugador
$token = $jugador->insertaSolcitudRegistro();
$token = $token->idToken;

// Informamos del proceso completado con éxito
$respuesta->exito = true;
$respuesta->mensaje = "Se ha intentado enviar el enlace de activación a la dirección de correo asociada";

echo json_encode($respuesta);

// Enviamos el email con el enlace 
enviaMail('registro', $jugador->alias, $jugador->email, $token);
