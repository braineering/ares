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

package com.giammp.botnet.tools;

import java.util.Random;

/**
 * This class realizes random number generation utilities.
 *
 * @author Giacomo Marciani <gmarciani@ieee.org>
 * @author Michele Porretta <mporretta@acm.org>
 * @since 1.0.0
 */
public class RandomTools {

  /**
   * Generates a uniform random number in [a, b].
   * @param a The lower bound.
   * @param b The upper bound.
   * @param rndgen The random number generator.
   * @return The uniform random number in [a,b].
   */
  public static int getRandomInt(final int a, final int b, Random rndgen) {
    return rndgen.nextInt(b - a) + a;
  }
}
