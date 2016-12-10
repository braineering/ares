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

package com.acmutv.botnet.service;

import com.acmutv.botnet.view.BaseOptions;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * This class realizes the application control entry-point.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BaseOptions
 */
public class AppController {
  public static final String APP_NAME = "BOT";
  public static final String TEAM_NAME = "Giacomo Marciani and Michele Porretta";
  public static final String APP_VERSION = "1.0";
  public static final String APP_DESCRIPTION = "A botnet showcase.\n";

  /**
   * Prints the application usage instructions.
   */
  public static final void printUsage() {
    System.out.format("%s version %s (by %s)\n", APP_NAME, APP_VERSION, TEAM_NAME);
    System.out.format("%s\n", APP_DESCRIPTION);
    System.out.format("Usage: %s [options,...]\n", APP_NAME);
  }

  /**
   * Prints the application command line helper.
   * @param opts The CLI options.
   * @see org.apache.commons.cli.Option
   * @see org.apache.commons.cli.Options
   */
  public static final void printHelp(final Options opts) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(APP_NAME, opts, true);
  }

  /**
   * Prints the application version.
   */
  public static final void printVersion() {
    System.out.format("%s version %s (by %s)\n", APP_NAME, APP_VERSION, TEAM_NAME);
  }

}
