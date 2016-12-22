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

import com.acmutv.botnet.config.serial.AppConfigurationJsonMapper;
import com.acmutv.botnet.config.serial.AppConfigurationYamlMapper;
import com.acmutv.botnet.tool.io.IOManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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
   * The singleton of {@link AppConfiguration}.
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
   * Returns the default configuration.
   */
  public static AppConfiguration fromDefault() {
    LOGGER.traceEntry();
    final AppConfiguration config = new AppConfiguration();
    return LOGGER.traceExit(config);
  }

  /**
   * Deserializes {@link AppConfiguration} from JSON.
   * @param in the stream providing a JSON.
   * @return the parsed configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration fromJson(final InputStream in) throws IOException {
    LOGGER.traceEntry("in={}", in);
    final ObjectMapper mapper = new AppConfigurationJsonMapper();
    final AppConfiguration config = mapper.readValue(in, AppConfiguration.class);
    return LOGGER.traceExit(config);
  }

  /**
   * Deserializes {@link AppConfiguration} from a resource providing a JSON.
   * @param resource the resource providing a JSON.
   * @return the deserialized configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration fromJsonResource(final String resource) throws IOException {
    LOGGER.traceEntry("resource={}", resource);
    AppConfiguration config;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      config = fromJson(in);
    }
    return LOGGER.traceExit(config);
  }

  /**
   * Deserializes {@link AppConfiguration} from YAML.
   * @param in the stream providing a YAML.
   * @return the deserialized configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration fromYaml(final InputStream in) throws IOException {
    LOGGER.traceEntry("in={}", in);
    final YAMLMapper mapper = new AppConfigurationYamlMapper();
    final AppConfiguration config = mapper.readValue(in, AppConfiguration.class);
    return LOGGER.traceExit(config);
  }

  /**
   * Deserializes {@link AppConfiguration} from a resource providing a YAML.
   * @param resource the resource providing a YAML.
   * @return the deserialized configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration fromYamlResource(final String resource) throws IOException {
    LOGGER.traceEntry("resource={}", resource);
    AppConfiguration config;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      config = fromYaml(in);
    }
    return LOGGER.traceExit(config);
  }

  /**
   * Loads the default configuration.
   */
  public static void loadDefault() {
    LOGGER.traceEntry();
    getConfigurations().toDefault();
  }

  /**
   * Loads the configuration specified in a JSON file.
   * @param in the JSON configuration file.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void loadJson(final InputStream in) throws IOException {
    LOGGER.traceEntry("in={}", in);
    final AppConfiguration config = fromJson(in);
    getConfigurations().copy(config);
  }

  /**
   * Loads {@link AppConfiguration} from a resource providing a JSON.
   * @param resource the resource providing a JSON.
   * @return the deserialized configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void loadJsonResource(final String resource) throws IOException {
    LOGGER.traceEntry("resources={}", resource);
    final AppConfiguration config = fromJsonResource(resource);
    getConfigurations().copy(config);
  }

  /**
   * Loads the configuration specified in a YAML file.
   * @param in the YAML configuration file.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void loadYaml(final InputStream in) throws IOException {
    LOGGER.traceEntry("in={}", in);
    final AppConfiguration config = fromYaml(in);
    getConfigurations().copy(config);
  }

  /**
   * Loads {@link AppConfiguration} from a resource providing a YAML.
   * @param resource the resource providing a YAML.
   * @return the deserialized configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void loadYamlResource(final String resource) throws IOException {
    LOGGER.traceEntry("resources={}", resource);
    final AppConfiguration config = fromYamlResource(resource);
    getConfigurations().copy(config);
  }

}