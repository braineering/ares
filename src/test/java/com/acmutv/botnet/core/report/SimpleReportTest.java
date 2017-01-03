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
package com.acmutv.botnet.core.report;

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.config.AppConfigurationServiceTest;
import com.acmutv.botnet.core.report.serial.ReportJsonMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * JUnit tests for {@link SimpleReport}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see SimpleReport
 */
public class SimpleReportTest {

  /**
   * Tests report merging.
   */
  @Test
  public void test_merge() {
    Report r1 = new SimpleReport();
    r1.put("prop1", "val1");
    Report r2 = new SimpleReport();
    r2.put("prop2", "val2");
    Report r3 = new SimpleReport();
    r2.put("prop1", "val1-bis");

    Report actual = new SimpleReport();
    actual.merge(r1);
    actual.merge(r2);
    actual.merge(r3);

    Report expected = new SimpleReport();
    expected.put("prop1", "val1-bis");
    expected.put("prop2", "val2");

    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests report serialization.
   * @throws IOException when report cannot be serialized.
   */
  @Test
  public void test_toJson() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/report/report.json");
    Report actual = new SimpleReport();
    actual.put("prop1", "val1");
    actual.put("prop2", "val2");

    String actualJson = actual.toJson();
    String expectedJson = IOUtils.toString(injson, Charset.defaultCharset());

    Assert.assertEquals(expectedJson, actualJson);
  }

  /**
   * Tests report serialization with {@link AppConfiguration}.
   * @throws IOException when report cannot be serialized.
   */
  @Test
  public void test_toJson_withAppConfiguration() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/report/report.withconfig.json");
    Report actual = new SimpleReport();
    actual.put("config", new AppConfiguration());
    actual.put("prop1", "val1");
    actual.put("prop2", "val2");

    String actualJson = actual.toJson();
    String expectedJson = IOUtils.toString(injson, Charset.defaultCharset());

    Assert.assertEquals(expectedJson, actualJson);
  }

  /**
   * Tests report deserialization.
   * @throws IOException when report cannot be deserialized.
   */
  @Test
  public void test_fromJson() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/report/report.json");
    Report actual = new ReportJsonMapper().readValue(injson, Report.class);
    Report expected = new SimpleReport();
    expected.put("prop1", "val1");
    expected.put("prop2", "val2");
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests report deserialization with {@link AppConfiguration}.
   * @throws IOException when report cannot be deserialized.
   */
  @Test
  public void test_fromJson_withAppConfiguration() throws IOException {
    InputStream injson = AppConfigurationServiceTest.class.getResourceAsStream("/report/report.withconfig.json");
    Report actual = new ReportJsonMapper().readValue(injson, Report.class);
    Report expected = new SimpleReport();
    expected.put("config", new AppConfiguration());
    expected.put("prop1", "val1");
    expected.put("prop2", "val2");
    Assert.assertEquals(expected, actual);
  }

}
