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

import com.acmutv.botnet.config.serial.AppConfigurationFormat;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes JUnit tests for {@link AppConfigurationService}.
 * and {@link AppConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfigurationService
 */
public class AppConfigurationServiceTest {

  /**
   * Tests the restoring of default settings in app configuration.
   */
  @Test
  public void test_fromDefault() {
    AppConfiguration actual = AppConfigurationService.fromDefault();
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration deserialization from a resource.
   * In this test the configuration file provides with complete custom settings.
   * The configuration file has non-null values and template string (${RES}).
   */
  @Test
  public void test_from_custom() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.yaml");
    AppConfiguration actualjson = AppConfigurationService.from(AppConfigurationFormat.JSON, injson, null);
    AppConfiguration actualyaml = AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml, null);

    AppConfiguration expected = new AppConfiguration();
    expected.setSysInfo(true);
    expected.setNetInfo(true);
    expected.setSysStat(false);
    expected.setNetStat(false);
    expected.setSampling(new Duration(1, TimeUnit.HOURS));
    expected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setReconnections(5L);
    expected.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setProxy(new HttpProxy("192.168.0.1", 8080));
    expected.setUserAgent("Custom user agent");
    List<Controller> controllers = new ArrayList<>();
    Controller controller1 = new Controller(
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botinit.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botcmd.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botlog.json")
    );
    Controller controller2 = new Controller(
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botinit2.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botcmd2.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botlog2.json"),
        new Interval(10, 20, TimeUnit.SECONDS),
        -1L,
        new Interval(10, 20, TimeUnit.SECONDS),
        new HttpProxy("192.168.0.1", 3000)
    );
    Controller controller3 = new Controller(
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botinit3.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botcmd3.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botlog3.json")
    );
    controller3.setProxy(HttpProxy.NONE);
    controllers.add(controller1);
    controllers.add(controller2);
    controllers.add(controller3);
    expected.setControllers(controllers);

    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the app configuration deserialization from a resource.
   * In this test the configuration file provides with complete default settings.
   */
  @Test
  public void test_from_default() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.yaml");
    AppConfiguration actualjson = AppConfigurationService.from(AppConfigurationFormat.JSON, injson, null);
    AppConfiguration actualyaml = AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml, null);

    AppConfiguration expected = new AppConfiguration();

    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the app configuration deserialization from a resource.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void test_from_empty() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.yaml");
    try {
      AppConfigurationService.from(AppConfigurationFormat.JSON, injson, null);
      AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml, null);
    } catch (IOException exc) {return;}
    Assert.fail();
  }

  /**
   * Tests the app configuration deserialization from a resource.
   * In this test the configuration file provides with partial custom settings.
   */
  @Test
  public void test_from_partialCustom() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.yaml");
    AppConfiguration actualjson = AppConfigurationService.from(AppConfigurationFormat.JSON, injson, null);
    AppConfiguration actualyaml = AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml, null);

    AppConfiguration expected = new AppConfiguration();
    expected.setNetInfo(false);

    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the app configuration serialization from a resource.
   * In this test the configuration file provides with complete custom settings.
   * The configuration file has non-null values and template string (${RES}).
   */
  @Test
  public void test_to_custom() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.check.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.check.yaml");

    AppConfiguration config = new AppConfiguration();
    config.setSysInfo(true);
    config.setNetInfo(true);
    config.setSysStat(false);
    config.setNetStat(false);
    config.setSampling(new Duration(1, TimeUnit.HOURS));
    config.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    config.setReconnections(5L);
    config.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    config.setProxy(new HttpProxy("192.168.0.1", 8080));
    config.setUserAgent("Custom user agent");
    List<Controller> controllers = new ArrayList<>();
    Controller controller1 = new Controller(
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botinit.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botcmd.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botlog.json")
    );
    Controller controller2 = new Controller(
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botinit2.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botcmd2.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botlog2.json"),
        new Interval(10, 20, TimeUnit.SECONDS),
        -1L,
        new Interval(10, 20, TimeUnit.SECONDS),
        new HttpProxy("192.168.0.1", 3000)
    );
    Controller controller3 = new Controller(
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botinit3.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botcmd3.json"),
        TemplateEngine.getInstance().replace("${PWD}/data/controller/botlog3.json")
    );
    controller3.setProxy(HttpProxy.NONE);
    controllers.add(controller1);
    controllers.add(controller2);
    controllers.add(controller3);
    config.setControllers(controllers);

    OutputStream outjson = new ByteArrayOutputStream();
    OutputStream outyaml = new ByteArrayOutputStream();
    AppConfigurationService.to(AppConfigurationFormat.JSON, outjson, config);
    AppConfigurationService.to(AppConfigurationFormat.YAML, outyaml, config);
    String actualJson = outjson.toString();
    String actualYaml = outyaml.toString();

    String expectedJson = IOUtils.toString(injson, Charset.defaultCharset());
    String expectedYaml = IOUtils.toString(inyaml, Charset.defaultCharset());

    Assert.assertEquals(expectedJson, actualJson);
    Assert.assertEquals(expectedYaml, actualYaml);
  }

  /**
   * Tests the app configuration serialization to a resource.
   * In this test the configuration file provides with complete default settings.
   */
  @Test
  public void test_to_default() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.check.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.check.yaml");

    AppConfiguration config = new AppConfiguration();

    OutputStream outjson = new ByteArrayOutputStream();
    OutputStream outyaml = new ByteArrayOutputStream();
    AppConfigurationService.to(AppConfigurationFormat.JSON, outjson, config);
    AppConfigurationService.to(AppConfigurationFormat.YAML, outyaml, config);
    String actualJson = outjson.toString();
    String actualYaml = outyaml.toString();

    String expectedJson = IOUtils.toString(injson, Charset.defaultCharset());
    String expectedYaml = IOUtils.toString(inyaml, Charset.defaultCharset());

    Assert.assertEquals(expectedJson, actualJson);
    Assert.assertEquals(expectedYaml, actualYaml);
  }
}