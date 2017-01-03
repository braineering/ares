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

package com.acmutv.botnet.core.analysis;

import com.acmutv.botnet.core.exception.BotAnalysisException;
import com.acmutv.botnet.core.report.Report;
import com.acmutv.botnet.core.report.SimpleReport;
import com.acmutv.botnet.tool.reflection.ReflectionManager;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * JUnit tests for {@link Analyzer}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Analyzer
 */
public class AnalyzerTest {

  /**
   * Utility class (for testing use only).
   */
  @Data
  public static class CustomObject {
    private boolean propBoolean = false;
    private int propInt = 10;
    private long propLong = 10;
    private double propDouble = 10.10;
    private String propString = "default";
  }

  /**
   * Utility class (for testing use only).
   */
  public static class CustomAnalyzer implements Analyzer {

    @Override
    public Report makeReport() throws BotAnalysisException {
      CustomObject custom = new CustomObject();
      Map<String,Object> attributes;
      try {
        attributes = ReflectionManager.getAttributes(CustomObject.class, custom);
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException exc) {
        throw new BotAnalysisException(exc.getMessage());
      }
      return new SimpleReport(attributes);
    }
  }

  /**
   * Tests report generation.
   */
  @Test
  public void test_makeReport() throws BotAnalysisException {
    Report actual = new CustomAnalyzer().makeReport();
    Report expected = new SimpleReport();
    expected.put("propBoolean", false);
    expected.put("propInt", 10);
    expected.put("propLong", 10L);
    expected.put("propDouble", 10.10);
    expected.put("propString", "default");
    Assert.assertEquals(expected, actual);
  }
}
