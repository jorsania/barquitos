<?php
include('lib/util.php');
include('lib/jugador.php');

$respuesta = new Respuesta();

// Comprobamos que se ha proporcionado un token
if (!isset($_GET['token'])) {
    $respuesta->exito = false;
    $respuesta->mensaje = "Hay que proporcionar el token asociado a la solicitud de registro";
    die(json_encode($respuesta));
}
$token  = $_GET['token'];
$motivo = $_GET['motivo'];

if (!empty($motivo)) {
    if ($motivo == 'activa') { 
        // Comprobamos que el token proporcionado está asociado a una solicitud de registro vigente
        $idJugador = Jugador::obtenSolcitudRegistro($token);
    } else if ($motivo == 'recupera') {
        // Comprobamos que el token proporcionado está asociado a una recuperación de cuenta vigente
        $idJugador = Jugador::obtenSolcitudRecuperacion($token);
    }
    if (empty($idJugador)) {
        $respuesta->exito = false;
        $respuesta->mensaje = "El token no es válido o ha expirado";
        die(json_encode($respuesta));
    }
} else {
    $idJugador = iniciaSesion();
    if (!$idJugador) {
        $respuesta->exito = false;
        $respuesta->mensaje = "Esta acción require autenticación previa o un token válido";
        die(json_encode($respuesta));
    }    
}

// Obtenemos el jugador asociado a la solicitud
$jugador = Jugador::obten($idJugador);

// Si es una solicitud GET el cliente sólo quiere saber si el token es válido
if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    $respuesta->exito = true;
    $respuesta->contenido = $jugador;
    exit(json_encode($respuesta));

// Si es una solicitud POST el cliente quiere completar el proceso de registro
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Comprobamos que se ha proporcionado un password
    $password = file_get_contents("php://input");
    if (empty($password)) {
        $respuesta->exito = false;
        $respuesta->contenido = "La contraseña está vacía";
        $respuesta->mensaje = "Hay que proporcionar la contraseña";
        die(json_encode($respuesta));
    }

    // Finalizamos el cambio de password
    $erroresValidacion = $jugador->cambiaPassword($password, $token);
    if ($erroresValidacion) {
        $respuesta->exito = false;
        $respuesta->contenido = $erroresValidacion;
        $respuesta->mensaje = "La contraseña no es válida";
        die(json_encode($respuesta));
    }

    $respuesta->exito = true;
    $respuesta->mensaje = "El cambio de password se ha completado con éxito";
    die(json_encode($respuesta));
}