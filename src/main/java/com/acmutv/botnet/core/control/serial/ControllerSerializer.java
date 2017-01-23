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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * The JSON serializer for {@link Controller}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Controller
 * @see ControllerDeserializer
 */
public class ControllerSerializer extends StdSerializer<Controller> {

  /**
   * The singleton of {@link ControllerSerializer}.
   */
  private static ControllerSerializer instance;

  /**
   * Returns the singleton of {@link ControllerSerializer}.
   * @return the singleton.
   */
  public static ControllerSerializer getInstance() {
    if (instance == null) {
      instance = new ControllerSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link ControllerSerializer}.
   */
  private ControllerSerializer() {
    super((Class<Controller>) null);
  }

  @Override
  public void serialize(Controller value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    final String init = value.getInitResource();
    final String cmd = value.getCmdResource();
    final String log = value.getLogResource();
    final Interval polling = value.getPolling();
    final Long reconnections = value.getReconnections();
    final Interval reconnectionWait = value.getReconnectionWait();
    final HttpProxy proxy = value.getProxy();
    final String sleep = value.getSleep();
    final Map<String,String> authentication = value.getAuthentication();

    gen.writeStringField("init", init);
    gen.writeStringField("command", cmd);
    gen.writeStringField("log", log);

    if (polling != null) {
      gen.writeStringField("polling", polling.toString());
    }

    if (reconnections != null) {
      gen.writeNumberField("reconnections", reconnections);
    }

    if (reconnectionWait != null) {
      gen.writeStringField("reconnectionWait", reconnectionWait.toString());
    }

    if (proxy != null) {
      gen.writeStringField("proxy", proxy.toCompactString());
    }

    if (sleep != null) {
      gen.writeStringField("sleep", sleep);
    }

    if (authentication != null) {
      gen.writeObjectFieldStart("authentication");
      for (Map.Entry<String,String> property : authentication.entrySet()) {
        gen.writeStringField(property.getKey(), property.getValue());
      }
      gen.writeEndObject();
    }

    gen.writeEndObject();

  }
}