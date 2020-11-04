#!/usr/bin/env bash
#
# Copyright (C) 2018-2020 toop.eu
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#




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
