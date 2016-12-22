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

import com.acmutv.botnet.core.command.BotCommand;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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

  private static final Logger LOGGER = LogManager.getLogger(BotCommandSerializer.class);

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
    LOGGER.traceEntry("value={}", value);
    gen.writeStartObject();
    gen.writeStringField("command", value.getScope().getName());
    if (value.getScope().isWithParams()) {
      switch (value.getScope()) {

        case RESTART:
          final String resource = value.getParams().get("resource").toString();
          gen.writeStringField("resource", resource);
          break;

        case UPDATE:
          final Map<String,String> settings = (Map<String,String>) value.getParams().get("settings");
          gen.writeObjectField("settings", settings);
          break;

        case SLEEP:
          final Duration sleepTimeout = (Duration) value.getParams().get("timeout");
          gen.writeStringField("timeout", sleepTimeout.toString());
          break;

        case SHUTDOWN:
          final Duration shutdownTimeout = (Duration) value.getParams().get("timeout");
          gen.writeStringField("timeout", shutdownTimeout.toString());
          break;

        case ATTACK_HTTP:
          final HttpMethod httpMethod = (HttpMethod) value.getParams().get("method");
          final List<HttpTarget> httpTargets = (List<HttpTarget>) value.getParams().get("targets");
          final HttpProxy httpProxy = (HttpProxy) value.getParams().get("proxy");
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
          break;

        default:
          break;
      }
    }
    gen.writeEndObject();
    LOGGER.traceExit();
  }
}