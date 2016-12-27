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
import com.acmutv.botnet.tool.net.HttpProxy;
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

  private static AppConfiguration DEFAULT;

  /**
   * Initializes {@link AppConfigurationDeserializer} with no default deserialization.
   */
  public AppConfigurationDeserializer() {
    super((Class<AppConfiguration>)null);
    this.DEFAULT = new AppConfiguration();
  }

  /**
   * Initializes {@link AppConfigurationDeserializer} with default deserialization.
   */
  public AppConfigurationDeserializer(AppConfiguration defaultConfig) {
    this();
    if (defaultConfig != null) {
      this.DEFAULT = defaultConfig;
    }
  }

  @Override
  public AppConfiguration deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    AppConfiguration config = new AppConfiguration(this.DEFAULT);
    JsonNode node = parser.getCodec().readTree(parser);

    if (node.hasNonNull("sysInfo")) {
      final boolean sysInfo = node.get("sysInfo").asBoolean();
      config.setSysInfo(sysInfo);
    }

    if (node.hasNonNull("netInfo")) {
      final boolean netInfo = node.get("netInfo").asBoolean();
      config.setNetInfo(netInfo);
    }

    if (node.hasNonNull("sysStat")) {
      final boolean sysStat = node.get("sysStat").asBoolean();
      config.setSysStat(sysStat);
    }

    if (node.hasNonNull("netStat")) {
      final boolean netStat = node.get("netStat").asBoolean();
      config.setNetStat(netStat);
    }

    if (node.hasNonNull("sampling")) {
      final Duration sampling = Duration.valueOf(node.get("sampling").asText());
      config.setSampling(sampling);
    }

    if (node.hasNonNull("polling")) {
      final Interval polling = Interval.valueOf(node.get("polling").asText());
      config.setPolling(polling);
    }

    if (node.hasNonNull("reconnections")) {
      final Long reconnections = (node.get("reconnections").asLong() >= 0) ?
          node.get("reconnections").asLong()
          :
          Long.MAX_VALUE;
      config.setReconnections(reconnections);
    }

    if (node.hasNonNull("reconnectionWait")) {
      final Interval reconnectionWait = Interval.valueOf(node.get("reconnectionWait").asText());
      config.setReconnectionWait(reconnectionWait);
    }

    if (node.hasNonNull("proxy")) {
      final HttpProxy proxy = HttpProxy.valueOf(node.get("proxy").asText());
      config.setProxy(proxy);
    }

    if (node.hasNonNull("controllers")) {
      final List<Controller> controllers = parseControllers(node.get("controllers"), config);
      if (!controllers.isEmpty()) config.setControllers(controllers);
    }
    return config;
  }

  /**
   * Parses a list of {@link Controller} from a JSON node.
   * @param node the JSON node to parse.
   * @param config the default configuration.
   * @return the parsed list of {@link Controller}.
   */
  private static List<Controller> parseControllers(JsonNode node, AppConfiguration config) {
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
          n.get("init").asText()
      );
      final String commandResource = TemplateEngine.getInstance().replace(
          n.get("command").asText()
      );
      final String logResource = TemplateEngine.getInstance().replace(
          n.get("log").asText()
      );

      Interval polling = config.getPolling();
      Long reconnections = config.getReconnections();
      Interval reconnectionWait = config.getReconnectionWait();
      HttpProxy proxy = config.getProxy();

      if (n.hasNonNull("polling")) {
        polling = Interval.valueOf(n.get("polling").asText());
      }

      if (n.hasNonNull("reconnections")) {
        reconnections = n.get("reconnections").asLong();
      }

      if (n.hasNonNull("reconnectionWait")) {
        reconnectionWait = Interval.valueOf(n.get("reconnectionWait").asText());
      }

      if (n.hasNonNull("proxy")) {
        proxy = HttpProxy.valueOf(n.get("proxy").asText());
      }

      Controller controller = new Controller(
          initResource, commandResource, logResource,
          polling, reconnections, reconnectionWait, proxy
          );
      controllers.add(controller);
    }
    return controllers;
  }
}
