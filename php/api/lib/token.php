<?php

class Token
{
    public $idToken;
    public $expira;

    private function __construct($token = null)
    {
        if ($token) $this->idToken = $token;
    }

    public static function limpiaTabla()
    {
        $bd = BaseDeDatos::conecta();
        $sql = "DELETE FROM token WHERE CURRENT_TIMESTAMP > expira;";
        $bd->ejecutaSentencia($sql);
    }

    public static function genera($vida)
    {
        $token = base64_url_encode(random_bytes(12));
        $nuevo = new Token($token);
        $sql = "INSERT INTO token (idToken, expira) VALUES (?, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL $vida));";
        $bd = BaseDeDatos::conecta();
        $sentencia = $bd->prepara($sql);
        $bd->ejecuta($sentencia, [$nuevo->idToken]);
        return $nuevo;
    }

    public static function obten($token)
    {
        Token::limpiaTabla();
        $sql = "SELECT * FROM token WHERE idToken = '$token';";
        $bd = BaseDeDatos::conecta();
        $token = $bd->ejecutaConsulta($sql)->fetchObject('Token');
        return $token;
    }

    public function borra() {
        $sql = "DELETE FROM token WHERE idToken = '$this->idToken';";
        $bd = BaseDeDatos::conecta();
        $bd->ejecutaSentencia($sql);
    }

    public function compruebaValido($tipo)
    {
        $sql = "SELECT idJugador FROM $tipo WHERE idToken = '$this->idToken'";
        $bd = BaseDeDatos::conecta();
        $consulta = $bd->ejecutaConsulta($sql);
        $result = $consulta->fetch();
        $idJugador = null;
        if ($result)
            $idJugador = $result['idJugador'];
        return $idJugador;
    }
}
