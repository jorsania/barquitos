<?php

class Partida
{
    public $idPartida;
    public $nJugadores;
    public $idJugador1;

    public static function construye($idJugador, $nJugadores, &$error)
    {
        $partida = new Partida();
        $partida->idPartida = base64_encode(random_bytes(6));
        $partida->idJugador1 = $idJugador;
        if (!in_array($nJugadores, [1, 2])) {
            $error = "El número de jugadores debe valer 1 o 2";
            return null;
        }
        $partida->nJugadores = $nJugadores;
        $bd = BaseDeDatos::conecta();
        $sql =
            "INSERT INTO 
                partida (idPartida, nJugadores, idJugador1)
            VALUES
                ('$partida->idPartida',
                $partida->nJugadores,
                $partida->idJugador1);";
        $bd->ejecutaSentencia($sql);

        $codigo = base64_encode(random_bytes(6));
        $sql =
            "INSERT INTO
                codigosPartida (idPartida, idJugador, codigo)
            VALUES
                ('$partida->idPartida',
                $partida->idJugador1,
                '$codigo')";
        $bd->ejecutaSentencia($sql);
        return $partida;
    }
}
