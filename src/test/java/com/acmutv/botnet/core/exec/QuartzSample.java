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

import lombok.Data;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.quartz.*;

/**
 * Samples using the Quartz framework.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class QuartzSample {

  private static final Logger LOGGER = LogManager.getLogger(QuartzSample.class);

  public static class SimpleTask implements Job {

    private static final Logger LOGGER = LogManager.getLogger(SimpleTask.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      LOGGER.info("Hello World!");
    }
  }

  @Test
  public void sample_oneshot() throws SchedulerException, InterruptedException {
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();

    JobDetail job = JobBuilder.newJob(SimpleTask.class)
        .withIdentity("myJob", "group1")
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .startNow()
        .build();

    sched.scheduleJob(job, trigger);

    sched.start();

    Thread.sleep(3000);

    sched.shutdown(true);
  }

  @Test
  public void sample_repetition() throws SchedulerException, InterruptedException {
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();

    JobDetail job = JobBuilder.newJob(SimpleTask.class)
        .withIdentity("myJob", "group1")
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(2)
            .withRepeatCount(2))
        .build();

    sched.scheduleJob(job, trigger);

    LOGGER.info("Starting");

    sched.start();

    Thread.sleep(8000);

    sched.shutdown(true);
  }

  @Setter
  @PersistJobDataAfterExecution
  @DisallowConcurrentExecution
  public static class ComplexTask implements Job {

    private static final Logger LOGGER = LogManager.getLogger(ComplexTask.class);

    String message;
    int maxIterations;
    int iter;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      JobDataMap jobmap = context.getJobDetail().getJobDataMap();
      final String out = String.format("(%d/%d) : %s", this.iter, this.maxIterations, this.message);
      LOGGER.info(out);
      jobmap.put("iter", ++this.iter);
    }
  }

  @Test
  public void sample_fixedRepetition() throws SchedulerException, InterruptedException {
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();

    JobDetail job = JobBuilder.newJob(ComplexTask.class)
        .withIdentity("myJob", "group1")
        .usingJobData("message", "Hello World!")
        .usingJobData("maxIterations", 3)
        .usingJobData("iter", 1)
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .startNow()
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(1)
            .withRepeatCount(2))
        .build();

    sched.scheduleJob(job, trigger);

    sched.start();

    Thread.sleep(4000);

    sched.shutdown(true);
  }

  @Test
  public void sample_pauseResume() throws SchedulerException, InterruptedException {
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();

    JobDetail job = JobBuilder.newJob(SimpleTask.class)
        .withIdentity("myJob", "group1")
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(1)
            .withRepeatCount(5)
        )
        .build();

    sched.scheduleJob(job, trigger);

    sched.start();

    Thread.sleep(3000);

    sched.pauseAll();

    LOGGER.info("PAUSING");

    Thread.sleep(61000);

    LOGGER.info("RESUMING");

    // if pause time is less than 1 MINUTE, all misfire triggers are fired at once.
    sched.resumeAll();

    Thread.sleep(3000);

    sched.shutdown(true);
  }

  @Setter
  @PersistJobDataAfterExecution
  @DisallowConcurrentExecution
  public static class CompositeTask implements Job {

    private static final Logger LOGGER = LogManager.getLogger(ComplexTask.class);

    CustomObject object;
    String message;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      final String out = String.format("%s : %s",
          this.message, this.object);
      LOGGER.info(out);
    }
  }

  @Data
  private static class CustomObject  {
    private final int a;
    private final int b;
  }

  @Test
  public void sample_composite_one() throws SchedulerException, InterruptedException {
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();

    JobDataMap jmap = new JobDataMap();
    jmap.put("message", "Hello World!");
    jmap.put("object", new CustomObject(10, 20));

    JobDetail job = JobBuilder.newJob(CompositeTask.class)
        .withIdentity("myJob", "group1")
        .usingJobData(jmap)
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .startNow()
        .build();

    sched.scheduleJob(job, trigger);

    sched.start();

    Thread.sleep(3000);

    sched.shutdown(true);
  }
}
