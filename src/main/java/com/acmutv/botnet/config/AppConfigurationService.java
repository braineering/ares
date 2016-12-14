/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani and Michele Porretta

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.botnet.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;

/**
 * This class realizes the app configuration services. *
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 */
public class AppConfigurationService {

  private static final Logger LOGGER = LogManager.getLogger(AppConfiguration.class);

  private static AppConfiguration appConfig;

  public static synchronized AppConfiguration getConfigurations() {
    if (appConfig == null) {
      synchronized (AppConfigurationService.class) {
        if (appConfig == null) {
          appConfig = new AppConfiguration();
        }
      }
    }
    return appConfig;
  }

  /**
   * Loads the configuration specified in a YAML file.
   *
   * @param configPath the path to a YAML configuration file.
   */
  public static void loadYaml(final String configPath) {
    LOGGER.traceEntry("configPath={}", configPath);
    final AppConfiguration config = fromYaml(configPath);
    getConfigurations().copy(config);
  }

  /**
   * Parses an app configuration model from the specified YAML file.
   * If something goes wrong, the default configuration is loaded.
   *
   * @param path the absolute path to the configuration file.
   * @return the configuration.
   */
  public static AppConfiguration fromYaml(final String path) {
    final Yaml yaml = new Yaml(AppConfigurationYaml.getInstance());

    AppConfiguration config = null;
    try {
      FileReader file = new FileReader(path);
      config = yaml.loadAs(file, AppConfiguration.class);
    } catch (Exception e) {
      LOGGER.trace(e.getMessage());
    }

    if (config == null) {
      LOGGER.warn("Cannot parse custom YAML configuration, loading defaults");
      config = new AppConfiguration();
    }

    return config;
  }

}