/**
 * Copyright (C) 2018-2020 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.roa.controller;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dagger.Component;
import io.jooby.Environment;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * The utility class for reading the roa-config.conf file.
 *
 * @author yerlibilgin
 */

public class RoaConfig {
  /**
   * Logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(RoaConfig.class);

  private String roaVersion;
  private String buildDate;

  @Inject
  public RoaConfig() {
    Config config = ConfigFactory.load().
        withFallback(ConfigFactory.systemEnvironment()).
        withFallback(ConfigFactory.systemProperties()).
        resolve();

    roaVersion = config.getString("roa.version");
    buildDate = config.getString("roa.buildDate");

    LOGGER.info("--------- RUNNING ROA-" + roaVersion + " ---------");
  }

  /**
   * Gets roa version.
   *
   * @return the roa version
   */
  public String getRoaVersion() {
    return roaVersion;
  }


  /**
   * Gets build date.
   *
   * @return the build date
   */
  public String getBuildDate() {
    return buildDate;
  }
}
