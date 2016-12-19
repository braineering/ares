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

package com.acmutv.botnet.tool.time;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class realizes JUnit tests for {@link Period}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Period
 */
public class PeriodTest {

  /**
   * Tests the Period creation in the joint form. E.g.: `1`.
   */
  @Test
  public void test_creator_joint() {
    final String str = "1";
    Period actual = new Period(str);
    Period expected = new Period(1, 1, TimeUnit.SECONDS);
    assertEquals(expected, actual);
  }

  /**
   * Tests the Period creation in the disjoint form. E.g.: `1-3`.
   */
  @Test
  public void test_creator_disjoint() {
    final String str = "1-3";
    Period actual = new Period(str);
    Period expected = new Period(1, 3, TimeUnit.SECONDS);
    assertEquals(expected, actual);
  }


  /**
   * Tests the Period parsing in the joint form. E.g.: `1`.
   */
  @Test
  public void testValueOf_joint() {
    final String str = "1";
    Period actual = Period.valueOf(str);
    Period expected = new Period(1, 1, TimeUnit.SECONDS);
    assertEquals(expected, actual);
  }

  /**
   * Tests the Period parsing in the disjoint form. E.g.: `1-3`.
   */
  @Test
  public void test_fromString_disjoint() {
    final String str = "1-3";
    Period actual = Period.valueOf(str);
    Period expected = new Period(1, 3, TimeUnit.SECONDS);
    assertEquals(expected, actual);
  }

  /**
   * Tests the Period string validity check.
   */
  @Test
  public void test_isValidString() {
    assertTrue(Period.isValidString("1"));
    assertTrue(Period.isValidString("1-3"));

    assertFalse(Period.isValidString(""));
    assertFalse(Period.isValidString("-"));
    assertFalse(Period.isValidString("1-"));
    assertFalse(Period.isValidString("-3"));
  }
}
