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

package com.acmutv.botnet.core.attack.flooding.serial;

import com.acmutv.botnet.core.attack.flooding.HttpFloodAttack;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * The JSON deserializer for {@link HttpFloodAttack}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpFloodAttack
 * @see HttpFloodAttackSerializer
 */
public class HttpFloodAttackDeserializer extends StdDeserializer<HttpFloodAttack> {

  /**
   * The singleton of {@link HttpFloodAttackDeserializer}.
   */
  private static HttpFloodAttackDeserializer instance;

  /**
   * Returns the singleton of {@link HttpFloodAttackDeserializer}.
   * @return the singleton.
   */
  public static HttpFloodAttackDeserializer getInstance() {
    if (instance == null) {
      instance = new HttpFloodAttackDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link HttpFloodAttackDeserializer}.
   */
  private HttpFloodAttackDeserializer() {
    super((Class<HttpFloodAttack>) null);
  }


  @Override
  public HttpFloodAttack deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if (!node.hasNonNull("method") || !node.hasNonNull("target")) {
      throw new IOException("[method,target] required");
    }
    final HttpMethod method = HttpMethod.valueOf(node.get("method").asText());
    final URL target = new URL(node.get("target").asText());

    HttpFloodAttack attack = new HttpFloodAttack(method, target);

    if (node.has("proxy")) {
      final HttpProxy proxy = HttpProxy.valueOf(node.get("proxy").asText());
      attack.setProxy(proxy);
    }

    if (node.hasNonNull("executions")) {
      final int executions = node.get("executions").asInt();
      attack.setExecutions(executions);
    }

    if (node.hasNonNull("period")) {
      final Interval period = Interval.valueOf(node.get("period").asText());
      attack.setPeriod(period);
    }

    if (node.hasNonNull("header")) {
      Map<String,String> header = new HashMap<>();
      node.get("header").fields().forEachRemaining(f -> header.put(f.getKey(), f.getValue().asText()));
      attack.setHeader(header);
    }

    if (node.hasNonNull("params")) {
      Map<String,String> params = new HashMap<>();
      node.get("params").fields().forEachRemaining(f -> params.put(f.getKey(), f.getValue().asText()));
      attack.setParams(params);
    }

    return attack;
  }
}
