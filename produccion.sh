#!/bin/bash
docker compose stop
[ -x docker-compose.override.yml ] && rm docker-compose.override.yml
ln -s docker-compose.prod.yml docker-compose.override.yml
docker compose up --build -d