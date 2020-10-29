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

FROM ubuntu

ARG ROA_VERSION="2.0.0-SNAPSHOT"
ARG JAR_NAME=roa-service-${ROA_VERSION}-bundle.jar

RUN apt-get update && apt-get install openjdk-11-jre -y
RUN apt-get clean && apt-get autoremove

#create app folder
WORKDIR /roa
COPY target/${JAR_NAME} ./

RUN echo "java -jar $JAR_NAME" > run.sh && \
    chmod +x run.sh

CMD ["sh", "-c", "./run.sh"]
