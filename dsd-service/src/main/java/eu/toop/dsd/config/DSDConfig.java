/**
 * Copyright (C) 2018-2020 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.dsd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * The utility class for reading the dsd-config.conf file.
 *
 * @author yerlibilgin
 */
public class DSDConfig {
  /**
   * Logger instance
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DSDConfig.class);

  /**
   * The TOOP Directory URL
   */
  private static final String toopDirUrl;
  private static final String dsdVersion;
  private static final String buildDate;

  private static final String DSD_CONFIG_RESOURCE_NAME = "/dsd-config.conf";

  static {
    //check if the file toop-commander.conf exists, and load it,
    //otherwise go for classpath resource
    String pathName = "dsd-config.conf";

    LOGGER.info("Loading config from the \"" + pathName);
    Config config = ConfigFactory.parseURL(
        DSDConfig.class.getResource(DSD_CONFIG_RESOURCE_NAME)).
        withFallback(ConfigFactory.systemProperties()).resolve();

    toopDirUrl = config.getString("dsd.toop-dir-url");
    dsdVersion = config.getString("dsd.version");
    buildDate = config.getString("dsd.buildDate");

    LOGGER.info("--------- RUNNING DSD-" + dsdVersion + " ---------");
    LOGGER.debug("toopDirUrl: " + toopDirUrl);
  }


  /**
   * Gets toop dir url.
   *
   * @return the toop dir url
   */
  public static String getToopDirUrl() {
    return toopDirUrl;
  }

  /**
   * Gets dsd version.
   *
   * @return the dsd version
   */
  public static String getDsdVersion() {
    return dsdVersion;
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
