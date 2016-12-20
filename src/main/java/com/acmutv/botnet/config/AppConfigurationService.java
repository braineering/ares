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

import com.acmutv.botnet.config.json.AppConfigurationMapper;
import com.acmutv.botnet.config.yaml.AppConfigurationConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * This class realizes the app configuration services.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 */
public class AppConfigurationService {

  private static final Logger LOGGER = LogManager.getLogger(AppConfigurationService.class);

  /**
   * The default configuration filename.
   */
  public static final String DEFAULT_CONFIG_FILENAME = "config.yaml";

  /**
   * The singleton of {@link AppConfiguration} for the whole app.
   */
  private static AppConfiguration appConfig;

  /**
   * Returns the app configuration singleton.
   * @return the app configuration.
   */
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
   * Loads the default configuration.
   */
  public static void loadDefault() {
    LOGGER.traceEntry();
    getConfigurations().toDefault();
  }

  /**
   * Loads the configuration specified in a YAML file.
   * @param in the YAML configuration file.
   */
  public static void loadYaml(final InputStream in) {
    LOGGER.traceEntry("in={}", in);
    final AppConfiguration config = fromYaml(in);
    getConfigurations().copy(config);
  }

  /**
   * Loads the configuration specified in a JSON file.
   * @param in the JSON configuration file.
   */
  public static void loadJson(final InputStream in) {
    LOGGER.traceEntry("in={}", in);
    final AppConfiguration config = fromJson(in);
    getConfigurations().copy(config);
  }

  /**
   * Returns the default configuration.
   */
  public static AppConfiguration fromDefault() {
    LOGGER.traceEntry();
    final AppConfiguration config = new AppConfiguration();
    return LOGGER.traceExit(config);
  }

  /**
   * Parses an app configuration model from the specified YAML file.
   * If something goes wrong, the default configuration is loaded.
   * @param in the YAML configuration file.
   * @return the configuration.
   */
  public static AppConfiguration fromYaml(final InputStream in) {
    LOGGER.traceEntry("in={}", in);
    final Yaml yaml = new Yaml(AppConfigurationConstructor.getInstance());

    AppConfiguration config = null;
    try {
      config = yaml.loadAs(in, AppConfiguration.class);
    } catch (Exception exc) {
      LOGGER.warn("Cannot parse YAML configuration: {}", exc.getMessage());
    }

    if (config == null) {
      LOGGER.warn("Loading default configuration");
      config = new AppConfiguration();
    }

    return LOGGER.traceExit(config);
  }

  /**
   * Parses an app configuration model from the specified JSON file.
   * If something goes wrong, the default configuration is loaded.
   * @param in the JSON configuration file.
   * @return the configuration.
   */
  public static AppConfiguration fromJson(final InputStream in) {
    LOGGER.traceEntry("in={}", in);
    final AppConfigurationMapper mapper = new AppConfigurationMapper();

    AppConfiguration config = null;
    try {
      config = mapper.readValue(in, AppConfiguration.class);
    } catch (Exception exc) {
      LOGGER.warn("Cannot parse JSON configuration: {}", exc.getMessage());
    }

    if (config == null) {
      LOGGER.warn("Loading default configuration");
      config = new AppConfiguration();
    }

    return LOGGER.traceExit(config);
  }

}