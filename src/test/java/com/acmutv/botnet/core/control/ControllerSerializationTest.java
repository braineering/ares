/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani and Michele Porretta

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
package com.acmutv.botnet.core.control;

import com.acmutv.botnet.core.control.serial.ControllerJsonMapper;
import com.acmutv.botnet.core.control.serial.ControllerYamlMapper;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for {@link Controller} serialization.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Controller
 * @see ControllerJsonMapper
 * @see ControllerYamlMapper
 */
public class ControllerSerializationTest {

  /**
   * Tests {@link Controller} serialization/deserialization.
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    Controller ctrlExpected = new Controller(
        "init1", "command1", "log1");
    ObjectMapper mapper = new ControllerJsonMapper();
    String jsonActual = mapper.writeValueAsString(ctrlExpected);
    Controller ctrlActual = mapper.readValue(jsonActual, Controller.class);
    Assert.assertEquals(ctrlExpected, ctrlActual);
  }

  /**
   * Tests {@link Controller} serialization/deserialization.
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_medium() throws IOException {
    Controller ctrlExpected = new Controller(
        "initCustom2", "cmdCustom2", "logCustom2",
        new Interval(10, 15, TimeUnit.SECONDS),
        10L,
        new Interval(10, 15, TimeUnit.SECONDS),
        HttpProxy.NONE,
        null,
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}});
    ObjectMapper mapper = new ControllerJsonMapper();
    String jsonActual = mapper.writeValueAsString(ctrlExpected);
    Controller ctrlActual = mapper.readValue(jsonActual, Controller.class);
    Assert.assertEquals(ctrlExpected, ctrlActual);
  }

  /**
   * Tests {@link Controller} serialization/deserialization.
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_complex() throws IOException, ParseException {
    Controller ctrlExpected = new Controller(
        "initCustom3", "cmdCustom3", "logCustom3",
        new Interval(10, 15, TimeUnit.SECONDS),
        10L,
        new Interval(10, 15, TimeUnit.SECONDS),
        new HttpProxy("192.168.0.1", 8080),
        "* * * ? * SAT,SUN",
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}});
    ObjectMapper mapper = new ControllerJsonMapper();
    String jsonActual = mapper.writeValueAsString(ctrlExpected);
    Controller ctrlActual = mapper.readValue(jsonActual, Controller.class);
    Assert.assertEquals(ctrlExpected.getSleep(), ctrlActual.getSleep());
    Assert.assertEquals(ctrlExpected, ctrlActual);
  }
}
