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

import com.acmutv.botnet.config.serial.AppConfigurationFormat;
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
   * Deserializes {@link AppConfiguration} from an input.
   * @param format the serialization format.
   * @param resource the resource providing the specified serialization.
   * @param defaultConfig the default configuration.
   * @return the parsed configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration from(final AppConfigurationFormat format, final String resource, final AppConfiguration defaultConfig) throws IOException {
    LOGGER.traceEntry("resource={}", resource);
    AppConfiguration config;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      config = from(format, in, defaultConfig);
    }
    return LOGGER.traceExit(config);
  }

  /**
   * Deserializes {@link AppConfiguration} from a stream.
   * @param format the serialization format.
   * @param in the stream providing the serialization.
   * @param defaultConfig the default configuration.
   * @return the parsed configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration from(final AppConfigurationFormat format, final InputStream in, final AppConfiguration defaultConfig) throws IOException {
    ObjectMapper mapper;
    if (format.equals(AppConfigurationFormat.JSON)) {
      mapper = new AppConfigurationJsonMapper(defaultConfig);
    } else if (format.equals(AppConfigurationFormat.YAML)) {
      mapper = new AppConfigurationYamlMapper(defaultConfig);
    } else {
      throw new IOException("Unsupported serialization format");
    }

    return mapper.readValue(in, AppConfiguration.class);
  }

  /**
   * Loads {@link AppConfiguration} from a resource.
   * @param format the serialization format.
   * @param resource the resource providing the serialization.
   * @param defaultConfig the default configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void load(final AppConfigurationFormat format, final String resource, final AppConfiguration defaultConfig) throws IOException {
    LOGGER.traceEntry("resource={}", resource);
    final AppConfiguration config = from(format, resource, defaultConfig);
    getConfigurations().copy(config);
  }

  /**
   * Loads {@link AppConfiguration} from a stream.
   * @param format the serialization format.
   * @param in the stream providing the serialization.
   * @param defaultConfig the default configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void load(final AppConfigurationFormat format, final InputStream in, final AppConfiguration defaultConfig) throws IOException {
    final AppConfiguration config = from(format, in, defaultConfig);
    getConfigurations().copy(config);
  }

  /**
   * Loads the default configuration.
   */
  public static void loadDefault() {
    LOGGER.traceEntry();
    getConfigurations().toDefault();
  }
}