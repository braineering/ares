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

import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
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
   * Tests the app configuration parsing from an external JSON/YAML file.
   * In this test the configuration file provides with complete custom settings.
   * The configuration file has non-null values and template string (${RES}).
   */
  @Test
  public void test_fromJsonYaml_custom() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom.yaml");
    AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
    AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    AppConfiguration expected = new AppConfiguration();
    expected.setSysInfo(true);
    expected.setNetInfo(true);
    expected.setSysStat(false);
    expected.setNetStat(false);
    expected.setSampling(new Duration(1, TimeUnit.HOURS));
    List<Controller> controllers = new ArrayList<>();
    controllers.add(
        new Controller(
            TemplateEngine.getInstance().replace("${PWD}/cc/botinit.json"),
            TemplateEngine.getInstance().replace("${PWD}/cc/botcmd.json"),
            TemplateEngine.getInstance().replace("${PWD}/cc/botlog.json"),
            new Interval(10, 15, TimeUnit.SECONDS),
            5L,
            new Interval(10, 15, TimeUnit.SECONDS),
            new HttpProxy("192.168.0.1", 8080)
        )
    );
    controllers.add(
        new Controller(
            TemplateEngine.getInstance().replace("${PWD}/cc/botinit2.json"),
            TemplateEngine.getInstance().replace("${PWD}/cc/botcmd2.json"),
            TemplateEngine.getInstance().replace("${PWD}/cc/botlog2.json"),
            new Interval(10, 15, TimeUnit.SECONDS),
            5L,
            new Interval(10, 15, TimeUnit.SECONDS),
            HttpProxy.NONE
        )
    );
    controllers.add(
        new Controller(
            TemplateEngine.getInstance().replace("${PWD}/cc/botinit2.json"),
            TemplateEngine.getInstance().replace("${PWD}/cc/botcmd2.json"),
            TemplateEngine.getInstance().replace("${PWD}/cc/botlog2.json"),
            new Interval(10, 20, TimeUnit.SECONDS),
            -1L,
            new Interval(10, 20, TimeUnit.SECONDS),
            new HttpProxy("192.168.0.1", 3000)
        )
    );
    expected.setControllers(controllers);
    expected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setReconnections(5L);
    expected.setReconnectionWait(new Interval(10, 15, TimeUnit.SECONDS));
    expected.setProxy(new HttpProxy("192.168.0.1", 8080));
    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the app configuration parsing from an external JSON/YAML file.
   * In this test the configuration file provides with complete default settings.
   */
  @Test
  public void test_fromJsonYaml_default() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/default.yaml");
    AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
    AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the configuration parsing from an external JSON/YAML file.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void test_fromJsonYaml_empty() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/empty.yaml");
    try {
      AppConfigurationService.fromJson(injson);
      AppConfigurationService.fromYaml(inyaml);
    } catch (IOException exc) {return;}
    Assert.fail();
  }

  /**
   * Tests the configuration parsing from an external JSON/YAML configuration file.
   * In this test the configuration file provides with partial custom settings.
   */
  @Test
  public void test_fromJsonYaml_partialCustom() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/partial.yaml");
    AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
    AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    AppConfiguration expected = new AppConfiguration();
    expected.setNetInfo(false);
    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }
}