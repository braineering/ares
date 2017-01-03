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

import com.acmutv.botnet.core.attack.serial.HttpAttackJsonMapper;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.core.control.command.CommandScope;
import com.acmutv.botnet.core.control.command.serial.BotCommandJsonMapper;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for {@link HttpAttack} serialization.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpAttack
 * @see HttpAttackJsonMapper
 */
public class HttpAttackSerializationTest {

  /**
   * Tests {@link HttpAttack} serialization/deserialization.
   * Method: provided | Target: provided | Proxy: not provided
   * Properties: not provided | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    HttpAttack attackExpected = new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"));
    ObjectMapper mapper = new HttpAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpAttack attackActual = mapper.readValue(jsonActual, HttpAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpAttack} serialization/deserialization.
   * Method: provided | Target: provided | Proxy: null
   * Properties: not provided | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxyNull() throws IOException {
    HttpAttack attackExpected = new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
        (HttpProxy) null
    );
    ObjectMapper mapper = new HttpAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpAttack attackActual = mapper.readValue(jsonActual, HttpAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpAttack} serialization/deserialization.
   * Method: provided | Target: provided | Proxy: NONE
   * Properties: not provided | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxyNone() throws IOException {
    HttpAttack attackExpected = new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
        HttpProxy.NONE
    );
    ObjectMapper mapper = new HttpAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpAttack attackActual = mapper.readValue(jsonActual, HttpAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpAttack} serialization/deserialization.
   * Method: provided | Target: provided | Proxy: provided
   * Properties: not provided | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxy() throws IOException {
    HttpAttack attackExpected = new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
      new HttpProxy("192.168.0.1", 8080)
    );
    ObjectMapper mapper = new HttpAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpAttack attackActual = mapper.readValue(jsonActual, HttpAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpAttack} serialization/deserialization.
   * Method: provided | Target: provided | Proxy: provided
   * Properties: provided | Executions: not provided | Period: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxyProperties() throws IOException {
    HttpAttack attackExpected = new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("prop1", "val1");}}
    );
    ObjectMapper mapper = new HttpAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpAttack attackActual = mapper.readValue(jsonActual, HttpAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

  /**
   * Tests {@link HttpAttack} serialization/deserialization.
   * Method: provided | Target: provided | Proxy: provided
   * Properties: provided | Executions: provided | Period: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_proxyPropertiesExecutions() throws IOException {
    HttpAttack attackExpected = new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("prop1", "val1");}},
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    );
    ObjectMapper mapper = new HttpAttackJsonMapper();
    String jsonActual = mapper.writeValueAsString(attackExpected);
    HttpAttack attackActual = mapper.readValue(jsonActual, HttpAttack.class);
    Assert.assertEquals(attackExpected, attackActual);
  }

}
