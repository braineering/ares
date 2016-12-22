/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani

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

package com.acmutv.botnet.config.serial;

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.tool.time.Duration;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the JSON deserializer for {@link AppConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 */
public class AppConfigurationDeserializer extends StdDeserializer<AppConfiguration> {

  private static final Logger LOGGER = LogManager.getLogger(AppConfigurationDeserializer.class);

  /**
   * The singleton of {@link AppConfigurationDeserializer}.
   */
  private static AppConfigurationDeserializer instance;

  /**
   * Returns the singleton of {@link AppConfigurationDeserializer}.
   * @return the singleton.
   */
  public static AppConfigurationDeserializer getInstance() {
    if (instance == null) {
      instance = new AppConfigurationDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link AppConfigurationDeserializer}.
   */
  private AppConfigurationDeserializer() {
    super((Class<?>)null);
  }

  @Override
  public AppConfiguration deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    LOGGER.traceEntry();
    AppConfiguration config = new AppConfiguration();
    JsonNode node = parser.getCodec().readTree(parser);
    LOGGER.trace("node={}", node);

    if (node.has("sysInfo")) {
      final boolean sysInfo = node.get("sysInfo").asBoolean();
      config.setSysInfo(sysInfo);
    }

    if (node.has("netInfo")) {
      final boolean netInfo = node.get("netInfo").asBoolean();
      config.setNetInfo(netInfo);
    }

    if (node.has("sysStat")) {
      final boolean sysStat = node.get("sysStat").asBoolean();
      config.setSysStat(sysStat);
    }

    if (node.has("netStat")) {
      final boolean netStat = node.get("netStat").asBoolean();
      config.setNetStat(netStat);
    }

    if (node.has("sampling")) {
      Duration sampling = new Duration();
      JsonNode samplingNode = node.get("sampling");
      if (samplingNode.has("amount")) {
        final long a = samplingNode.get("amount").asLong();
        sampling.setAmount(a);
      }

      if (samplingNode.has("unit")) {
        final TimeUnit unit = TimeUnit.valueOf(samplingNode.get("unit").asText());
        sampling.setUnit(unit);
      }

      config.setSampling(sampling);
    }


    if (node.has("initResource")) {
      final String initResource = TemplateEngine.getInstance().replace(
          node.get("initResource").asText(null)
      );
      config.setInitResource(initResource);
    }

    if (node.has("cmdResource")) {
      final String cmdResource = TemplateEngine.getInstance().replace(
          node.get("cmdResource").asText(null)
      );
      config.setCmdResource(cmdResource);
    }

    if (node.has("logResource")) {
      final String logResource = TemplateEngine.getInstance().replace(
          node.get("logResource").asText(null)
      );
      config.setLogResource(logResource);
    }

    return LOGGER.traceExit(config);
  }
}
