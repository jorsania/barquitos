#!/bin/bash
. build/config.dvlp
. build/mail.dvlp
. build/setenv.sh
[ -h docker-compose.yml ] && rm docker-compose.yml
ln -s build/docker-compose.dvlp.yml docker-compose.yml

echo ""
echo "Se ha configurado el entorno para DESARROLLO:"
echo ""
. build/outenv.sh
