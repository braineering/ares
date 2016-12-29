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

import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;

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
          final HttpMethod httpMethod = (HttpMethod) value.getParams().get("method");
          @SuppressWarnings("unchecked") final List<HttpTarget> httpTargets = (List<HttpTarget>) value.getParams().get("targets");
          final HttpProxy httpProxy = (HttpProxy) value.getParams().get("proxy");
          final Interval httpDelay = (Interval) value.getParams().get("delay");

          gen.writeStringField("method", httpMethod.name());

          gen.writeArrayFieldStart("targets");
          for (HttpTarget target : httpTargets) {
            gen.writeStartObject();
            gen.writeStringField("url", target.getUrl().toString());
            gen.writeNumberField("maxAttempts", target.getMaxAttempts());
            gen.writeStringField("period", target.getPeriod().toString());
            if (target.getProxy() != null) {
              gen.writeStringField("proxy", target.getProxy().toCompactString());
            }
            gen.writeEndObject();
          }
          gen.writeEndArray();

          if (httpProxy != null) {
            gen.writeStringField("proxy", httpProxy.toCompactString());
          }

          if (httpDelay != null) {
            gen.writeStringField("delay", httpDelay.toString());
          }

          break;

        case CALMDOWN:
          final Interval calmdownDelay = (Interval) value.getParams().get("delay");

          if (calmdownDelay != null) {
            gen.writeStringField("delay", calmdownDelay.toString());
          }

          break;

        case KILL:
          final Interval killTimeout = (Interval) value.getParams().get("timeout");
          final Interval killDelay = (Interval) value.getParams().get("delay");

          if (killTimeout != null) {
            gen.writeStringField("timeout", killTimeout.toString());
          }

          if (killDelay != null) {
            gen.writeStringField("delay", killDelay.toString());
          }

          break;

        case RESTART:
          final String resource = value.getParams().get("resource").toString();
          final Interval restartDelay = (Interval) value.getParams().get("delay");

          gen.writeStringField("resource", resource);

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

          gen.writeStringField("timeout", sleepTimeout.toString());

          if (sleepDelay != null) {
            gen.writeStringField("delay", sleepDelay.toString());
          }

          break;

        default:
          break;
      }
    }
    gen.writeEndObject();
  }
}