/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Giacomo Marciani, Michele Porretta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.giammp.botnet.config;

import com.giammp.botnet.model.Target;
import com.giammp.botnet.model.SleepCondition;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author Giacomo Marciani <gmarciani@ieee.org>
 * @author Michele Porretta <mporretta@acm.org>
 * @since 1.0.0
 * @see com.giammp.botnet.TestAll
 */
public class ConfigurationTest {

  /**
   * Tests the configuration loading from an external YAML configuration file.
   * In this test the configuration file provides with default settings.
   */
  @Test
  public void testLoadYaml_default() {
    String path = "src/test/resources/config.default.yml";
    BotConfiguration config = BotConfiguration.fromYaml(path);
    BotConfiguration expected = new BotConfiguration();
    assertEquals(expected, config);
  }

  /**
   * Tests the configuration loading from an external YAML configuration file.
   * In this test the configuration file provides with complete non default settings.
   */
  @Test
  public void testLoadYaml_complete() {
    String path = "src/test/resources/config.complete.yml";
    BotConfiguration config = BotConfiguration.fromYaml(path);
    BotConfiguration expected = new BotConfiguration();
    expected.setSysInfo(true);
    expected.setNetInfo(false);
    expected.setSysStat(true);
    expected.setNetStat(false);
    expected.setSysStatTime(5000);
    expected.setNetStatTime(5000);
    expected.setLogfile("/home/giammp/botnet/log.txt");
    expected.getTargets().add(new Target("http://www.target1.com", 10000, 20000, 10));
    expected.getTargets().add(new Target("http://www.target2.com", 15000, 20000, 15));
    expected.getSleep().add(new SleepCondition("expression1"));
    expected.getSleep().add(new SleepCondition("expression2"));
    expected.setDebug(true);
    assertEquals(expected, config);
  }

  /**
   * Tests the configuration loading from an external YAML configuration file.
   * In this test the configuration file provides with incomplete non default settings.
   */
  @Test
  public void testloadYaml_incomplete() {
    String path = "src/test/resources/config.incomplete.yml";
    BotConfiguration config = BotConfiguration.fromYaml(path);
    BotConfiguration expected = new BotConfiguration();
    expected.setSysStat(false);
    expected.setNetStat(false);
    assertEquals(expected, config);
  }

  /**
   * Tests the configuration loading from an external YAML configuration file.
   * In this test the configuration file provides with empty settings.
   */
  @Test
  public void testloadYaml_empty() {
    String path = "src/test/resources/config.empty.yml";
    BotConfiguration config = BotConfiguration.fromYaml(path);
    BotConfiguration expected = new BotConfiguration();
    assertEquals(expected, config);
  }

}
