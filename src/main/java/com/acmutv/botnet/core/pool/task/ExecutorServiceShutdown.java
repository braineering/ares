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

package com.acmutv.botnet.core.pool.task;

import com.acmutv.botnet.tool.time.Duration;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

/**
 * This class realizes an ExecutorService shutdown task.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see ExecutorService
 */
@Data
public class ExecutorServiceShutdown implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(ExecutorServiceShutdown.class);

  /**
   * The service to shutdown.
   */
  private final ExecutorService executor;

  /**
   * The shutdown timeout.
   */
  private final Duration timeout;

  /**
   * Shuts down the specified service.
   */
  @Override
  public void run() {
    LOGGER.trace("Shutting down executor {}", this.executor);
    this.executor.shutdown();
    try {
      this.executor.awaitTermination(this.getTimeout().getAmount(), this.getTimeout().getUnit());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}