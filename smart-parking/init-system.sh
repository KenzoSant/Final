#!/usr/bin/bash

# Verifica se o arquivo .env já existe
if [ ! -f .env ]; then
  cp .env-example .env
fi

cp src/main/resources/application.properties.example src/main/resources/application.properties  

docker network create smart_parking
docker compose down && docker compose build && docker compose up -d

