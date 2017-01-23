/*
  The MIT License (MIT)
  <p>
  Copyright (c) 2016 Giacomo Marciani and Michele Porretta
  <p>
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  <p>
  <p>
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  <p>
  <p>
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.botnet.core.control;

import com.acmutv.botnet.core.attack.flooding.HttpFloodAttack;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.core.control.command.BotCommandService;
import com.acmutv.botnet.core.control.command.CommandScope;
import com.acmutv.botnet.core.control.command.serial.BotCommandJsonMapper;
import com.acmutv.botnet.tool.io.IOManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for {@link BotCommandService}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommandService
 */
public class BotCommandServiceTest {

  /**
   * Tests command consume from a JSON resource.
   */
  @Test
  public void test_consumeJsonResource() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/consume.json").getPath();
    IOManager.writeResource(resource, new BotCommandJsonMapper().writeValueAsString(new BotCommand(1, CommandScope.KILL)));
    BotCommand actual = BotCommandService.consumeJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.KILL);
    Assert.assertEquals(expected, actual);
    String actualJson = IOManager.readResource(resource);
    String expectedJson = new BotCommandJsonMapper().writeValueAsString(BotCommand.NONE);
    Assert.assertEquals(expectedJson, actualJson);
    IOManager.writeResource(resource, new BotCommandJsonMapper().writeValueAsString(new BotCommand(1, CommandScope.KILL)));
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: ATTACK_HTTPFLOOD | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_attackHTTP() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/attack.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.ATTACK_HTTPFLOOD);
    List<HttpFloodAttack> attacks = new ArrayList<>();
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com")));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080)));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.POST, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    expected.getParams().put("attacks", attacks);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: ATTACK_HTTPFLOOD | Delay: provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_attackHTTP_delay() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/attack.delay.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.ATTACK_HTTPFLOOD);
    List<HttpFloodAttack> attacks = new ArrayList<>();
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com")));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080)));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.POST, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    expected.getParams().put("attacks", attacks);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: ATTACK_HTTPFLOOD | Delay: provided | Report: provided
   */
  @Test
  public void test_fromJsonResource_attackHTTP_delayReport() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/attack.delay.report.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.ATTACK_HTTPFLOOD);
    List<HttpFloodAttack> attacks = new ArrayList<>();
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com")));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080)));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.POST, new URL("http://www.gmarciani.com"),
        new HttpProxy("192.168.0.1", 8080),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(10, 15, TimeUnit.SECONDS)
    ));
    expected.getParams().put("attacks", attacks);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("report", true);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: CALMDOWN | Wait: not provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_calmdown() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/calmdown.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.CALMDOWN);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: CALMDOWN | Wait: provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_calmdown_wait() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/calmdown.wait.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.CALMDOWN);
    expected.getParams().put("wait", true);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: CALMDOWN | Wait: provided | Delay: provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_calmdown_waitDelay() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/calmdown.wait.delay.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.CALMDOWN);
    expected.getParams().put("wait", true);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: CALMDOWN | Wait: provided | Delay: provided | Report: provided
   */
  @Test
  public void test_fromJsonResource_calmdown_waitDelayReport() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/calmdown.wait.delay.report.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.CALMDOWN);
    expected.getParams().put("wait", true);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("report", true);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Empty JSON.
   */
  @Test
  public void test_fromJsonResource_empty() {
    String resource = BotCommandServiceTest.class.getResource("/command/empty.json").getPath();
    try {
      BotCommandService.fromJsonResource(resource);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: KILL | Wait: not provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_kill() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/kill.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.KILL);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: KILL | Wait: provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_kill_wait() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/kill.wait.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.KILL);
    expected.getParams().put("wait", true);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: KILL | Wait: provided | Delay: provided | Report: not provided
   */
  @Test
  public void test_fromJsonResource_kill_waitDelay() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/kill.wait.delay.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.KILL);
    expected.getParams().put("wait", true);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: KILL | Wait: provided | Delay: provided | Report: provided
   */
  @Test
  public void test_fromJsonResource_kill_waitDelayReport() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/kill.wait.delay.report.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(1, CommandScope.KILL);
    expected.getParams().put("wait", true);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("report", true);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: NONE
   */
  @Test
  public void test_fromJsonResource_none() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/command/none.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(CommandScope.NONE);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Null JSON
   */
  @Test
  public void test_fromJSONFile_null() {
    String resource = BotCommandServiceTest.class.getResource("/command/null.json").getPath();
    try {
      BotCommandService.fromJsonResource(resource);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: REPORT | Delay: not provided
   */
  @Test
  public void test_fromJSONFile_report() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/report.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(1, CommandScope.REPORT);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: REPORT | Delay: provided
   */
  @Test
  public void test_fromJSONFile_report_delay() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/report.delay.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(1, CommandScope.REPORT);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: RESTART | Wait: not provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_restart() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/restart.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(1, CommandScope.RESTART);
    expected.getParams().put("controller",
        new Controller("init", "cmd", "log"));
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: RESTART | Wait: provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_restart_wait() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/restart.wait.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(1, CommandScope.RESTART);
    expected.getParams().put("controller",
        new Controller("init", "cmd", "log"));
    expected.getParams().put("wait", true);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: RESTART | Wait: provided | Delay: provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_restart_waitDelay() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/restart.wait.delay.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(1, CommandScope.RESTART);
    expected.getParams().put("controller",
        new Controller("init", "cmd", "log"));
    expected.getParams().put("wait", true);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: RESTART | Wait: provided | Delay: provided | Report: provided
   */
  @Test
  public void test_fromJSONFile_restart_waitDelayReport() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/restart.wait.delay.report.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(1, CommandScope.RESTART);
    expected.getParams().put("controller",
        new Controller("init", "cmd", "log"));
    expected.getParams().put("wait", true);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("report", true);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: SLEEP | Timeout: not provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_sleep() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/sleep.json");
    BotCommand expected = new BotCommand(1, CommandScope.SLEEP);
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: SLEEP | Timeout: provided | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_sleep_timeout() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/sleep.timeout.json");
    BotCommand expected = new BotCommand(1, CommandScope.SLEEP);
    expected.getParams().put("timeout", new Interval(10, 15, TimeUnit.SECONDS));
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: SLEEP | Timeout: provided | Delay: provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_sleep_timeoutDelay() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/sleep.timeout.delay.json");
    BotCommand expected = new BotCommand(1, CommandScope.SLEEP);
    expected.getParams().put("timeout", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: SLEEP | Timeout: provided | Delay: provided | Report: provided
   */
  @Test
  public void test_fromJSONFile_sleep_timeoutDelayReport() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/sleep.timeout.delay.report.json");
    BotCommand expected = new BotCommand(1, CommandScope.SLEEP);
    expected.getParams().put("timeout", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("report", true);
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: UPDATE | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_update() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/update.json");
    BotCommand expected = new BotCommand(1, CommandScope.UPDATE);
    expected.getParams().put("settings", new HashMap<String,String>(){{put("prop1","val1");}});
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: UPDATE | Delay: provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_update_delay() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/update.delay.json");
    BotCommand expected = new BotCommand(1, CommandScope.UPDATE);
    expected.getParams().put("settings", new HashMap<String,String>(){{put("prop1","val1");}});
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: UPDATE | Delay: provided | Report: provided
   */
  @Test
  public void test_fromJSONFile_update_delayReport() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/update.delay.report.json");
    BotCommand expected = new BotCommand(1, CommandScope.UPDATE);
    expected.getParams().put("settings", new HashMap<String,String>(){{put("prop1","val1");}});
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("report", true);
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: WAKEUP | Delay: not provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_wakeup() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/wakeup.json");
    BotCommand expected = new BotCommand(1, CommandScope.WAKEUP);
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: WAKEUP | Delay: provided | Report: not provided
   */
  @Test
  public void test_fromJSONFile_wakeup_delay() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/wakeup.delay.json");
    BotCommand expected = new BotCommand(1, CommandScope.WAKEUP);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * Command: WAKEUP | Delay: provided | Report: provided
   */
  @Test
  public void test_fromJSONFile_wakeup_delayReport() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/command/wakeup.delay.report.json");
    BotCommand expected = new BotCommand(1, CommandScope.WAKEUP);
    expected.getParams().put("delay", new Interval(10, 15, TimeUnit.SECONDS));
    expected.getParams().put("report", true);
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

}
