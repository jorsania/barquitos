# Juego de los Barquitos

**Proyecto de Fin de Grado** del alumno **Javier Ors Ania** para el **Ciclo
Formativo de Grado Superior** en **Desarrollo de Aplicaciones Web**, cursado en
el **IES Mor de Fuentes** de **Monzón (Huesca)**.

## Instalación y requisitos previos

El proyecto se basa en una arquitectura de microservicios que se compila,
ejecuta y despliega usando las herramientas *Docker* y *Docker Compose*. Por
ello, en principio tan sólo es necesario disponer de un entorno de ejecución
*UNIX* con el *terminal Bash* y una instalación *reciente* de dichas
herramientas para poder compilar, ejecutar y desplegar el proyecto siguiendo las
instrucciones de este documento.

## Arquitectura del proyecto: entornos, contenedores, imágenes y servicios  

El código de éste repositorio está concebido para trabar con diferentes
*entornos*. Existe un **entorno de desarrollo**, un **entorno de producción** y
un **entorno de despliegue**. 

Por otro lado, en su estado actual el proyecto se compone de cinco *servicios*
que se ejecutan en *contenedores* independientes, si bien en un futuro próximo
se añadirán dos adicionales. Se generan y suben a este repositorio *imágenes*
públicas genéricas para tres de estos *servicios*.

  - Servicio **pasarela**:
    
    Este servicio hace de punto de entrada al proyecto. Es el único que expone
    un puerto configurable al exterior, al cual se accede mediante un navegador
    web. Se encarga de atender las peticiones entrantes y reenviarlas al
    servicio adecuado en función de la *URL* solicitada. 
    
    Para este servicio se genera una *imagen Docker* derivada de la imagen
    oficial del servidor web *NGINX* en su versión *alpine*. Esta imagen es
    común a todos los entornos y es una de las que se suben al repositorio.

  - Servicio **frontend**:
    
    Es el servicio que aloja el *frontend **Angular*** que se ejecutará en el
    navegador del usuario. Se encarga de generar y mostrar la interfaz de
    usuario, y de la comunicación del navegador web con las diferentes *API's*
    del proyecto.

    Para este servicio se genera una *imagen Docker* diferente en función del
    entorno: 
    
      * En el **entorno de desarrollo** la imagen utiliza el servidor web de
        desarrollo integrado en el *framework de **Angular***, que se invoca
        mediante la instrucción `ng serve`. El código no se incorpora a la
        imagen, sino que se monta como volumen en el directorio correspondiente
        de la copia local del repositorio.
      
      * En el **entorno de producción** la se utiliza la imagen oficial del
        servidor web *NGINX* en su versión *alpine* para servir la versión
        compilada del *frontend* mediante la instrucción `ng build`.
      
      Sólo se sube al repositorio la imagen del **entorno de producción**.

  - Servicio **php**:

    Es el servicio que ejecuta las diferentes *API's* en *PHP* del proyecto.
    Genera una *imagen Docker* derivada de la imagen oficial del proyecto *PHP*
    en su versión *fpm-alpine*.
    
    En la versión del **entorno de desarrollo** el código no se incorpora a la
    imagen, sino que se monta como volumen en el directorio correspondiente de
    la copia local del repositorio.
    
    En la versión del **entorno de producción** el código se incorpora en la
    imagen en el momento de su creación.
    
    Sólo se sube al repositorio la imagen del **entorno de producción**.
    
  - Servicio **phpmyadmin**:

    Ejecuta una interfaz *phpMyAdmin* para poder consultar y manipular
    fácilmente los contenidos de la base de datos durante las pruebas. En un
    caso de desarrollo profesional esta función se desactivaría en el **entorno
    de producción**. Pero al tratarse de un proyecto académico de índole
    demostrativa he optado por mantenerla en todos los entornos por practicidad
    y conveniencia. No se genera ninguna imagen propia en ninguno de los
    entornos, puesto que su nivel de adaptación es mínimo y se puede usar
    directamente la imagen oficial del proyecto *phpMyAdmin*.

  - Servicio **mysql**:
    
    Este servicio aloja el gestor de base de datos *MySQL* y es el encargado de
    almacenar toda la información que necesita el proyecto para funcionar;
    usuarios de la web, partidas en curso, estadísticas y logs, etc. No se
    genera ninguna imagen propia en ninguno de los entornos, puesto que su nivel
    de adaptación es mínimo y se puede usar directamente la imagen oficial del
    proyecto *MySQL*.
  
    Lo utiliza internamente el servicio *php* y no es directamente accesible
    desde el exterior. Si bien se puede interactuar con él a través de la
    interfaz *phpMyAdmin* incluida en este último.

    Monta automáticamente el directorio de la base de datos como volumen
    anónimo, por lo que el contenido persiste entre reinicios del servicio, pero
    se perderá si eliminamos el contenedor.

    Dado que la única manera de cambiar el entorno en ejecución para una
    determinada copia local del repositorio pasa por eliminar los contenedores y
    volverlos a crear, si queremos usar varios entornos sin perder el contenido
    de la *BBDD* en el cambio lo más práctico es disponer de varias copias en
    diferentes directorios, por ejemplo así:

        |- barquitos_development/
        |  |- build/
        |  |- ...
        |  
        |-barquitos_production/
        |  |- build/
        |  |- ...
        |
        |-barquitos_deployment/
          |- build/
          |- ...

    Otra opción pasaría por montar el contenido de la base de datos en un
    subdirectorio creado en la copia local del repositorio. Para ello habría que
    ejecutar en la raíz del mismo la instrucción `mkdir mysql`, y modificar los
    ficheros `build/docker-compose.*.yml` cambiando las líneas:

        volumes:
          - /var/lib/mysql

    por:

        volumes:
          - mysql:/var/lib/mysql

