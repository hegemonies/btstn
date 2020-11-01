#!/bin/bash

## Build image
docker build --tag btstn/frontend:dev --file docker/Dockerfile .

## Clean after building
docker system prune --volumes -f
