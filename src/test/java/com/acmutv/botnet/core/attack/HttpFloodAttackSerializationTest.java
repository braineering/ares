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

package com.acmutv.botnet.core.attack;

import com.acmutv.botnet.core.attack.flooding.HttpFloodAttack;
import com.acmutv.botnet.core.attack.flooding.serial.FloodAttackJsonMapper;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for {@link HttpFloodAttack} serialization.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpFloodAttack
 * @see FloodAttackJsonMapper
 */
public class HttpFloodAttackSerializationTest {

  /**
   * Tests {@link HttpFloodAttack} serialization/deserialization.
   * Target: provided | Proxy: not provided | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    HttpFloodAttack attackExpected = new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"));
    ObjectMapper mapper = new FloodAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpFloodAttack attackActual = mapper.readValue(jsonActual, HttpFloodAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpFloodAttack} serialization/deserialization.
   * Target: provided | Proxy: null | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxyNull() throws IOException {
    HttpFloodAttack attackExpected = new HttpFloodAttack(
        HttpMethod.GET,
        new URL("http://www.gmarciani.com"),
        (HttpProxy) null
    );
    ObjectMapper mapper = new FloodAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpFloodAttack attackActual = mapper.readValue(jsonActual, HttpFloodAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpFloodAttack} serialization/deserialization.
   * Target: provided | Proxy: NONE | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxyNone() throws IOException {
    HttpFloodAttack attackExpected = new HttpFloodAttack(
        HttpMethod.GET,
        new URL("http://www.gmarciani.com"),
        HttpProxy.NONE
    );
    ObjectMapper mapper = new FloodAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpFloodAttack attackActual = mapper.readValue(jsonActual, HttpFloodAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpFloodAttack} serialization/deserialization.
   * Target: provided | Proxy: provided | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxy() throws IOException {
    HttpFloodAttack attackExpected = new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
      new HttpProxy("192.168.0.1", 8080)
    );
    ObjectMapper mapper = new FloodAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpFloodAttack attackActual = mapper.readValue(jsonActual, HttpFloodAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpFloodAttack} serialization/deserialization.
   * Method: provided | Target: provided | Proxy: provided | Executions: provided | Period: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxyPropertiesExecutions() throws IOException {
    HttpFloodAttack attackExpected = new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    );
    ObjectMapper mapper = new FloodAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpFloodAttack attackActual = mapper.readValue(jsonActual, HttpFloodAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

}
