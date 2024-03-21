<?php

$MAX_LON_ALIAS = MAX_LON_ALIAS;
$MAX_LON_PASS = MAX_LON_PASS;
$MAX_LON_NOMBRE = MAX_LON_NOMBRE;
$MAX_LON_EMAIL = MAX_LON_EMAIL;
$MAX_LON_TELEFONO = MAX_LON_TELEFONO;

return 
"CREATE DATABASE nombre_bd;
USE nombre_bd;

CREATE TABLE jugador (
    idJugador   INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    alias       VARCHAR($MAX_LON_ALIAS) UNIQUE NOT NULL,
    email       VARCHAR($MAX_LON_EMAIL) UNIQUE NOT NULL,
    pass        VARCHAR($MAX_LON_PASS),
    nombre      VARCHAR($MAX_LON_NOMBRE) NOT NULL,
    apellidos   VARCHAR($MAX_LON_NOMBRE),
    telefono    VARCHAR($MAX_LON_TELEFONO),
    sexo        CHAR(1) NOT NULL CHECK (sexo IN('H', 'M')),
    fechaNac    DATE NOT NULL,
    fechaReg    DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE partida (
    idPartida   CHAR(8) PRIMARY KEY,
    nJugadores  INT(1) NOT NULL CHECK (nJugadores IN('1', '2')),
    idJugador1  INTEGER UNSIGNED NOT NULL,
    idJugador2  INTEGER UNSIGNED,
    turno       INTEGER UNSIGNED,
    partida     BLOB,
    fechaCrea   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fechaMod    TIMESTAMP NOT NULL,
    FOREIGN KEY (idJugador1) REFERENCES jugador (idJugador) ON DELETE CASCADE,
    FOREIGN KEY (idJugador2) REFERENCES jugador (idJugador) ON DELETE CASCADE,
    FOREIGN KEY (turno) REFERENCES jugador (idJugador) ON DELETE CASCADE
);

CREATE TABLE codigosPartida (
    idPartida   CHAR(8),
    idJugador   INTEGER UNSIGNED,
    codigo      CHAR(8),
    PRIMARY KEY (idJugador, idPartida),
    FOREIGN KEY (idJugador) REFERENCES jugador (idJugador) ON DELETE CASCADE,
    FOREIGN KEY (idPartida) REFERENCES partida (idPartida) ON DELETE CASCADE
);

CREATE TABLE token (
    idToken     CHAR(16) PRIMARY KEY,
    expira      DATETIME
);

CREATE TABLE registro (
    idToken     CHAR(16),
    idJugador   INTEGER UNSIGNED,
    PRIMARY KEY (idToken, idJugador),
    FOREIGN KEY (idToken) REFERENCES token (idToken) ON DELETE CASCADE, 
    FOREIGN KEY (idJugador) REFERENCES jugador (idJugador) ON DELETE CASCADE
);

CREATE TABLE verificacion (
    idToken     CHAR(16),
    idJugador   INTEGER UNSIGNED,
    PRIMARY KEY (idToken, idJugador),
    FOREIGN KEY (idToken) REFERENCES token (idToken) ON DELETE CASCADE,
    FOREIGN KEY (idJugador) REFERENCES jugador (idJugador) ON DELETE CASCADE
);

CREATE TABLE recuperacion (
    idToken     CHAR(16),
    idJugador   INTEGER UNSIGNED,
    PRIMARY KEY (idToken, idJugador),
    FOREIGN KEY (idToken) REFERENCES token (idToken) ON DELETE CASCADE,
    FOREIGN KEY (idJugador) REFERENCES jugador (idJugador) ON DELETE CASCADE
);";