## Configuración del proyecto

Los entornos de **desarrollo** y de **producción** comparten el mismo conjunto
de parámetros de configuración, pero sus valores se establecen
independientemente. El entorno de **despliegue** comparte parámetros y valores
con el de **producción**.

### Configuración de los parámetros en los diferentes entornos

En el subdirectorio `build/` tenemos los siguientes ficheros que se pueden
editar para asignar valores a los parámetros de ambos entornos:

  - `config.dvlp` - Configuración general en entorno de **desarrollo**   
  - `config.prod` - Configuración general en entorno de **producción** y
    **despliegue**   
  - `mail.dvlp` - Configuración del envío de correo en entorno de **desarrollo**
  - `mail.prod` - Configuración del envío de correo en entorno de **producción**
    y **despliegue**

En cualquiera de los entornos, las imágenes que genera el repositorio no
contienen ninguno de los valores consignados en estos ficheros. Los parámetros
de configuración se suministran siempre a los contenedores en el momento de su
creación mediante *scripts* que utilizan los mecanismos que el sistema Docker
ofrece para ello.

### Parámetros de configuración general

Esta es la lista de parámetros que se pueden configurar actualmente:

  - `HOST_ADDR` - Es la *URL* completa donde se alojará el proyecto, **la que
    vamos a teclear en la barra del navegador**. Si simplemente vamos a hacer
    pruebas en desarrollo local sería por ejemplo `http://localhost/` o
    `http://localhost:8080/`.
    
    Sin embargo, si vamos a desplegar una versión accesible desde intenet,
    tendremos que ajustar su valor a lo que corresponda. Es importante que se
    especifiquen correctamente tanto el esquema que se utilizará para la
    conexión, ya sea `http://` o `https://`, como la dirección base de
    referencia en caso de haberla como por ejemplo en
    `https://midominio.com/juegodelosbarquitos/`. Es recomendable finalizar la
    dirección siempre con una `/` final, pero si se omite el sistema la añadirá
    internamente.
    
    Es necesario matizar que la simple especificación de este parámetro no hará
    que el proyecto sea *mágicamente* accesible desde internet. Para ello serán
    necesarios una serie de requisitos y pasos adicionales que no están
    cubiertos en el ámbito de éste manual. Pero en cualquier caso, el proyecto
    **debe** conocer su dirección completa para que pueda funcionar
    correctamente, y la manera de informarlo es mediante éste parámetro.

  - `PORT` - Es el único puerto de escucha del proyecto, lo expone el
    contenedor/servicio *pasarela*, y en él se recibirán las peticiones *HTTP*
    de los clientes web. En el caso que estemos realizando pruebas de desarrollo
    en local coincidirá con el especificado en la dirección, por ejemplo `80`
    para `http://localhost/`, o bien 8080 para `http://localhost:8080/`. En el
    caso de despliegue abierto a internet pueden no coincidir.

    Es necesario matizar que, independientemente del puerto elegido, la pasarela
    sólo aceptará conexiones no cifradas mediante el protocolo *HTTP*. Es
    posible hacer el proyecto accesible desde el exterior mediante cifrado
    *HTTPS*, pero deberá hacerse a través de una redirección externa mediante
    algún otro servicio que se encargue de hacer de intermediario entre el
    cliente *HTTPS* y la conexión *HTTP* con la pasarela, idealmente una vez ya
    dentro de la misma máquina o entorno de Cloud. Nuevamente estamos hablando
    de una configuración compleja que queda fuera del ámbito de éste manual.

  - `DB_PASSWORD` - Como su propio nombre indica, se trata de la contraseña que
    se usará para comunicarse con el servicio de base de datos *MySQL*. El
    servicio no será directamente accesible desde el exterior, ni indirectamente
    tampoco si se elimina la interfaz *phpMyAdmin* de la instalación. Se podría
    dejar la contraseña vacía en tal caso, pero nunca está de más tenerla, ya
    que si un atacante llegara a comprometer el servicio *pasarela* o *frontend*
    aún no tendrían acceso a la base de datos siempre que ésta tuviera una
    contraseña *fuerte*, en caso contrario se podría acceder a todo su contenido
    tras haber derribado esa primera barrera.

