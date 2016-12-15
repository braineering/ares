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

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class realizes JUnit tests for {@link AppConfiguration}
 * and {@link AppConfiguration}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 */
public class AppConfigurationTest {

  /**
   * Tests the restoring of default configuration.
   */
  @Test
  public void test_toDefault() {
    AppConfiguration actual = new AppConfiguration();
    actual.setNetInfo(false);
    actual.toDefault();
    final AppConfiguration expected = new AppConfiguration();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests the configuration loading from an external YAML file.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void test_loadYaml_empty() {
    final String file = AppConfigurationTest.class.getResource("/config/empty.yml").getPath();
    final AppConfiguration actual = AppConfigurationService.fromYaml(file);
    final AppConfiguration expected = new AppConfiguration();
    assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration loading from an external YAML file.
   * In this test the configuration file provides with complete default settings.
   */
  @Test
  public void test_loadYaml_default() {
    final String file = AppConfigurationTest.class.getResource("/config/default.yml").getPath();
    AppConfiguration actual = AppConfigurationService.fromYaml(file);
    AppConfiguration expected = new AppConfiguration();
    assertEquals(expected, actual);
  }

  /**
   * Tests the app configuration loading from an external YAML file.
   * In this test the configuration file provides with complete custom settings.
   */
  @Test
  public void test_loadYaml_custom() {
    final String file = AppConfigurationTest.class.getResource("/config/custom.yml").getPath();
    AppConfiguration actual = AppConfigurationService.fromYaml(file);
    AppConfiguration expected = new AppConfiguration();
    expected.setNetInfo(false);
    assertEquals(expected, actual);
  }

  /**
   * Tests the configuration loading from an external YAML configuration file.
   * In this test the configuration file provides with partial custom settings.
   */
  @Test
  public void test_loadYaml_partialCustom() {
    final String file = AppConfigurationTest.class.getResource("/config/partial.yml").getPath();
    AppConfiguration actual = AppConfigurationService.fromYaml(file);
    AppConfiguration expected = new AppConfiguration();
    expected.setNetInfo(false);
    assertEquals(expected, actual);
  }

}
