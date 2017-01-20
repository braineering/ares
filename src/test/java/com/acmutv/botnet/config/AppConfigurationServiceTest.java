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
import com.acmutv.botnet.tool.time.Interval;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for {@link AppConfigurationService}.
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
  public void test_from_custom() throws IOException, ParseException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.yaml");
    AppConfiguration actualjson = AppConfigurationService.from(AppConfigurationFormat.JSON, injson);
    AppConfiguration actualyaml = AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml);

    AppConfiguration expected = new AppConfiguration();
    expected.setCnfInfo(true);
    expected.setTgtInfo(true);
    expected.setSysInfo(false);
    expected.setNetInfo(false);
    expected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setReconnections(5L);
    expected.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setProxy(new HttpProxy("192.168.0.1", 8080));
    expected.setAuthentication(new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}});
    List<Controller> controllers = new ArrayList<>();
    Controller controller1 = new Controller(
        "init", "cmd", "log");
    Controller controller2 = new Controller(
        "init2", "cmd2", "log2",
        new Interval(10, 20, TimeUnit.SECONDS),
        10L,
        new Interval(10, 20, TimeUnit.SECONDS),
        HttpProxy.NONE,
        null,
        null);
    Controller controller3 = new Controller(
        "init3", "cmd3", "log3",
        new Interval(10, 20, TimeUnit.SECONDS),
        10L,
        new Interval(10, 20, TimeUnit.SECONDS),
        new HttpProxy("192.168.0.1", 8080),
        "* * * ? * SAT,SUN",
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}});
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
    AppConfiguration actualjson = AppConfigurationService.from(AppConfigurationFormat.JSON, injson);
    AppConfiguration actualyaml = AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml);

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
      AppConfigurationService.from(AppConfigurationFormat.JSON, injson);
      AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml);
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
    AppConfiguration actualjson = AppConfigurationService.from(AppConfigurationFormat.JSON, injson);
    AppConfiguration actualyaml = AppConfigurationService.from(AppConfigurationFormat.YAML, inyaml);

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
  public void test_to_custom() throws IOException, ParseException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.check.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.check.yaml");

    AppConfiguration expected = new AppConfiguration();
    expected.setCnfInfo(true);
    expected.setTgtInfo(true);
    expected.setSysInfo(false);
    expected.setNetInfo(false);
    expected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setReconnections(5L);
    expected.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setProxy(new HttpProxy("192.168.0.1", 8080));
    expected.setAuthentication(new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}});
    List<Controller> controllers = new ArrayList<>();
    Controller controller1 = new Controller(
        "init", "cmd", "log");
    Controller controller2 = new Controller(
        "init2", "cmd2", "log2",
        new Interval(10, 20, TimeUnit.SECONDS),
        10L,
        new Interval(10, 20, TimeUnit.SECONDS),
        HttpProxy.NONE,
        null,
        null);
    Controller controller3 = new Controller(
        "init3", "cmd3", "log3",
        new Interval(10, 20, TimeUnit.SECONDS),
        10L,
        new Interval(10, 20, TimeUnit.SECONDS),
        new HttpProxy("192.168.0.1", 8080),
        "* * * ? * SAT,SUN",
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}});
    controllers.add(controller1);
    controllers.add(controller2);
    controllers.add(controller3);
    expected.setControllers(controllers);

    OutputStream outjson = new ByteArrayOutputStream();
    OutputStream outyaml = new ByteArrayOutputStream();
    AppConfigurationService.to(AppConfigurationFormat.JSON, outjson, expected);
    AppConfigurationService.to(AppConfigurationFormat.YAML, outyaml, expected);
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