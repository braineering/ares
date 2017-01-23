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

package com.acmutv.botnet.core.control.serial;

import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The JSON deserializer for {@link Controller}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Controller
 * @see ControllerSerializer
 */
public class ControllerDeserializer extends StdDeserializer<Controller> {

  /**
   * The singleton of {@link ControllerDeserializer}.
   */
  private static ControllerDeserializer instance;

  /**
   * Returns the singleton of {@link ControllerDeserializer}.
   * @return the singleton.
   */
  public static ControllerDeserializer getInstance() {
    if (instance == null) {
      instance = new ControllerDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link ControllerDeserializer}.
   */
  private ControllerDeserializer() {
    super((Class<Controller>) null);
  }


  @Override
  public Controller deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if (!node.hasNonNull("init") ||
        !node.hasNonNull("command") ||
        !node.hasNonNull("log")) {
      throw new IOException("[init,command,log] must be there");
    }

    final String initResource = node.get("init").asText();

    final String commandResource = node.get("command").asText();

    final String logResource = node.get("log").asText();

    Controller controller = new Controller(initResource, commandResource, logResource);

    if (node.hasNonNull("polling")) {
      final Interval polling = Interval.valueOf(node.get("polling").asText());
      controller.setPolling(polling);
    }

    if (node.hasNonNull("reconnections")) {
      final Long reconnections = node.get("reconnections").asLong();
      controller.setReconnections(reconnections);
    }

    if (node.hasNonNull("reconnectionWait")) {
      final Interval reconnectionWait = Interval.valueOf(node.get("reconnectionWait").asText());
      controller.setReconnectionWait(reconnectionWait);
    }

    if (node.hasNonNull("proxy")) {
      final HttpProxy proxy = HttpProxy.valueOf(node.get("proxy").asText());
      controller.setProxy(proxy);
    }

    if (node.hasNonNull("sleep")) {
      final String sleep = node.get("sleep").asText();
      controller.setSleep(sleep);
    }

    if (node.hasNonNull("authentication")) {
      Map<String,String> authentication = new HashMap<>();
      node.get("authentication").fields().forEachRemaining(f -> authentication.put(f.getKey(), f.getValue().asText()));
      controller.setAuthentication(authentication);
    }

    return controller;
  }
}