### Parámetros de configuración para el envío de correo electrónico

El proyecto ofrece la posibilidad de envío de correos electrónicos para el
típico registro de usuarios con verificación de cuenta mediante enlace de
activación. Pero es necesario tener disponible y correctamente configurado un
servidor SMTP de envío de correo externo. Los parámetros de configuración
necesarios para ello son:

  - `MAIL_FROM` - Dirección de correo saliente.
  - `SMTP_AUTH_USER` - Usuario de autenticación en servidor de correo saliente.
  - `SMTP_SERVER_ADDRESS` - Dirección IP o dominio del servidor de correo
    saliente.
  - `SMTP_SERVER_PORT` - Puerto de escucha del servidor de correo saliente.
  - `SMTP_PASSWORD` - Contraseña de autenticación en servidor de correo
    saliente.

Es posible utilizar el servidor de correo saliente de *Google* si activamos la
verificación en dos pasos en una *gMail* y generamos una contraseña de
aplicación, tal y como se explica en [esta entrada de
blog](https://dev.to/yactouat/send-gmail-emails-from-a-dockerized-php-app-the-easy-and-free-way-4jn7).

## Selección del entorno
Para seleccionar, por ejemplo, el entorno de desarrollo nos situamos en la raíz
del proyecto e invocamos el *script* de configuración de entorno correspondiente
mediante:

    source development.sh

o simplemente (notar el espacio después del punto):

    . development.sh

Tras esto obtendremos un mensaje que nos indica que ya está listo el entorno
seleccionado, junto con un resumen de las variables consignadas en el archivo de
configuración correspondiente.

    barquitos@srv:~/barquitos$ . development.sh

    Se ha configurado el entorno para DESARROLLO:

          Dirección del proyecto: http://localhost:8080/
    Puerto entrante del proyecto: 8080
    Dirección de correo saliente: donotreply@domain.com
                Cuenta de correo: donotreply@domain.com
     Servidor de correo saliente: smpt.domain.com
       Puerto servidor de correo: 25

Podemos seleccionar cualquiera de los otros dos entornos invocando sus
respectivos *scripts* `production.sh` o `deployment.sh` 

## Compilación, creación de contenedores y ejecución del proyecto

Una vez seleccionado el entorno podemos hacer uso de las diferentes
instrucciones `docker compose` para construir y ejecutar los servicios que
conforman el proyecto. A continuación las más útiles:

  - `docker compose up` - Con esta sencilla instrucción se crean y ejecutan los
    contenedores con los parámetros de configuración elegidos en cualquiera de
    los tres entornos. Dependiendo del entorno seleccionado también se compila
    el código y se crean las imágenes correspondientes.
    
    Se puede combinar con el flag `-d` para ejecutar los contenedores en segundo
    plano y con el flag `--build` para forzar la reconstrucción de las imágenes.
    Si bien este último no debería ser necesario en la mayoría de los casos,
    pues *Docker* suele autodetectar con bastante precisión cuándo es necesaria
    una reconstrucción.

  - `docker compose stop` - Detiene todos los servicios pero no elimina los
    contenedores.

  - `docker compose start` - Reinicia los servicios tras la instrucción
    anterior.

  - `docker compose down` - Detiene todos los servicios y elimina los
    contenedores, esto eliminará también los contenidos de la *BBDD* del
    proyecto.

Como ya hemos visto en las secciones anteriores, hay difencias importantes en
todo el proceso de creación de los contenedores entre los entornos. Pero cabe
destacar que en el **entorno de despliegue** no se compila ningún código ni se
genera ninguna imagen. En su lugar el sistema Docker descargará la última
versión disponible de las imágenes publicadas en este repositorio y las usará
para crear y ejecutar los contenedores. Es el entorno más rápido en establecer
una ejecución local del proyecto.

Por ello, los entornos de **desarrollo** y **producción** necesitan una copia
completa del repositorio, pero el de **despliegue** se puede usar copiando tan
sólo el subdirectorio `build/` y el *script* de selección `deployment.sh`
