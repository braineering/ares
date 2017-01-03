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

package com.acmutv.botnet.core.exec;

import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.SchedulerException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * JUnit test for {@link BotPool}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotPool
 */
public class BotPoolTest {

  /**
   * Tests normal pool lifecycle.
   * @throws SchedulerException when some scheduler error occurs.
   */
  @Test
  public void test_lifecycle() throws SchedulerException {
    BotPool pool = new BotPool();
    Assert.assertTrue(pool.isActive());
    pool.pause();
    Assert.assertTrue(pool.isSleeping());
    pool.destroy(false);
    Assert.assertTrue(pool.isShutdown());
  }

  /**
   * Tests normal pool lifecycle (wait for termination).
   * @throws SchedulerException when some scheduler error occurs.
   */
  @Test
  public void test_lifecycle_wait() throws SchedulerException {
    BotPool pool = new BotPool();
    pool.destroy(true);
    Assert.assertTrue(pool.isShutdown());
  }

  /**
   * Tests attacks scheduling.
   * @throws SchedulerException when some scheduler error occurs.
   */
  @Test
  public void test_schedulingAttack() throws SchedulerException, MalformedURLException, InterruptedException {
    BotPool pool = new BotPool();
    pool.scheduleAttackHttp(
        new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"))
    );
    pool.scheduleAttackHttp(
        new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
            new HttpProxy("31.220.56.101", 80))
    );
    pool.scheduleAttackHttp(
        new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
            new HashMap<String,String>(){{put("User-Agent", "CustomUSerAgent");}})
    );
    pool.scheduleAttackHttp(
        new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
            new HttpProxy("31.220.56.101", 80),
            new HashMap<String,String>(){{put("User-Agent", "CustomUSerAgent");}})
    );
    pool.scheduleAttackHttp(
        new HttpAttack(HttpMethod.GET, new URL("http://www.google.com"),
            new HttpProxy("31.220.56.101", 80),
            new HashMap<String,String>(){{put("User-Agent", "CustomUSerAgent");}},
        3, new Interval(2, 3, TimeUnit.SECONDS))
    );
    Thread.sleep(10000);
    pool.destroy(true);
    Assert.assertTrue(pool.isShutdown());
  }
}
