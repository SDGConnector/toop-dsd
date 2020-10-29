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
package eu.toop.roa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * The utility class for reading the roa-config.conf file.
 *
 * @author yerlibilgin
 */
public class ROAConfig {
  /**
   * Logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ROAConfig.class);

  private static final String roaVersion;
  private static final String buildDate;

  private static final String ROA_CONFIG_RESOURCE_NAME = "/roa-config.conf";

  static {
    //check if the file toop-commander.conf exists, and load it,
    //otherwise go for classpath resource
    String pathName = "roa-config.conf";

    LOGGER.info("Loading config from the \"" + pathName);
    Config config = ConfigFactory.parseURL(
        ROAConfig.class.getResource(ROA_CONFIG_RESOURCE_NAME)).
        withFallback(ConfigFactory.systemProperties()).resolve();

    roaVersion = config.getString("roa.version");
    buildDate = config.getString("roa.buildDate");

    LOGGER.info("--------- RUNNING ROA-" + roaVersion + " ---------");
  }

  /**
   * Gets roa version.
   *
   * @return the roa version
   */
  public static String getRoaVersion() {
    return roaVersion;
  }


  /**
   * Gets build date.
   *
   * @return the build date
   */
  public static String getBuildDate() {
    return buildDate;
  }
}
