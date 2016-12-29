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
package com.acmutv.botnet.core.control.command;

import com.acmutv.botnet.core.control.command.serial.BotCommandJsonMapper;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes JUnit tests for {@link BotCommand} serialization.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 * @see BotCommandJsonMapper
 */
public class BotCommandSerializationTest {

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ATTACK_HTTP | Method: GET | Delay: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_attackHttpGet() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.ATTACK_HTTP);
    cmdExpected.getParams().put("method", HttpMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10, null));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10, new HttpProxy("192.168.0.1", 80)));
    cmdExpected.getParams().put("targets", targets);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ATTACK_HTTP | Method: GET | Delay: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_attackHttpGet_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.ATTACK_HTTP);
    cmdExpected.getParams().put("method", HttpMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10, null));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10, new HttpProxy("192.168.0.1", 80)));
    cmdExpected.getParams().put("targets", targets);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ATTACK_HTTP | Method: POST | Delay: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_attackHttpPost() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.ATTACK_HTTP);
    cmdExpected.getParams().put("method", HttpMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10, null));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10, new HttpProxy("192.168.0.1", 80)));
    cmdExpected.getParams().put("targets", targets);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ATTACK_HTTP | Method: POST | Delay: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_attackHttpPost_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.ATTACK_HTTP);
    cmdExpected.getParams().put("method", HttpMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10, null));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10, new HttpProxy("192.168.0.1", 80)));
    cmdExpected.getParams().put("targets", targets);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: CALMDOWN | Delay: not provived
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_calmdown() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.CALMDOWN);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: CALMDOWN | Delay: provived
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_calmdown_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.CALMDOWN);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: empty
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @SuppressWarnings("ConstantConditions")
  @Test
  public void test_empty() throws IOException {
    BotCommand cmdExpected = null;
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: KILL | Timeout: not provided | Delay: not provived
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_kill() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.KILL);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: KILL | Timeout: provided | Delay: not provived
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_kill_timeout() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.KILL);
    cmdExpected.getParams().put("timeout", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: KILL | Timeout: not provided | Delay: provived
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_kill_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.KILL);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: KILL | Timeout: provided | Delay: provived
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_kill_timeoutDelay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.KILL);
    cmdExpected.getParams().put("timeout", new Interval(10, 15, TimeUnit.SECONDS));
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: NONE
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_none() throws IOException {
    BotCommand cmdExpected = new BotCommand();
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: RESTART | Delay: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_restart() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.RESTART);
    cmdExpected.getParams().put("resource", "Custom");
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: RESTART | Delay: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_restart_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.RESTART);
    cmdExpected.getParams().put("resource", "Custom");
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: SAVE_CONFIG | Delay: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_saveConfig() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SAVE_CONFIG);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: SAVE_CONFIG | Delay: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_saveConfig_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SAVE_CONFIG);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: SLEEP | Delay: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_sleep() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SLEEP);
    cmdExpected.getParams().put("timeout", new Interval(3, 3, TimeUnit.MINUTES));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: SLEEP | Delay: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_sleep_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SLEEP);
    cmdExpected.getParams().put("timeout", new Interval(3, 3, TimeUnit.MINUTES));
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.MINUTES));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

}
