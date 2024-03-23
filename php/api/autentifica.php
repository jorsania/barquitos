<?php
include('lib/util.php');
include('lib/jugador.php');

$respuesta = new Respuesta();

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $idJugador = iniciaSesion();
    if ($idJugador) {
        $jugador = Jugador::obten($idJugador);
        $respuesta->exito = true;
        $respuesta->autentificado = true;
        $respuesta->contenido = $jugador;
        exit(json_encode($respuesta));
    } else {
        $respuesta->exito = true;
        $respuesta->autentificado = false;
        exit(json_encode($respuesta));
    }
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtenemos los datos de autenticación
    $autenticacion = json_decode(file_get_contents("php://input"));

    // Comprobamos que los datos están debidamente formateados
    if (!$autenticacion) {
        $respuesta->exito = false;
        $respuesta->mensaje = "No hay datos o no son válidos";
        die(json_encode($respuesta));
    }

    if (!isset($autenticacion->login)) {
        $respuesta->exito = false;
        $respuesta->mensaje = "Hay que proporcionar el nombre de usuario o correo electrónico";
        die(json_encode($respuesta));
    }

    if (!isset($autenticacion->pass)) {
        $respuesta->exito = false;
        $respuesta->mensaje = "Hay que proporcionar la contraseña";
        die(json_encode($respuesta));
    }

    $jugador = Jugador::autentifica($autenticacion->login, $autenticacion->pass);

    if ($jugador) {
        // La autentificación ha resultado exitosa
        session_start();
        if (!empty($autenticacion->permanente)) {
            setcookie(session_name(), session_id(), time() + 30 * 24 * 3600, "/");
            $_SESSION['permanente'] = true;
        } else {
            setcookie(session_name(), session_id(), 0, "/");
            $_SESSION['permanente'] = false;
        }
        $_SESSION['idJugador'] = $jugador->idJugador;

        $respuesta->exito = true;
        $respuesta->autentificado = true;
        $respuesta->contenido = $jugador;
        exit(json_encode($respuesta));
    } else {
        $respuesta->exito = true;
        $respuesta->autentificado = false;
        $respuesta->mensaje = "Nombre de usuario, email o contraseña incorrectos";
        die(json_encode($respuesta));
    }
}
