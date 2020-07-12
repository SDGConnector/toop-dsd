#!/usr/bin/env bash


moduleVersion=2.0.0-SNAPSHOT

mvn clean package

docker build -t toop/roa-service:${moduleVersion} .
docker build -t toop/roa-service:latest .
