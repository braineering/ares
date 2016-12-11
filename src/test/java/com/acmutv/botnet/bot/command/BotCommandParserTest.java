/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani and Michele Porretta
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet.bot.command;

import com.acmutv.botnet.attack.HttpAttackMethod;
import com.acmutv.botnet.config.ConfigurationTest;
import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.target.HttpTargetProxy;
import com.acmutv.botnet.time.Period;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes JUnit tests for {@link BotCommandParser}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommandParser
 */
public class BotCommandParserTest {

  /**
   * Tests command parsing from a null JSON (empty string).
   */
  @Test
  public void test_fromJSONFile_null() {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/null.json");
    BotCommand expected = new BotCommand(CommandScope.NONE);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from an empty JSON (not empty string).
   */
  @Test
  public void test_fromJSONFile_empty() {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/empty.json");
    BotCommand expected = new BotCommand(CommandScope.NONE);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: ATTACK_HTTP, method: GET, proxy: none)
   */
  @Test
  public void test_fromJSONFile_attackHTTPGet() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.get.json");
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Period(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Period(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    expected.getParams().put("proxy", null);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a malformed JSON file (command: ATTACK_HTTP, method: GET, proxy: none)
   */
  @Test
  public void test_fromJSONFile_attackHTTPGet_malformed() throws MalformedURLException {
    InputStream file1 = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.get.malformed1.json");
    InputStream file2 = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.get.malformed2.json");
    InputStream file3 = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.get.malformed3.json");
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    expected.getParams().put("targets", targets);
    expected.getParams().put("proxy", null);
    BotCommand actual1 = BotCommandParser.fromJson(file1);
    BotCommand actual2 = BotCommandParser.fromJson(file2);
    BotCommand actual3 = BotCommandParser.fromJson(file3);
    Assert.assertEquals(expected, actual1);
    Assert.assertEquals(expected, actual2);
    Assert.assertEquals(expected, actual3);
  }

  /**
   * Tests command parsing from a JSON file (command: ATTACK_HTTP, method: GET, proxy: provided)
   */
  @Test
  public void test_fromJSONFile_attackHTTPGetWithProxy() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.get.proxy.json");
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.GET);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Period(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Period(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    HttpTargetProxy proxy = new HttpTargetProxy("104.28.5.228", 80);
    expected.getParams().put("proxy", proxy);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: ATTACK_HTTP, method: POST, proxy: none)
   */
  @Test
  public void test_fromJSONFile_attackHTTPPost() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.post.json");
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Period(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Period(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    expected.getParams().put("proxy", null);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a malformed JSON file (command: ATTACK_HTTP, method: POST, proxy: none)
   */
  @Test
  public void test_fromJSONFile_attackHTTPPost_malformed() throws MalformedURLException {
    InputStream file1 = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.post.malformed1.json");
    InputStream file2 = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.post.malformed2.json");
    InputStream file3 = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.post.malformed3.json");
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    expected.getParams().put("targets", targets);
    expected.getParams().put("proxy", null);
    BotCommand actual1 = BotCommandParser.fromJson(file1);
    BotCommand actual2 = BotCommandParser.fromJson(file2);
    BotCommand actual3 = BotCommandParser.fromJson(file3);
    Assert.assertEquals(expected, actual1);
    Assert.assertEquals(expected, actual2);
    Assert.assertEquals(expected, actual3);
  }

  /**
   * Tests command parsing from a JSON file (command: ATTACK_HTTP, method: POST, proxy: provided)
   */
  @Test
  public void test_fromJSONFile_attackHTTPPostWithProxy() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/attack.http.post.proxy.json");
    BotCommand expected = new BotCommand(CommandScope.ATTACK_HTTP);
    expected.getParams().put("method", HttpAttackMethod.POST);
    List<HttpTarget> targets = new ArrayList<>();
    targets.add(new HttpTarget(new URL("http://www.google.com"), new Period(1, 1, TimeUnit.SECONDS), 10));
    targets.add(new HttpTarget(new URL("http://www.twitter.com"), new Period(3, 5, TimeUnit.SECONDS), 10));
    expected.getParams().put("targets", targets);
    HttpTargetProxy proxy = new HttpTargetProxy("104.28.5.228", 80);
    expected.getParams().put("proxy", proxy);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: INIT)
   */
  @Test
  public void test_fromJSONFile_init() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/init.json");
    BotCommand expected = new BotCommand(CommandScope.INIT);
    expected.getParams().put("resource", "/config/config.default.yml");
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: KILL)
   */
  @Test
  public void test_fromJSONFile_kill() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/kill.json");
    BotCommand expected = new BotCommand(CommandScope.KILL);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: NONE)
   */
  @Test
  public void test_fromJSONFile_none() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/none.json");
    BotCommand expected = new BotCommand(CommandScope.NONE);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: SET)
   */
  @Test
  public void test_fromJSONFile_set() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/set.json");
    BotCommand expected = new BotCommand(CommandScope.SET);
    Map<String,String> settings = new HashMap<>();
    for (int i=1;i<=3;i++) settings.put(String.format("prop%d",i), String.format("val%d",i));
    expected.getParams().put("settings", settings);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests command parsing from a JSON file (command: SHUTDOWN)
   */
  @Test
  public void test_fromJSONFile_shutdown() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/shutdown.json");
    BotCommand expected = new BotCommand(CommandScope.SHUTDOWN);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);

  }

  /**
   * Tests command parsing from a JSON file (command: SLEEP)
   */
  @Test
  public void test_fromJSONFile_sleep() throws MalformedURLException {
    InputStream file = ConfigurationTest.class.getResourceAsStream("/cmd/sleep.json");
    BotCommand expected = new BotCommand(CommandScope.SLEEP);
    expected.getParams().put("amount", 10);
    expected.getParams().put("unit", TimeUnit.SECONDS);
    BotCommand actual = BotCommandParser.fromJson(file);
    Assert.assertEquals(expected, actual);
  }

}
