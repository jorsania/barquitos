# Juego de los Barquitos

Proyecto de fin de Grado del alumno Javier Ors Ania para el Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Web - IES Mor de Fuentes - Monzón.

A continuación se detallan los pasos a seguir para poner en marcha el proyecto.

## Configuración inicial

- Es obligatorio configurar el fichero `env-plantilla` y renombrarlo a `.env`.

- El obligatorio renombrar el archivo `conf/msmtprc-plantilla`  a `conf/msmtprc`, y opcionalmente configurar el envío de correo mediante una cuenta de gmail personal.

## Puesta en marcha

Una vez realizada la configuración inicial, se puede lanzar el proyecto mediante los scripts `./desarrollo.sh` y `./produccion.sh`.

## Diferencias entre entornos

- El entorno de desarrollo monta algunas de las rutas del proyecto como volúmenes en los contenedores que genera. El entorno de producción no monta ningún volumen, copia en las imágenes todo lo que necesita para funcionar.

- En el entorno de producción el código fuente Angular del frontend se compila mediante la instrucción `ng build` de `angular/cli` en una imagen de `node.js`, el resultado de esta compilación se traspasa a una imagen del servidor `nginx`, que se encarga de su ejecución. En el entorno de desarrollo se monta como volumen el código fuente del frontend en un contenedor de `node.js` con `angular/cli`, en dicho contenedor se ejecuta un servidor web de desarrollo mediante la instrucción `ng serve`.

## Acceso a la BBDD

Actualmente se instala en ambos entornos, en el mismo contenedor de PHP que contiene la API del backend, una interfaz phpMyAdmin, para poder consultar y modificar directamente la base de datos `mysql` del proyecto. En el despliegue definitivo ésta
será eliminada del entorno de producción.
