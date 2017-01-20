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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The JSON serializer for {@link AppConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 * @see AppConfigurationDeserializer
 */
public class AppConfigurationSerializer extends StdSerializer<AppConfiguration> {

  /**
   * The singleton of {@link AppConfigurationSerializer}.
   */
  private static AppConfigurationSerializer instance;

  /**
   * Returns the singleton of {@link AppConfigurationSerializer}.
   * @return the singleton.
   */
  public static AppConfigurationSerializer getInstance() {
    if (instance == null) {
      instance = new AppConfigurationSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link AppConfigurationSerializer}.
   */
  private AppConfigurationSerializer() {
    super((Class<AppConfiguration>) null);
  }

  @Override
  public void serialize(AppConfiguration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    final boolean cnfInfo = value.isCnfInfo();
    gen.writeBooleanField("cnfInfo", cnfInfo);

    final boolean tgtInfo = value.isTgtInfo();
    gen.writeBooleanField("tgtInfo", tgtInfo);

    final boolean sysInfo = value.isSysInfo();
    gen.writeBooleanField("sysInfo", sysInfo);

    final boolean netInfo = value.isNetInfo();
    gen.writeBooleanField("netInfo", netInfo);

    final Interval polling = value.getPolling();
    gen.writeStringField("polling", polling.toString());

    final Long reconnections = value.getReconnections();
    gen.writeNumberField("reconnections", reconnections);

    final Interval reconnectionWait = value.getReconnectionWait();
    gen.writeStringField("reconnectionWait", reconnectionWait.toString());

    final HttpProxy proxy = value.getProxy();
    gen.writeStringField("proxy", (proxy != null) ? proxy.toCompactString() : "none");

    final String sleep = value.getSleep();
    gen.writeStringField("sleep", sleep);

    final Map<String,String> authentication = value.getAuthentication();
    if (authentication != null) {
      gen.writeObjectFieldStart("authentication");
      for (Map.Entry<String,String> property : authentication.entrySet()) {
        gen.writeStringField(property.getKey(), property.getValue());
      }
      gen.writeEndObject();
    }

    final List<Controller> controllers = value.getControllers();
    gen.writeArrayFieldStart("controllers");
    for (Controller controller : controllers) {
      provider.findValueSerializer(Controller.class).serialize(controller, gen, provider);
    }
    gen.writeEndArray();

    gen.writeEndObject();
  }

}