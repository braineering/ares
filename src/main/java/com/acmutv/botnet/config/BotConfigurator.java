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

package com.acmutv.botnet.config;

import com.acmutv.botnet.BotMain;
import com.acmutv.botnet.control.AppController;
import com.acmutv.botnet.view.BaseOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

/**
 * This class realizes the configuration static layer for the whole application.
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotConfiguration
 * @see YamlConstructor
 * @see CommandLine
 * @see AppController
 */
public class BotConfigurator {

  public static BaseOptions OPTS = BaseOptions.getInstance();

  /**
   * Loads the bot configuration, according to the possible arguments passed to the main method.
   * @param argv The command line arguments passed to the main method.
   * @return The bot configuration.
   * @see BotMain
   * @see CommandLine
   * @see BotConfiguration
   * @see AppController
   */
  public static BotConfiguration loadConfiguration(String[] argv) {
    CommandLine cmd = getCommandLine(argv);
    BotConfiguration config = null;

    if (cmd.hasOption("help")) {
      AppController.printHelp(OPTS);
      System.exit(0);
    } else if (cmd.hasOption("version")) {
      AppController.printVersion();
      System.exit(0);
    }

    if (cmd.hasOption("configuration")) {
      String configPath = cmd.getOptionValue("configuration");
      config = BotConfiguration.fromYaml(configPath);
    } else {
      config = new BotConfiguration();
    }

    if (cmd.hasOption("debug")) {
      config.setDebug(true);
    }

    String args[] = cmd.getArgs();

    if (config.isDebug()) {
      // TODO
    }

    return config;
  }

  /**
   * Returns command line options/arguments parsing utility.
   * @param argv The command line arguments passed to the main method.
   * @return The command line options/arguments parsing utility.
   * @see CommandLineParser
   * @see CommandLine
   * @see AppController
   */
  private static CommandLine getCommandLine(String argv[]) {
    CommandLineParser cmdParser = new DefaultParser();
    CommandLine cmd = null;

    try {
      cmd = cmdParser.parse(OPTS, argv);
    } catch (ParseException exc) {
      System.err.println("[BOT]> ERROR: " + exc.getMessage());
      AppController.printUsage();
    }

    return cmd;
  }
}
