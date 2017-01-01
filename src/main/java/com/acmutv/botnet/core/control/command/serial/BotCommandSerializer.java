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
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class realizes the JSON serializer for {@link BotCommand}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
public class BotCommandSerializer extends StdSerializer<BotCommand> {

  /**
   * The singleton of {@link BotCommandSerializer}.
   */
  private static BotCommandSerializer instance;

  /**
   * Returns the singleton of {@link BotCommandSerializer}.
   * @return the singleton.
   */
  public static BotCommandSerializer getInstance() {
    if (instance == null) {
      instance = new BotCommandSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link BotCommandSerializer}.
   */
  private BotCommandSerializer() {
    super((Class<BotCommand>) null);
  }

  @Override
  public void serialize(BotCommand value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("command", value.getScope().getName());
    if (value.getScope().isWithParams()) {
      switch (value.getScope()) {
        case ATTACK_HTTP:
          @SuppressWarnings("unchecked") final List<HttpAttack> httpAttacks = (List<HttpAttack>) value.getParams().get("attacks");
          final Interval httpDelay = (Interval) value.getParams().get("delay");

          serializeHttpAttacks("attacks", gen, httpAttacks);

          if (httpDelay != null) {
            gen.writeStringField("delay", httpDelay.toString());
          }

          break;

        case CALMDOWN:
          final Boolean calmdownWait = (Boolean) value.getParams().get("wait");
          final Interval calmdownDelay = (Interval) value.getParams().get("delay");

          if (calmdownWait != null) {
            gen.writeBooleanField("wait", calmdownWait);
          }

          if (calmdownDelay != null) {
            gen.writeStringField("delay", calmdownDelay.toString());
          }

          break;

        case KILL:
          final Boolean killWait = (Boolean) value.getParams().get("wait");
          final Interval killDelay = (Interval) value.getParams().get("delay");

          if (killWait != null) {
            gen.writeBooleanField("wait", killWait);
          }

          if (killDelay != null) {
            gen.writeStringField("delay", killDelay.toString());
          }

          break;

        case RESTART:
          final String resource = value.getParams().get("resource").toString();
          final Boolean restartWait = (Boolean) value.getParams().get("wait");
          final Interval restartDelay = (Interval) value.getParams().get("delay");

          gen.writeStringField("resource", resource);

          if (restartWait != null) {
            gen.writeBooleanField("wait", restartWait);
          }

          if (restartDelay != null) {
            gen.writeStringField("delay", restartDelay.toString());
          }

          break;

        case SAVE_CONFIG:
          final Interval saveConfigDelay = (Interval) value.getParams().get("delay");

          if (saveConfigDelay != null) {
            gen.writeStringField("delay", saveConfigDelay.toString());
          }

          break;

        case SLEEP:
          final Interval sleepTimeout = (Interval) value.getParams().get("timeout");
          final Interval sleepDelay = (Interval) value.getParams().get("delay");

          if (sleepTimeout != null) {
            gen.writeStringField("timeout", sleepTimeout.toString());
          }

          if (sleepDelay != null) {
            gen.writeStringField("delay", sleepDelay.toString());
          }

          break;

        case UPDATE:
          final Interval updateDelay = (Interval) value.getParams().get("delay");
          @SuppressWarnings("unchecked") final Map<String,String> settings = (Map<String,String>) value.getParams().get("settings");

          if (settings != null) {
            gen.writeObjectFieldStart("settings");
            for (Map.Entry<String,String> entry : settings.entrySet()) {
              gen.writeStringField(entry.getKey(), entry.getValue());
            }
            gen.writeEndObject();
          }

          if (updateDelay != null) {
            gen.writeStringField("delay", updateDelay.toString());
          }

          break;

        case WAKEUP:
          final Interval wakeupDelay = (Interval) value.getParams().get("delay");

          if (wakeupDelay != null) {
            gen.writeStringField("delay", wakeupDelay.toString());
          }

          break;

        default:
          break;
      }
    }
    gen.writeEndObject();
  }

  /**
   * Serializes a list of {@link HttpAttack}.
   * @param fieldName the field to serialize into.
   * @param gen the generator to serialize with.
   * @param attacks the list of attacks to serialize.
   * @throws IOException when the list of {@link HttpAttack} cannot be serialized.
   */
  private void serializeHttpAttacks(String fieldName, JsonGenerator gen, List<HttpAttack> attacks) throws IOException {
    gen.writeArrayFieldStart(fieldName);
    for (HttpAttack attack : attacks) {
      gen.writeStartObject();
      gen.writeStringField("method", attack.getMethod().name());
      gen.writeStringField("target", attack.getTarget().toString());
      if (attack.getProxy() != null) {
        gen.writeStringField("proxy", attack.getProxy().toCompactString());
      }
      if (attack.getProperties() != null) {
        gen.writeObjectFieldStart("properties");
        List<String> sortedKeys = new ArrayList<>();
        attack.getProperties().keySet().stream().sorted().forEachOrdered(sortedKeys::add);
        for (String k : sortedKeys) {
          gen.writeStringField(k, attack.getProperties().get(k));
        }
        gen.writeEndObject();
      }
      gen.writeNumberField("executions", attack.getExecutions());
      if (attack.getPeriod() != null) {
        gen.writeStringField("period", attack.getPeriod().toString());
      }
      gen.writeEndObject();
    }
    gen.writeEndArray();
  }
}