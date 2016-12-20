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

package com.acmutv.botnet.core.command;

import com.acmutv.botnet.config.AppConfigurationService;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.core.attack.http.HttpAttackMethod;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.core.target.HttpTargetProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes JUnit tests for {@link BotCommandService}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommandService
 */
public class BotCommandServiceTest {

  /**
   * Tests command parsing from a JSON file.
   * command: ATTACK_HTTP, method: GET, targets: provided, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPGet() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.get.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    expected.getParams().put("proxy", null);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a malformed JSON file.
   * command: ATTACK_HTTP, method: none, targets: none, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPGet_malformed1() {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.get.malformed1.json");
    try {
      BotCommandService.fromJson(file);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a malformed JSON file.
   * command: ATTACK_HTTP, method: GET, targets: none, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPGet_malformed2() {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.get.malformed2.json");
    try {
      BotCommandService.fromJson(file);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a JSON file.
   * command: ATTACK_HTTP, method: GET, targets: empty, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPGet_notargets() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.get.notargets.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.GET);
    expected.getParams().put("targets", new ArrayList<>());
    expected.getParams().put("proxy", null);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * command: ATTACK_HTTP, method: GET, targets: provided, proxy: provided
   */
  @Test
  public void test_fromJSONFile_attackHTTPGetWithProxy() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.get.proxy.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    HttpTargetProxy proxy = new HttpTargetProxy("104.28.5.228", 80);
    expected.getParams().put("proxy", proxy);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * command: ATTACK_HTTP, method: POST, targets: provided, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPPost() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.post.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    expected.getParams().put("proxy", null);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a malformed JSON file.
   * command: ATTACK_HTTP, method: none, targets: none, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPPost_malformed1() {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.post.malformed1.json");
    try {
      BotCommandService.fromJson(file);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a malformed JSON file.
   * command: ATTACK_HTTP, method: POST, targets: none, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPPost_malformed2() {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.post.malformed2.json");
    try {
      BotCommandService.fromJson(file);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a JSON file.
   * command: ATTACK_HTTP, method: POST, targets: empty, proxy: none
   */
  @Test
  public void test_fromJSONFile_attackHTTPPost_notargets() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.post.notargets.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.POST);
    expected.getParams().put("targets", new ArrayList<>());
    expected.getParams().put("proxy", null);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file.
   * command: ATTACK_HTTP, method: POST, targets: provided, proxy: provided
   */
  @Test
  public void test_fromJSONFile_attackHTTPPostWithProxy() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/attack-http.post.proxy.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Interval(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Interval(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    HttpTargetProxy proxy = new HttpTargetProxy("104.28.5.228", 80);
    expected.getParams().put("proxy", proxy);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from an empty JSON (not empty string).
   */
  @Test
  public void test_fromJSONFile_empty() {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/empty.json");
    try {
      BotCommandService.fromJson(file);
    } catch (IOException exc) { return; }
    Assert.fail();
  }

  /**
   * Tests command parsing from a JSON file (command: KILL)
   */
  @Test
  public void test_fromJSONFile_kill() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/kill.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.KILL);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: NONE)
   */
  @Test
  public void test_fromJSONFile_none() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/none.json");
    BotCommand actual = BotCommandService.fromJson(file);
    BotCommand expected = new BotCommand(CommandScope.NONE);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a null JSON (empty string).
   */
  @Test
  public void test_fromJSONFile_null() {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/null.json");
    try {
      BotCommandService.fromJson(file);
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
    expected.getParams().put("timeout", new Duration(3, TimeUnit.MINUTES));
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: SET, settings: provided).
   */
  @Test
  public void test_fromJSONFile_update_settings() throws IOException {
    InputStream file = BotCommandServiceTest.class.getResourceAsStream("/cmd/update.json");
    BotCommand expected = new BotCommand(CommandScope.UPDATE);
    Map<String,String> settings = new HashMap<>();
    for (int i=1;i<=3;i++) settings.put(String.format("prop%d",i), String.format("val%d",i));
    expected.getParams().put("settings", settings);
    BotCommand actual = BotCommandService.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

}
