<?php
include('token.php');

#[AllowDynamicProperties]
class Jugador
{
    // Los campos que aparecen aquí son obligatorios para el registro en la base de datos
    public $alias = null;
    public $email = null;
    public $nombre = null;
    public $sexo = null;
    public $fechaNac = null;

    // Limpia la tabla de jugadores de intentos de registro caducados
    public static function limpiaTabla()
    {
        $bd = BaseDeDatos::conecta();
        $sql =
            "DELETE FROM jugador 
             WHERE 
                pass IS NULL AND
                CURRENT_TIMESTAMP > DATE_ADD(fechaReg ,INTERVAL " . EXPIRA_SOLICITUD_REGISTRO . ");";
        $bd->ejecutaSentencia($sql);
    }

    // Construye un nuevo jugador a partir de un JSON con datos de registro
    public static function construye($datosRegistro, &$erroresValidacion)
    {
        // Construimos un nuevo objeto jugador a partir de los datos de registro
        $jugador = new Jugador($datosRegistro);

        // Asignamos los datos a sus respectivos campos
        foreach ($datosRegistro as $campo => $valor)
            $jugador->$campo = $valor;

        // Comprobamos si valida
        $erroresValidacion = $jugador->noValida();
        if ($erroresValidacion)
            $jugador = null;

        return $jugador;
    }

    // Busca un usuario registrado por nombre de usuario  o dirección de correo
    public static function busca($login)
    {
        $sql =
            "SELECT 
                * 
            FROM 
                jugador
            WHERE
                alias = ? OR email = ?";
        $bd = BaseDeDatos::conecta();
        $consulta = $bd->prepara($sql);
        $bd->ejecuta($consulta, [$login, $login]);
        $jugador = $consulta->fetchObject('Jugador');
        return $jugador;
    }

    // Obtiene un jugador a partir de su id (sin la contraseña)
    public static function obten($id)
    {
        $bd = BaseDeDatos::conecta();
        $sql = "SELECT * FROM jugador WHERE idJugador = $id;";
        $jugador = $bd->ejecutaConsulta($sql)->fetchObject('Jugador');
        if ($jugador) unset($jugador->pass);
        return $jugador;
    }

    // Autentifica un usuario registrado a partir de su alias/email y contraseña
    public static function autentifica($login, $pass)
    {
        $jugador = self::busca($login);
        if ($jugador && $jugador->pass == $pass)
            unset($jugador->pass);
        else
            $jugador = null;

        return $jugador;
    }

    // Inserta un nuevo jugador en la base de datos a partir de sus datos de registro
    public function inserta()
    {
        $bd = BaseDeDatos::conecta();
        $datos = (array)$this;
        $campos = implode(",", array_keys($datos));
        $placeholders = ":" . implode(",:", array_keys($datos));
        $sql = "INSERT INTO jugador ($campos) VALUES ($placeholders)";
        $sentencia = $bd->prepara($sql);
        $bd->ejecuta($sentencia, $datos);
    }

    // Actualiza los datos del jugador en la base de datos
    public function actualiza()
    {
        $datos = (array)$this;
        $campos = array_keys($datos);
        $asignaciones = "";
        foreach ($campos as $campo) {
            if ($campo != "idJugador")
                $asignaciones .= " $campo=:$campo,";
        }
        $asignaciones = rtrim($asignaciones, ',');
        $bd = BaseDeDatos::conecta();
        $sql =
            "UPDATE jugador 
             SET $asignaciones
             WHERE idJugador=:idJugador";
        $sentencia = $bd->prepara($sql);
        $bd->ejecuta($sentencia, $datos);
    }

    // Obtiene una solicitud de registro a partir de un token
    public static function obtenSolcitudRegistro($token)
    {
        $idJugador = null;

        // Limpiamos la tabla de registro de tokens inválidos
        $bd = BaseDeDatos::conecta();
        $sql = "DELETE FROM registro WHERE idToken NOT IN (SELECT idToken FROM token);";
        $sentencia = $bd->prepara($sql);
        $bd->ejecuta($sentencia);

        // Comprobamos que el token recibido existe y no está expirado
        $token = Token::obten($token);
        if ($token) {
            // Comprobamos que está asociado a una solicitud de registro
            $sql =
                "SELECT idJugador 
                 FROM jugador NATURAL JOIN registro 
                 WHERE idToken = '$token->idToken' AND pass IS NULL";
            $idJugador = $bd->ejecutaConsulta($sql)->fetchColumn();
        }
        return $idJugador;
    }

