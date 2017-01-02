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

package com.acmutv.botnet.config.serial;

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.quartz.CronExpression;

import java.io.IOException;
import java.util.List;

/**
 * The JSON serializer for {@link AppConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 * @see AppConfigurationDeserializer
 */
public class AppConfigurationSerializer extends StdSerializer<AppConfiguration> {

  /**
   * The singleton of {@link AppConfigurationSerializer}.
   */
  private static AppConfigurationSerializer instance;

  /**
   * Returns the singleton of {@link AppConfigurationSerializer}.
   * @return the singleton.
   */
  public static AppConfigurationSerializer getInstance() {
    if (instance == null) {
      instance = new AppConfigurationSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link AppConfigurationSerializer}.
   */
  private AppConfigurationSerializer() {
    super((Class<AppConfiguration>) null);
  }

  @Override
  public void serialize(AppConfiguration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    final boolean sysInfo = value.isSysInfo();
    gen.writeBooleanField("sysInfo", sysInfo);

    final boolean netInfo = value.isNetInfo();
    gen.writeBooleanField("netInfo", netInfo);

    final boolean sysStat = value.isSysStat();
    gen.writeBooleanField("sysStat", sysStat);

    final boolean netStat = value.isNetStat();
    gen.writeBooleanField("netStat", netStat);

    final Duration sampling = value.getSampling();
    gen.writeStringField("sampling", sampling.toString());

    final Interval polling = value.getPolling();
    gen.writeStringField("polling", polling.toString());

    final Long reconnections = value.getReconnections();
    gen.writeNumberField("reconnections", reconnections);

    final Interval reconnectionWait = value.getReconnectionWait();
    gen.writeStringField("reconnectionWait", reconnectionWait.toString());

    final HttpProxy proxy = value.getProxy();
    gen.writeStringField("proxy", (proxy != null) ? proxy.toCompactString() : "none");

    final String userAgent = value.getUserAgent();
    gen.writeStringField("userAgent", userAgent);

    final CronExpression sleep = value.getSleep();
    if (sleep == null) {
      gen.writeStringField("sleep", null);
    } else {
      gen.writeStringField("sleep", sleep.getCronExpression());
    }

    final List<Controller> controllers = value.getControllers();
    writeArrayController("controllers", controllers, gen);

    gen.writeEndObject();
  }

  /**
   * Serializes the list of `controllers` to the specified `fieldName` with the specified generator `gen`.
   * @param fieldName the filed name to serialize into.
   * @param controllers the list of controllers to serialize.
   * @param gen the generator to serialize with.
   * @throws IOException when serialization cannot be executed.
   */
  private void writeArrayController(String fieldName, List<Controller> controllers, JsonGenerator gen) throws IOException {
    gen.writeArrayFieldStart(fieldName);
    for (Controller controller : controllers) {
      gen.writeStartObject();
      final String controllerInit = controller.getInitResource();
      final String controllerCommand = controller.getCmdResource();
      final String controllerLog = controller.getLogResource();
      final Interval controllerPolling = controller.getPolling();
      final Long controllerReconnections = controller.getReconnections();
      final Interval controllerReconnectionWait = controller.getReconnectionWait();
      final HttpProxy controllerProxy = controller.getProxy();

      gen.writeStringField("init", controllerInit);
      gen.writeStringField("command", controllerCommand);
      gen.writeStringField("log", controllerLog);

      if (controllerPolling != null) {
        gen.writeStringField("polling", controllerPolling.toString());
      }

      if (controllerReconnections != null) {
        gen.writeNumberField("reconnections", controllerReconnections);
      }

      if (controllerReconnectionWait != null) {
        gen.writeStringField("reconnectionWait", controllerReconnectionWait.toString());
      }

      if (controllerProxy != null) {
        gen.writeStringField("proxy", controllerProxy.toCompactString());
      }

      gen.writeEndObject();
    }
    gen.writeEndArray();
  }

}