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

package com.acmutv.botnet.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;

/**
 * This class realizes the app lifecycle services.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class RuntimeManager {

  private static final Logger LOGGER = LogManager.getLogger(RuntimeManager.class);

  /**
   * Registers atexit runnables as JVM shutdown hooks.
   * @param hooks atexit runnables.
   * @see Runtime
   * @see Thread
   * @see Runnable
   */
  public static void registerShutdownHooks(Runnable ...hooks) {
    Runtime runtime = Runtime.getRuntime();
    for (Runnable hook : hooks) {
      runtime.addShutdownHook(new Thread(hook));
      LOGGER.trace("Registered shutdown hook {}", hook.getClass().getName());
    }
  }

  /**
   * Retrieves the local number of cores.
   * @return the number of cores.
   */
  public static int getCores() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * Returns the current JVM name.
   * @return the JVM name.
   */
  public static String getJvmName() {return ManagementFactory.getRuntimeMXBean().getName();}

}
