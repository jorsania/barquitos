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
$respuesta->mensaje =
    "<p>" .
    "Se requiere verificar el correo electrónico para completar el " .
    "proceso de registro, de ha enviado un enlace a la dirección suministrada " .
    "que tendrás que visitar en los próximos 30 minutos para activar la cuenta. " .
    "Puedes cerrar esta pestaña del navegador." .
    "</p><hr/>" .
    "<h3><strong>IMPORTANTE:</strong></h3>" .
    "<p>" .
    "Se suministra el enlace a continuación por si no está configurada la " .
    "entrega de correo electrónico en PHP. Esta parte del mensaje debe ser " .
    "eliminada en producción:" .
    "</p>" .
    "<a href='". $_ENV['DIRECCION'] . "/activa/$token'>" .
    " Pincha aquí para activar la cuenta" .
    "</a>";

echo json_encode($respuesta);

// Enviamos el email con el enlace 
enviaMail('registro', $jugador->alias, $jugador->email, $token);
