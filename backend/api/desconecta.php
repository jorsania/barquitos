<?php
include('lib/util.php');
$respuesta = new Respuesta();
session_start();
session_destroy();
$respuesta->exito = true;
$respuesta->autentificado = false;
exit(json_encode($respuesta));
