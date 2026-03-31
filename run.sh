#!/usr/bin/env bash

if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
fi

if [ "$1" = clean ]; then
    ./mvnw clean install
elif [ "$1" = test ]; then
    ./mvnw test
    exit 0
fi

./mvnw spring-boot:run
