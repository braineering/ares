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

package com.acmutv.botnet.time;

import com.acmutv.botnet.target.HttpTarget;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * This class realizes a time interval.
 * Typically used for timing interval representation.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpTarget
 * @see TimeUnit
 */
@Data
@AllArgsConstructor
public class Period {
  private int min;
  private int max;
  private TimeUnit unit;

  public static final String REG_PATTERN = "\\d+(-\\d+)?";

  /**
   * Constructs a period from string.
   * @param str the string to parse.
   */
  public Period(final String str) {
    String values[] = str.split("-", 2);
    int min;
    int max;
    if (values.length == 2) {
      min = Integer.valueOf(values[0]);
      max = Integer.valueOf(values[1]);
    } else {
      min = max = Integer.valueOf(values[0]);
    }
    this.min = min;
    this.max = max;
    this.unit = TimeUnit.SECONDS;
  }

  /**
   * Checks if the string represents a valid Period.
   * @param str the string to check.
   * @return true, if the given string represents a valid Period; false, otherwise.
   */
  public static boolean isValidString(String str) {
    return Pattern.matches(REG_PATTERN, str);
  }

  /**
   * Parses a Period from the given string.
   * @param str the string to parse.
   * @return the parsed Period.
   */
  public static Period valueOf(final String str) {
    String values[] = str.split("-", 2);
    int min;
    int max;
    if (values.length == 2) {
      min = Integer.valueOf(values[0]);
      max = Integer.valueOf(values[1]);
    } else {
      min = max = Integer.valueOf(values[0]);
    }
    TimeUnit unit = TimeUnit.SECONDS;
    return new Period(min, max, unit);
  }
}
