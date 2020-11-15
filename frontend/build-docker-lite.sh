#!/bin/bash

## Build image
docker build --tag btstn/frontend:dev --file docker/Dockerfile.lite .

## Clean after building
docker system prune --volumes -f
