#!/bin/bash
export USRID=$(id -u)
export GRPID=$(id -g)

source .env
shopt -s extglob
D=${DIRECCION#http?(s)://}
export DOMINIO=${D%%/*}

docker compose down
[ -x docker-compose.override.yml ] && rm docker-compose.override.yml
ln -s docker-compose.dev.yml  docker-compose.override.yml
docker compose up
