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

package com.acmutv.botnet.ui;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * The command line interface options of the whole application.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Option
 */
public class BaseOptions extends Options {

  private static final long serialVersionUID = 1L;

  /**
   * The CLI description for the option `version`.
   */
  private static final String DESCRIPTION_VERSION = "Show app version.";

  /**
   * The CLI description for the option `help`.
   */
  private static final String DESCRIPTION_HELP = "Show app helper.";

  /**
   * The CLI description for the option `silent`.
   */
  private static final String DESCRIPTION_SILENT = "Activate silent mode.";

  /**
   * The CLI description for the option `trace`.
   */
  private static final String DESCRIPTION_TRACE = "Activate trace mode.";

  /**
   * The CLI description for the option `config`.
   */
  private static final String DESCRIPTION_CONFIG = "Custom configuration.";

  /**
   * The singleton instance of {@link BaseOptions}.
   */
  private static BaseOptions instance;

  /**
   * Returns the singleton of {@link BaseOptions}.
   * @return the singleton.
   */
  public static BaseOptions getInstance() {
    if (instance == null) {
      instance = new BaseOptions();
    }
    return instance;
  }

  /**
   * Constructs the singleton of {@link BaseOptions}.
   */
  private BaseOptions() {
    Option version = this.optVersion();
    Option help = this.optHelp();
    Option silent = this.optSilent();
    Option trace = this.optTrace();
    Option config = this.optConfig();

    super.addOption(version);
    super.addOption(help);
    super.addOption(silent);
    super.addOption(trace);
    super.addOption(config);
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
   * Builds the option `silent`.
   * @return the option.
   */
  private Option optSilent() {
    return Option.builder("s")
        .longOpt("silent")
        .desc(DESCRIPTION_SILENT)
        .required(false)
        .hasArg(false)
        .build();
  }

  /**
   * Builds the option `trace`.
   * @return the option.
   */
  private Option optTrace() {
    return Option.builder("t")
        .longOpt("trace")
        .desc(DESCRIPTION_TRACE)
        .required(false)
        .hasArg(false)
        .build();
  }

  /**
   * Builds the option `config`.
   * @return the option.
   */
  private Option optConfig() {
    return Option.builder("c")
        .longOpt("config")
        .desc(DESCRIPTION_CONFIG)
        .required(false)
        .hasArg(true)
        .numberOfArgs(1)
        .argName("YAML-FILE")
        .build();
  }

}
