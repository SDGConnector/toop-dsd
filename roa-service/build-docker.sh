#!/usr/bin/env bash



#check if we have a build done
if ! ls target/*bundle.jar 1> /dev/null 2>&1; then
  echo "Building project first"

  if ! mvn verify; then
    echo "Build failed. Exiting"
    exit 2
  fi
fi


#Read module version from the conf file after maven build.
moduleVersion=$(grep version target/classes/roa-config.conf)
moduleVersion=${moduleVersion#*\"}
moduleVersion=${moduleVersion%\"}
echo "Building docker image ROA toop/roa-service:${moduleVersion}"

#build docker images
docker build --build-arg ROA_VERSION="$moduleVersion" -t toop/roa-service:"${moduleVersion}" .
docker tag toop/roa-service:"${moduleVersion}" toop/roa-service:latest
