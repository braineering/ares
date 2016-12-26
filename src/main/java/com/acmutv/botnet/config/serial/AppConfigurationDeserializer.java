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
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class realizes the JSON deserializer for {@link AppConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 */
public class AppConfigurationDeserializer extends StdDeserializer<AppConfiguration> {

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
    super((Class<AppConfiguration>)null);
  }

  @Override
  public AppConfiguration deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    AppConfiguration config = new AppConfiguration();
    JsonNode node = parser.getCodec().readTree(parser);

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
      final Duration sampling = Duration.valueOf(node.get("sampling").asText());
      config.setSampling(sampling);
    }

    if (node.has("controllers")) {
      final List<Controller> controllers = parseControllers(node.get("controllers"));
      config.setControllers(controllers);
    }

    if (node.has("polling")) {
      final Interval polling = Interval.valueOf(node.get("polling").asText());
      config.setPolling(polling);
    }
    return config;
  }

  /**
   * Parses a list of {@link Controller} from a JSON node.
   * @param node the JSON node to parse.
   * @return the parsed list of {@link Controller}.
   */
  private static List<Controller> parseControllers(JsonNode node) {
    List<Controller> controllers = new ArrayList<>();
    Iterator<JsonNode> iter = node.elements();
    while (iter.hasNext()) {
      JsonNode n = iter.next();
      if (!n.hasNonNull("init") ||
          !n.hasNonNull("command") ||
          !n.hasNonNull("log")) {
        continue;
      }
      final String initResource = TemplateEngine.getInstance().replace(
          n.get("init").asText(null)
      );
      final String commandResource = TemplateEngine.getInstance().replace(
          n.get("command").asText(null)
      );
      final String logResource = TemplateEngine.getInstance().replace(
          n.get("log").asText(null)
      );

      Controller controller = new Controller(initResource, commandResource, logResource);
      controllers.add(controller);
    }
    return controllers;
  }
}
