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

package com.acmutv.botnet.config.serial;

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.*;

/**
 * The JSON deserializer for {@link AppConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 * @see AppConfigurationSerializer
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
    super((Class<AppConfiguration>) null);
  }


  @Override
  public AppConfiguration deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    AppConfiguration config = new AppConfiguration();
    JsonNode node = parser.getCodec().readTree(parser);

    if (node.hasNonNull("cnfInfo")) {
      final boolean cnfInfo = node.get("cnfInfo").asBoolean();
      config.setCnfInfo(cnfInfo);
    }

    if (node.hasNonNull("tgtInfo")) {
      final boolean tgtInfo = node.get("tgtInfo").asBoolean();
      config.setTgtInfo(tgtInfo);
    }

    if (node.hasNonNull("sysInfo")) {
      final boolean sysInfo = node.get("sysInfo").asBoolean();
      config.setSysInfo(sysInfo);
    }

    if (node.hasNonNull("netInfo")) {
      final boolean netInfo = node.get("netInfo").asBoolean();
      config.setNetInfo(netInfo);
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

    if (node.hasNonNull("sleep")) {
      final String sleep = node.get("sleep").asText();
      config.setSleep(sleep);
    }

    if (node.hasNonNull("authentication")) {
      Map<String,String> authentication = new HashMap<>();
      node.get("authentication").fields().forEachRemaining(f -> authentication.put(f.getKey(), f.getValue().asText()));
      config.setAuthentication(authentication);
    }

    if (node.hasNonNull("controllers")) {
      List<Controller> controllers = new ArrayList<>();
      Iterator<JsonNode> iter = node.get("controllers").elements();
      while (iter.hasNext()) {
        JsonNode n = iter.next();
        Controller controller = ctx.readValue(n.traverse(parser.getCodec()), Controller.class);
        controllers.add(controller);
      }
      config.setControllers(controllers);
    }

    return config;
  }
}
