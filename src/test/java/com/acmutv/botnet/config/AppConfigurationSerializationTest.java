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
package com.acmutv.botnet.config;

import com.acmutv.botnet.config.serial.AppConfigurationJsonMapper;
import com.acmutv.botnet.config.serial.AppConfigurationYamlMapper;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes JUnit tests for {@link AppConfiguration} serialization.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 * @see AppConfigurationJsonMapper
 * @see AppConfigurationYamlMapper
 */
public class AppConfigurationSerializationTest {

  /**
   * Tests {@link AppConfiguration} serialization/deserialization.
   * Type: default
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_default() throws IOException {
    AppConfiguration configExpected = new AppConfiguration();
    ObjectMapper mapperJson = new AppConfigurationJsonMapper();
    ObjectMapper mapperYaml = new AppConfigurationYamlMapper();
    String jsonActual = mapperJson.writeValueAsString(configExpected);
    String yamlActual = mapperYaml.writeValueAsString(configExpected);
    AppConfiguration configJsonActual = mapperJson.readValue(jsonActual, AppConfiguration.class);
    AppConfiguration configYamlActual = mapperYaml.readValue(yamlActual, AppConfiguration.class);
    Assert.assertEquals(configExpected, configJsonActual);
    Assert.assertEquals(configExpected, configYamlActual);
  }

  /**
   * Tests {@link AppConfiguration} serialization/deserialization.
   * Type: custom | no controllers
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_custom_noControllers() throws IOException {
    AppConfiguration configExpected = new AppConfiguration();
    configExpected.setSysStat(false);
    configExpected.setNetStat(false);
    configExpected.setSampling(new Duration(10, TimeUnit.SECONDS));
    configExpected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    configExpected.setReconnections(5L);
    configExpected.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    configExpected.setProxy(new HttpProxy("192.168.0.1", 8080));
    configExpected.setUserAgent("Custom user agent");
    List<Controller> controllers = new ArrayList<>();
    configExpected.setControllers(controllers);
    ObjectMapper mapperJson = new AppConfigurationJsonMapper();
    ObjectMapper mapperYaml = new AppConfigurationYamlMapper();
    String jsonActual = mapperJson.writeValueAsString(configExpected);
    String yamlActual = mapperYaml.writeValueAsString(configExpected);
    AppConfiguration configJsonActual = mapperJson.readValue(jsonActual, AppConfiguration.class);
    AppConfiguration configYamlActual = mapperYaml.readValue(yamlActual, AppConfiguration.class);
    Assert.assertEquals(configExpected, configJsonActual);
    Assert.assertEquals(configExpected, configYamlActual);
  }

  /**
   * Tests {@link AppConfiguration} serialization/deserialization.
   * Type: custom | with controllers
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_custom_withControllers() throws IOException {
    AppConfiguration configExpected = new AppConfiguration();
    configExpected.setSysStat(false);
    configExpected.setNetStat(false);
    configExpected.setSampling(new Duration(10, TimeUnit.SECONDS));
    configExpected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    configExpected.setReconnections(5L);
    configExpected.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    configExpected.setProxy(new HttpProxy("192.168.0.1", 8080));
    configExpected.setUserAgent(null);
    List<Controller> controllers = new ArrayList<>();
    Controller controller1 = new Controller("initCustom", "cmdCustom", "logCustom");
    Controller controller2 = new Controller("initCustom2", "cmdCustom2", "logCustom2");
    controllers.add(controller1);
    controllers.add(controller2);
    configExpected.setControllers(controllers);
    ObjectMapper mapperJson = new AppConfigurationJsonMapper();
    ObjectMapper mapperYaml = new AppConfigurationYamlMapper();
    String jsonActual = mapperJson.writeValueAsString(configExpected);
    String yamlActual = mapperYaml.writeValueAsString(configExpected);
    AppConfiguration configJsonActual = mapperJson.readValue(jsonActual, AppConfiguration.class);
    AppConfiguration configYamlActual = mapperYaml.readValue(yamlActual, AppConfiguration.class);
    Assert.assertEquals(configExpected, configJsonActual);
    Assert.assertEquals(configExpected, configYamlActual);
  }

  /**
   * Tests {@link AppConfiguration} serialization/deserialization.
   * Type: custom | with controllers (customized)
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_custom_withControllersCustomized() throws IOException {
    AppConfiguration configExpected = new AppConfiguration();
    configExpected.setSysStat(false);
    configExpected.setNetStat(false);
    configExpected.setSampling(new Duration(10, TimeUnit.SECONDS));
    configExpected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    configExpected.setReconnections(5L);
    configExpected.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    configExpected.setProxy(new HttpProxy("192.168.0.1", 8080));
    List<Controller> controllers = new ArrayList<>();
    Controller controller1 = new Controller("initCustom", "cmdCustom", "logCustom");
    Controller controller2 = new Controller(
        "initCustom2", "cmdCustom2", "logCustom2",
        new Interval(10, 15, TimeUnit.SECONDS),
        10L,
        new Interval(10, 15, TimeUnit.SECONDS),
        new HttpProxy("192.168.0.1", 8080));
    Controller controller3 = new Controller(
        "initCustom3", "cmdCustom3", "logCustom3");
    controller3.setProxy(HttpProxy.NONE);
    controllers.add(controller1);
    controllers.add(controller2);
    configExpected.setControllers(controllers);
    ObjectMapper mapperJson = new AppConfigurationJsonMapper();
    ObjectMapper mapperYaml = new AppConfigurationYamlMapper();
    String jsonActual = mapperJson.writeValueAsString(configExpected);
    String yamlActual = mapperYaml.writeValueAsString(configExpected);
    AppConfiguration configJsonActual = mapperJson.readValue(jsonActual, AppConfiguration.class);
    AppConfiguration configYamlActual = mapperYaml.readValue(yamlActual, AppConfiguration.class);
    Assert.assertEquals(configExpected, configJsonActual);
    Assert.assertEquals(configExpected, configYamlActual);
  }

}
