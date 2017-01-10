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

package com.acmutv.botnet.core.attack;

import com.acmutv.botnet.tool.net.ConnectionManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for {@link HttpAttacker}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see QuartzAttacker
 * @see HttpAttacker
 */
public class HttpAttackerTest {

  @Before
  public void setup() {
    org.junit.Assume.assumeTrue(ConnectionManager.checkConnection());
  }

  @Test
  public void test_get() throws InterruptedException, MalformedURLException, SchedulerException {
    HttpAttack attack = new HttpAttack(
        HttpMethod.GET, new URL("http://www.google.it"),
        HttpProxy.NONE,
        new HashMap<String,String>(){{put("User-Agent", "CustomUserAgent");}},
        3,
        new Interval(1, 2, TimeUnit.SECONDS)
    );

    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();

    JobDataMap jdata = new JobDataMap();
    jdata.put("method", attack.getMethod());
    jdata.put("target", attack.getTarget());
    jdata.put("proxy", attack.getProxy());
    jdata.put("properties", attack.getProperties());
    jdata.put("executions", attack.getExecutions());

    JobDetail job = JobBuilder.newJob(HttpAttacker.class)
        .withIdentity("myJob", "group1")
        .usingJobData(jdata)
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withRepeatCount(attack.getExecutions() - 1)
            .withIntervalInMilliseconds(attack.getPeriod().getRandomDuration().toMillis()))
        .build();

    sched.scheduleJob(job, trigger);

    sched.start();

    Thread.sleep(8000);

    sched.shutdown(true);
  }
}
