/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani, Michele Porretta
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

package com.acmutv.botnet.attacks;

import com.acmutv.botnet.model.Period;
import com.acmutv.botnet.model.Target;
import com.acmutv.botnet.tools.ConnectionTools;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the unit tests on HTTP attacks.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 * @see HTTPAttack
 * @see Target
 */
public class HTTPAttackTest {

  @Before
  public void setup() {
    org.junit.Assume.assumeTrue(ConnectionTools.checkConnection());
  }

  @Test
  public void testGET_single() throws InterruptedException, MalformedURLException {
    Target tgt = new Target(new URL("http://www.google.com"), new Period(1, 1), 1);
    ExecutorService executor = Executors.newFixedThreadPool(1);
    Runnable attacker = new HTTPAttack(tgt);
    executor.execute(attacker);
    executor.shutdown();
    executor.awaitTermination(60, TimeUnit.SECONDS);
  }
}
