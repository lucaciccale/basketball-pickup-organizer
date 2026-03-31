#!/usr/bin/env bash

if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
fi

if [ "$1" = clean ]; then
    ./mvnw clean install
fi

./mvnw spring-boot:run
