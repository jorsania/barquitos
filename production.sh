#!/bin/bash
. build/config.prod
. build/mail.prod
. build/setenv.sh
[ -h docker-compose.yml ] && rm docker-compose.yml
ln -s build/docker-compose.prod.yml docker-compose.yml

echo ""
echo "Se ha configurado el entorno para PRODUCCIÓN:"
echo ""
. build/outenv.sh
