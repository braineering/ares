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

package com.acmutv.botnet.view;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * This class realizes the command line interface options of the whole application.
 * The class is implemented as a singleton.
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Option
 */
public class BaseOptions extends Options {

  private static final long serialVersionUID = 1L;

  public static final String DESCRIPTION_CONFIGURATION = "YAML configuration file.";
  public static final String DESCRIPTION_DEBUG = "Debug mode.";
  public static final String DESCRIPTION_HELP = "Project helper.";
  public static final String DESCRIPTION_VERSION = "Project version.";

  private static BaseOptions instance;

  /**
   * Initializes the singleton instance of the class.
   * @return the singleton instance of the class.
   */
  public static BaseOptions getInstance() {
    if (instance == null) {
      instance = new BaseOptions();
    }
    return instance;
  }

  /**
   * Constructor for the singleton of the class.
   */
  private BaseOptions() {
    Option debug = this.optDebug();
    Option configuration = this.optConfiguration();
    Option help = this.optHelp();
    Option version = this.optVersion();

    super.addOption(debug);
    super.addOption(configuration);
    super.addOption(help);
    super.addOption(version);
  }

  /**
   * Builds the option `debug`.
   * @return the option.
   */
  private Option optDebug() {
    return Option.builder("D")
        .longOpt("debug")
        .desc(DESCRIPTION_DEBUG)
        .required(false)
        .hasArg(false)
        .build();
  }

  /**
   * Builds the option `configuration`.
   * @return the option.
   */
  private Option optConfiguration() {
    return Option.builder("c")
        .longOpt("configuration")
        .desc(DESCRIPTION_CONFIGURATION)
        .required(false)
        .hasArg(true)
        .numberOfArgs(1)
        .argName("YAML-FILE")
        .build();
  }

  /**
   * Builds the option `help`.
   * @return the option.
   */
  private Option optHelp() {
    return Option.builder("h")
        .longOpt("help")
        .desc(DESCRIPTION_HELP)
        .required(false)
        .hasArg(false)
        .build();
  }

  /**
   * Builds the option `version`.
   * @return the option.
   */
  private Option optVersion() {
    return Option.builder("v")
        .longOpt("version")
        .desc(DESCRIPTION_VERSION)
        .required(false)
        .hasArg(false)
        .build();
  }
}
