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

package com.acmutv.botnet.core.attack.serial;

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.config.serial.AppConfigurationSerializer;
import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.quartz.CronExpression;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * The JSON deserializer for {@link HttpAttack}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 * @see HttpAttack
 * @see HttpAttackSerializer
 */
public class HttpAttackDeserializer extends StdDeserializer<HttpAttack> {

  /**
   * The singleton of {@link HttpAttackDeserializer}.
   */
  private static HttpAttackDeserializer instance;

  /**
   * Returns the singleton of {@link HttpAttackDeserializer}.
   * @return the singleton.
   */
  public static HttpAttackDeserializer getInstance() {
    if (instance == null) {
      instance = new HttpAttackDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link HttpAttackDeserializer}.
   */
  private HttpAttackDeserializer() {
    super((Class<HttpAttack>) null);
  }


  @Override
  public HttpAttack deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if (!node.hasNonNull("method") || !node.hasNonNull("target")) {
      throw new IOException("[method,target] required");
    }

    final HttpMethod method = HttpMethod.valueOf(node.get("method").asText());
    final URL target = new URL(node.get("target").asText());

    HttpAttack attack = new HttpAttack(method, target);

    if (node.has("proxy")) {
      final HttpProxy proxy = HttpProxy.valueOf(node.get("proxy").asText());
      attack.setProxy(proxy);
    }

    if (node.hasNonNull("properties")) {
      Map<String,String> properties = new HashMap<>();
      node.get("properties").fields().forEachRemaining(f -> {
        properties.put(f.getKey(), f.getValue().asText());
      });
      attack.setProperties(properties);
    }

    if (node.hasNonNull("executions")) {
      final int executions = node.get("executions").asInt();
      attack.setExecutions(executions);
    }

    if (node.hasNonNull("period")) {
      final Interval period = Interval.valueOf(node.get("period").asText());
      attack.setPeriod(period);
    }

    return attack;
  }
}
