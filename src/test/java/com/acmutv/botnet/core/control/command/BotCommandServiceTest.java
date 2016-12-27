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

package com.acmutv.botnet.core.control.command;

import com.acmutv.botnet.core.control.command.serial.BotCommandJsonMapper;
import com.acmutv.botnet.tool.io.IOManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes JUnit tests for {@link BotCommandService}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommandService
 */
public class BotCommandServiceTest {

  @Before
  public void before() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/cmd/consume.json").getPath();
    BotCommand cmd = new BotCommand(CommandScope.KILL);
    String json = new BotCommandJsonMapper().writeValueAsString(cmd);
    IOManager.writeResource(resource, json);
  }

  /**
   * Tests command consume from a JSON resource.
   */
  @Test
  public void test_consumeJsonResource() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/cmd/consume.json").getPath();
    BotCommand actual = BotCommandService.consumeJsonResource(resource);
    BotCommand expected = new BotCommand(CommandScope.KILL);
    Assert.assertEquals(expected, actual);
    String actualJson = IOManager.readResource(resource);
    String expectedJson = new BotCommandJsonMapper().writeValueAsString(BotCommand.NONE);
    Assert.assertEquals(expectedJson, actualJson);
  }

  @After
  public void after() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/cmd/consume.json").getPath();
    BotCommand cmd = new BotCommand(CommandScope.KILL);
    String json = new BotCommandJsonMapper().writeValueAsString(cmd);
    IOManager.writeResource(resource, json);
  }

  /**
   * Tests command parsing from a JSON resource.
   * command: ATTACK_HTTP, Method: GET
   */
  @Test
  public void test_fromJsonResource_attackHTTPGet() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/cmd/attack-http.get.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10, null));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10, new HttpProxy("192.168.0.1", 80)));
    expected.getParams().put("targets", targets);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * command: ATTACK_HTTP, Method: POST
   */
  @Test
  public void test_fromJsonResource_attackHTTPPost() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/cmd/attack-http.post.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10, null));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10, new HttpProxy("192.168.0.1", 80)));
    expected.getParams().put("targets", targets);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Empty JSON.
   */
  @Test
  public void test_fromJsonResource_empty() {
    String resource = BotCommandServiceTest.class.getResource("/cmd/empty.json").getPath();
    try {
      BotCommandService.fromJsonResource(resource);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: KILL
   */
  @Test
  public void test_fromJsonResource_kill() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/cmd/kill.json").getPath();
    BotCommand actual = BotCommandService.fromJsonResource(resource);
    BotCommand expected = new BotCommand(CommandScope.KILL);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON resource.
   * Command: NONE
   */
  @Test
  public void test_fromJsonResource_none() throws IOException {
    String resource = BotCommandServiceTest.class.getResource("/cmd/none.json").getPath();
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
    String resource = BotCommandServiceTest.class.getResource("/cmd/null.json").getPath();
    try {
      BotCommandService.fromJsonResource(resource);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a JSON file (command: RESTART, resource: provided)
   */
  @Test
  public void test_fromJSONFile_restart() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/restart.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.RESTART);
    expected.getParams().put("resource", TemplateEngine.getInstance().replace("${PWD}/cc/botinit.json"));
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: SHUTDOWN).
   */
  @Test
  public void test_fromJSONFile_shutdown() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/shutdown.json");
    BotCommand expected = new BotCommand(CommandScope.SHUTDOWN);
    expected.getParams().put("timeout", new Duration(3, TimeUnit.MINUTES));
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: SLEEP).
   */
  @Test
  public void test_fromJSONFile_sleep() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/sleep.json");
    BotCommand expected = new BotCommand(CommandScope.SLEEP);
    expected.getParams().put("timeout", new Interval(3, 3, TimeUnit.MINUTES));
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

}