    // Inserta una solicitud de registro y devuelve un token asociado a la misma
    public function insertaSolcitudRegistro()
    {
        // Generamos el token asociado a la solicitud
        $token = Token::genera(EXPIRA_SOLICITUD_REGISTRO);

        // Insertamos el jugador en la base de datos
        $this->inserta();

        // Creamos una entrada en la tabla de solicitudes de registro para asociar el token
        // a la entrada de la tabla de jugadores
        $sql =
            "INSERT INTO registro (idToken, idJugador) 
            VALUES (?, 
                (SELECT idJugador FROM jugador WHERE email = ?))";
        $bd = BaseDeDatos::conecta();
        $sentencia = $bd->prepara($sql);
        $bd->ejecuta($sentencia, [$token->idToken, $this->email]);
        return $token;
    }

    // Obtiene una solicitud de recuperación a partir de un token
    public static function obtenSolcitudRecuperacion($token)
    {
        $idJugador = null;

        // Limpiamos la tabla de recuperacion de tokens inválidos
        $bd = BaseDeDatos::conecta();
        $sql = "DELETE FROM recuperacion WHERE idToken NOT IN (SELECT idToken FROM token);";
        $sentencia = $bd->prepara($sql);
        $bd->ejecuta($sentencia);

        // Comprobamos que el token recibido existe y no está expirado
        $token = Token::obten($token);
        if ($token) {
            // Comprobamos que está asociado a una solicitud de recuperacion
            $sql =
                "SELECT idJugador 
                 FROM jugador NATURAL JOIN recuperacion 
                 WHERE idToken = '$token->idToken'";
            $idJugador = $bd->ejecutaConsulta($sql)->fetchColumn();
        }
        return $idJugador;
    }

    // Inserta una solicitud de recuperación y devuelve un token asociado a la misma
    public function insertaSolcitudRecuperacion()
    {
        // Comprobamos si ya existe una solicitud vigente para este jugador
        Token::limpiaTabla();
        $sql = "SELECT idToken FROM recuperacion NATURAL JOIN token WHERE idJugador = '$this->idJugador'";
        $bd = BaseDeDatos::conecta();
        $idToken = $bd->ejecutaConsulta($sql)->fetchColumn();
        if ($idToken) $token = null;
        else {
            // Generamos el token asociado a la solicitud
            $token = Token::genera(EXPIRA_SOLICITUD_RECUPERACION);

            // Creamos una entrada en la tabla de solicitudes de recuperación para asociar el token
            // a la entrada de la tabla de jugadores
            $sql =
                "INSERT INTO recuperacion (idToken,idJugador) 
                VALUES (?, (SELECT idJugador FROM jugador WHERE email = ?))";
            $sentencia = $bd->prepara($sql);
            $bd->ejecuta($sentencia, [$token->idToken, $this->email]);
        }

        return $token;
    }

    public function cambiaPassword($pass, $token)
    {
        $erroresValidacion = self::validaPassword($pass);
        if (!$erroresValidacion) {
            $this->pass = $pass;
            $this->actualiza();
            Token::obten($token)->borra();
        }
        return $erroresValidacion;
    }

    // Envía un enlace de recuperación de contraseña a una cuenta si existe
    public static function recuperaCuenta($cuenta)
    {
        $jugador = self::busca($cuenta);
        if (!empty($jugador)) {
        }
    }

    // Validaciones para cada campo
    private function valida_alias()
    {
        $lon = strlen($this->alias);
        if ($lon == 0) {
            return "El alias es obligatorio";
        }
        if ($lon < MIN_LON_ALIAS)
            return "El alias debe tener más de " . MIN_LON_ALIAS . " caracteres";
        if ($lon > MAX_LON_ALIAS)
            return "El alias debe ser inferior a " . MAX_LON_ALIAS . " caracteres";
    }

    private function valida_nombre()
    {
        $lon = strlen($this->nombre);
        if ($lon == 0)
            return "El nombre es obligatorio";
        if ($lon < MIN_LON_NOMBRE)
            return "Debe tener más de " . MIN_LON_NOMBRE . " caracteres";
        if ($lon  > MAX_LON_NOMBRE)
            return "Debe ser inferior a " . MAX_LON_NOMBRE . " caracteres";
    }

