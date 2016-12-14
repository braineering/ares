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

package com.acmutv.botnet.service;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This class realizes functions for the execution of bash commands.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see ProcessBuilder
 * @see Process
 */
public class BashExecutor {

  private static final Logger LOGGER = LogManager.getLogger(BashExecutor.class);

  /**
   * Executes the given command and arguments.
   * @param command The command to execute. Arguments must be given as a separated strings.
   *                E.g.: BashExecutor.run("ls", "-la") or BashExecutor.run("ls", "-l", "-a")
   * @return The command output as a string.
   * @throws IOException when error in process generation or output.
   */
  @SuppressWarnings("ConfusingArgumentToVarargsMethod")
  public static String run(String ...command) throws IOException {
    LOGGER.traceEntry("command={}", command);
    ProcessBuilder pb = new ProcessBuilder(command);
    Process p = pb.start();
    String out = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());
    return out.trim();
  }

}
