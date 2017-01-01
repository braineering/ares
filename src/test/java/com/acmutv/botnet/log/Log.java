/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani

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
package com.acmutv.botnet.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * This class realizes logging simulation.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
public class Log {

  private static final Logger LOGGER = LogManager.getLogger(Log.class);

  @Test
  public void test_rolling() throws IOException {
    final String configPath = Log.class.getResource("/log/log4j2.rolling.xml").getPath();
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    File file = new File(configPath);
    context.setConfigLocation(file.toURI());

    LOGGER.trace("trace message");
    LOGGER.info("info message");
    LOGGER.info(AppLogMarkers.COMMAND, "command message");
    LOGGER.info(AppLogMarkers.ATTACK, "attack message");
  }

  @Test
  public void test_single() throws IOException {
    final String configPath = Log.class.getResource("/log/log4j2.single.xml").getPath();
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    File file = new File(configPath);
    context.setConfigLocation(file.toURI());

    LOGGER.trace("trace message");
    LOGGER.info("info message");
    LOGGER.info(AppLogMarkers.COMMAND, "command message");
    LOGGER.info(AppLogMarkers.ATTACK, "attack message");
  }

}
