<?php
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Headers: *");

include('bd.php');

function iniciaSesion()
{
    // Recuperamos las variables de sesión
    session_start();
    $idJugador = null;
    if (isset($_SESSION['idJugador'])) {
        if (isset($_SESSION['permanente'])) {
            // Existe sesión iniciada
            if ($_SESSION['permanente'])
                // La sesión se inició con la opción recordar usuario, 
                // extendemos la vida de la cookie de sesión
                setcookie(session_name(), session_id(), time() + 30 * 24 * 3600, "/");
            // Devolvemos el objeto usuario
            $idJugador = $_SESSION['idJugador'];
        } else {
            session_destroy();
        }
    }
    return $idJugador;
}

define("MIN_LON_ALIAS", 3);
define("MAX_LON_ALIAS", 15);
define("MIN_LON_PASS", 8);
define("MAX_LON_PASS", 256);
define("MIN_LON_NOMBRE", 2);
define("MAX_LON_NOMBRE", 30);
define("MAX_LON_EMAIL", 320);
define("MAX_LON_TELEFONO", 15);

define("EXPIRA_SOLICITUD_REGISTRO", "30 MINUTE");
define("EXPIRA_TOKEN_PARTIDA", "100 YEAR");
define("KEEPALIVE_ESPERA_JUGADOR", "2 SECOND");

#[AllowDynamicProperties]
class Respuesta
{
    public $exito;
    public $mensaje = "";
    public $contenido = [];
}

function enviaMail($tipo, $alias, $email, $token)
{
    // Envío del correo electrónico
    switch ($tipo) {
        case 'registro':
            $asunto = "Registro de cuenta en el Juego de los Barquitos";
            $mensaje =
                "Se ha recibido una solicitud de registro para la cuenta '$alias' en el Juego de los Barquitos, " .
                "haz click en el siguiente enlace para completar el proceso y activar la cuenta:\n\n" .
                "   ". $_ENV['DIRECCION'] . "/activa/$token\n\n" .
                "Si no eres tú quien ha realizado esta petición puedes ignorar este mensaje.";
            break;
        case 'recuperacion':
            $asunto = "Recuperación de cuenta en el Juego de los Barquitos";
            $mensaje =
                "Se ha recibido una solicitud de recuperación para la cuenta '$alias' en el Juego de los Barquitos, " .
                "haz click en el siguiente enlace para cambiar la contraseña de tu cuenta:\n\n" .
                "   ". $_ENV['DIRECCION'] . "/recuperacion?token=$token\n\n" .
                "Si no has sido tú quien ha realizado esta petición puedes ignorar este mensaje.";
            break;
        case 'email':
            $asunto = "Cambio de dirección de correo en el Juego de los Barquitos";
            $mensaje =
                "Se ha recibido una solicitud para asociar esta dirección de correo electrónico a la cuenta " .
                "'$alias' en el Juego de los Barquitos, haz click en el siguiente enlace para completar el proceso de verificación:\n\n" .
                "   ". $_ENV['DIRECCION'] . "/verificaEmail?token=$token\n\n" .
                "Si no has sido tú quien ha realizado esta petición puedes ignorar este mensaje.";
            break;
    }
    mail($email, $asunto, $mensaje);
}

function base64_url_encode($input) {
    return strtr(base64_encode($input), '+/=', '._-');
   }
   
   function base64_url_decode($input) {
    return base64_decode(strtr($input, '._-', '+/='));
}