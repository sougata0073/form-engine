#!/bin/sh

mvn clean package -DskipTests && touch docker-compose.watch