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

package com.acmutv.botnet.core.control;

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.config.serial.AppConfigurationFormat;
import com.acmutv.botnet.config.serial.AppConfigurationJsonMapper;
import com.acmutv.botnet.config.serial.AppConfigurationYamlMapper;
import com.acmutv.botnet.core.control.serial.ControllerPropertiesFormat;
import com.acmutv.botnet.tool.io.IOManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * I/O services for controllers.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Controller
 */
public class ControllerService {

  private static final Logger LOGGER = LogManager.getLogger(ControllerService.class);

  /**
   * Deserializes {@link ControllerProperties} from an input.
   * @param format the serialization format.
   * @param resource the resource providing the specified serialization.
   * @return the parsed configuration.
   * @throws IOException if {@link Map} cannot be deserialized.
   */
  public static ControllerProperties from(final ControllerPropertiesFormat format, final String resource) throws IOException {
    ControllerProperties config;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      config = from(format, in);
    }
    return config;
  }

  /**
   * Deserializes {@link ControllerProperties} from a stream.
   * @param format the serialization format.
   * @param in the stream providing the serialization.
   * @return the parsed configuration.
   * @throws IOException if {@link Map} cannot be deserialized.
   */
  public static ControllerProperties from(final ControllerPropertiesFormat format, final InputStream in) throws IOException {
    ObjectMapper mapper = getMapper(format);
    ControllerProperties config = new ControllerProperties();
    Map<String,String> properties = mapper.readValue(in, new TypeReference<Map<String,String>>() { });
    config.putAll(properties);
    return LOGGER.traceExit(config);
  }

  /**
   * Returns the mapper for the serialization {@code format}.
   * @param format the serialization format.
   * @return the mapper for the serialization format.
   * @throws IOException when the mapper cannot be retrieved due to unavailable {@code format}.
   */
  private static ObjectMapper getMapper(ControllerPropertiesFormat format) throws IOException {
    ObjectMapper mapper;
    if (format.equals(ControllerPropertiesFormat.JSON)) {
      mapper = new ObjectMapper();
    } else {
      throw new IOException("Unsupported serialization format");
    }
    return mapper;
  }
}