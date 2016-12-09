/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani and Michele Porretta
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet.bot;

import com.acmutv.botnet.bot.command.BotCommand;
import com.acmutv.botnet.bot.command.CommandScope;
import com.acmutv.botnet.config.BotConfiguration;
import com.acmutv.botnet.tool.LoggerTools;
import com.acmutv.botnet.tool.SystemTools;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the bot.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotState
 * @see CommandScope
 * @see BotService
 */
@Data
@AllArgsConstructor
public class Bot {
  private String id;
  private BotState state;
  private BotConfiguration config;
  private BotPool pool;
  private boolean debug;

  public Bot(BotConfiguration config) {
    this(null, BotState.INIT, config, new BotPool(), false);
  }

  public Bot() {
    this(null, BotState.INIT, new BotConfiguration(), new BotPool(), false);
  }

  public void run() {
    this.generateId();
    while (true) {
      BotCommand cmd = this.getNextCommand();

      switch (cmd.getScope()) {

        case SLEEP:
          LoggerTools.info("COMMAND :: SLEEP");
          long sleepTimeoutAmount = Long.valueOf(cmd.getParams().get("amount").toString());
          TimeUnit sleepTimeoutUnit = TimeUnit.valueOf(cmd.getParams().get("unit").toString());
          this.sleep(sleepTimeoutAmount, sleepTimeoutUnit);
          break;

        case SHUTDOWN:
          LoggerTools.info("COMMAND :: SHUTDOWN");
          long shutdownTimeoutAmount = Long.valueOf(cmd.getParams().get("amount").toString());
          TimeUnit shutdownTimeoutUnit = TimeUnit.valueOf(cmd.getParams().get("unit").toString());
          this.shutdown(shutdownTimeoutAmount, shutdownTimeoutUnit);
          return;

        case KILL:
          LoggerTools.info("COMMAND :: KILL");
          this.kill();
          return;

        default:
          LoggerTools.info("COMMAND :: UNKNOWN");
          break;
      }
    }
  }

  private void generateId() {
    LoggerTools.info("IDGEN");
    String mac = SystemTools.getMAC();
    this.setId(mac);
    LoggerTools.info(String.format("IDGEN :: ID = %s", this.getId()));
  }
  private BotCommand getNextCommand() {
    LoggerTools.info("NXTCMD");
    return new BotCommand(CommandScope.SHUTDOWN, new HashMap<>());
  }

  private void sleep(long amount, TimeUnit unit) {
    LoggerTools.info(String.format("STATUS :: SLEEP :: %d %s", amount, unit));
    try {
      unit.sleep(amount);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void shutdown(long amount, TimeUnit unit) {
    LoggerTools.info(String.format("STATUS :: SHUTDOWN :: %d %s", amount, unit));
    this.pool.shutdown(amount, unit);
  }

  private void kill() {
    LoggerTools.info("STATUS :: KILLED");
    this.pool.kill();
  }



}
