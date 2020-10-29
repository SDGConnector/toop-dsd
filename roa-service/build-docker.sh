#!/usr/bin/env bash



if [ ! -d "target/classes" ]; then
  echo "Building project first"

  if ! mvn verify; then
    echo "Build failed. Exiting"
    exit 2
  fi
fi


moduleVersion=$(cat target/classes/roa-config.conf | grep version)

moduleVersion=${moduleVersion#*\"}
moduleVersion=${moduleVersion%\"}
echo "$moduleVersion"

docker build --build-arg ROA_VERSION="$moduleVersion" -t toop/roa-service:"${moduleVersion}" .
docker tag toop/roa-service:"${moduleVersion}" toop/roa-service:latest
