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

import com.acmutv.botnet.service.RandomGenerator;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * This class realizes JUnit tests for {@link RandomGenerator}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see RandomGenerator
 */
public class RandomGeneratorTest {

  /**
   * Tests the random integer number generation inside interval in joint form.
   */
  @Test
  public void test_getRandomInt_joint() {
    int number = RandomGenerator.getRandomInt(1, 1, new Random());
    assertTrue(number == 1);
  }

  /**
   * Tests the random integer number generation inside interval in disjoint form.
   */
  @Test
  public void test_getRandomInt_disjoint() {
    int number = RandomGenerator.getRandomInt(2, 4, new Random());
    assertTrue(number >= 2 && number <= 4);
  }

  /**
   * Tests the random double number generation inside interval in joint form.
   */
  @Test
  public void testGetRandomDouble_joint() {
    double number = RandomGenerator.getRandomDouble(1.0, 1.0, new Random());
    assertTrue(number == 1.0);
  }

  /**
   * Tests the random double number generation inside interval in disjoint form.
   */
  @Test
  public void test_getRandomDouble_disjoint() {
    double number = RandomGenerator.getRandomDouble(2.0, 4.0, new Random());
    assertTrue(number >= 2.0 && number <= 4.0);
  }

}
