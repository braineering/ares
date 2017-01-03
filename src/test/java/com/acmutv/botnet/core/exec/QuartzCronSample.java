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

package com.acmutv.botnet.core.exec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.matchers.GroupMatcher;

import java.text.ParseException;
import java.util.Properties;

/**
 * Samples using the Quartz framework (Cron scheduler).
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class QuartzCronSample {

  private static final Logger LOGGER = LogManager.getLogger(QuartzCronSample.class);

  public static class SimpleTask implements Job {

    private static final Logger LOGGER = LogManager.getLogger(SimpleTask.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      LOGGER.info("Hello World!");
    }
  }

  @Test
  public void sample_repetition() throws SchedulerException, InterruptedException, ParseException {
    CronCalendar croncal = new CronCalendar("* * 0-7 ? * *");
    //CronCalendar croncal = new CronCalendar("* * * ? * * *");
    Properties props = new Properties();
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();
    sched.addCalendar("CALENDAR_SLEEP", croncal, true, true);

    JobDetail job = JobBuilder.newJob(SimpleTask.class)
        .withIdentity("myJob", "group1")
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(1)
            .repeatForever()
        )
        .modifiedByCalendar("CALENDAR_SLEEP")
        .build();

    try {
      sched.scheduleJob(job, trigger);
    } catch (SchedulerException exc) {
      if (!exc.getMessage().endsWith("will never fire."))
        throw new SchedulerException(exc.getCause());
    }
    LOGGER.info("Starting");

    sched.start();

    Thread.sleep(5000);
    for (TriggerKey tk : sched.getTriggerKeys(GroupMatcher.anyTriggerGroup())) {
      Trigger t = sched.getTrigger(tk);
      sched.rescheduleJob(tk, t.getTriggerBuilder().startNow().modifiedByCalendar(null).build());
    }
    sched.deleteCalendar("CALENDAR_SLEEP");
    Thread.sleep(5000);

    sched.shutdown(true);
  }
}
