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

import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
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
    expected.setNetInfo(false);
    expected.setSysInfo(true);
    expected.setNetStat(false);
    expected.setSampling(new Duration(1, TimeUnit.HOURS));
    expected.setInitResource(TemplateEngine.getInstance().replace("${RES}/cc/botinit2.json"));
    expected.setCmdResource(TemplateEngine.getInstance().replace("${RES}/cc/botcmd2.json"));
    expected.setLogResource(TemplateEngine.getInstance().replace("${RES}/cc/botlog2.json"));
    expected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
    Assert.assertEquals(expected, actualjson);
    Assert.assertEquals(expected, actualyaml);
  }

  /**
   * Tests the app configuration parsing from an external JSON/YAML file.
   * In this test the configuration file provides with complete custom settings.
   * The configuration file has null values and template string (${PWD}).
   */
  @Test
  public void test_fromJsonYaml_custom2() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom2.json");
    InputStream inyaml = AppConfigurationServiceTest.class.getResourceAsStream("/config/custom2.yaml");
    AppConfiguration actualjson = AppConfigurationService.fromJson(injson);
    AppConfiguration actualyaml = AppConfigurationService.fromYaml(inyaml);
    AppConfiguration expected = new AppConfiguration();
    expected.setSysInfo(true);
    expected.setNetInfo(false);
    expected.setSysInfo(true);
    expected.setNetStat(false);
    expected.setSampling(new Duration(1, TimeUnit.HOURS));
    expected.setInitResource(TemplateEngine.getInstance().replace("${PWD}/cc/botinit.json"));
    expected.setCmdResource(TemplateEngine.getInstance().replace("${PWD}/cc/botcmd.json"));
    expected.setLogResource(TemplateEngine.getInstance().replace("${PWD}/cc/botlog.json"));
    expected.setPolling(new Interval(10, 15, TimeUnit.SECONDS));
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