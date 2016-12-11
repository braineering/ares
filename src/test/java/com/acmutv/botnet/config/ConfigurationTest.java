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

import org.junit.Test;

import java.io.InputStream;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

/**
 * This class realizes JUnit tests for {@link BotConfigurator}
 * and {@link BotConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotConfigurator
 * @see BotConfiguration
 */
public class ConfigurationTest {

  /**
   * Tests the configuration loading fromJson an external YAML configuration file.
   * In this test the configuration file provides with default settings.
   */
  @Test
  public void testLoadYaml_default() {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/config/config.default.yml");
    BotConfiguration config = new BotConfiguration();
    config.fromYaml(file);
    BotConfiguration expected = new BotConfiguration();
    assertEquals(expected, config);
  }

  /**
   * Tests the configuration loading fromJson an external YAML configuration file.
   * In this test the configuration file provides with complete non default settings.
   * @throws MalformedURLException when error in test.
   */
  @Test
  public void testLoadYaml_complete() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/config/config.complete.yml");
    BotConfiguration config = new BotConfiguration();
    config.fromYaml(file);
    BotConfiguration expected = new BotConfiguration();
    expected.setSysInfo(true);
    expected.setNetInfo(false);
    expected.setSysStat(true);
    expected.setNetStat(false);
    expected.setSysStatFreq(5);
    expected.setNetStatFreq(5);
    expected.setInitResource("com/acmutv/botnet/botinit.txt");
    expected.setCmdResource("com/acmutv/botnet/botcmd.txt");
    expected.setLogResource("com/acmutv/botnet/botlog.txt");
    expected.setMaxTime(60);
    expected.setDebug(true);
    assertEquals(expected, config);
  }

  /**
   * Tests the configuration loading fromJson an external YAML configuration file.
   * In this test the configuration file provides with incomplete non default settings.
   */
  @Test
  public void testloadYaml_incomplete() {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/config/config.incomplete.yml");
    BotConfiguration config = new BotConfiguration();
    config.fromYaml(file);
    BotConfiguration expected = new BotConfiguration();
    expected.setSysStat(false);
    expected.setNetStat(false);
    assertEquals(expected, config);
  }

  /**
   * Tests the configuration loading fromJson an external YAML configuration file.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void testloadYaml_empty() {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/config/config.empty.yml");
    BotConfiguration config = new BotConfiguration();
    config.fromYaml(file);
    BotConfiguration expected = new BotConfiguration();
    assertEquals(expected, config);
  }

}
