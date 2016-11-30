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

package com.acmutv.botnet.tools;

import com.acmutv.botnet.model.Period;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class realizes the unit tests on random number generation tools.
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0.0
 * @see RandomTools
 */
public class RandomToolsTest {

  /**
   * Tests the random integer number generation inside interval in joint form.
   */
  @Test
  public void testGetRandomInt_joint() {
    int number = RandomTools.getRandomInt(1, 1, new Random());
    assertTrue(number == 1);
  }

  /**
   * Tests the random integer number generation inside interval in disjoint form.
   */
  @Test
  public void testGetRandomInt_disjoint() {
    int number = RandomTools.getRandomInt(2, 4, new Random());
    assertTrue(number >= 2 && number <= 4);
  }

  /**
   * Tests the random double number generation inside interval in joint form.
   */
  @Test
  public void testGetRandomDouble_joint() {
    double number = RandomTools.getRandomDouble(1.0, 1.0, new Random());
    assertTrue(number == 1.0);
  }

  /**
   * Tests the random double number generation inside interval in disjoint form.
   */
  @Test
  public void testGetRandomDouble_disjoint() {
    double number = RandomTools.getRandomDouble(2.0, 4.0, new Random());
    assertTrue(number >= 2.0 && number <= 4.0);
  }

  /**
   * Tests the random integer number generation inside period in joint form.
   */
  @Test
  public void testGetRandomIntInPeriod_joint() {
    Period period = new Period(1, 1);
    int seconds = RandomTools.getRandomIntInPeriod(period, new Random());
    assertTrue(seconds == 1);
  }

  /**
   * Tests the random integer number generation inside period in disjoint form.
   */
  @Test
  public void testGetRandomIntInPeriod_disjoint() {
    Period period = new Period(2, 4);
    int seconds = RandomTools.getRandomIntInPeriod(period, new Random());
    assertTrue(seconds >= 2 && seconds <= 4);
  }

  /**
   * Tests the random double number generation inside period in joint form.
   */
  @Test
  public void testGetRandomDoubleInPeriod_joint() {
    Period period = new Period(1, 1);
    double seconds = RandomTools.getRandomDoubleInPeriod(period, new Random());
    assertTrue(seconds == 1);
  }

  /**
   * Tests the random double number generation inside period in disjoint form.
   */
  @Test
  public void testGetRandomDoubleInPeriod_disjoint() {
    Period period = new Period(2, 4);
    double seconds = RandomTools.getRandomDoubleInPeriod(period, new Random());
    assertTrue(seconds >= 2 && seconds <= 4);
  }
}
