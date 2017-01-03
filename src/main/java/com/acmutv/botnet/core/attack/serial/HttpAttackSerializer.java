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

package com.acmutv.botnet.core.attack.serial;

import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * The JSON serializer for {@link HttpAttack}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpAttack
 * @see HttpAttackDeserializer
 */
public class HttpAttackSerializer extends StdSerializer<HttpAttack> {

  /**
   * The singleton of {@link HttpAttackSerializer}.
   */
  private static HttpAttackSerializer instance;

  /**
   * Returns the singleton of {@link HttpAttackSerializer}.
   * @return the singleton.
   */
  public static HttpAttackSerializer getInstance() {
    if (instance == null) {
      instance = new HttpAttackSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link HttpAttackSerializer}.
   */
  private HttpAttackSerializer() {
    super((Class<HttpAttack>) null);
  }

  @Override
  public void serialize(HttpAttack value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    final HttpMethod method = value.getMethod();
    gen.writeStringField("method", method.name());

    final URL target = value.getTarget();
    gen.writeStringField("target", target.toString());

    final HttpProxy proxy = value.getProxy();
    if (proxy == null) {
      gen.writeStringField("proxy", null);
    } else {
      gen.writeStringField("proxy", proxy.toCompactString());
    }

    final Map<String,String> properties = value.getProperties();
    gen.writeObjectFieldStart("properties");
    for (Map.Entry<String,String> property : properties.entrySet()) {
      gen.writeStringField(property.getKey(), property.getValue());
    }
    gen.writeEndObject();

    final int executions = value.getExecutions();
    gen.writeNumberField("executions", executions);

    final Interval period = value.getPeriod();
    if (period == null) {
      gen.writeStringField("period", null);
    } else {
      gen.writeStringField("period", period.toString());
    }

    gen.writeEndObject();
  }
}