    private function valida_email()
    {
        if (empty($this->email))
            return "El email el obligatorio";
        if (!filter_var($this->email, FILTER_VALIDATE_EMAIL)) {
            return "La dirección de correo no tiene un formato válido";
        }
    }

    private function valida_apellidos()
    {
        if (strlen($this->apellidos) > MAX_LON_NOMBRE)
            return "Debe ser inferior a " . MAX_LON_NOMBRE . " caracteres";
    }

    private function valida_telefono()
    {
        $lon = strlen($this->telefono);
        $pattern = "/^((?:\+?\(\d{1,3}\))|(\+\d{1,3} ))?[ \-\d]{9,15}$/";
        if ($lon > MAX_LON_TELEFONO)
            return "Debe ser inferior a " . MAX_LON_TELEFONO . " caracteres";
        if (
            $lon > 0 &&
            !preg_match($pattern, $this->telefono)
        ) {
            return "No es un número de teléfono válido";
        }
    }

    private function valida_sexo()
    {
        if (empty($this->sexo)) {
            return "Es obligatorio especificar el sexo";
        }

        if (!$this->sexo == "H" || !$this->sexo == "M")
            return "El sexo sólo puede tomar los valores 'H' o 'M'";
    }

    private function valida_fechaNac()
    {
        if (strlen($this->fechaNac) == 0)
            return "La fecha de nacimiento es obligatoria";
        if (!preg_match("/^(\d{4})-(\d{2})-(\d{2})(\D|$)/", $this->fechaNac, $matches))
            return "La fecha de nacimiento no tiene un formato válido";
        if (!checkdate($matches[2], $matches[3], $matches[1]))
            return "La fecha de nacimiento no es válida";
        $this->fechaNac = substr($this->fechaNac, 0, 10);
    }

    // Validación para el password, esta no se hace automáticamente como el resto
    public static function validaPassword($pass)
    {
        $pattern = "/(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[.@$!%*?&]).*/";
        $valida = true;
        $lon = strlen($pass);
        if ($lon < MIN_LON_PASS)
            $valida = false;
        else if ($lon > MAX_LON_PASS)
            $valida = false;
        else if (!preg_match($pattern, $pass))
            $valida = false;
        if (!$valida)
            return "La contraseña debe tener entre  " . MIN_LON_PASS . " y " . MAX_LON_PASS . " caracteres, una mayúscula, una minúscula, un número y un carácter especial (.@$!%*?&)";
    }

    // Llama a todas las funciones de validación de todos los campos, tanto los declarados explícitamente
    //  en la clase como los que se hayan asignado en tiempo de ejecución
    public function noValida()
    {
        $noValida = [];
        $campos = array_keys((array)$this);
        foreach ($campos as $campo) {
            $funcValida = "valida_" . $campo;
            if (method_exists($this, $funcValida)) {
                $error = $this->$funcValida();
                if ($error) {
                    $noValida[$campo] = $error;
                }
            }
        }

        // Si el alias o el email validan 
        if (!isset($noValida['alias']) || !isset($noValida['email'])) {

            // Limpiamos la tabla de jugadores para asegurarnos que no estén 
            // en algún intento de registro caducado
            self::limpiaTabla();

            $bd = BaseDeDatos::conecta();
            // Si el email valida
            if (!isset($noValida['email'])) {
                // Comprobamos que la dirección de correo esté libre
                $sql = "SELECT idJugador FROM jugador WHERE email = ?;";
                $consulta = $bd->prepara($sql);
                $bd->ejecuta($consulta, [$this->email]);
                if ($consulta->fetchColumn()) {
                    $noValida["email"] = "La dirección de correo ya está asociada a una cuenta registrada";
                }
            }

            // Si el alias valida
            if (!isset($noValida['alias'])) {
                // Comprobamos que el alias elegido esté libre
                $sql = "SELECT idJugador FROM jugador WHERE alias = ?;";
                $consulta = $bd->prepara($sql);
                $bd->ejecuta($consulta, [$this->alias]);
                if ($consulta->fetchColumn()) {
                    $noValida["alias"] = "El alias elegido no está libre";
                }
            }
        }

        return $noValida;
    }
}
