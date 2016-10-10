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

package com.giammp.botnet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * This class realizes an interval of integers.
 *
 * @author Giacomo Marciani <gmarciani@ieee.org>
 * @author Michele Porretta <mporretta@acm.org>
 * @since 1.0.0
 * @see Target
 */
@Data
@AllArgsConstructor
public class Period {
  private int min;
  private int max;

  public static final String REG_PATTERN = "\\d+(-\\d+){0,1}";

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
    int min = 0;
    int max = 0;
    if (values.length == 2) {
      min = Integer.valueOf(values[0]);
      max = Integer.valueOf(values[1]);
    } else {
      min = max = Integer.valueOf(values[0]);
    }
    return new Period(min, max);
  }
}
