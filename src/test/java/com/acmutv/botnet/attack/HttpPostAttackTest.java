/*
  The MIT License (MIT)
  <p>
  Copyright (c) 2016 Giacomo Marciani and Michele Porretta
  <p>
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  <p>
  <p>
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  <p>
  <p>
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.botnet.attack;

import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.time.Period;
import com.acmutv.botnet.service.ConnectionService;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes JUnit tests for {@link HttpPostAttack}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpPostAttack
 * @see HttpTarget
 */
public class HttpPostAttackTest {

  @Before
  public void setup() {
    org.junit.Assume.assumeTrue(ConnectionService.checkConnection());
  }

  @Test
  public void test_makeAttack() throws InterruptedException, MalformedURLException {
    HttpTarget tgt = new HttpTarget(new URL("http://www.google.com"), new Period(1, 1, TimeUnit.SECONDS), 1);
    ExecutorService executor = Executors.newFixedThreadPool(1);
    Runnable attacker = new HttpPostAttack(tgt);
    executor.execute(attacker);
    executor.shutdown();
    executor.awaitTermination(60, TimeUnit.SECONDS);
  }
}
