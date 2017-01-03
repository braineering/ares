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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    final AppConfiguration config = new AppConfiguration();
    return LOGGER.traceExit(config);
  }

  /**
   * Deserializes {@link AppConfiguration} from an input.
   * @param format the serialization format.
   * @param resource the resource providing the specified serialization.
   * @return the parsed configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration from(final AppConfigurationFormat format, final String resource) throws IOException {
    AppConfiguration config;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      config = from(format, in);
    }
    return config;
  }

  /**
   * Deserializes {@link AppConfiguration} from a stream.
   * @param format the serialization format.
   * @param in the stream providing the serialization.
   * @return the parsed configuration.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static AppConfiguration from(final AppConfigurationFormat format, final InputStream in) throws IOException {
    ObjectMapper mapper;
    if (format.equals(AppConfigurationFormat.JSON)) {
      mapper = new AppConfigurationJsonMapper();
    } else if (format.equals(AppConfigurationFormat.YAML)) {
      mapper = new AppConfigurationYamlMapper();
    } else {
      throw new IOException("Unsupported serialization format");
    }

    AppConfiguration config = mapper.readValue(in, AppConfiguration.class);

    return LOGGER.traceExit(config);
  }

  /**
   * Loads {@link AppConfiguration} from a resource.
   * @param format the serialization format.
   * @param resource the resource providing the serialization.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void load(final AppConfigurationFormat format, final String resource) throws IOException {
    try (InputStream in = IOManager.getInputStream(resource)) {
      load(format, in);
    }
  }

  /**
   * Loads {@link AppConfiguration} from a stream.
   * @param format the serialization format.
   * @param in the stream providing the serialization.
   * @throws IOException if {@link AppConfiguration} cannot be deserialized.
   */
  public static void load(final AppConfigurationFormat format, final InputStream in) throws IOException {
    final AppConfiguration config = from(format, in);
    getConfigurations().copy(config);
  }

  /**
   * Loads the default configuration.
   */
  public static void loadDefault() {
    getConfigurations().toDefault();
  }

  /**
   * Stores the current configuration.
   * @param format the serialization format.
   * @param resource the path to save to.
   * @throws IOException when configuration cannot be stored.
   */
  public static void store(AppConfigurationFormat format, String resource) throws IOException {
    final AppConfiguration config = getConfigurations();
    try (OutputStream out = IOManager.getOutputStream(resource)) {
      to(format, out, config);
    }
  }

  /**
   * Serialize the specified configuration to the specified path in the specified format.
   * @param format the serialization format.
   * @param resource the path to save to.
   * @param config the configuration to save.
   * @throws IOException when configuration cannot be serialized.
   */
  public static void to(AppConfigurationFormat format, String resource, AppConfiguration config) throws IOException {
    try (OutputStream out = IOManager.getOutputStream(resource))  {
      to(format, out, config);
    }
  }

  /**
   * Serialize the specified configuration to the specified stream in the specified format.
   * @param format the serialization format.
   * @param out the stream to save to.
   * @param config the configuration to save.
   * @throws IOException when configuration cannot be serialized.
   */
  public static void to(AppConfigurationFormat format, OutputStream out, AppConfiguration config) throws IOException {
    ObjectMapper mapper;
    if (format.equals(AppConfigurationFormat.JSON)) {
      mapper = new AppConfigurationJsonMapper();
    } else if (format.equals(AppConfigurationFormat.YAML)) {
      mapper = new AppConfigurationYamlMapper();
    } else {
      throw new IOException("Unsupported serialization format");
    }

    mapper.writeValue(out, config);
  }
}