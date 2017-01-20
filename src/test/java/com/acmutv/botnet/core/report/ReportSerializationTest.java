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
import com.acmutv.botnet.core.attack.flooding.HttpFloodAttack;
import com.acmutv.botnet.core.report.serial.SimpleReportJsonMapper;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for {@link SimpleReport} serialization.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see SimpleReport
 * @see SimpleReportJsonMapper
 */
public class ReportSerializationTest {

  /**
   * Tests {@link SimpleReport} serialization/deserialization.
   * Properties: not provided | Configuration: not provided | Attacks: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_empty() throws IOException {
    SimpleReport reportExpected = new SimpleReport();
    ObjectMapper mapper = new SimpleReportJsonMapper();
    String jsonActual = mapper.writeValueAsString(reportExpected);
    SimpleReport reportActual = mapper.readValue(jsonActual, SimpleReport.class);
    Assert.assertEquals(reportExpected, reportActual);
  }

  /**
   * Tests {@link SimpleReport} serialization/deserialization.
   * Properties: provided | Configuration: not provided | Attacks: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_properties() throws IOException {
    SimpleReport reportExpected = new SimpleReport();
    reportExpected.put("prop1", "val1");
    reportExpected.put("prop2", "val2");
    ObjectMapper mapper = new SimpleReportJsonMapper();
    String jsonActual = mapper.writeValueAsString(reportExpected);
    SimpleReport reportActual = mapper.readValue(jsonActual, SimpleReport.class);
    Assert.assertEquals(reportExpected, reportActual);
  }

  /**
   * Tests {@link SimpleReport} serialization/deserialization.
   * Properties: provided | Configuration: provided | Attacks: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_propertiesConfiguration() throws IOException {
    SimpleReport reportExpected = new SimpleReport();
    reportExpected.put("prop1", "val1");
    reportExpected.put("prop2", "val2");
    reportExpected.put(SimpleReport.KEY_CONFIGURATION, new AppConfiguration());
    ObjectMapper mapper = new SimpleReportJsonMapper();
    String jsonActual = mapper.writeValueAsString(reportExpected);
    SimpleReport reportActual = mapper.readValue(jsonActual, SimpleReport.class);
    Assert.assertEquals(reportExpected, reportActual);
  }

  /**
   * Tests {@link SimpleReport} serialization/deserialization.
   * Properties: provided | Configuration: provided | Attacks: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_propertiesConfigurationAttacks() throws IOException {
    SimpleReport reportExpected = new SimpleReport();
    reportExpected.put("prop1", "val1");
    reportExpected.put("prop2", "val2");
    reportExpected.put(SimpleReport.KEY_CONFIGURATION, new AppConfiguration());
    reportExpected.put(SimpleReport.KEY_ATTACKS_HTTP, new ArrayList<HttpFloodAttack>(){{
      add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com")));
      add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
          (HttpProxy) null
      ));
      add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
          HttpProxy.NONE
      ));
      add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
          new HttpProxy("192.168.0.1", 8080)
      ));
      add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
          new HttpProxy("192.168.0.1", 8080),
          3,
          new Interval(10, 15, TimeUnit.SECONDS)
      ));
    }});
    ObjectMapper mapper = new SimpleReportJsonMapper();
    String jsonActual = mapper.writeValueAsString(reportExpected);
    SimpleReport reportActual = mapper.readValue(jsonActual, SimpleReport.class);
    Assert.assertEquals(reportExpected, reportActual);
  }

}
