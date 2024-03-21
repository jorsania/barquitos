<?php
include('lib/util.php');

$sql =
    "INSERT INTO jugador 
    (alias, email, pass, nombre, apellidos, sexo, fechaNac)  
VALUES
    ('jugador1','jugador1@barquitos.com','123','Juan','Nadie', 'H', '1998-01-01'),
    ('jugador2','jugador2@barquitos.com','123','Juan','Nadie', 'H', '1998-01-01'),
    ('jugador3','jugador3@barquitos.com','123','Juan','Nadie', 'H', '1998-01-01'),
    ('jugador4','jugador4@barquitos.com','123','Juan','Nadie', 'H', '1998-01-01'),
    ('jugador5','jugador5@barquitos.com','123','Juan','Nadie', 'H', '1998-01-01');";

$bd = BaseDeDatos::conecta();
$bd->ejecutaSentencia($sql);
