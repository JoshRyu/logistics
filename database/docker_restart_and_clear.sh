#!/bin/bash

docker-compose down

docker volume rm database_PGDATA

docker-compose up -d