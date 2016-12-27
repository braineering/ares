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

import com.acmutv.botnet.core.target.HttpTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes a time period.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpTarget
 * @see TimeUnit
 */
@Data
@AllArgsConstructor
public class Interval {

  /**
   * The amount of time (lower bound).
   */
  private long min;

  /**
   * The amount of time (upper bound).
   */
  private long max;

  /**
   * The period time unit.
   */
  @NonNull
  private TimeUnit unit = TimeUnit.SECONDS;

  /**
   * Returns a random {@link Duration} with amount within the interval.
   * @return the duration.
   */
  public Duration getRandomDuration() {
    final long min = this.getMin();
    final long max = this.getMax();
    final long amount = (min == max) ? min :
        ThreadLocalRandom.current().nextLong(this.getMin(), this.getMax());
    return new Duration(amount, this.getUnit());
  }

  /**
   * Parses {@link Interval} from string.
   * @param string the string to parse.
   * @return the parsed {@link Interval}; null if cannot be parsed.
   */
  public static Interval valueOf(final String string) {
    if (string == null) return null;
    String parts[] = string.split(":", 2);
    if (parts.length != 2) return null;
    String subparts[] = parts[0].split("-",2);
    long min, max;
    if (subparts.length == 1) {
      min = max = Long.valueOf(subparts[0]);
    } else if (subparts.length == 2) {
      min = Long.valueOf(subparts[0]);
      max = Long.valueOf(subparts[1]);
    } else {
      return null;
    }
    TimeUnit unit = TimeUnit.valueOf(parts[1]);
    return new Interval(min, max, unit);
  }

  @Override
  public String toString() {
    return String.format("%d-%d:%s", this.getMin(), this.getMax(), this.getUnit());
  }
}
