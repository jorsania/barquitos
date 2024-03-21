<?php

// Clase para interactuar con la base de datos
class BaseDeDatos
{
    private static $bd = null;
    private $con = null;
    private $nombre_bd = "barquitos";

    public static function conecta()
    {
        if (self::$bd == null) self::$bd = new BaseDeDatos;

        return self::$bd;
    }

    private function __construct()
    {
        // Datos para la conexión
        $conexion = "mysql:dbname=$this->nombre_bd;host=mysql";
        try {
            // Intentamos conectar a la base de datos
            $this->con = new PDO($conexion, 'root', file_get_contents('/run/secrets/db_password'));
        } catch (PDOException $e) {
            // Interceptamos posibles errores (excepciones)
            if ($e->getCode() == 1049) {
                // La base de datos no existe, intentamos crearla
                $this->creaBaseDeDatos();
            } else {
                $this->finaliza($e);
            }
        }
        if ($this->con == null) {
            $this->finaliza($e);
        }
    }

    public function prepara($sql)
    {
        try {
            return $this->con->prepare($sql);
        } catch (PDOException $e) {
            $this->finaliza($e);
        }
    }

    public function ejecuta($sentencia, $params = null)
    {
        try {
            $sentencia->execute($params);
        } catch (PDOException $e) {
            $this->finaliza($e);
        }
    }

    public function ejecutaConsulta($sql)
    {
        try {
            return $this->con->query($sql);
        } catch (PDOException $e) {
            $this->finaliza($e);
        }
    }

    public function ejecutaSentencia($sql)
    {
        try {
            return $this->con->exec($sql);
        } catch (PDOException $e) {
            $this->finaliza($e);
        }
    }

    // Crea la estructura de la base de datos 
    private function creaBaseDeDatos()
    {
        $conexion = 'mysql:host=mysql';
        try {
            $this->con = new PDO($conexion, 'root', file_get_contents('/run/secrets/db_password'));
            $sql = include('creaBD.php');
            $sql = str_replace("nombre_bd", $this->nombre_bd, $sql);
            $this->con->exec($sql);
        } catch (PDOException $e) {
            $this->finaliza($e);
        }
    }

    private function finaliza($e) {
        $respuesta = new Respuesta();
        $respuesta->exito = false;
        $respuesta->mensaje = "Se ha producido un error en la base de datos";
        $respuesta->codigoError = $e->getCode();
        $respuesta->mensajeBD = $e->getMessage();
        $respuesta->fichero = $e->getFile();
        $respuesta->linea = $e->getLine();
        $respuesta->traza = $e->getTraceAsString();
        die(json_encode($respuesta));
    }
}
